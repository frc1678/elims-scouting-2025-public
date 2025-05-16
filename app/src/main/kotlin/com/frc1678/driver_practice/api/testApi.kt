package com.frc1678.driver_practice.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** tests writing to the google sheet*/
fun testApi(sheetInfo: SheetInfo) {
    CoroutineScope(Dispatchers.Main).launch {
        writeDataToSheet(sheetInfo)
    }
}
