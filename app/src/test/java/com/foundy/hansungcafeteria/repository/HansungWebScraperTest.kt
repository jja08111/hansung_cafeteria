package com.foundy.hansungcafeteria.repository

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class HansungWebScraperTest : FunSpec({
    test("searchCafeteria works correctly if the url is default") {
        val dailyMenus = HansungWebScraper().searchCafeteria()
        dailyMenus?.isNotEmpty() shouldBe true
    }

    test("searchCafeteria returns an empty Menus if date is holiday") {
        // 3월 1일이 포함된 기간이다.
        val url = "https://www.hansung.ac.kr/hansung/1920/subview.do?enc=Zm5jdDF8QEB8JTJGZGlldCUyRmhhbnN1bmclMkYyJTJGdmlldy5kbyUzRm1vbmRheSUzRDIwMjIuMDMuMDclMjZ3ZWVrJTNEcHJlJTI2"
        val dailyMenus = HansungWebScraper().searchCafeteria(url = url)

        dailyMenus?.get(0)?.menuDivisions?.forEach {
            it.menus.isEmpty() shouldBe false
        }
        // 2022년 3월 1일은 화요일이다.
        dailyMenus?.get(1)?.menuDivisions?.forEach {
            it.menus.isEmpty() shouldBe true
        }
    }
})