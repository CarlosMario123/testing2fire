package com.example.testing2fire.features.orders.data.repository

import com.example.testing2fire.features.orders.data.api.OrderApiService
import com.example.testing2fire.features.orders.data.mapper.OrderMapper
import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.model.OrderList
import com.example.testing2fire.features.orders.domain.model.PendingOrdersResult
import com.example.testing2fire.features.orders.domain.repository.OrderRepository
import retrofit2.HttpException
import java.io.IOException

class OrderRepositoryImpl(
    private val apiService: OrderApiService,
    private val orderMapper: OrderMapper
) : OrderRepository {

    override suspend fun getPendingOrders(limit: Int, skip: Int): Result<PendingOrdersResult> {
        return try {
            val response = apiService.getPendingOrders(limit, skip)
            Result.success(orderMapper.mapPendingOrdersResponseToDomain(response))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a internet."))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "No autorizado. Por favor inicia sesión nuevamente."
                403 -> "No tienes permiso para ver pedidos pendientes."
                else -> "Error al obtener pedidos pendientes: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    override suspend fun getOrderDetails(orderId: String): Result<Order> {
        return try {
            val response = apiService.getOrderDetails(orderId)
            // Acceder a la propiedad 'order' del objeto response
            Result.success(orderMapper.mapToDomain(response.order))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a internet."))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "No autorizado. Por favor inicia sesión nuevamente."
                403 -> "No tienes permiso para ver este pedido."
                404 -> "Pedido no encontrado."
                else -> "Error al obtener detalles del pedido: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    override suspend fun getCourierOrders(status: String?, limit: Int, skip: Int): Result<OrderList> {
        return try {
            val response = apiService.getCourierOrders(status, limit, skip)
            Result.success(
                OrderList(
                    orders = response.orders.map { orderMapper.mapToDomain(it) },
                    metadata = orderMapper.mapMetadataToDomain(response.metadata)
                )
            )
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a internet."))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "No autorizado. Por favor inicia sesión nuevamente."
                else -> "Error al obtener pedidos: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    override suspend fun assignOrder(orderId: String): Result<Order> {
        return try {
            val response = apiService.assignOrder(orderId)
            // Acceder a la propiedad 'order' del objeto response
            Result.success(orderMapper.mapToDomain(response.order))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a internet."))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "No autorizado. Por favor inicia sesión nuevamente."
                403 -> "No tienes permiso para asignar pedidos."
                404 -> "Pedido no encontrado."
                409 -> "Este pedido ya ha sido asignado a otro repartidor."
                else -> "Error al asignar pedido: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    override suspend fun completeOrder(orderId: String): Result<Order> {
        return try {
            val response = apiService.completeOrder(orderId)
            // Acceder a la propiedad 'order' del objeto response
            Result.success(orderMapper.mapToDomain(response.order))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a internet."))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "No autorizado. Por favor inicia sesión nuevamente."
                403 -> "No tienes permiso para completar este pedido."
                404 -> "Pedido no encontrado."
                409 -> "Este pedido no puede ser completado en su estado actual."
                else -> "Error al completar pedido: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }
}