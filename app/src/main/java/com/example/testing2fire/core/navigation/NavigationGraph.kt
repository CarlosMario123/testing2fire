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
    // Usar OrderManager en lugar de estado local
    val orderManager = OrderManager.getInstance()
    val activeOrder by orderManager.activeOrder.collectAsState()

    // Determine if we should show the bottom navigation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Only show bottom nav for order-related screens
    val showBottomBar = currentRoute == Screen.PendingOrders.route ||
            currentRoute?.startsWith("active_order") == true

    // Extraer orderId y actualizar OrderManager si es necesario
    if (currentRoute?.startsWith("active_order") == true) {
        navBackStackEntry?.arguments?.getString("orderId")?.let { orderId ->
            // Solo notificar si cambia el ID
            if (orderManager.getActiveOrderId() != orderId) {
                onActiveOrderSelected(orderId)
                // La orden completa debería obtenerse del repositorio y establecerse en OrderManager
                // Aquí solo manejamos la notificación del cambio
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