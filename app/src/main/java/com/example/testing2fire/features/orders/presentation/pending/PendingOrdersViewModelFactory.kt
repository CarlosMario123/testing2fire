import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.features.orders.data.api.OrderApiService
import com.example.testing2fire.features.orders.data.mapper.OrderMapper
import com.example.testing2fire.features.orders.data.repository.OrderRepositoryImpl
import com.example.testing2fire.features.orders.domain.usecase.GetPendingOrdersUseCase
import com.example.testing2fire.features.orders.presentation.pending.PendingOrdersViewModel

class PendingOrdersViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PendingOrdersViewModel::class.java)) {

            val apiService = RetrofitConfig.createApi(OrderApiService::class.java)
            val orderMapper = OrderMapper()
            val orderRepository = OrderRepositoryImpl(apiService, orderMapper)
            val getPendingOrdersUseCase = GetPendingOrdersUseCase(orderRepository)

            return PendingOrdersViewModel(getPendingOrdersUseCase, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}