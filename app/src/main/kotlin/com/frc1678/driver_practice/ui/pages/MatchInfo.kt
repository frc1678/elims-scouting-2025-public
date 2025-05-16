package com.frc1678.driver_practice.ui.pages

import android.annotation.SuppressLint
import android.content.ContentValues
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frc1678.driver_practice.R
import com.frc1678.driver_practice.State
import com.frc1678.driver_practice.ui.theme.Styles
import java.text.DateFormat.getDateInstance
import java.util.Calendar
import java.util.Date
import kotlinx.serialization.json.Json
import android.util.Log
import android.content.Context
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import java.text.SimpleDateFormat


@SuppressLint("NewApi")
@Composable

fun MatchInfo(state: State, updateState: (State) -> Unit, onStart: () -> Unit, onHistoryLoad: () -> Unit) {
    Scaffold { padding ->
        // Fetches the Local Context
        val mContext = LocalContext.current
        var showHistoryPopup by remember { mutableStateOf(false) }
        var showPopup by remember { mutableStateOf(false) }
        var showFileSelectionPopup by remember { mutableStateOf(false) } // State for file selection popup
        var selectedFileForExport by remember { mutableStateOf<File?>(null) } // Keep track of selected file for export

        val brighterCardColor = Color(0xFFF3EDE6)

        /**
        // SAF launcher for creating a CSV document
        val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri ->
        if (uri != null) {
        val csvString = stateToCsv(state) // Convert State to CSV
        try {
        mContext.contentResolver.openOutputStream(uri)?.use { outputStream ->
        outputStream.write(csvString.toByteArray())
        }
        Log.d("ExportCSV", "File exported successfully to $uri")
        } catch (e: Exception) {
        Log.e("ExportCSV", "Error exporting file: ${e.message}")
        }
        } else {
        Log.e("ExportCSV", "File export cancelled.")
        }
        }
        )
         */

        //SAF launcher for creating a JSON document
        val createJsonDocumentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/json"),
            onResult = { uri ->
                if (uri != null) {
                    val jsonString = Json.encodeToString(State.serializer(), state)
                    try {
                        mContext.contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(jsonString.toByteArray())
                        }
                    } catch (e: Exception) {
                    }
                } else {
                }
            }
        )

        // Updated JSON Export Logic with Descriptive File Name
        val exportJsonToAppStorage: () -> Unit = {
            val jsonString = Json.encodeToString(State.serializer(), state)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // e.g., 20240112_143530
            val fileName = "match_${state.match}_$timestamp.json"

            saveJsonToExternalStorage(
                context = mContext,
                jsonString = jsonString,
                fileName = fileName
            )
        }

        // Integer values for the year, month and day (this is used for the DatePicker)
        val datePickerYear: Int
        val datePickerMonth: Int
        val datePickerDay: Int

        // Initializes a Calendar
        val datePickerCalendar = Calendar.getInstance()

        // Sets the DatePicker default values to the current year, month and day
        datePickerYear = datePickerCalendar.get(Calendar.YEAR)
        datePickerMonth = datePickerCalendar.get(Calendar.MONTH)
        datePickerDay = datePickerCalendar.get(Calendar.DAY_OF_MONTH)

        datePickerCalendar.time = Date()

        // DatePickerDialog; sets the initial values to be the default values
        val mDatePickerDialog = android.app.DatePickerDialog(
            mContext,
            { _, mYear, mMonth, mDayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, mYear)
                    set(Calendar.MONTH, mMonth)
                    set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                }

                val dateFormat = SimpleDateFormat("HH:mm:ss dd MMMM", java.util.Locale.getDefault())
                updateState(state.copy(date = dateFormat.format(selectedDate.time)))
            },
            datePickerYear, datePickerMonth, datePickerDay
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // History button
                Button(
                    onClick = { showHistoryPopup = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.size(150.dp)
                ) {
                    val historyIcon: Painter = painterResource(id = R.drawable.history_icon)

                    // History Image
                    Image(
                        painter = historyIcon,
                        contentDescription = null,
                        modifier = Modifier.size(75.dp),
                        colorFilter = if (isSystemInDarkTheme()) {
                            ColorFilter.tint(Color.White)
                        } else {
                            ColorFilter.tint(Color.Black)
                        }
                    )
                }

                // Export button
                Button(
                    onClick = { showPopup = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.size(150.dp)
                ) {
                    // Export Icon
                    Icon(
                        Icons.Outlined.Share,
                        contentDescription = null,
                        modifier = Modifier.size(75.dp),
                        tint = if (isSystemInDarkTheme()) Color.White; else Color.Black
                    )
                }
            }
            //History Popup
            if (showHistoryPopup) {
                var showDeleteConfirmation by remember { mutableStateOf(false) }
                var fileToDelete by remember { mutableStateOf<File?>(null) } // Track which file to delete
                val matchHistory = getMatchHistory()
                AlertDialog(
                    onDismissRequest = { showHistoryPopup = false },
                    title = { Text("Past Results") },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (matchHistory.isEmpty()) {
                                Text("No matches found.")
                            } else {
                                // Scrollable list of files
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f) // Allows scrolling when the content exceeds the space
                                        .padding(8.dp)
                                ) {
                                    LazyColumn {
                                        items(matchHistory) { (matchNumber, matchData) ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Match $matchNumber",
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.bodySmall
                                                )

                                                // Load Button
                                                Button(
                                                    onClick = {
                                                        updateState( // Loads the state
                                                            State(
                                                                match = matchData.getOrElse(0) { "1" },
                                                                allianceColor = matchData.getOrElse(1) { "" },
                                                                date = matchData.getOrElse(2) { "" },
                                                                team = matchData.getOrElse(3) { "" },
                                                                autoPathRun = matchData.getOrElse(4) { "" },
                                                                autoL1 = matchData.getOrElse(5) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                autoL1fail = matchData.getOrElse(6) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoL2 = matchData.getOrElse(7) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                autoL2fail = matchData.getOrElse(8) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoL3 = matchData.getOrElse(9) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                autoL3fail = matchData.getOrElse(10) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoL4 = matchData.getOrElse(11) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                autoL4fail = matchData.getOrElse(12) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleL1 = matchData.getOrElse(13) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                teleL1fail = matchData.getOrElse(14) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleL2 = matchData.getOrElse(15) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                teleL2fail = matchData.getOrElse(16) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleL3 = matchData.getOrElse(17) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                teleL3fail = matchData.getOrElse(18) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleL4 = matchData.getOrElse(19) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                teleL4fail = matchData.getOrElse(20) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleNet = matchData.getOrElse(21) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                teleNetFail = matchData.getOrElse(22) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleProcessor = matchData.getOrElse(23) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                teleProcessorFail = matchData.getOrElse(24) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoNet = matchData.getOrElse(21) { "0" }.toIntOrNull()
                                                                    ?: 0,
                                                                autoNetFail = matchData.getOrElse(22) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoProcessor = matchData.getOrElse(23) { "0" }
                                                                    .toIntOrNull() ?: 0,
                                                                autoProcessorFail = matchData.getOrElse(24) { "0" }
                                                                    .toIntOrNull() ?: 0,

                                                                climbLevel = matchData.getOrElse(25) { "none" },
                                                                climbSuccess = matchData.getOrElse(26) { "false" }
                                                                    .toBooleanStrictOrNull() ?: false,
                                                                brokenIntake = matchData.getOrElse(27) { "false" }
                                                                    .toBooleanStrictOrNull() ?: false,
                                                                batteryDied = matchData.getOrElse(28) { "false" }
                                                                    .toBooleanStrictOrNull() ?: false,
                                                                transferIssue = matchData.getOrElse(29) { "false" }
                                                                    .toBooleanStrictOrNull() ?: false,
                                                                fromHistory = true

                                                            )
                                                        )
                                                        onHistoryLoad()
                                                    },
                                                    modifier = Modifier.padding(end = 8.dp)
                                                ) {
                                                    Text("Load")
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Add Delete All button
                            Button(
                                onClick = { showDeleteConfirmation = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Delete All")
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showHistoryPopup = false }) {
                            Text("Close")
                        }
                    }
                )

                // Confirmation dialog for deleting a specific file
                if (showDeleteConfirmation && fileToDelete != null) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmation = false },
                        title = { Text("Confirm Deletion") },
                        text = { Text("Are you sure you want to delete the selected match file? This action cannot be undone.") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    fileToDelete?.let { file ->
                                        if (file.delete()) {
                                        } else {
                                        }
                                    }
                                    fileToDelete = null // Clear the file to delete
                                    showDeleteConfirmation = true // Close confirmation dialog
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                fileToDelete = null // Clear the file to delete
                                showDeleteConfirmation = false // Close confirmation dialog
                            }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                // Confirmation dialog for Delete All
                if (showDeleteConfirmation && fileToDelete == null) { // Separate check for Delete All
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmation = false },
                        title = { Text("Confirm Deletion") },
                        text = { Text("Are you sure you want to delete all saved files? This action cannot be undone.") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    deleteAllFiles(mContext) // Clear all files
                                    showDeleteConfirmation = false
                                    showHistoryPopup = true // keep history menu open
                                }
                            ) {
                                Text("Delete All")
                            }
                        }
                    )
                }
            }


            // Popup dialog for export options
            // Manage popup states
            var showFileSelectionPopup by remember { mutableStateOf(false) } // State for file selection popup
            var selectedFileForExport by remember { mutableStateOf<File?>(null) } // Keep track of selected file for export
            var exportFormat by remember { mutableStateOf("JSON") } // Tracks the selected export format (JSON or CSV)
            if (showPopup) {
                AlertDialog(
                    onDismissRequest = { showPopup = false },
                    title = { Text("Export Options") },
                    text = { Text("Choose an export format:") },
                    confirmButton = {
                        Column {
                            // Button for exporting as JSON
                            Button(
                                onClick = {
                                    exportFormat = "JSON" // Set format to JSON
                                    showFileSelectionPopup = true // Open file selection popup
                                    showPopup = false // Close main popup
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Export as JSON")
                            }


                            // Button for exporting as CSV
                            Button(
                                onClick = {
                                    saveCsvToExternalStorage(mContext, state)
                                    /**exportFormat = "CSV" // Set format to CSV
                                    showFileSelectionPopup = true // Open file selection popup */

                                    showPopup = false // Close main popup
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Export as CSV")
                            }

                            Button(
                                onClick = {
                                    resetCsvFile()
                                    showPopup = false // Close main popup
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text("Reset CSV File")
                            }

                            // Cancel Button
                            Button(
                                onClick = { showPopup = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Cancel")
                            }
                        }
                    },
                    dismissButton = {} // No dismiss button needed
                )
            }

            // File Selection Popup
            if (showFileSelectionPopup) {
                AlertDialog(
                    onDismissRequest = { showFileSelectionPopup = false },
                    title = { Text("Select File to Export") },
                    text = {
                        Column {
                            val files = getSavedFiles(mContext) // Retrieve saved files
                            if (files.isEmpty()) {
                                Text("No saved files found.")
                            } else {
                                LazyColumn {
                                    items(files) { file ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = file.name,
                                                modifier = Modifier.weight(1f),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Button(
                                                onClick = {
                                                    selectedFileForExport = file // Select the file for export
                                                    showFileSelectionPopup = false // Close file selection popup
                                                    val fileName =
                                                        if (exportFormat == "JSON") file.name else file.name.replace(
                                                            ".json",
                                                            ".csv"
                                                        )
                                                    if (exportFormat == "JSON") {
                                                        createJsonDocumentLauncher.launch(fileName) // Launch JSON export
                                                    } else {
                                                        //   createDocumentLauncher.launch(fileName) // Launch CSV export
                                                    }
                                                }
                                            ) {
                                                Text("Export")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showFileSelectionPopup = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }


            HorizontalDivider(thickness = 4.dp, color = brighterCardColor)
            Spacer(Modifier.height(30.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Red Alliance Button
                Card(
                    onClick = { updateState(state.copy(allianceColor = "Red")) },
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = if (state.allianceColor == "Red") Styles.selectedRed; else Styles.unselectedRed,
                    border = if (state.allianceColor == "Red")
                        if (isSystemInDarkTheme()) Styles.lightSelectedBorder; else Styles.darkSelectedBorder;
                    else Styles.unselectedRedBorder
                ) {
                    Spacer(modifier = Modifier.size(200.dp, 150.dp))
                }
                // Blue Alliance Button
                Card(
                    onClick = { updateState(state.copy(allianceColor = "Blue")) },
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = if (state.allianceColor == "Blue") Styles.selectedBlue; else Styles.unselectedBlue,
                    border = if (state.allianceColor == "Blue")
                        if (isSystemInDarkTheme()) Styles.lightSelectedBorder; else Styles.darkSelectedBorder;
                    else Styles.unselectedBlueBorder
                ) {
                    Spacer(modifier = Modifier.size(200.dp, 150.dp))
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(padding)
                    .weight(10f)
            ) {
                // Match Number Input
                OutlinedTextField(
                    modifier = Modifier
                        .size(400.dp, 100.dp)
                        .padding(vertical = 5.dp),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 45.sp, textAlign = TextAlign.Center,
                    ),
                    value = state.match,
                    onValueChange = { updateState(state.copy(match = it)) },
                    label = { Text("Match Number") }
                )
                // Team Number Input
                OutlinedTextField(
                    modifier = Modifier
                        .size(400.dp, 100.dp)
                        .padding(vertical = 5.dp),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 45.sp, textAlign = TextAlign.Center,
                    ),
                    value = state.team,
                    onValueChange = { updateState(state.copy(team = it)) },
                    label = { Text("Team Number") }
                )
                Row(
                    modifier = Modifier.height(150.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.size(50.dp)) // Spacer to align the below elements with the TextField above
                    // Date Text Field (read only)
                    val dateParts = state.date.split(" ")
                    val displayDate = "${dateParts[1]} ${dateParts[2]}" // Extracts "DD MMMM"
                    OutlinedTextField(
                        modifier = Modifier
                            .size(300.dp, 150.dp)
                            .padding(vertical = 25.dp),
                        value = displayDate,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 45.sp, textAlign = TextAlign.Center,
                        ),
                        readOnly = true,
                        onValueChange = {},
                        label = { Text("Date") }
                    )
                    // Calendar Button - allows the user to change the date
                    Button(
                        onClick = { mDatePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Transparent,
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.size(150.dp)
                    ) {
                        // Calendar Icon
                        Icon(
                            Icons.Outlined.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(75.dp),
                            tint = if (isSystemInDarkTheme()) Color.White; else Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Start Button
                Card(
                    onClick = {
                        onStart()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),
                ) {
                    Text(
                        "Start",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(40.dp),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}


//Saving to json files accessible through the app-specific storage
//exports the file to /storage/emulated/0/Android/data/com.frc1678.driver_practice/files/Documents/exported_data.json
fun saveJsonToExternalStorage(context: Context, jsonString: String, fileName: String = "exported_data.json") {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        try {
            val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(documentsDir, fileName)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
    }
}


// Get saved JSON files from app-specific storage
fun getSavedFiles(context: Context): List<File> {
    val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    return documentsDir?.listFiles()?.filter { it.extension == "json" } ?: emptyList()
}

// Load JSON file content into State
fun loadJsonFile(context: Context, file: File): State? {
    return try {
        val jsonString = file.readText()
        Json.decodeFromString(State.serializer(), jsonString)
    } catch (e: Exception) {
               null
    }
}

//Delete all saved files
fun deleteAllFiles(context: Context) {
    val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

    documentsDir?.listFiles()?.forEach { file ->
        if (file.delete()) {
            resetCsvFile()
        } else {
        }
    }
}


//Finds CSV file in downloads
fun getCsvFilePath(): File {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    return File(downloadsDir, "match_data.csv")
}

fun saveCsvToExternalStorage(context: Context, state: State) {
    val file = getCsvFilePath()
    val isNewFile = !file.exists() // Check if file exists

    val headers = listOf(
        "match", "allianceColor", "date", "team", "autoPathRun",
        "autoL1", "autoL1fail", "autoL2", "autoL2fail",
        "autoL3", "autoL3fail", "autoL4", "autoL4fail",
        "teleL1", "teleL1fail", "teleL2", "teleL2fail",
        "teleL3", "teleL3fail", "teleL4", "teleL4fail",
        "net", "netFail", "processor", "processorFail",
        "climbLevel", "climbSuccess", "brokenIntake", "batteryDied", "transferIssue", "other"
    )

    val values = listOf(
        state.match,
        state.allianceColor,
        state.date,
        state.team,
        state.autoPathRun,
        state.autoL1.toString(),
        state.autoL1fail.toString(),
        state.autoL2.toString(),
        state.autoL2fail.toString(),
        state.autoL3.toString(),
        state.autoL3fail.toString(),
        state.autoL4.toString(),
        state.autoL4fail.toString(),
        state.teleL1.toString(),
        state.teleL1fail.toString(),
        state.teleL2.toString(),
        state.teleL2fail.toString(),
        state.teleL3.toString(),
        state.teleL3fail.toString(),
        state.teleL4.toString(),
        state.teleL4fail.toString(),
        state.teleNet.toString(),
        state.teleNetFail.toString(),
        state.teleProcessor.toString(),
        state.teleProcessorFail.toString(),
        state.autoNet.toString(),
        state.autoNetFail.toString(),
        state.autoProcessor.toString(),
        state.autoProcessorFail.toString(),
        state.climbLevel.toString(),
        state.climbSuccess.toString(),
        state.brokenIntake.toString(),
        state.batteryDied.toString(),
        state.transferIssue.toString(),
        state.other.toString()
    )

    if (isNewFile) {
        file.writeText(headers.joinToString(",") + "\n") // Write headers only if new file
    }

    file.appendText(values.joinToString(",") + "\n") // Append new match data
}


fun resetCsvFile() {
    val file = getCsvFilePath()
    if (file.exists()) {
        file.delete() // Delete the file
       }
}

fun getLastMatchNumber(): Int {
    val file = getCsvFilePath()
    if (!file.exists()) return 1 // If no CSV exists, start at match 1

    val lines = file.readLines()
    if (lines.size < 2) return 1 // If only headers exist, start at match 1

    val lastLine = lines.lastOrNull() ?: return 1 // Get last match entry safely
    val matchNumber = lastLine.split(",").firstOrNull()?.toIntOrNull() ?: 1

    return matchNumber
}

fun getMatchHistory(): List<Pair<String, List<String>>> {
    val file = getCsvFilePath()
    if (!file.exists()) return emptyList() // Return empty list if file doesn't exist

    val lines = file.readLines()
    if (lines.size < 2) return emptyList() // No matches found (only headers exist)

    val matchHistory = mutableListOf<Pair<String, List<String>>>()

    for (line in lines.drop(1)) { // Skip headers
        val matchData = line.split(",") // Split line into list
        if (matchData.isNotEmpty()) {
            matchHistory.add(matchData[0] to matchData) // Store match number + full data
        }
    }
    return matchHistory
}







