package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InternetNotConnectedPage(onClickRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Warning,
            contentDescription = "인터넷",
            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.3F),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(60.dp)
        )
        Text(text = "인터넷에 연결할 수 없습니다.")
        TextButton(onClick = onClickRefresh) {
            Text("새로고침")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InternetNotConnectedPagePreview() {
    InternetNotConnectedPage(onClickRefresh = {})
}