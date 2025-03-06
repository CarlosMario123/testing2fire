package com.example.testing2fire.features.orders.domain.model

data class PendingOrdersResult(
    val orders: List<Order>,
    val metadata: Metadata
)