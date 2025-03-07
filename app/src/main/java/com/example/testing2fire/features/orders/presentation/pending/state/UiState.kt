package com.example.testing2fire.features.orders.presentation.pending.state

import com.example.testing2fire.features.orders.domain.model.Order

sealed class UiState {
    object Loading : UiState()
    data class Success(val orders: List<Order>, val hasMore: Boolean) : UiState()
    data class Error(val message: String) : UiState()
    object Empty : UiState()
}