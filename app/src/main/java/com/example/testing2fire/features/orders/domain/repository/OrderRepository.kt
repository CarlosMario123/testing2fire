package com.example.testing2fire.features.orders.domain.repository

import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.model.OrderList
import com.example.testing2fire.features.orders.domain.model.PendingOrdersResult

interface OrderRepository {
    suspend fun getPendingOrders(limit: Int, skip: Int): Result<PendingOrdersResult>
    suspend fun getOrderDetails(orderId: String): Result<Order>
    suspend fun getCourierOrders(status: String?, limit: Int, skip: Int): Result<OrderList>
    suspend fun assignOrder(orderId: String): Result<Order>
    suspend fun completeOrder(orderId: String): Result<Order>
}