package com.foundy.hansungcafeteria.model

import org.joda.time.DateTime
import java.text.NumberFormat
import java.util.*

data class DailyMenuModel(val date: DateTime, val menuDivisions: List<MenuDivision>)

data class MenuDivision(val name: String, val menus: List<Menu>)

data class Menu(val name: String, val price: Int) {
    val priceWithComma: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(price)
}