package com.example.testing2fire.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.testing2fire.core.components.BottomNavigationBar
import com.example.testing2fire.features.orders.domain.context.OrderManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    modifier: Modifier = Modifier,
    onOrderDetailsSelected: (String) -> Unit = {},
    onActiveOrderSelected: (String) -> Unit = {}
) {
    // Usar OrderManager esta en contexto global
    val orderManager = OrderManager.getInstance()
    val activeOrder by orderManager.activeOrder.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val showBottomBar = currentRoute == Screen.PendingOrders.route ||
            currentRoute?.startsWith("active_order") == true


    if (currentRoute?.startsWith("active_order") == true) {
        navBackStackEntry?.arguments?.getString("orderId")?.let { orderId ->
            if (orderManager.getActiveOrderId() != orderId) {
                onActiveOrderSelected(orderId)

            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    orderManager = orderManager,
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)
            .background(Color(0xFF07203C))) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.background(Color(0xFF07203C))
            ) {
                authRoutes(navController)
                ordersRoutes(
                    navController = navController,
                    orderManager = orderManager,
                    onOrderDetailsSelected = onOrderDetailsSelected
                )
            }
        }
    }
}