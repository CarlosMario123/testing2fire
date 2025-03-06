package com.example.testing2fire.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.testing2fire.core.navigation.BottomNavItem
import com.example.testing2fire.features.orders.domain.context.OrderManager

@Composable
fun BottomNavigationBar(
    navController: NavController,
    orderManager: OrderManager
) {
    val items = listOf(
        BottomNavItem.PendingOrders,
        BottomNavItem.ActiveOrders
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Obtener la orden activa del OrderManager
    val activeOrder by orderManager.activeOrder.collectAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val selected = when (item) {
                BottomNavItem.PendingOrders ->
                    currentDestination?.hierarchy?.any { it.route == Screen.PendingOrders.route } == true

                BottomNavItem.ActiveOrders ->
                    currentDestination?.route?.startsWith("active_order") == true
            }

            // Determinar si el ítem debe estar habilitado
            val enabled = when (item) {
                BottomNavItem.PendingOrders -> true // Siempre habilitado
                BottomNavItem.ActiveOrders -> activeOrder != null // Solo habilitado si hay una orden activa
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)
                    )
                },
                selected = selected,
                enabled = enabled,
                onClick = {
                    if (enabled) {
                        when (item) {
                            BottomNavItem.PendingOrders -> {
                                navController.navigate(Screen.PendingOrders.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            BottomNavItem.ActiveOrders -> {
                                // Solo navegar si tenemos una orden activa
                                activeOrder?.let { order ->
                                    navController.navigate(Screen.ActiveOrder.createRoute(order.id)) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}