package com.example.testing2fire.features.orders.domain.model

import CourierInfo
import java.util.Date

data class Order(
    val id: String,
    val notes: String,
    val address: String,
    val status: String,
    val userInfo: UserInfo,
    val courierId: String? = null,
    val courierInfo: CourierInfo? = null,
    val createdAt: Date,
    val updatedAt: Date,
    val assignedAt: Date? = null,
    val completedAt: Date? = null
)