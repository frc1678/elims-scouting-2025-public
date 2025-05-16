package com.frc1678.driver_practice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.frc1678.driver_practice.ui.pages.Auto
import com.frc1678.driver_practice.ui.pages.Endgame
import com.frc1678.driver_practice.ui.pages.MatchInfo
import com.frc1678.driver_practice.ui.pages.Results
import com.frc1678.driver_practice.ui.pages.Tele
import com.frc1678.driver_practice.ui.theme.DriverPracticeTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private val REQUEST_CODE = 100 // Request code for permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Check and request permissions at runtime
        checkAndRequestPermissions()

        if (!Environment.isExternalStorageManager()) {
            val intent =
                Intent().apply {
                    action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                }
            startActivity(intent)
        }


        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            DriverPracticeTheme {
                var currentPage by rememberSaveable { mutableStateOf(Page.MATCH_INFO) }

                //Back button
                BackHandler(enabled = currentPage != Page.MATCH_INFO) {
                    when (currentPage) {
                        Page.AUTO -> currentPage = Page.MATCH_INFO
                        Page.TELE -> currentPage = Page.AUTO
                        Page.ENDGAME -> currentPage = Page.TELE
                        Page.RESULTS -> currentPage = Page.ENDGAME
                        else -> {}
                    }
                }

                when (currentPage) {
                    Page.MATCH_INFO -> MatchInfo(
                        state = state,
                        updateState = { viewModel.state.value = it },
                        onStart = { currentPage = Page.AUTO },
                        onHistoryLoad = { currentPage = Page.RESULTS }
                    )

                    Page.AUTO -> Auto(
                        state = state,
                        updateState = { viewModel.state.value = it },
                        toTele = { currentPage = Page.TELE }
                    )

                    Page.TELE -> Tele(
                        state = state,
                        updateState = { viewModel.state.value = it },
                        toEndgame = { currentPage = Page.ENDGAME }
                    )

                    Page.ENDGAME -> Endgame(
                        state = state,
                        updateState = { viewModel.state.value = it },
                        onSubmit = { currentPage = Page.RESULTS }
                    )

                    Page.RESULTS -> Results(
                        state = state,
                        updateState = { viewModel.state.value = it },
                        onSubmit = { currentPage = Page.MATCH_INFO }
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        )

        // Check if permissions are already granted
        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            // Request missing permissions
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }

            if (deniedPermissions.isNotEmpty()) {
                // Permissions were denied, handle as needed
                println("Permissions denied: $deniedPermissions")
            } else {
                // All permissions granted
                println("All permissions granted")
            }
        }
    }
}

enum class Page { MATCH_INFO, AUTO, TELE, ENDGAME, RESULTS }
