package com.frc1678.driver_practice.api

import android.content.Context
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets

const val sheetId = "12L43TrDzcFtQ7W4AfMm4kmcOZ-M9DW5jlIpHVQ6zDnc"
const val range = "elimsData!" // Adjust based on your needs

fun getSheetsService(context: Context): Sheets {
    val jsonFactory = JacksonFactory.getDefaultInstance()
    val httpTransport = NetHttpTransport.Builder().build()
    // Load the service account JSON key file
    val credentials = context.assets.open("service-account.json").use { inputStream ->
        GoogleCredential.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets"))
    }

    return Sheets.Builder(httpTransport, jsonFactory, credentials)
        .setApplicationName("Elims Scouting")
        .build()
}
