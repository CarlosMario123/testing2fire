package com.example.testing2fire.features.orders.domain.usecase

import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.repository.OrderRepository

class AssignOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String): Result<Order> {
        return orderRepository.assignOrder(orderId)
    }
}