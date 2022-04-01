package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import org.joda.time.DateTimeConstants
import java.lang.Exception

val tabs = listOf(
    TabItem.Monday,
    TabItem.Tuesday,
    TabItem.Wednesday,
    TabItem.Thursday,
    TabItem.Friday
)

@ExperimentalPagerApi
@Composable
fun HansungHorizontalPager(pagerState: PagerState) {
    val homeViewModel by remember { mutableStateOf(PagerViewModel()) }

    HorizontalPager(
        state = pagerState,
        count = tabs.size,
        modifier = Modifier.fillMaxHeight()
    ) {
        tabs[it].screen(homeViewModel)
    }
}

@Composable
fun TapView(weekday: Int, pagerViewModel: PagerViewModel) {
    val dailyMenus = remember { pagerViewModel.dailyMenus }

    if (dailyMenus.isEmpty()) {
        Text("로딩중...")
    } else {
        Text(
            modifier = Modifier.padding(16.dp),
            text = dailyMenus[weekday].toString()
        )
    }
}

sealed class TabItem(
    private val weekday: Int,
    val screen: @Composable (pagerViewModel: PagerViewModel) -> Unit
) {
    fun weekdayString(): String = weekday.toWeekLocaleShort()

    object Monday :
        TabItem(
            DateTimeConstants.MONDAY,
            { TapView(weekday = DateTimeConstants.MONDAY, pagerViewModel = it) })

    object Tuesday :
        TabItem(
            DateTimeConstants.TUESDAY,
            { TapView(weekday = DateTimeConstants.TUESDAY, pagerViewModel = it) })

    object Wednesday :
        TabItem(
            DateTimeConstants.WEDNESDAY,
            { TapView(weekday = DateTimeConstants.WEDNESDAY, pagerViewModel = it) })

    object Thursday :
        TabItem(
            DateTimeConstants.THURSDAY,
            { TapView(weekday = DateTimeConstants.THURSDAY, pagerViewModel = it) })

    object Friday :
        TabItem(
            DateTimeConstants.FRIDAY,
            { TapView(weekday = DateTimeConstants.FRIDAY, pagerViewModel = it) })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun TabBar(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = { Text(tab.weekdayString()) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

/**
 * Using for [DateTimeConstants] values.
 */
fun Int.toWeekLocaleShort(): String {
    return when (this) {
        DateTimeConstants.MONDAY -> "월"
        DateTimeConstants.TUESDAY -> "화"
        DateTimeConstants.WEDNESDAY -> "수"
        DateTimeConstants.THURSDAY -> "목"
        DateTimeConstants.FRIDAY -> "금"
        DateTimeConstants.SATURDAY -> "토"
        DateTimeConstants.SUNDAY -> "일"
        else -> throw Exception("잘못된 값을 전달했습니다. 요일을 맞게 전달했는지 확인하세요.")
    }
}