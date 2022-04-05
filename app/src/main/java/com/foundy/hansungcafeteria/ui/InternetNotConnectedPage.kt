package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InternetNotConnectedPage(onClickRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "인터넷에 연결되지 않았습니다.")
        TextButton(onClick = onClickRefresh) {
            Text("새로고침")
        }
    }
}

@Preview
@Composable
fun InternetNotConnectedPagePreview() {
    InternetNotConnectedPage(onClickRefresh = {})
}