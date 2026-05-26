package com.example.chaalfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.chaalfitness.ui.theme.ChaalFitnessTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize our data manager using the Activity context
        val settingsManager = DeviceStoredValues(this)

        setContent {
            ChaalFitnessTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var currentScreen by remember { mutableStateOf(ScreenDestination.GeneralInformationInput) }
                    val savedCurrentGoal by settingsManager.currentGoalFlow.collectAsState(
                        initial = ""
                    )

                    // STREAM DATA FROM THE FILE: Automatically reads local storage
                    val savedCurrentWeight by settingsManager.currentWeightFlow.collectAsState(
                        initial = ""
                    )
                    val savedHeight by settingsManager.heightFlow.collectAsState(initial = "")
                    val savedAge by settingsManager.ageFlow.collectAsState(initial = "")

                    LaunchedEffect(savedCurrentWeight, savedHeight, savedAge) {
                        if (
                            savedCurrentWeight.isNotEmpty() &&
                            savedHeight.isNotEmpty() &&
                            savedAge.isNotEmpty()
                        ) {
                            currentScreen = ScreenDestination.MainScreen
                        }
                    }
                    when (currentScreen) {
                        ScreenDestination.MainScreen -> {
                            MainScreen(
                                chosenGoal = savedCurrentGoal.ifEmpty { "Build Muscle" }, // fallback value if launched via auto-login
                                currentWeight = savedCurrentWeight,
                                modifier = Modifier.padding(innerPadding),
                                onChangeInformationClicked = {
                                    // Reset preferences to allow re-entering stats
                                    currentScreen = ScreenDestination.GeneralInformationInput
                                }
                            )
                        }

                        ScreenDestination.GeneralInformationInput -> {
                            GeneralInformationInputScreen(
                                modifier = Modifier.padding(innerPadding),
                                currentGoal = savedCurrentGoal,
                                currentWeight = savedCurrentWeight, // Pass saved data down!
                                currentHeight = savedHeight,
                                currentAge = savedAge,
                                onBackClicked = {
                                    currentScreen = ScreenDestination.MainScreen
                                },
                                onFinishClicked = { currentGoal,
                                                    currentWeight,
                                                    currentHeight,
                                                    currentAge ->
                                    // LAUNCH A COROUTINE: Writing to disk MUST happen in a background thread
                                    lifecycleScope.launch {
                                        // We still save the target weight as empty or keep it if we want,
                                        // but since it's removed from UI, we might want to update saveMetrics
                                        // to not require it, or just pass the existing saved value.
                                        // For now, I'll update saveMetrics to not require it if it's truly gone.
                                        settingsManager.saveMetrics(
                                            currentGoal = currentGoal,
                                            currentWeight = currentWeight,
                                            currentHeight = currentHeight,
                                            currentAge = currentAge
                                        )
                                        currentScreen = ScreenDestination.MainScreen
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}