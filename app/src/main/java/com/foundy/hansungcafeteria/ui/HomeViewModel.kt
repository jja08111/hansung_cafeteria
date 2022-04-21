package com.foundy.hansungcafeteria.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundy.hansungcafeteria.exception.InternetNotConnectedException
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.repository.DailyMenuRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.min
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class HomeViewModel(
    private val dailyMenuRepository: DailyMenuRepository = DailyMenuRepository(),
    @VisibleForTesting private val coroutineContext: CoroutineContext = EmptyCoroutineContext
) : ViewModel() {
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState()

    /**
     * [DateTimeConstants]의 주 시작일에 맞게 일, 월, 화, ... , 토요일의 한성대 식단이 들어있는 리스트이다.
     */
    private val _dailyMenus = mutableStateListOf<DailyMenuModel>()
    val dailyMenus: List<DailyMenuModel> = _dailyMenus

    private val _isConnected = mutableStateOf(true)
    val isConnected: State<Boolean> = _isConnected

    val scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())

    @VisibleForTesting
    var job: Job? = null
        private set

    /**
     * 식단 정보의 월요일부터 금요일까지의 기간을 문자열로 나타낸다.
     */
    val currentDuration: String
        get() {
            if (_dailyMenus.isEmpty()) return ""
            return "(" +
                    _dailyMenus[DateTimeConstants.MONDAY].date.toString("M/dd") +
                    " - " +
                    _dailyMenus[DateTimeConstants.FRIDAY].date.toString("M/dd") +
                    ")"
        }

    init {
        updateDailyMenus()
    }

    fun updateDailyMenus() {
        job = viewModelScope.launch(context = coroutineContext) {
            try {
                dailyMenuRepository.getDailyMenus()?.let {
                    _isConnected.value = true
                    _dailyMenus.addAll(adaptWeekDayToDateConstant(it))
                    moveWeekdayTap()
                }
            } catch (e: InternetNotConnectedException) {
                _isConnected.value = false
                scaffoldState.snackbarHostState.showSnackbar("정보 얻기 실패. 인터넷 연결을 확인해주세요.")
            }
        }
    }

    /**
     * [DateTimeConstants]는 일요일이 주의 시작이기 때문에 여기에 순서를 일치시킨 리스트를 반환한다.
     *
     * 한성대 사이트 식단 정보에서는 월요일이 주의 시작이다.
     */
    private fun adaptWeekDayToDateConstant(
        hansungMenuList: List<DailyMenuModel>
    ): List<DailyMenuModel> {
        if (hansungMenuList.isEmpty()) {
            return emptyList()
        }
        return mutableListOf<DailyMenuModel>().apply {
            addAll(hansungMenuList)
            val sundayMenu = removeLast()
            add(0, sundayMenu)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    private suspend fun moveWeekdayTap() {
        if (_dailyMenus.isEmpty())
            return

        val now = DateTime.now()
        when {
            // 첫 식단(월요일)의 날짜가 오늘 이후인 경우 월요일로 이동
            now.isBefore(_dailyMenus[DateTimeConstants.MONDAY].date) -> {
                pagerState.scrollToPage(0)
            }
            // 마지막 식단(일요일)의 날짜가 오늘 이전인 경우 금요일로 이동
            _dailyMenus[DateTimeConstants.SUNDAY % 7].date.isBefore(now) -> {
                pagerState.scrollToPage(4)
            }
            // 모든 범위 안에 포함된 경우 해당 요일로 이동한다. 단, 토요일과 일요일은 금요일 탭으로 이동한다.
            else -> {
                // 탭은 0부터 월요일이고 DateTimeConstants는 1부터 월요일이므로 값을 맞춰준다.
                var targetTapIndex = now.dayOfWeek - 1
                if (targetTapIndex < 0) {
                    targetTapIndex = 6
                }
                // 최대 금요일까지이므로 최대값을 4로 제한한다.
                targetTapIndex = min(targetTapIndex, 4)
                pagerState.scrollToPage(targetTapIndex)
            }
        }
    }
}