package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.theme.HansungCafeteriaTheme
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HansungCafeteria() {
    HansungCafeteriaTheme {
        val tabs = listOf(
            TabItem.Monday,
            TabItem.Tuesday,
            TabItem.Wednesday,
            TabItem.Thursday,
            TabItem.Friday
        )
        val pagerState = rememberPagerState()

        Scaffold(
            topBar = {
                TopAppBar(contentPadding = PaddingValues(horizontal = 24.dp)) {
                    Text(text = "한성대 학생 식당")
                }
            }
        ) {
            Column {
                Tabs(tabs = tabs, pagerState = pagerState)
                HorizontalPager(
                    state = pagerState,
                    count = tabs.size,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    tabs[it].screen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HansungCafeteria()
}