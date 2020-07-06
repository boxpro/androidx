/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.ui.desktop.examples.popupexample

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.desktop.AppManager
import androidx.ui.desktop.AppWindow
import androidx.ui.desktop.Dialog
import androidx.ui.desktop.core.Popup
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.height
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredSize
import androidx.ui.layout.preferredWidth
import androidx.ui.layout.width
import androidx.ui.material.Button
import androidx.ui.material.Surface
import androidx.ui.unit.IntSize
import androidx.ui.unit.IntOffset
import androidx.ui.unit.dp

@Composable
fun content() {

    val popupState = state { false }
    val dialogState = state { false }
    val amount = state { 0 }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(Modifier.gravity(Alignment.CenterHorizontally)) {
            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.preferredHeight(40.dp)) {
                Button("Popup", { popupState.value = true })
                Spacer(modifier = Modifier.width(50.dp))
                Button("Dialog window", { dialogState.value = true })
                Spacer(modifier = Modifier.width(50.dp))
                Button(
                    text = "Second window: ${amount.value}",
                    onClick = {
                        AppWindow(
                            title = "Second window",
                            size = IntSize(500, 300),
                            onDismissEvent = {
                                println("Second window is dismissed.")
                            }
                        ).show {
                            WindowContent(amount, onClose = {
                                AppManager.getCurrentFocusedWindow()?.close()
                            })
                        }
                    }
                )
            }
        }
    }

    PopupSample(
        displayed = popupState.value,
        onDismiss = {
            popupState.value = false
            println("Popup is dismissed.")
        }
    )
    if (popupState.value) {
        // To make sure the popup is displayed on the top.
        Box(
            Modifier.fillMaxSize(),
            backgroundColor = Color(10, 162, 232, 200)
        )
    }

    if (dialogState.value) {
        val dismiss = {
            dialogState.value = false
            println("Dialog window is dismissed.")
        }
        Dialog(
            title = "Dialog window",
            size = IntSize(500 + amount.value * 10, 300 + amount.value * 10),
            onDismissEvent = dismiss
        ) {
            WindowContent(amount, onClose = dismiss)
        }
    }
}

@Composable
fun PopupSample(displayed: Boolean, onDismiss: () -> Unit) {
    Box(
        Modifier.fillMaxSize()
    ) {
        if (displayed) {
            Popup(
                alignment = Alignment.Center,
                offset = IntOffset(100, 100),
                isFocusable = true,
                onDismissRequest = onDismiss
            ) {
                PopupContent(onDismiss)
            }
        }
    }
}

@Composable
fun PopupContent(onDismiss: () -> Unit) {
    Box(
        Modifier.preferredSize(300.dp, 150.dp),
        backgroundColor = Color.Gray,
        shape = RoundedCornerShape(4.dp),
        gravity = ContentGravity.Center
    ) {
        Column {
            Text(text = "Are you sure?")
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = { onDismiss.invoke() },
                modifier = Modifier.gravity(Alignment.CenterHorizontally)
            ) {
                Text(text = "Yes")
            }
        }
    }
}

@Composable
fun WindowContent(amount: MutableState<Int>, onClose: () -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        backgroundColor = Color.White,
        gravity = ContentGravity.Center
    ) {
        Box(
            Modifier.preferredSize(300.dp, 150.dp).gravity(Alignment.CenterHorizontally),
            backgroundColor = Color.Gray,
            shape = RoundedCornerShape(4.dp),
            gravity = ContentGravity.Center
        ) {
            Column() {
                Text(text = "Increment value?")
                Spacer(modifier = Modifier.height(50.dp))
                Row() {
                    Button(
                        onClick = { amount.value++ }
                    ) {
                        Text(text = "Yes")
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        onClick = { onClose.invoke() }
                    ) {
                        Text(text = "Close")
                    }
                }
            }
        }
    }
}

@Composable
fun Button(
    text: String = "Button",
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        backgroundColor = Color(10, 162, 232),
        modifier = Modifier.preferredHeight(40.dp)
            .preferredWidth(200.dp)
    ) {
        Text(text = text)
    }
}