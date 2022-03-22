package com.foundy.hansungcafeteria.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Calendar

@Composable
fun TapView(weekday: Int) {
    Text(text = weekday.toLocaleShort())
}

sealed class TabItem(private val weekday: Int, val screen: @Composable () -> Unit) {
    fun weekdayString(): String = weekday.toLocaleShort()

    object Monday : TabItem(Calendar.MONDAY, { TapView(weekday = Calendar.MONDAY) })
    object Tuesday : TabItem(Calendar.TUESDAY, { TapView(weekday = Calendar.TUESDAY) })
    object Wednesday : TabItem(Calendar.WEDNESDAY, { TapView(weekday = Calendar.WEDNESDAY) })
    object Thursday : TabItem(Calendar.THURSDAY, { TapView(weekday = Calendar.THURSDAY) })
    object Friday : TabItem(Calendar.FRIDAY, { TapView(weekday = Calendar.FRIDAY) })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
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
 * Using for [Calendar.DAY_OF_WEEK] values.
 */
fun Int.toLocaleShort(): String {
    return when (this) {
        Calendar.MONDAY -> "월"
        Calendar.TUESDAY -> "화"
        Calendar.WEDNESDAY -> "수"
        Calendar.THURSDAY -> "목"
        Calendar.FRIDAY -> "금"
        Calendar.SATURDAY -> "토"
        Calendar.SUNDAY -> "일"
        else -> throw Exception("잘못된 값을 전달했습니다. 요일을 맞게 전달했는지 확인하세요.")
    }
}