package com.example.testing2fire.features.orders.data.model


import com.google.gson.annotations.SerializedName

data class PendingOrdersResponseDTO(
    @SerializedName("orders") val orders: List<OrderDTO>,
    @SerializedName("metadata") val metadata: MetadataDTO
)

data class MetadataDTO(
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("has_more") val hasMore: Boolean
)

data class OrderResponseDTO(
    @SerializedName("order") val order: OrderDTO,
    @SerializedName("message") val message: String
)