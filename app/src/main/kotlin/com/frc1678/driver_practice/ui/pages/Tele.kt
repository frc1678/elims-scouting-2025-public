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
fun Tele(state: State, updateState: (State) -> Unit, toEndgame: () -> Unit) {
    val scalePercent = 70
    val boxWidth: Dp = (250 * scalePercent / 100).dp
    val buttonWidth: Dp = (90 * scalePercent / 100).dp
    val buttonHeight: Dp = (45 * scalePercent / 100).dp
    val textSize = (20f * scalePercent / 100).sp

    val levels: List<Triple<String, Pair<Int, Int>, (Int, Int) -> State>> = listOf(
        Triple("L4", state.teleL4 to state.teleL4fail) { count, fail -> state.copy(teleL4 = count, teleL4fail = fail) },
        Triple("L3", state.teleL3 to state.teleL3fail) { count, fail -> state.copy(teleL3 = count, teleL3fail = fail) },
        Triple("L2", state.teleL2 to state.teleL2fail) { count, fail -> state.copy(teleL2 = count, teleL2fail = fail) },
        Triple("L1", state.teleL1 to state.teleL1fail) { count, fail -> state.copy(teleL1 = count, teleL1fail = fail) }
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
                        count = state.teleNet,
                        failedCount = state.teleNetFail,
                        brighterCardColor = brighterCardColor,
                        failedColor = failedColor,
                        onIncrement = { updateState(state.copy(teleNet = state.teleNet + 1)) },
                        onDecrement = { updateState(state.copy(teleNet = maxOf(0, state.teleNet - 1))) },
                        onFailedIncrement = { updateState(state.copy(teleNetFail = state.teleNetFail + 1)) },
                        onFailedDecrement = { updateState(state.copy(teleNetFail = maxOf(0, state.teleNetFail - 1))) },
                        boxWidth = boxWidth,
                        buttonWidth = buttonWidth,
                        buttonHeight = buttonHeight,
                        textSize = textSize,
                        compact = true
                    )
                    LevelRow(
                        label = "Processor",
                        count = state.teleProcessor,
                        failedCount = state.teleProcessorFail,
                        brighterCardColor = brighterCardColor,
                        failedColor = failedColor,
                        onIncrement = { updateState(state.copy(teleProcessor = state.teleProcessor + 1)) },
                        onDecrement = { updateState(state.copy(teleProcessor = maxOf(0, state.teleProcessor - 1))) },
                        onFailedIncrement = { updateState(state.copy(teleProcessorFail = state.teleProcessorFail + 1)) },
                        onFailedDecrement = { updateState(state.copy(teleProcessorFail = maxOf(0, state.teleProcessorFail - 1))) },
                        boxWidth = boxWidth,
                        buttonWidth = buttonWidth,
                        buttonHeight = buttonHeight,
                        textSize = textSize,
                        compact = true
                    )
                }
            }

            Card(
                onClick = { toEndgame() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "To Endgame",
                        modifier = Modifier.padding(6.dp),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}
