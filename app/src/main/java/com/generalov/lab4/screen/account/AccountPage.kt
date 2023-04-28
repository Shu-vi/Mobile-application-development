package com.generalov.lab4.screen.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.generalov.lab4.screen.account.login.LoginPage
import com.generalov.lab4.screen.account.registration.RegistrationPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Account(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = TabPage.values().size)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TabHome(
                selectedTabIndex = pagerState.currentPage,
                onSelectedTab = { scope.launch { pagerState.animateScrollToPage(it.ordinal) } })
        },
        content = { padding ->
            HorizontalPager(state = pagerState) { index ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    when (index) {
                        0 -> LoginPage(navController)
                        1 -> RegistrationPage()
                    }
                }
            }
        }
    )
}