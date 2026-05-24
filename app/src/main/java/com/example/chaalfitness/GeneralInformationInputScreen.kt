package com.example.chaalfitness

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chaalfitness.ui.theme.ChaalFitnessTheme

@Composable
fun GeneralInformationInputScreen(
    currentWeight: String,
    currentHeight: String,
    currentAge: String,
    onBackClicked: () -> Unit,
    currentGoal: String,
    onFinishClicked: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var weight by remember(currentWeight) { mutableStateOf(currentWeight) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf(currentGoal) }
    var displayedText by remember { mutableStateOf("") }

    // Logic to split initial height (total inches) into feet and inches
    val initialTotalInches = currentHeight.toIntOrNull() ?: 0
    var heightFeet by remember(currentHeight) { mutableStateOf((initialTotalInches / 12).let { if (it == 0) "" else it.toString() }) }
    var heightInches by remember(currentHeight) { mutableStateOf((initialTotalInches % 12).let { if (currentHeight.isEmpty()) "" else it.toString() }) }

    var age by remember(currentAge) { mutableStateOf(currentAge) }
    var summaryText by remember { mutableStateOf("") }

    val fitnessGoals =
        listOf("Build Muscle", "Lose Body Fat", "Increase Strength", "Improve Endurance")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to ChaalFitness",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

// 1. CHOOSE AN ANCHOR LAYER FOR CONTAINER STABILITY
        Box(modifier = Modifier.fillMaxWidth()) {

            val isOptionSelected = selectedGoal.isNotEmpty()

            // 2. THE ANCHOR TEXT FIELD (CONTAINER STAYS TRANSPARENT/CLEAN)
            OutlinedTextField(
                // We leave the value blank here if it's selected because we are going to
                // draw our custom highlighted text precisely on top of it manually!
                value = if (isOptionSelected) "" else selectedGoal,
                onValueChange = {},
                readOnly = true,
                enabled = true,
                label = { Text("Select Goal") },
                colors = OutlinedTextFieldDefaults.colors(
                    // Keep the container invisible to let the inner text box shine
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = if (isOptionSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Gray
                ),
                trailingIcon = {
                    val activeBrandColor = MaterialTheme.colorScheme.primary
                    val idleGrayColor = androidx.compose.ui.graphics.Color.Gray
                    val finalArrowColor = if (isOptionSelected) activeBrandColor else idleGrayColor
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(12.dp)
                            .drawBehind {
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(0f, 0f)
                                    lineTo(size.width, 0f)
                                    lineTo(size.width / 2f, size.height)
                                    close()
                                }
                                drawPath(path, color = finalArrowColor)
                            }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 3. PRECISION CUSTOM TEXT OVERLAY (ONLY RAW CHARACTERS GET A RECTANGLE)
            if (isOptionSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(
                            start = 16.dp,
                            top = 4.dp
                        ) // Aligns perfectly inside input box borders
                ) {
                    Text(
                        text = selectedGoal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            // 🌟 THIS DRAWS THE HIGHLIGHT RECTANGLE ONLY ON THE TEXT SPAN
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp) // Subtle curve on the selection box edges
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ) // Pads the background slightly past the text bounds
                    )
                }
            }

            // 4. THE TRANSPARENT INTERCEPTOR LAYER
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { isMenuExpanded = !isMenuExpanded }
            )

            // 5. THE FLOATING MENU OVERLAY
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                fitnessGoals.forEach { goalOption ->
                    DropdownMenuItem(
                        text = { Text(text = goalOption) },
                        onClick = {
                            selectedGoal = goalOption
                            isMenuExpanded = false
                            Log.d("Goal", selectedGoal)
                        }
                    )
                }
            }
        }

        // Current Weight Input (Numeric Keyboard)
        OutlinedTextField(
            value = weight,
            onValueChange = { input ->
                weight = input.filter { it.isDigit() }
                Log.d("Weight", weight)
            },
            label = { Text("Current Weight (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Height Input (Feet and Inches next to each other)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = heightFeet,
                onValueChange = { input ->
                    heightFeet = input.filter { it.isDigit() }
                    Log.d("Feet", heightFeet)
                },
                label = { Text("Height (ft)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = heightInches,
                onValueChange = { input ->
                    heightInches = input.filter { it.isDigit() }
                    Log.d("Inches", heightInches)
                },
                label = { Text("Height (in)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // Age Input
        OutlinedTextField(
            value = age,
            onValueChange = { input ->
                age = input.filter { it.isDigit() }
                Log.d("Age", age)
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (
                        selectedGoal.isNotEmpty() &&
                        weight.isNotEmpty() &&
                        heightFeet.isNotEmpty() &&
                        heightInches.isNotEmpty() &&
                        age.isNotEmpty()
                    ) {
                        val totalInches =
                            ((heightFeet.toIntOrNull() ?: 0) * 12) + (heightInches.toIntOrNull()
                                ?: 0)
                        summaryText =
                            "Profile Updated! Starting at $currentWeight lbs. Height: $heightFeet' $heightInches\", Age: $age"
                        onFinishClicked(selectedGoal, currentWeight, totalInches.toString(), age)
                    }
                },
                enabled = weight.isNotBlank() && heightFeet.isNotBlank() && heightInches.isNotBlank() && age.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Finish")
            }
        }

        if (summaryText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = summaryText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneralInformationInputScreenPreview() {
    ChaalFitnessTheme {
        GeneralInformationInputScreen(
            currentGoal = "Build Muscle",
            onBackClicked = {},
            onFinishClicked = { _, _, _, _ -> },
            currentWeight = "23",
            currentHeight = "70",
            currentAge = "25",
        )
    }
}