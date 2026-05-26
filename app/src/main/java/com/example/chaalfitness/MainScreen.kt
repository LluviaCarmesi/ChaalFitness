package com.example.chaalfitness

import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chaalfitness.ui.theme.ChaalFitnessTheme

// 1. SUB-NAVIGATION STATES FOR THE BOTTOM BAR
enum class DashboardTab {
    Main,
    Profile
}

@Composable
fun MainScreen(
    chosenGoal: String,
    currentWeight: String,
    onChangeInformationClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(DashboardTab.Main) }

    // 2. THE MAIN TAB LAYOUT FRAME
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                NavigationBarItem(
                    selected = activeTab == DashboardTab.Main,
                    onClick = { activeTab = DashboardTab.Main },
                    label = { Text("Main") },
                    // 🌟 SWAP OUT THE VECTOR ICON FOR AN EMOJI TEXT SLOT
                    icon = { Text(text = "💪", fontSize = 20.sp) }
                )

                NavigationBarItem(
                    selected = activeTab == DashboardTab.Profile,
                    onClick = { activeTab = DashboardTab.Profile },
                    label = { Text("Profile") },
                    // 🌟 SWAP OUT THE VECTOR ICON FOR AN EMOJI TEXT SLOT
                    icon = { Text(text = "👤", fontSize = 20.sp) }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 3. REACTIVE CONDITIONAL VIEWS SWITCH
            when (activeTab) {
                DashboardTab.Main -> {
                    MainTabView()
                }

                DashboardTab.Profile -> {
                    ProfileTabView(
                        goal = chosenGoal,
                        currentWeight = currentWeight,
                        onChangeInformationClicked = onChangeInformationClicked
                    )
                }
            }
        }
    }
}

// 4. MAIN WORKOUTS TAB VIEW
@Composable
fun MainTabView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "My Workouts 💪",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Today's Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Warm-up: 5 mins stretching\n• Bench Press: 4 sets x 8 reps\n• Squats: 3 sets x 10 reps\n• Core: Plank 3 mins",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

// 5. PROFILE DETAIL TAB VIEW
@Composable
fun ProfileTabView(
    goal: String,
    currentWeight: String,
    onChangeInformationClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your Profile 👤",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileItemRow(
                    label = "Primary Goal",
                    value = if (goal.isNotEmpty()) goal else "Not set"
                )
                ProfileItemRow(
                    label = "Current Weight",
                    value = if (currentWeight.isNotEmpty()) "$currentWeight lbs" else "Not set"
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onChangeInformationClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Profile Settings")
        }
    }
}

@Composable
fun ProfileItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ChaalFitnessTheme {
        MainScreen(
            chosenGoal = "Build Muscle",
            currentWeight = "185",
            onChangeInformationClicked = {}
        )
    }
}