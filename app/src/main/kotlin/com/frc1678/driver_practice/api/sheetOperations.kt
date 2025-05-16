package com.frc1678.driver_practice.api

import android.content.Context
import android.util.Log
import com.frc1678.driver_practice.State
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject


/** write data to the google sheet */
suspend fun writeDataToSheet(sheetInfo: SheetInfo) {
    withContext(Dispatchers.IO) {
        // set json to leave default values in
        val json = Json { encodeDefaults = true }
        // encode data class to json string
        val jsonString = json.encodeToString(sheetInfo.values)
        // parse it back to jsonElement
        val parsed = json.parseToJsonElement(jsonString)
        // convert to list of lists
        val contents = listOf(parsed.jsonObject.toMap().values.map { it.toString().replace("\"", "") })
        // converts `state` dataclass to list and converts that to range usable by google sheets
        val body = ValueRange().setValues(contents)
        // append the data to the sheet
        updateSheet(sheetInfo, sheetInfo.values.match, body)
    }
}


fun updateSheet(sheetInfo: SheetInfo, searchValue: String, rowData: ValueRange) {

    val sheetsService = getSheetsService(sheetInfo.context)

    val response = sheetsService.spreadsheets().values()
        .get(sheetInfo.spreadsheetId, sheetInfo.range)
        .execute()

    val values = response.getValues() ?: listOf(listOf())
    var rowIndex: Int? = null
    for ((index, row) in values.withIndex()) {
        if (row.isNotEmpty() && row[0] == searchValue) {
            rowIndex = index + 1 // Google Sheets uses 1-based index
            break
        }
    }

    val updateRange = range + "A$rowIndex:AI$rowIndex"
    if (rowIndex != null) {
        sheetsService.spreadsheets().values()
            .update(sheetInfo.spreadsheetId, updateRange, rowData)
            .setValueInputOption("RAW")
            .execute()
    } else {
        sheetsService.spreadsheets().values()
            .append(sheetInfo.spreadsheetId, sheetInfo.range, rowData)
            .setValueInputOption("RAW")
            .execute()
    }
}


data class SheetInfo(
    val context: Context,
    val spreadsheetId: String,
    val range: String,
    val values: State
)
