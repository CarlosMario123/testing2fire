package com.example.testing2fire.features.orders.domain.model

/**
 * Modelo que representa una lista paginada de pedidos.
 *
 * @property orders Lista de pedidos.
 * @property metadata Metadatos de paginación.
 */
data class OrderList(
    val orders: List<Order>,
    val metadata: Metadata
)