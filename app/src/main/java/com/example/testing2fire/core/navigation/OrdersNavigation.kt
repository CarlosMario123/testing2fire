package com.example.testing2fire.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testing2fire.features.orders.presentation.active.ActiveOrderScreen
import com.example.testing2fire.features.orders.presentation.details.OrderDetailsScreen
import com.example.testing2fire.features.orders.presentation.pending.PendingOrdersScreen
import com.example.testing2fire.features.orders.presentation.pending.PendingOrdersViewModel
import PendingOrdersViewModelFactory

fun NavGraphBuilder.ordersRoutes(
    navController: NavHostController,
    onOrderDetailsSelected: (String) -> Unit,
    onActiveOrderSelected: (String) -> Unit
) {
    composable(route = Screen.PendingOrders.route) {
        val context = LocalContext.current
        val pendingOrdersViewModel: PendingOrdersViewModel = viewModel(
            factory = PendingOrdersViewModelFactory(context)
        )
        PendingOrdersScreen(
            viewModel = pendingOrdersViewModel,
            navController = navController,
            onOrderSelected = { orderId ->
                onOrderDetailsSelected(orderId)
                navController.navigate(Screen.OrderDetails.createRoute(orderId))
            },
            onActiveOrderSelected = { orderId ->
                onActiveOrderSelected(orderId)
                navController.navigate(Screen.ActiveOrder.createRoute(orderId))
            }
        )
    }

    composable(
        route = Screen.OrderDetails.route,
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        onOrderDetailsSelected(orderId)
        OrderDetailsScreen(
            orderId = orderId,
            onBackPressed = { navController.navigateUp() }
        )
    }

    composable(
        route = Screen.ActiveOrder.route,
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        onActiveOrderSelected(orderId)
        ActiveOrderScreen(
            orderId = orderId,
            onBackPressed = { navController.navigateUp() },
            onOrderCompleted = {
                navController.navigate(Screen.PendingOrders.route) {
                    popUpTo(Screen.PendingOrders.route) { inclusive = true }
                }
            }
        )
    }
}
