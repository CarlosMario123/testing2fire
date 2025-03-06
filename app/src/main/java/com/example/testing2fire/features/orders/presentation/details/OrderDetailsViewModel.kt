package com.example.testing2fire.features.orders.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing2fire.features.orders.domain.model.Order
import com.example.testing2fire.features.orders.domain.usecase.GetOrderDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsViewModel(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val orderId: String
) : ViewModel() {

    data class OrderDetailsState(
        val order: Order? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(OrderDetailsState(isLoading = true))
    val uiState: StateFlow<OrderDetailsState> = _uiState.asStateFlow()

    init {
        loadOrderDetails()
    }

    fun loadOrderDetails() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getOrderDetailsUseCase(orderId).fold(
                onSuccess = { order ->
                    _uiState.update {
                        it.copy(
                            order = order,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error desconocido"
                        )
                    }
                }
            )
        }
    }
}