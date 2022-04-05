package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.components.MenuDivisionCard
import com.foundy.hansungcafeteria.ui.components.TapViewShimmer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import org.joda.time.DateTimeConstants
import kotlin.Exception

val tabs = listOf(
    TabItem.Monday,
    TabItem.Tuesday,
    TabItem.Wednesday,
    TabItem.Thursday,
    TabItem.Friday
)

@ExperimentalPagerApi
@Composable
fun HansungHorizontalPager(homeViewModel: HomeViewModel) {
    HorizontalPager(
        state = homeViewModel.pagerState,
        count = tabs.size,
        modifier = Modifier.fillMaxHeight()
    ) {
        if (homeViewModel.dailyMenus.isEmpty()) {
            TapViewShimmer()
        } else {
            tabs[it].view(homeViewModel)
        }
    }
}

/**
 * @param homeViewModel [HomeViewModel.dailyMenus]는 반드시 비어있으면 안된다.
 */
@Composable
fun TapView(weekday: Int, homeViewModel: HomeViewModel) {
    val dailyMenuList = remember { homeViewModel.dailyMenus }

    if (dailyMenuList.isEmpty()) {
        throw Exception("비어있는 식단 리스트를 가진 HomeViewModel을 전달했습니다. 초기화가 되어있는 경우만 이용하세요.")
    }

    val scrollState = rememberScrollState()
    val dailyMenu = dailyMenuList[weekday]

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                dailyMenu.date.toString("yyyy년 M월 d일"),
                style = MaterialTheme.typography.subtitle1.copy(
                    color = MaterialTheme.colors.onBackground.copy(0.6F)
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            for (division in dailyMenu.menuDivisions) {
                MenuDivisionCard(division)
            }
        }
    }
}

sealed class TabItem(
    private val weekday: Int,
    val view: @Composable (homeViewModel: HomeViewModel) -> Unit
) {
    fun weekdayString(): String = weekday.toWeekLocaleShort()

    object Monday :
        TabItem(
            DateTimeConstants.MONDAY,
            { TapView(weekday = DateTimeConstants.MONDAY, homeViewModel = it) })

    object Tuesday :
        TabItem(
            DateTimeConstants.TUESDAY,
            { TapView(weekday = DateTimeConstants.TUESDAY, homeViewModel = it) })

    object Wednesday :
        TabItem(
            DateTimeConstants.WEDNESDAY,
            { TapView(weekday = DateTimeConstants.WEDNESDAY, homeViewModel = it) })

    object Thursday :
        TabItem(
            DateTimeConstants.THURSDAY,
            { TapView(weekday = DateTimeConstants.THURSDAY, homeViewModel = it) })

    object Friday :
        TabItem(
            DateTimeConstants.FRIDAY,
            { TapView(weekday = DateTimeConstants.FRIDAY, homeViewModel = it) })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun TabBar(tabs: List<TabItem>, homeViewModel: HomeViewModel) {
    val pagerState = homeViewModel.pagerState
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
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