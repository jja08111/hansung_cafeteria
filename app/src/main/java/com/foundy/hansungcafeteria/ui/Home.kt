package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.theme.HansungCafeteriaTheme
import com.google.accompanist.pager.*

@Composable
fun HansungCafeteria() {
    HansungCafeteriaTheme {
        val homeViewModel by remember { mutableStateOf(HomeViewModel()) }

        HomeView(homeViewModel = homeViewModel)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeView(homeViewModel: HomeViewModel) {
    Scaffold(
        scaffoldState = homeViewModel.scaffoldState,
        topBar = { HansungTopAppBar(homeViewModel = homeViewModel) }
    ) {
        val tabs = remember {
            listOf(
                TabItem.Monday,
                TabItem.Tuesday,
                TabItem.Wednesday,
                TabItem.Thursday,
                TabItem.Friday
            )
        }

        Column {
            TabBar(tabs = tabs, homeViewModel = homeViewModel)
            HansungHorizontalPager(
                tabs = tabs,
                homeViewModel = homeViewModel
            )
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