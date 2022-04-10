package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.theme.HansungCafeteriaTheme
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HansungCafeteria() {
    HansungCafeteriaTheme {
        val scaffoldState = rememberScaffoldState()
        val homeViewModel by remember { mutableStateOf(HomeViewModel(scaffoldState)) }
        val tabs = remember {
            listOf(
                TabItem.Monday,
                TabItem.Tuesday,
                TabItem.Wednesday,
                TabItem.Thursday,
                TabItem.Friday
            )
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { HansungTopAppBar(homeViewModel = homeViewModel) }
        ) {
            Column {
                TabBar(tabs = tabs, homeViewModel = homeViewModel)
                HansungHorizontalPager(
                    tabs = tabs,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
fun HansungTopAppBar(homeViewModel: HomeViewModel) {
    TopAppBar(contentPadding = PaddingValues(horizontal = 24.dp)) {
        Text(text = "한성대 학생 식당 ${homeViewModel.currentDuration}")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HansungCafeteria()
}