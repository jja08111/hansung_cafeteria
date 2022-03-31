package com.foundy.hansungcafeteria.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.repository.HansungWebScraper
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val hansungWebScraper = HansungWebScraper()
    private val dailyMenus = mutableStateListOf<DailyMenuModel?>()

    init {
        viewModelScope.launch {
            hansungWebScraper.searchCafeteria()?.let {
                dailyMenus.addAll(it)
            }
        }
    }

    fun getDailyMenu(weekDay: Int): DailyMenuModel? {
        // 한성대 사이트에서는 월요일이 주의 시작이고 DateTimeConstants에서는 일요일이 주의 시작이다. 따라서 하나를 빼야 일치한다.
        var actualWeek = weekDay - 1
        if (actualWeek < 0) {
            actualWeek = 6
        }
        val hasData = actualWeek < dailyMenus.size
        if (hasData) {
            return dailyMenus[actualWeek]
        }
        return null
    }
}