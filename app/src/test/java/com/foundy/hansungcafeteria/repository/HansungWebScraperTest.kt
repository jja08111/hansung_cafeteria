package com.foundy.hansungcafeteria.repository

import com.foundy.hansungcafeteria.service.HansungWebScraper
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.specs.FunSpec

class HansungWebScraperTest : FunSpec({
    test("searchCafeteria works correctly if the url is default") {
        val dailyMenus = HansungWebScraper.searchCafeteria()
        dailyMenus?.shouldNotBeEmpty()
    }

    test("searchCafeteria returns an empty Menus if date is holiday") {
        // 3월 1일이 포함된 기간이다.
        val url = "https://www.hansung.ac.kr/hansung/1920/subview.do?enc=Zm5jdDF8QEB8JTJGZGlldCUyRmhhbnN1bmclMkYyJTJGdmlldy5kbyUzRm1vbmRheSUzRDIwMjIuMDMuMDclMjZ3ZWVrJTNEcHJlJTI2"
        val dailyMenus = HansungWebScraper.searchCafeteria(testUrl = url)

        dailyMenus?.get(0)?.menuDivisions?.forEach {
            it.menus.shouldNotBeEmpty()
        }
        // 2022년 3월 1일은 화요일이다.
        dailyMenus?.get(1)?.menuDivisions?.forEach {
            it.menus.shouldBeEmpty()
        }
    }
})