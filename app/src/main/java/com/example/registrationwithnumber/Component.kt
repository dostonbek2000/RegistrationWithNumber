package com.example.registrationwithnumber

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OTPTextFields(
    modifier: Modifier = Modifier,
    length: Int,
    onFilled: (code: String) -> Unit
) {
    var code: List<Char> by remember { mutableStateOf(listOf()) }
    var focusRequesters: List<FocusRequester> = remember {
        val temp = mutableListOf<FocusRequester>()
        repeat(length) {
            temp.add(FocusRequester())
        }
        temp
    }

    Row(
        modifier = Modifier.height(50.dp)
    ) {
        (0 until length).forEach { index ->
            OutlinedTextField(
                modifier = Modifier

                    .width(50.dp)
                    .height(50.dp)
                    .clickable{
                        focusRequesters = listOf(focusRequesters[index])
                            focusRequesters[index + 1].requestFocus()
                        }
                    // FocusRequester is not initialized , this error was fixed with adding .focusRequester(focusRequesters[index])
                    .focusRequester(focusRequesters[index]),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center, color = Color.Black
                ),
                singleLine = true,
                value = code.getOrNull(index = index)?.takeIf {
                    it.isDigit()
                }?.toString() ?: "",
                onValueChange = { value: String ->
                    if (focusRequesters[index].freeFocus()) {
                        val temp = code.toMutableList()
                        if (value == "") {
                            if (temp.size > index) {
                                temp.removeAt(index = index)
                                code = temp
                                focusRequesters.getOrNull(index - 1)?.requestFocus()
                            }
                        } else {
                            if (code.size > index) {
                                temp[index] = value.getOrNull(0)?: ' '
                            } else {
                                temp.add(value.getOrNull(0) ?: ' ')
                                code = temp
                                focusRequesters.getOrNull(index + 1)?.requestFocus() ?: onFilled(
                                    code.joinToString(separator = "")
                                )
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}


















