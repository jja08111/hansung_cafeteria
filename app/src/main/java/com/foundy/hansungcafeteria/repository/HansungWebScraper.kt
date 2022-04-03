package com.foundy.hansungcafeteria.repository

import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.model.Menu
import com.foundy.hansungcafeteria.model.MenuDivision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.jsoup.Jsoup

@Suppress("BlockingMethodInNonBlockingContext")
class HansungWebScraper {
    /**
     * 식단 정보를 스크래핑 해온후 매핑 객체를 반환한다.
     *
     * 스크래핑은 다음과 같이 진행한다.
     * 1. tbody 태그로 된 Element를 찾는다.
     * 2. tbody에서 tr들을 찾는다.
     * 3. tr에서 th가 있으면 해당 날짜이고 없으면 이전과 동일한 날짜이다.(rowspan=2임)
     */
    suspend fun searchCafeteria(url: String? = null): List<DailyMenuModel>? {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(
                url ?: "https://www.hansung.ac.kr/hansung/1920/subview.do"
            ).get()
        }
        val tableBody = doc.getElementsByTag("tbody").first()
        val tableRows = tableBody?.children()
        val result = mutableListOf<DailyMenuModel>()

        if (tableRows == null) return null

        for (tableRow in tableRows) {
            val rowDateTimeString = tableRow
                .getElementsByTag("th")
                .first()
                ?.text()
                ?.split(" ")
                ?.first()
            val formatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy.MM.dd")
            val dateTime =
                if (rowDateTimeString != null) formatter.parseDateTime(rowDateTimeString) else null
            val tableDataList = tableRow.getElementsByTag("td")
            val rowMenus = tableDataList[1].text().split(" ")
            val divisionName = tableDataList[0].text()
            val menus = mutableListOf<Menu>()

            for (rowMenuIndex: Int in 0..(rowMenus.size - 2) step (2)) {
                try {
                    menus.add(
                        Menu(
                            rowMenus[rowMenuIndex],
                            rowMenus[rowMenuIndex + 1].replace(",", "").toInt()
                        )
                    )
                } catch (e: NumberFormatException) { // 식단이 없는 경우 변환에 실패한다.
                    break
                }
            }
            val divisions = mutableListOf<MenuDivision>().apply {
                add(MenuDivision(divisionName, menus))
            }

            if (dateTime != null) {
                result.add(DailyMenuModel(dateTime, divisions))
            } else { // 날짜가 바뀌지 않은 경우 식단 구분을 이전 날짜 결과에 추가한다.
                val lastModel = result.last()
                val newDivisions = mutableListOf<MenuDivision>().apply {
                    addAll(lastModel.menuDivisions)
                    addAll(divisions)
                }
                val newLastModel = result.last().copy(menuDivisions = newDivisions)
                result[result.lastIndex] = newLastModel
            }
        }

        return result
    }
}
