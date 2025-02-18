package com.example.size_adviser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InputData(
    val weight: Double,
    val height: Double
)

@Composable
internal fun InputDialog(
    onSubmit: (data: InputData?) -> Unit
) {

    val viewModel = viewModel<DialogViewModel>()
    val openDialog = remember { mutableStateOf(true) }

    val height = viewModel.height.collectAsState().value
    val weight = viewModel.weight.collectAsState().value

    Surface {
        AlertDialog(
            onDismissRequest = {
                onSubmit.invoke(null)
                openDialog.value = false
            },
            title = { Text("Find Your Perfect Fit") },
            text = {
                Column {
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {
                        TextField(
                            label = { Text("Height") },
                            placeholder = { Text("cm") },
                            value = height,
                            onValueChange = { value -> viewModel.height.update { value } }
                        )
                    }

                    Row {
                        TextField(
                            label = { Text("Weight") },
                            placeholder = { Text("kg") },
                            value = weight,
                            onValueChange = { value -> viewModel.weight.update { value } }
                        )
                    }
                }
            },
            buttons = {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        enabled = viewModel.isValid,
                        onClick = {
                            onSubmit.invoke(viewModel.inputData)
                            openDialog.value = false
                        }
                    ) {
                        Text("Get Size Recommendation")
                    }
                }

            }
        )
    }
}

internal class DialogViewModel: ViewModel() {
    var weight = MutableStateFlow("")
    var height = MutableStateFlow("")

    var inputData: InputData? = null
        private set

    val isValid: Boolean
        get() = inputData != null

    init {
        viewModelScope.launch {
            combine(weight, height) { weightVal, heightVal ->
                Pair(weightVal.toDoubleOptional(), heightVal.toDoubleOptional())
            }
                .collect { pair ->
                    val weight = pair.first
                    val height = pair.second
                    if (weight == null || height == null) {
                        inputData = null
                        return@collect
                    }

                    inputData = InputData(weight = weight, height = height)
                }
        }
    }
}

private fun String.toDoubleOptional(): Double? {
    return try {
        this.toDouble()
    } catch(_: Exception) {
        null
    }
}

@Preview
@Composable
internal fun DefaultPreview() {
    InputDialog {  }
}