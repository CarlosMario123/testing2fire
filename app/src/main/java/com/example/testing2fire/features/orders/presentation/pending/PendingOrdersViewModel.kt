package com.example.testing2fire.features.orders.presentation.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.repository.OrderRepository
import com.example.testing2fire.features.orders.domain.usecase.GetPendingOrdersUseCase
import com.example.testing2fire.features.orders.presentation.pending.state.AssignOrderState
import com.example.testing2fire.features.orders.presentation.pending.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PendingOrdersViewModel(
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Estados UI



    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _assignOrderState = MutableStateFlow<AssignOrderState>(AssignOrderState.Idle)
    val assignOrderState: StateFlow<AssignOrderState> = _assignOrderState

    private var currentPage = 0
    private val pageSize = 10
    private var isLastPage = false
    private var isLoading = false

    private val pendingOrders = mutableListOf<Order>()

    init {
        loadPendingOrders(refresh = true)
    }


    fun getOrderById(orderId: String): Order? {
        // Primero buscar en la lista local de órdenes pendientes
        val localOrder = pendingOrders.find { it.id == orderId }

        if (localOrder != null) {
            return localOrder
        }

        // Si no lo encontramos localmente, podríamos implementar una consulta
        // síncrona al repositorio, pero esto depende de cómo esté diseñado tu repositorio
        // Esta es una implementación de ejemplo:

        // NOTA: Esto es un enfoque bloqueante. En una aplicación de producción,
        // deberías considerar hacer esta operación de forma asíncrona.
        // Por ejemplo, podrías tener un método que devuelva un Flow<Order?>

        // Ejemplo de una implementación bloqueante (solo como referencia):
        // return runBlocking {
        //     try {
        //         orderRepository.getOrderById(orderId).getOrNull()
        //     } catch (e: Exception) {
        //         null
        //     }
        // }

        // Por ahora, solo retornamos el resultado de la búsqueda local
        return localOrder
    }

    fun loadPendingOrders(refresh: Boolean = false) {
        if (isLoading || (isLastPage && !refresh)) return

        isLoading = true

        if (refresh) {
            currentPage = 0
            pendingOrders.clear()
            _uiState.value = UiState.Loading
        }

        viewModelScope.launch {
            val skip = currentPage * pageSize

            getPendingOrdersUseCase(pageSize, skip).fold(
                onSuccess = { result ->
                    isLoading = false
                    isLastPage = !result.metadata.hasMore

                    if (result.orders.isEmpty() && pendingOrders.isEmpty()) {
                        _uiState.value = UiState.Empty
                    } else {
                        pendingOrders.addAll(result.orders)
                        currentPage++
                        _uiState.value = UiState.Success(
                            orders = pendingOrders.toList(),
                            hasMore = result.metadata.hasMore
                        )
                    }
                },
                onFailure = { error ->
                    isLoading = false
                    if (pendingOrders.isEmpty()) {
                        _uiState.value = UiState.Error(error.message ?: "Error desconocido")
                    } else {
                        // Mantener los datos existentes pero mostrar el error de paginación
                        _uiState.value = UiState.Success(
                            orders = pendingOrders.toList(),
                            hasMore = true // Permitir reintentar
                        )
                        // Aquí podrías mostrar un Snackbar con el error
                    }
                }
            )
        }
    }

    fun assignOrder(orderId: String) {
        if (_assignOrderState.value is AssignOrderState.Loading) return

        _assignOrderState.value = AssignOrderState.Loading

        viewModelScope.launch {
            orderRepository.assignOrder(orderId).fold(
                onSuccess = { order ->
                    _assignOrderState.value = AssignOrderState.Success(order)

                    // Actualizar la lista eliminando el pedido asignado
                    val updatedList = pendingOrders.filter { it.id != orderId }
                    pendingOrders.clear()
                    pendingOrders.addAll(updatedList)

                    if (pendingOrders.isEmpty()) {
                        _uiState.value = UiState.Empty
                    } else {
                        _uiState.value = UiState.Success(
                            orders = pendingOrders.toList(),
                            hasMore = (uiState.value as? UiState.Success)?.hasMore ?: false
                        )
                    }
                },
                onFailure = { error ->
                    _assignOrderState.value = AssignOrderState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun resetAssignOrderState() {
        _assignOrderState.value = AssignOrderState.Idle
    }

    fun retry() {
        loadPendingOrders(refresh = true)
    }
}