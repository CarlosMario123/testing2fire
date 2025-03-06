package com.example.testing2fire.features.orders.domain.usecase

import com.example.testing2fire.features.orders.domain.repository.OrderRepository

class CompleteOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String) =
        orderRepository.completeOrder(orderId)
}