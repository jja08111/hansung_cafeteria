@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.foundy.hansungcafeteria.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.foundy.hansungcafeteria.exception.InternetNotConnectedException
import com.foundy.hansungcafeteria.repository.DailyMenuRepositoryImpl
import com.google.accompanist.pager.ExperimentalPagerApi
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(DelicateCoroutinesApi::class)
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

    @OptIn(ExperimentalPagerApi::class)
    @Test
    fun currentPageShouldBeZero_whenDateOfFirstDataIsTomorrow() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 27, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            DailyMenuRepositoryImpl(testUrl = URL),
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            HomeView(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.job?.join()
        }

        assertEquals(homeViewModel.pagerState.currentPage, 0)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Test
    fun currentPageShouldBeZero_whenTodayIsMondayAndDataInRange() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 28, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            DailyMenuRepositoryImpl(testUrl = URL),
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            HomeView(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.job?.join()
        }

        assertEquals(homeViewModel.pagerState.currentPage, 0)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Test
    fun currentPageShouldBeOne_whenTodayIsTuesdayAndDataInRange() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 29, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            DailyMenuRepositoryImpl(testUrl = URL),
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            HomeView(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.job?.join()
        }

        assertEquals(homeViewModel.pagerState.currentPage, 1)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Test
    fun currentPageShouldBeFour_whenDateOfLastDataIsYesterday() {
        DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 4, 4, 14, 0).millis)

        val homeViewModel = HomeViewModel(
            DailyMenuRepositoryImpl(testUrl = URL),
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            HomeView(homeViewModel = homeViewModel)
        }

        runBlocking {
            homeViewModel.job?.join()
        }

        assertEquals(homeViewModel.pagerState.currentPage, 4)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Test
    fun showErrorPage_whenInternetIsNotConnected() {
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(URL) } throws InternetNotConnectedException()

        val homeViewModel = HomeViewModel(
            DailyMenuRepositoryImpl(testUrl = URL),
            coroutineContext = coroutineContext
        )

        composeTestRule.setContent {
            HomeView(homeViewModel = homeViewModel)
        }

        runBlocking {
            awaitFrame()
        }

        assertEquals(
            homeViewModel.scaffoldState.snackbarHostState.currentSnackbarData != null,
            true
        )
        composeTestRule
            .onAllNodesWithText("인터넷에 연결할 수 없습니다.")
            .onFirst()
            .assertIsDisplayed()
    }
}