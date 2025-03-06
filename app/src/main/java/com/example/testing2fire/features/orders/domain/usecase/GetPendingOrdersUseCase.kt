package com.example.testing2fire.features.orders.domain.usecase

import com.example.testing2fire.features.orders.domain.model.PendingOrdersResult
import com.example.testing2fire.features.orders.domain.repository.OrderRepository

class GetPendingOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(limit: Int, skip: Int): Result<PendingOrdersResult> {
        return orderRepository.getPendingOrders(limit, skip)
    }
}