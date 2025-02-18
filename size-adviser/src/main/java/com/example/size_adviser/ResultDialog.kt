package com.example.size_adviser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun ResultDialog(
    size: SizeAdviser.ProposedSize,
    onDismiss: () -> Unit
) {
    val sizeValue = size.stringValue()
    Surface {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Your Recommended Size: $sizeValue")},
            text = {
                Text("Based on your info, size $sizeValue is recommended.")
            },
            buttons = {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = onDismiss
                    ) {
                        Text("OK")
                    }
                }
            }
        )
    }
}

private fun SizeAdviser.ProposedSize.stringValue(): String {
    return when(this) {
        SizeAdviser.ProposedSize.S -> "S"
        SizeAdviser.ProposedSize.M -> "M"
        SizeAdviser.ProposedSize.L -> "L"
        SizeAdviser.ProposedSize.XL -> "XL"
    }
}

@Preview
@Composable
private fun Preview() {
    ResultDialog(SizeAdviser.ProposedSize.M) {}
}