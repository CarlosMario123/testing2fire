package com.example.testing2fire.features.orders.domain.usecase

import com.example.testing2fire.features.orders.domain.repository.OrderRepository

class GetOrderDetailsUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String) =
        orderRepository.getOrderDetails(orderId)
}