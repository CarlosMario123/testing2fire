package com.example.testing2fire.features.orders.data.api

import com.example.testing2fire.features.orders.data.model.OrderResponseDTO
import com.example.testing2fire.features.orders.data.model.PendingOrdersResponseDTO
import retrofit2.http.*

interface OrderApiService {
    @GET("orders/pending")
    suspend fun getPendingOrders(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PendingOrdersResponseDTO

    @GET("orders/{orderId}")
    suspend fun getOrderDetails(
        @Path("orderId") orderId: String
    ): OrderResponseDTO

    @GET("orders")
    suspend fun getCourierOrders(
        @Query("status") status: String? = null,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PendingOrdersResponseDTO

    @POST("orders/{orderId}/assign")
    suspend fun assignOrder(
        @Path("orderId") orderId: String
    ): OrderResponseDTO

    @POST("orders/{orderId}/complete")
    suspend fun completeOrder(
        @Path("orderId") orderId: String
    ): OrderResponseDTO
}