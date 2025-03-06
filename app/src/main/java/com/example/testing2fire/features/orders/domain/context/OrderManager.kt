package com.example.testing2fire.features.orders.domain.context

import com.example.testing2fire.features.orders.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class OrderManager private constructor() {
    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    fun setActiveOrder(order: Order?) {
        _activeOrder.value = order
    }

    fun getActiveOrderId(): String? {
        return _activeOrder.value?.id
    }

    fun clearActiveOrder() {
        _activeOrder.value = null
    }

    companion object {
        @Volatile
        private var instance: OrderManager? = null

        fun getInstance(): OrderManager {
            return instance ?: synchronized(this) {
                instance ?: OrderManager().also { instance = it }
            }
        }
    }
}