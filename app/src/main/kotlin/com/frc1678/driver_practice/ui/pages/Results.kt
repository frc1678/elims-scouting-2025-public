package com.frc1678.driver_practice.ui.pages

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frc1678.driver_practice.State
import com.frc1678.driver_practice.api.SheetInfo
import com.frc1678.driver_practice.api.range
import com.frc1678.driver_practice.api.sheetId
import com.frc1678.driver_practice.api.testApi
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Results(state: State, updateState: (State) -> Unit, onSubmit: () -> Unit) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val textMod = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(1.dp)
            val smallTextMod = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(1.dp)
            val cardMod = Modifier
                .padding(20.dp)
                .fillMaxWidth()
            val mContext = LocalContext.current

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(){
                    Text(
                        "Results:",
                        style = MaterialTheme.typography.displaySmall,
                        color = if (state.fromHistory) Color(0xFF800080) else Color.Black
                    )
                }

                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(bottom = 1.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    // Auto Column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Auto", fontSize = 18.sp, textDecoration = TextDecoration.Underline)
                        Row() {
                            Column() {
                                Text("L1: ${state.autoL1}", fontSize = 18.sp)
                                Text("L2: ${state.autoL2}", fontSize = 18.sp)
                                Text("L3: ${state.autoL3}", fontSize = 18.sp)
                                Text("L4: ${state.autoL4}", fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.padding(10.dp))
                            Column(){
                            Text("L1 Failed: ${state.autoL1fail}", fontSize = 18.sp)
                            Text("L2 Failed: ${state.autoL2fail}", fontSize = 18.sp)
                            Text("L3 Failed: ${state.autoL3fail}", fontSize = 18.sp)
                            Text("L4 Failed: ${state.autoL4fail}", fontSize = 18.sp)
                        }
                    }
                        Text("Net: ${state.autoNet}", fontSize = 18.sp)
                        Text("Net Failed: ${state.autoNetFail}", fontSize = 18.sp)
                        Text("Processor: ${state.autoProcessor}", fontSize = 18.sp)
                        Text("Processor Failed: ${state.autoProcessorFail}", fontSize = 18.sp)
                        HorizontalDivider(thickness = 1.dp)
                        Text("Endgame", fontSize = 18.sp, textDecoration = TextDecoration.Underline)
                        Text("Climb Level: ${state.climbLevel}", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                        Text("Climb Success: ${state.climbSuccess}", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }

                    VerticalDivider(thickness = 1.dp)

                    // Tele Column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tele", fontSize = 18.sp, textDecoration = TextDecoration.Underline)
                        Row() {
                                Column() {
                                    Text("L1: ${state.teleL1}", fontSize = 18.sp)
                                    Text("L2: ${state.teleL2}", fontSize = 18.sp)
                                    Text("L3: ${state.teleL3}", fontSize = 18.sp)
                                    Text("L4: ${state.teleL4}", fontSize = 18.sp)
                                }
                            Spacer(modifier = Modifier.padding(10.dp))

                        Column() {
                            Text("L1 Failed: ${state.teleL1fail}", fontSize = 18.sp)
                            Text("L2 Failed: ${state.teleL2fail}", fontSize = 18.sp)
                            Text("L3 Failed: ${state.teleL3fail}", fontSize = 18.sp)
                            Text("L4 Failed: ${state.teleL4fail}", fontSize = 18.sp)
                        }
                    }
                        Text("Net: ${state.teleNet}", fontSize = 18.sp)
                        Text("Net Failed: ${state.teleNetFail}", fontSize = 18.sp)
                        Text("Processor: ${state.teleProcessor}", fontSize = 18.sp)
                        Text("Processor Failed: ${state.teleProcessorFail}", fontSize = 18.sp)
                        HorizontalDivider(thickness = 1.dp)
                        Row() {
                            Column() {

                                Text("Broken Intake:", fontSize = 16.sp,
                                 modifier = Modifier.padding(8.dp))
                                Text("Battery Died:", fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp))
                                Text("Transfer Issue:", fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp))
                            }
                            Column() {
                                Switch(
                                    checked = state.brokenIntake, // Directly bind to state
                                    onCheckedChange = { updateState(state.copy(brokenIntake = it)) }
                                )
                                Switch(
                                    checked = state.batteryDied, // Directly bind to state
                                    onCheckedChange = { updateState(state.copy(batteryDied = it)) }
                                )
                                Switch(
                                    checked = state.transferIssue, // Directly bind to state
                                    onCheckedChange = { updateState(state.copy(transferIssue = it)) }
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp)
                Box() {
                    Text(
                        "Match #: ${state.match}, Team: ${state.team}, Alliance: ${state.allianceColor}",
                        fontSize = 16.sp
                    )
                }
                HorizontalDivider(thickness = 1.dp)
                //On submit to MatchInfo
                Card(onClick = {
//                    testApi(SheetInfo(context = mContext, spreadsheetId = sheetId, range = range + "A:AI", values = state))
                    if (!state.fromHistory) {
                        // Save the current state as a CSV file
                        saveCsvToExternalStorage(mContext, state)

                        // Save the current state as a JSON file
                        saveMatchStateToJson(context = mContext, state = state)
                    }
                    // Get the last match number (without pre-incrementing)
                    val lastMatch = getLastMatchNumber()
                    val newMatchValue = (lastMatch + 1).toString() // Now we increment it here properly

                    // Update the state
                    updateState(
                        state.copy(
                            match = newMatchValue,
                            team = "",
                            autoL1 = 0,
                            autoL2 = 0,
                            autoL3 = 0,
                            autoL4 = 0,
                            autoL1fail = 0,
                            autoL2fail = 0,
                            autoL3fail = 0,
                            autoL4fail = 0,
                            teleL1 = 0,
                            teleL2 = 0,
                            teleL3 = 0,
                            teleL4 = 0,
                            teleL1fail = 0,
                            teleL2fail = 0,
                            teleL3fail = 0,
                            teleL4fail = 0,
                            autoNet = 0,
                            autoProcessor = 0,
                            autoNetFail = 0,
                            autoProcessorFail = 0,
                            teleNet = 0,
                            teleProcessor = 0,
                            teleNetFail = 0,
                            teleProcessorFail = 0,
                            climbLevel = "none",
                            climbSuccess = false,
                            allianceColor = "",
                            brokenIntake = false,
                            batteryDied = false,
                            transferIssue = false,
                            other = "",
                            fromHistory = false
                        )
                    )

                    // On submit --> go back to MatchInfo
                    onSubmit()

                }, modifier = cardMod) {
                    Spacer(modifier = Modifier.weight(1F))
                    Text(
                        if (state.fromHistory) "Go Back" else "Submit", // Change text based on flag
                        modifier = textMod,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        }
    }
}

//json saving
fun saveMatchStateToJson(context: Context, state: State) {
    val jsonString = Json.encodeToString(State.serializer(), state)
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // Format timestamp
    val fileName = "match_${state.match}_$timestamp.json"

    // Save the file to external storage
    val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(documentsDir, fileName)
    try {
        file.writeText(jsonString)
        Log.d("SaveJSON", "File saved successfully at: ${file.absolutePath}")
    } catch (e: Exception) {
        Log.e("SaveJSON", "Failed to save JSON file: ${e.message}")
    }
}
