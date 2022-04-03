package com.foundy.hansungcafeteria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        @BeforeClass
        fun setUp() {
            DateTimeZone.setDefault(DateTimeZone.UTC)
        }

        /**
         * 3/28 ~ 4/03 기간의 식단을 보이는 url
         */
        // TODO: 실제 웹 주소에서 데이터를 얻지 않고 Mock을 이용하기
        const val URL =
            "https://www.hansung.ac.kr/hansung/1920/subview.do?enc=Zm5jdDF8QEB8JTJGZGlldCUyRmhhbnN1bmclMkYyJTJGdmlldy5kbyUzRm1vbmRheSUzRDIwMjIuMDQuMDQlMjZ3ZWVrJTNEcHJlJTI2"
    }

    @OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun currentPageShouldBeZero_whenDateOfFirstDataIsTomorrow() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 27, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            searchUrl = URL,
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            FakeHomeScreen(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.apply {
                job.join()
                assertEquals(pagerState.currentPage, 0)
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun currentPageShouldBeZero_whenTodayIsMondayAndDataInRange() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 28, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            searchUrl = URL,
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            FakeHomeScreen(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.apply {
                job.join()
                assertEquals(pagerState.currentPage, 0)
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun currentPageShouldBeOne_whenTodayIsTuesdayAndDataInRange() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 29, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            searchUrl = URL,
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            FakeHomeScreen(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.apply {
                job.join()
                assertEquals(pagerState.currentPage, 1)
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun currentPageShouldBeFour_whenDateOfLastDataIsYesterday() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 4, 4, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            searchUrl = URL,
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            FakeHomeScreen(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.apply {
                job.join()
                assertEquals(pagerState.currentPage, 4)
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun FakeHomeScreen(homeViewModel: HomeViewModel) {
        Column {
            TabBar(tabs = tabs, homeViewModel = homeViewModel)
            HansungHorizontalPager(
                homeViewModel = homeViewModel
            )
        }
    }
}