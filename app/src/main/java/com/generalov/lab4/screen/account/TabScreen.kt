package com.generalov.lab4.screen.account

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class TabPage {
    Authorization,
    Registration
}

@Composable
fun TabHome(selectedTabIndex: Int, onSelectedTab: (TabPage) -> Unit) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        TabPage.values().forEachIndexed { index, tabPage ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onSelectedTab(tabPage) },
                text = { Text(text = tabPage.name) },
                selectedContentColor = Color.Yellow,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        }
    }
}