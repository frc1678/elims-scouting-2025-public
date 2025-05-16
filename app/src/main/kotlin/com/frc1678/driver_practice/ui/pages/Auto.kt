package com.frc1678.driver_practice.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frc1678.driver_practice.State
import com.frc1678.driver_practice.ui.common.LevelRow

@Composable

fun Auto(state: State, updateState: (State) -> Unit, toTele: () -> Unit) {

    val scalePercent = 70
    val boxWidth: Dp = (250 * scalePercent / 100).dp
    val buttonWidth: Dp = (90 * scalePercent / 100).dp
    val buttonHeight: Dp = (45 * scalePercent / 100).dp
    val textSize = (20f * scalePercent / 100).sp

    val levels: List<Triple<String, Pair<Int, Int>, (Int, Int) -> State>> = listOf(
        Triple("L4", state.autoL4 to state.autoL4fail) { count, fail -> state.copy(autoL4 = count, autoL4fail = fail) },
        Triple("L3", state.autoL3 to state.autoL3fail) { count, fail -> state.copy(autoL3 = count, autoL3fail = fail) },
        Triple("L2", state.autoL2 to state.autoL2fail) { count, fail -> state.copy(autoL2 = count, autoL2fail = fail) },
        Triple("L1", state.autoL1 to state.autoL1fail) { count, fail -> state.copy(autoL1 = count, autoL1fail = fail) }
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val brighterCardColor = Color(0xFFFAF4ED)
            val failedColor = Color(0xFFFF4B4B)
            val pastelPurple = Color(0xFFE1BEE7)
            val pastelGreen = Color(0xFFB2DFDB)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .background(color = pastelPurple, shape = RoundedCornerShape(12.dp))
                    .padding(1.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    levels.forEach { (label, counts, updateStateFn) ->
                        LevelRow(
                            label = label,
                            count = counts.first,
                            failedCount = counts.second,
                            brighterCardColor = brighterCardColor,
                            failedColor = failedColor,
                            onIncrement = { updateState(updateStateFn(counts.first + 1, counts.second)) },
                            onDecrement = { updateState(updateStateFn(maxOf(0, counts.first - 1), counts.second)) },
                            onFailedIncrement = { updateState(updateStateFn(counts.first, counts.second + 1)) },
                            onFailedDecrement = { updateState(updateStateFn(counts.first, maxOf(0, counts.second - 1))) },
                            boxWidth = boxWidth,
                            buttonWidth = buttonWidth,
                            buttonHeight = buttonHeight,
                            textSize = textSize,
                            compact = true
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .background(color = pastelGreen, shape = RoundedCornerShape(12.dp))
                    .padding(1.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LevelRow(
                        label = "Net",
                        count = state.autoNet,
                        failedCount = state.autoNetFail,
                        brighterCardColor = brighterCardColor,
                        failedColor = failedColor,
                        onIncrement = { updateState(state.copy(autoNet = state.autoNet + 1)) },
                        onDecrement = { updateState(state.copy(autoNet = maxOf(0, state.autoNet - 1))) },
                        onFailedIncrement = { updateState(state.copy(autoNetFail = state.autoNetFail + 1)) },
                        onFailedDecrement = { updateState(state.copy(autoNetFail = maxOf(0, state.autoNetFail - 1))) },
                        boxWidth = boxWidth,
                        buttonWidth = buttonWidth,
                        buttonHeight = buttonHeight,
                        textSize = textSize,
                        compact = true
                    )
                    LevelRow(
                        label = "Processor",
                        count = state.autoProcessor,
                        failedCount = state.autoProcessorFail,
                        brighterCardColor = brighterCardColor,
                        failedColor = failedColor,
                        onIncrement = { updateState(state.copy(autoProcessor = state.autoProcessor + 1)) },
                        onDecrement = { updateState(state.copy(autoProcessor = maxOf(0, state.autoProcessor - 1))) },
                        onFailedIncrement = { updateState(state.copy(autoProcessorFail = state.autoProcessorFail + 1)) },
                        onFailedDecrement = { updateState(state.copy(autoProcessorFail = maxOf(0, state.autoProcessorFail - 1))) },
                        boxWidth = boxWidth,
                        buttonWidth = buttonWidth,
                        buttonHeight = buttonHeight,
                        textSize = textSize,
                        compact = true
                    )
                }
            }

            Card(
                onClick = { toTele() },
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Text(
                    "To Teleop",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}
