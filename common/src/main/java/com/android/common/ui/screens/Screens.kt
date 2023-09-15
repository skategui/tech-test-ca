package com.android.common.ui.screens

/**
 * Used for the navigation
 */
sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object BanksScreen : Screens("banks_screen")
    object AccountDetailScreen : Screens("account_detail_screen")
}