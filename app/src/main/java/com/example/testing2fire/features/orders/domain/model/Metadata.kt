package com.example.testing2fire.features.orders.domain.model

data class Metadata(
    val total: Int,
    val limit: Int,
    val skip: Int,
    val hasMore: Boolean
)
