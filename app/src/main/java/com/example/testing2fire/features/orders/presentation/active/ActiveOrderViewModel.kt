package com.example.testing2fire.features.orders.presentation.active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.usecase.CompleteOrderUseCase
import com.example.testing2fire.features.orders.domain.usecase.GetOrderDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActiveOrderViewModel(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val completeOrderUseCase: CompleteOrderUseCase,
    private val orderId: String
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val order: Order? = null,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    var isCompletingOrder by mutableStateOf(false)
        private set

    var orderCompleted by mutableStateOf(false)
        private set

    init {
        loadOrderDetails()
    }

    fun loadOrderDetails() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            getOrderDetailsUseCase(orderId)
                .onSuccess { order ->
                    _uiState.value = UiState(
                        isLoading = false,
                        order = order
                    )
                }
                .onFailure { error ->
                    _uiState.value = UiState(
                        isLoading = false,
                        error = error.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun completeOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isCompletingOrder = true

            completeOrderUseCase(orderId)
                .onSuccess { order ->
                    _uiState.value = UiState(
                        isLoading = false,
                        order = order
                    )
                    orderCompleted = true
                    isCompletingOrder = false

                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Error al completar el pedido"
                    )
                    isCompletingOrder = false
                }
        }
    }
}
