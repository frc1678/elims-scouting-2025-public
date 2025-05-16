package com.frc1678.driver_practice.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.frc1678.driver_practice.State

@Composable
fun Endgame(state: State, updateState: (State) -> Unit, onSubmit: () -> Unit) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val brighterCardColor = Color(0xFFFAF4ED)
            val boxColor = Color(0xFFA0DD9C)
            val textMod = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(32.dp)
            val padding = 5
            val cardMod = Modifier
                .padding(padding.dp)
                .fillMaxWidth()

            //Colored box
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .background(
                    color = boxColor,
                    shape = RoundedCornerShape(12.dp)
                )) {
                Spacer(modifier = Modifier.height(5.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    //Shallow Option
                    onClick = { updateState(state.copy(climbLevel = "shallow", climbSuccess = true)) },
                    modifier = Modifier
                        .padding(padding.dp)
                        .fillMaxWidth()
                        .let {
                            if (state.climbLevel == "shallow") {
                                it.border(2.dp, Color.Red, RoundedCornerShape(8.dp))
                            } else {
                                it
                            }
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),
                ) {
                    Text("Shallow", modifier = textMod, style = MaterialTheme.typography.displaySmall)
                }
                //Deep Option
                Card(
                    onClick = { updateState(state.copy(climbLevel = "deep", climbSuccess = true)) },
                    modifier = Modifier
                        .padding(padding.dp)
                        .fillMaxWidth()
                        .let {
                            if (state.climbLevel == "deep") {
                                it.border(2.dp, Color.Red, RoundedCornerShape(8.dp))
                            } else {
                                it
                            }
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),
                ) {
                    Text("Deep",
                        modifier = textMod,
                        style = MaterialTheme.typography.displaySmall)
                }
                //Park Option
                Card(
                    onClick = { updateState(state.copy(climbLevel = "park", climbSuccess = true)) },
                    modifier = Modifier
                        .padding(padding.dp)
                        .fillMaxWidth()
                        .let {
                            if (state.climbLevel == "park") {
                                it.border(2.dp, Color.Red, RoundedCornerShape(8.dp))
                            } else {
                                it
                            }
                        },elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),
                ) {
                    Text("Park", modifier = textMod, style = MaterialTheme.typography.displaySmall)
                }
                //None option
                Card(
                    onClick = { updateState(state.copy(climbLevel = "none", climbSuccess = false)) },
                    modifier = Modifier
                        .padding(padding.dp)
                        .fillMaxWidth()
                        .let {
                            if (state.climbLevel == "none") {
                                it.border(2.dp, Color.Red, RoundedCornerShape(8.dp))
                            } else {
                                it
                            }
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),
                ) {
                    Text("None", modifier = textMod, style = MaterialTheme.typography.displaySmall)
                }
                //Success or Fail
                Card(onClick = {
                    if (state.climbLevel != "none") {
                    updateState(state.copy(climbSuccess = !state.climbSuccess)) } }, modifier = cardMod,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = brighterCardColor
                    ),) {
                    Text("Success? ${state.climbSuccess}", modifier = textMod, style = MaterialTheme.typography.displaySmall)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }

        }
            Spacer(Modifier.height(30.dp))
            //Submit Button
            Card(onClick = onSubmit, modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )) {
                Text(
                    "Submit",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}
