package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.theme.HansungCafeteriaTheme
import com.foundy.hansungcafeteria.util.getMonToFriDateString
import com.google.accompanist.pager.*

val tabs = listOf(
    TabItem.Monday,
    TabItem.Tuesday,
    TabItem.Wednesday,
    TabItem.Thursday,
    TabItem.Friday
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HansungCafeteria() {
    HansungCafeteriaTheme {
        val pagerState = rememberPagerState()
        val homeViewModel by remember { mutableStateOf(HomeViewModel()) }

        Scaffold(
            topBar = { HansungTopAppBar() }
        ) {
            Column {
                TabBar(tabs = tabs, pagerState = pagerState)
                HorizontalPager(
                    state = pagerState,
                    count = tabs.size,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    tabs[it].screen(homeViewModel)
                }
            }
        }
    }
}

@Composable
fun HansungTopAppBar() {
    TopAppBar(contentPadding = PaddingValues(horizontal = 24.dp)) {
        val dateDurationString = remember { getMonToFriDateString() }
        Text(text = "한성대 학생 식당 ($dateDurationString)")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HansungCafeteria()
}