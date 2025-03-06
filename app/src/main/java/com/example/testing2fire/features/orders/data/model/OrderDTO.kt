package com.example.testing2fire.features.orders.data.model

import com.google.gson.annotations.SerializedName

data class OrderDTO(
    @SerializedName("_id") val id: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("address") val address: String,
    @SerializedName("status") val status: String,
    @SerializedName("user_info") val userInfo: UserInfoDTO,
    @SerializedName("courier_id") val courierId: String? = null,
    @SerializedName("courier_info") val courierInfo: CourierInfoDTO? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("assigned_at") val assignedAt: String? = null,
    @SerializedName("completed_at") val completedAt: String? = null
)

