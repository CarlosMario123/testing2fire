package com.example.testing2fire.features.orders.presentation.pending.state

import com.example.testing2fire.features.orders.domain.model.Order


sealed class AssignOrderState {
    object Idle : AssignOrderState()
    object Loading : AssignOrderState()
    data class Success(val order: Order) : AssignOrderState()
    data class Error(val message: String) : AssignOrderState()
}