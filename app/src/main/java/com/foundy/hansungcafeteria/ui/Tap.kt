package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.model.Menu
import com.foundy.hansungcafeteria.model.MenuDivision
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
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
fun HansungHorizontalPager(homeViewModel: HomeViewModel) {
    HorizontalPager(
        state = homeViewModel.pagerState,
        count = tabs.size,
        modifier = Modifier.fillMaxHeight()
    ) {
        tabs[it].screen(homeViewModel)
    }
}

@Composable
fun TapView(weekday: Int, homeViewModel: HomeViewModel) {
    val dailyMenuList = remember { homeViewModel.dailyMenus }

    if (dailyMenuList.isEmpty()) {
        Text("로딩중...")
    } else {
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
}

@Composable
fun MenuDivisionCard(division: MenuDivision) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                division.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column {
                for (menu in division.menus) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            menu.name,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "${menu.priceWithComma}원",
                            style = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.6F
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class TabItem(
    private val weekday: Int,
    val screen: @Composable (homeViewModel: HomeViewModel) -> Unit
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

@Preview(name = "DivisionCard")
@Composable
fun MenuDivisionCardPreview() {
    MenuDivisionCard(
        division = MenuDivision(
            name = "찌개&분식",
            menus = listOf(
                Menu("메뉴1", 1000),
                Menu("메뉴2", 5000),
                Menu("메뉴3", 5200),
            )
        )
    )
}