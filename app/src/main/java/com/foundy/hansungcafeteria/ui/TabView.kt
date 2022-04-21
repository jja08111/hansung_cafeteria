package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.ui.component.MenuDivisionCard
import com.foundy.hansungcafeteria.ui.component.TabViewShimmer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import org.joda.time.DateTimeConstants
import kotlin.Exception

@ExperimentalPagerApi
@Composable
fun HansungHorizontalPager(tabs: List<TabItem>, homeViewModel: HomeViewModel) {
    HorizontalPager(
        state = homeViewModel.pagerState,
        count = tabs.size,
        modifier = Modifier.fillMaxHeight()
    ) {
        if (!homeViewModel.isConnected.value) {
            InternetNotConnectedView(
                onClickRefresh = {
                    homeViewModel.updateDailyMenus()
                }
            )
        } else if (homeViewModel.dailyMenus.isEmpty()) {
            TabViewShimmer()
        } else {
            tabs[it].View(homeViewModel)
        }
    }
}

sealed class TabItem(
    private val weekday: Int,
) {
    object Monday : TabItem(DateTimeConstants.MONDAY)
    object Tuesday : TabItem(DateTimeConstants.TUESDAY)
    object Wednesday : TabItem(DateTimeConstants.WEDNESDAY)
    object Thursday : TabItem(DateTimeConstants.THURSDAY)
    object Friday : TabItem(DateTimeConstants.FRIDAY)

    fun weekdayString(): String = weekday.toWeekLocaleShort()

    /**
     * @param homeViewModel [HomeViewModel.dailyMenus]는 반드시 초기화 되어있어야 한다.
     */
    @Composable
    fun View(homeViewModel: HomeViewModel) {
        if (homeViewModel.dailyMenus.isEmpty()) {
            throw Exception("비어있는 식단 리스트를 가진 HomeViewModel을 전달했습니다. 초기화가 되어있는 경우만 이용하세요.")
        }

        val scrollState = rememberScrollState()
        val dailyMenu = homeViewModel.dailyMenus[weekday]

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