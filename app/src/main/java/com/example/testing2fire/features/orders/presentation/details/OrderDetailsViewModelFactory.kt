package com.example.testing2fire.features.orders.presentation.details

import android.content.Context
import com.example.testing2fire.core.factory.ViewModelFactory
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.features.orders.data.api.OrderApiService
import com.example.testing2fire.features.orders.data.mapper.OrderMapper
import com.example.testing2fire.features.orders.data.repository.OrderRepositoryImpl
import com.example.testing2fire.features.orders.domain.usecase.GetOrderDetailsUseCase

class OrderDetailsViewModelFactory(
    private val context: Context,
    private val orderId: String
) : ViewModelFactory<OrderDetailsViewModel>() {

    override fun getViewModelClass(): Class<OrderDetailsViewModel> {
        return OrderDetailsViewModel::class.java
    }

    override fun createViewModel(): OrderDetailsViewModel {
        val orderApiService = RetrofitConfig.createApi(OrderApiService::class.java)
        val orderMapper = OrderMapper()
        val orderRepository = OrderRepositoryImpl(orderApiService, orderMapper)

        val getOrderDetailsUseCase = GetOrderDetailsUseCase(orderRepository)

        return OrderDetailsViewModel(getOrderDetailsUseCase, orderId)
    }
}