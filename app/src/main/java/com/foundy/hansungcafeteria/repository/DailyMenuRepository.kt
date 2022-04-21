package com.foundy.hansungcafeteria.repository

import androidx.annotation.VisibleForTesting
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.service.HansungWebScraper

class DailyMenuRepository(
    @VisibleForTesting private val testUrl: String? = null
) {
    suspend fun getDailyMenus(): List<DailyMenuModel>? {
        return HansungWebScraper.searchCafeteria(testUrl)
    }
}