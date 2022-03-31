package com.foundy.hansungcafeteria.ui

import androidx.lifecycle.ViewModel
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.repository.HansungWebScraper

class TapViewModel : ViewModel() {
    private val hansungWebScraper = HansungWebScraper()
    private val dailyMenus = lazy { hansungWebScraper.searchCafeteria() }

    fun getDailyMenu(weekDay: Int): DailyMenuModel? {
        // 한성대 사이트에서는 월요일이 주의 시작이고 DateTimeConstants에서는 일요일이 주의 시작이다. 따라서 하나를 빼야 일치한다.
        var actualWeek = weekDay - 1;
        if (actualWeek < 0) {
            actualWeek = 6
        }
        return dailyMenus.value?.get(actualWeek)
    }
}