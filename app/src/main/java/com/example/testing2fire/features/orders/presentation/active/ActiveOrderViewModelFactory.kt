package com.example.testing2fire.features.orders.presentation.active

import android.content.Context
import com.example.testing2fire.core.factory.ViewModelFactory
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.features.orders.data.api.OrderApiService
import com.example.testing2fire.features.orders.data.mapper.OrderMapper
import com.example.testing2fire.features.orders.data.repository.OrderRepositoryImpl
import com.example.testing2fire.features.orders.domain.usecase.CompleteOrderUseCase
import com.example.testing2fire.features.orders.domain.usecase.GetOrderDetailsUseCase

class ActiveOrderViewModelFactory(
    private val context: Context,
    private val orderId: String
) : ViewModelFactory<ActiveOrderViewModel>() {

    override fun getViewModelClass(): Class<ActiveOrderViewModel> {
        return ActiveOrderViewModel::class.java
    }

    override fun createViewModel(): ActiveOrderViewModel {
        val orderApiService = RetrofitConfig.createApi(OrderApiService::class.java)
        val orderMapper = OrderMapper()
        val orderRepository = OrderRepositoryImpl(orderApiService, orderMapper)

        val getOrderDetailsUseCase = GetOrderDetailsUseCase(orderRepository)
        val completeOrderUseCase = CompleteOrderUseCase(orderRepository)

        return ActiveOrderViewModel(getOrderDetailsUseCase, completeOrderUseCase, orderId)
    }
}
