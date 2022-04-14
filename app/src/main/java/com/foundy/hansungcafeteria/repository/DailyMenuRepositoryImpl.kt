package com.foundy.hansungcafeteria.repository

import androidx.annotation.VisibleForTesting
import com.foundy.hansungcafeteria.model.DailyMenuModel
import com.foundy.hansungcafeteria.service.HansungWebScraper

class DailyMenuRepositoryImpl(
    @VisibleForTesting private val testUrl: String? = null
) : DailyMenuRepository {

    override suspend fun getDailyMenus(): List<DailyMenuModel>? {
        return HansungWebScraper.searchCafeteria(testUrl)
    }

}