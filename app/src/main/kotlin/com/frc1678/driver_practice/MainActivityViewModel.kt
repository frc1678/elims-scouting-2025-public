package com.frc1678.driver_practice

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {
    var state = MutableStateFlow(State())
}
