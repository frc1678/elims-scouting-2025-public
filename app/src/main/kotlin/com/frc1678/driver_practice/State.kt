package com.frc1678.driver_practice

import com.frc1678.driver_practice.ui.pages.getLastMatchNumber
import java.lang.Boolean.FALSE
import java.text.DateFormat.getDateInstance
import java.util.Date
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class State(
    // Match info datapoints
    val match: String = getLastMatchNumber().toString(),
    val team: String = "",
    val allianceColor: String = "",
    val date: String = SimpleDateFormat("HH:mm:ss dd MMMM", Locale.getDefault()).format(Date()),
    val autoPathRun: String = "", // Will be added later, once they have autos to run. Will be a dropdown.
    // Auto datapoints
    val autoL1: Int = 0,
    val autoL1fail: Int = 0,
    val autoL2: Int = 0,
    val autoL2fail: Int = 0,
    val autoL3: Int = 0,
    val autoL3fail: Int = 0,
    val autoL4: Int = 0,
    val autoL4fail: Int = 0,
    val autoNet: Int = 0,
    val autoNetFail: Int = 0,
    val autoProcessor: Int = 0,
    val autoProcessorFail: Int = 0,
    // Tele datapoints
    val teleL1: Int = 0,
    val teleL1fail: Int = 0,
    val teleL2: Int = 0,
    val teleL2fail: Int = 0,
    val teleL3: Int = 0,
    val teleL3fail: Int = 0,
    val teleL4: Int = 0,
    val teleL4fail: Int = 0,
    val teleNet: Int = 0,
    val teleNetFail: Int = 0,
    val teleProcessor: Int = 0,
    val teleProcessorFail: Int = 0,
    // Endgame datapoints
    val climbLevel: String = "none",
    val climbSuccess: Boolean = FALSE,
    // Results datapoints
    val brokenIntake: Boolean = FALSE,
    val batteryDied: Boolean = FALSE,
    val transferIssue: Boolean = FALSE,
    val other: String = "",
    //Old Results
    val fromHistory: Boolean = FALSE,
)


