package com.foundy.hansungcafeteria.repository

import com.foundy.hansungcafeteria.model.DailyMenuModel

interface DailyMenuRepository {

    suspend fun getDailyMenus(): List<DailyMenuModel>?

}