package com.example.testing2fire.features.orders.domain.usecase

import com.example.testing2fire.features.orders.domain.repository.OrderRepository

class GetCourierOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(status: String? = null, limit: Int = 10, skip: Int = 0) =
        orderRepository.getCourierOrders(status, limit, skip)
}