package com.foundy.hansungcafeteria.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.repository.HansungWebScraper
import kotlinx.coroutines.launch
import org.joda.time.DateTimeConstants

class HomeViewModel : ViewModel() {
    private val hansungWebScraper = HansungWebScraper()

    /**
     * [DateTimeConstants]의 주 시작일에 맞게 일, 월, 화, ... , 토요일의 한성대 식단이 들어있는 리스트이다.
     */
    val dailyMenus = mutableStateListOf<DailyMenuModel>()

    /**
     * 식단 정보의 월요일부터 금요일까지의 기간을 문자열로 나타낸다.
     */
    val currentDuration: String
        get() {
            if (dailyMenus.isEmpty()) return ""
            return "(" +
                    dailyMenus[DateTimeConstants.MONDAY].date.toString("M/dd") +
                    " - " +
                    dailyMenus[DateTimeConstants.FRIDAY].date.toString("M/dd") +
                    ")"
        }

    init {
        viewModelScope.launch {
            hansungWebScraper.searchCafeteria()?.let {
                dailyMenus.addAll(adaptWeekDayToDateConstant(it))
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
}