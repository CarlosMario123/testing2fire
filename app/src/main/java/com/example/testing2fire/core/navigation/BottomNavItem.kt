package com.example.testing2fire.core.navigation

import androidx.annotation.DrawableRes
import com.example.testing2fire.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object PendingOrders : BottomNavItem(Screen.PendingOrders.route, "Pendientes", R.drawable.baseline_punch_clock_24)
    object ActiveOrders : BottomNavItem(Screen.ActiveOrder.route, "Activos", R.drawable.baseline_add_task_24)
}
