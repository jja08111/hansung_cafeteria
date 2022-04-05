package com.foundy.hansungcafeteria.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.hansungcafeteria.model.Menu
import com.foundy.hansungcafeteria.model.MenuDivision

@Composable
fun MenuDivisionCard(division: MenuDivision) {
    HansungCard {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                division.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column {
                for (menu in division.menus) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            menu.name,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "${menu.priceWithComma}원",
                            style = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.6F
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "DivisionCard")
@Composable
fun MenuDivisionCardPreview() {
    MenuDivisionCard(
        division = MenuDivision(
            name = "찌개&분식",
            menus = listOf(
                Menu("메뉴1", 1000),
                Menu("메뉴2", 5000),
                Menu("메뉴3", 5200),
            )
        )
    )
}