sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object PendingOrders : Screen("pending_orders")


    object OrderDetails : Screen("order_details/{orderId}") {
        fun createRoute(orderId: String) = "order_details/$orderId"
    }

    object ActiveOrder : Screen("active_order/{orderId}") {
        fun createRoute(orderId: String) = "active_order/$orderId"
    }
}