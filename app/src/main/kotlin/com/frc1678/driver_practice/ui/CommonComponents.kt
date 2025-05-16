package com.frc1678.driver_practice.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun LevelRow(
    label: String,
    count: Int,
    failedCount: Int,
    brighterCardColor: Color,
    failedColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onFailedIncrement: () -> Unit,
    onFailedDecrement: () -> Unit,
    boxWidth: Dp = 300.dp,
    buttonWidth: Dp = 100.dp,
    buttonHeight: Dp = 60.dp,
    textSize: TextUnit = MaterialTheme.typography.displayMedium.fontSize,
    compact: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Success Box
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(if (compact) 4.dp else 8.dp) // Adjusted padding for compact mode
                .background(brighterCardColor, shape = RoundedCornerShape(12.dp))
                .padding(if (compact) 8.dp else 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp)
            ) {
                Text(text = "$label: $count", fontSize = textSize)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDecrement,
                        modifier = Modifier
                            .weight(1f)
                            .height(if (compact) (buttonHeight * 1.6f) else buttonHeight), // Adjusted height for compact mode
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) { Text("-", fontSize = textSize) }
                    Button(
                        onClick = onIncrement,
                        modifier = Modifier
                            .weight(1f)
                            .height(if (compact) (buttonHeight * 1.6f) else buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) { Text("+", fontSize = textSize) }
                }
            }
        }

        // Failed Box
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(if (compact) 4.dp else 8.dp)
                .background(failedColor, shape = RoundedCornerShape(12.dp))
                .padding(if (compact) 8.dp else 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp)
            ) {
                Text(text = "$label Failed: $failedCount", fontSize = textSize, color = Color.White)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onFailedDecrement,
                        modifier = Modifier
                            .weight(1f)
                            .height(if (compact) (buttonHeight * 1.6F) else buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) { Text("-", fontSize = textSize) }
                    Button(
                        onClick = onFailedIncrement,
                        modifier = Modifier
                            .weight(1f)
                            .height(if (compact) (buttonHeight * 1.6F) else buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) { Text("+", fontSize = textSize) }
                }
            }
        }
    }
}
