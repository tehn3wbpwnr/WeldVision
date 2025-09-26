package com.example.weldvision.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.weldvision.viewmodel.ImageProcessingViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.weldvision.model.GrooveSymbol
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun ConfirmationScreen(
    processingViewModel: ImageProcessingViewModel, onConfirm: () -> Unit, onCancel: () -> Unit
)
{
    val grooveData = processingViewModel.grooveData.value

    var arrowGroove by remember { mutableStateOf(grooveData?.arrowGroove ?: "None") }
    var otherGroove by remember { mutableStateOf(grooveData?.otherGroove ?: "None") }
    var arrowDepth by remember { mutableStateOf(grooveData?.arrowDepth?.toString() ?: "") }
    var otherDepth by remember { mutableStateOf(grooveData?.otherDepth?.toString() ?: "") }
    var rootOpening by remember { mutableStateOf(grooveData?.rootOpening?.toString() ?: "") }
    var arrowAngle by remember { mutableStateOf(grooveData?.arrowAngle?.toString() ?: "") }
    var otherAngle by remember { mutableStateOf(grooveData?.otherAngle?.toString() ?: "") }

    var warningMessage by remember { mutableStateOf("") }

    var arrowDepthError by remember { mutableStateOf(false) }
    var otherDepthError by remember { mutableStateOf(false) }
    var arrowAngleError by remember { mutableStateOf(false) }
    var otherAngleError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Confirm Extracted Data", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GrooveDropdownMenu(
                            label = "Arrow Groove",
                            selectedValue = arrowGroove.ifEmpty { "None" }, // Ensure "None" is used
                            onValueChange = { arrowGroove = it },
                            modifier = Modifier.weight(1f)
                        )
                        GrooveDropdownMenu(
                            label = "Other Groove",
                            selectedValue = otherGroove.ifEmpty { "None" }, // Ensure "None" is used
                            onValueChange = { otherGroove = it },
                            modifier = Modifier.weight(1f)
                        )
                        //OutlinedTextField(value = arrowGroove, onValueChange = { arrowGroove = it }, label = { Text("Arrow Groove") }, modifier = Modifier.weight(1f))
                        //OutlinedTextField(value = otherGroove, onValueChange = { otherGroove = it }, label = { Text("Other Groove") }, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        //OutlinedTextField(value = arrowDepth, onValueChange = { arrowDepth = it }, label = { Text("Arrow Depth") }, modifier = Modifier.weight(1f))
                        //OutlinedTextField(value = otherDepth, onValueChange = { otherDepth = it }, label = { Text("Other Depth") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = arrowDepth,
                            onValueChange = { arrowDepth = it },
                            label = { Text("Arrow Depth") },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (arrowDepthError) Color.Red else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        OutlinedTextField(
                            value = otherDepth,
                            onValueChange = { otherDepth = it },
                            label = { Text("Other Depth") },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (otherDepthError) Color.Red else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = rootOpening,
                            onValueChange = { rootOpening = it },
                            label = { Text("Root Opening") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        //OutlinedTextField(value = arrowAngle, onValueChange = { arrowAngle = it }, label = { Text("Arrow Angle") }, modifier = Modifier.weight(1f))
                        //OutlinedTextField(value = otherAngle, onValueChange = { otherAngle = it }, label = { Text("Other Angle") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = arrowAngle,
                            onValueChange = { arrowAngle = it },
                            label = { Text("Arrow Angle") },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (arrowAngleError) Color.Red else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        OutlinedTextField(
                            value = otherAngle,
                            onValueChange = { otherAngle = it },
                            label = { Text("Other Angle") },
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (otherAngleError) Color.Red else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }

            // New code to display the warning message if any
            Box(modifier = Modifier
                .height(20.dp)
            ) {
                if (warningMessage.isNotEmpty()) {
                    Text(text = warningMessage, color = Color.Red, fontSize = 12.sp)
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = onCancel) {
                        Text(text = "Back")

                    }
                    Button(onClick = {
                        arrowDepthError = false
                        otherDepthError = false
                        arrowAngleError = false
                        otherAngleError = false
                        if ( (arrowGroove == "None" || arrowGroove == "") && (arrowDepth.isNotEmpty() || arrowAngle.isNotEmpty()) ||
                            (otherGroove == "None" || otherGroove == "") && (otherDepth.isNotEmpty() || otherAngle.isNotEmpty())
                        ) {
                            // If groove is None but depth or angle is provided, show warning
                            warningMessage = "Cannot set depth or angle for an empty groove."

                            if ((arrowGroove == "None" || arrowGroove == "") && arrowDepth.isNotBlank()) arrowDepthError = true
                            if ((arrowGroove == "None" || arrowGroove == "") && arrowAngle.isNotBlank()) arrowAngleError = true
                            if ((otherGroove == "None" || otherGroove == "") && otherDepth.isNotBlank()) otherDepthError = true
                            if ((otherGroove == "None" || otherGroove == "") && otherAngle.isNotBlank()) otherAngleError = true
                        }
                        else {
                            warningMessage = ""
                            processingViewModel.updateGrooveData(
                                arrowGroove,
                                otherGroove,
                                arrowDepth.toDoubleOrNull(),
                                otherDepth.toDoubleOrNull(),
                                rootOpening.toDoubleOrNull(),
                                arrowAngle.toIntOrNull(),
                                otherAngle.toIntOrNull()
                            )
                            onConfirm()
                        }
                    }) {
                        Text(text = "Visualize")
                    }
                }
            }
        }
    }
}

@Composable
fun GrooveDropdownMenu(
    label: String,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("None", "Bevel", "V", "J", "U")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(text = label, fontSize = 14.sp, color = Color.White)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(12.dp)
        ) {
            Text(text = selectedValue, fontSize = 14.sp, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


