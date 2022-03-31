package com.foundy.hansungcafeteria.model

data class DailyMenuModel(val date: String, val menuDivisions: List<MenuDivision>)

data class MenuDivision(val name: String, val menus: List<Menu>)

data class Menu(val name: String, val price: Int)