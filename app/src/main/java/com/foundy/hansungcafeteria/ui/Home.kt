package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.theme.HansungCafeteriaTheme
import com.foundy.hansungcafeteria.util.getMonToFriDateString
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HansungCafeteria() {
    HansungCafeteriaTheme {
        val pagerState = rememberPagerState()

        Scaffold(
            topBar = { HansungTopAppBar() }
        ) {
            Column {
                TabBar(tabs = tabs, pagerState = pagerState)
                HansungHorizontalPager(pagerState = pagerState)
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