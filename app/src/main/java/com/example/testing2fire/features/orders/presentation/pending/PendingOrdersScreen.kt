package com.example.testing2fire.features.orders.presentation.pending

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.testing2fire.R
import com.example.testing2fire.features.orders.domain.model.Order
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingOrdersScreen(
    viewModel: PendingOrdersViewModel,
    navController: NavController,
    onOrderSelected: (String) -> Unit = {},
    onActiveOrderSelected: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val assignOrderState by viewModel.assignOrderState.collectAsState()
    val context = LocalContext.current

    // Efecto para mostrar mensajes de error/éxito en la asignación de pedidos
    LaunchedEffect(assignOrderState) {
        when (assignOrderState) {
            is PendingOrdersViewModel.AssignOrderState.Success -> {
                val order = (assignOrderState as PendingOrdersViewModel.AssignOrderState.Success).order
                Toast.makeText(
                    context,
                    "Pedido asignado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                // Navegar a la pantalla de detalles del pedido asignado
                onOrderSelected(order.id) // Update the selected order ID for bottom nav
                navController.navigate("order_details/${order.id}")
                viewModel.resetAssignOrderState()
            }
            is PendingOrdersViewModel.AssignOrderState.Error -> {
                val message = (assignOrderState as PendingOrdersViewModel.AssignOrderState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.resetAssignOrderState()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.background(Color(0xFF07203C)),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color(0xFF07203C)),
                title = { Text("Pedidos Pendientes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadPendingOrders(refresh = true) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF07203C))
        ) {
            when (uiState) {
                is PendingOrdersViewModel.UiState.Loading -> {
                    LoadingIndicator()
                }
                is PendingOrdersViewModel.UiState.Success -> {
                    val orders = (uiState as PendingOrdersViewModel.UiState.Success).orders
                    val hasMore = (uiState as PendingOrdersViewModel.UiState.Success).hasMore

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(orders) { order ->
                            PendingOrderItem(
                                order = order,
                                onAssignClicked = {
                                    viewModel.assignOrder(order.id)
                                    // When assigning an order, update the active order ID for bottom nav
                                    onActiveOrderSelected(order.id)
                                },
                                onItemClicked = {
                                    // When clicking an order, update the selected order ID for bottom nav
                                    onOrderSelected(order.id)
                                    navController.navigate("order_details/${order.id}")
                                }
                            )
                        }

                        if (hasMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp),
                                        strokeWidth = 2.dp
                                    )
                                }

                                // Cargar más al llegar al final
                                LaunchedEffect(true) {
                                    viewModel.loadPendingOrders()
                                }
                            }
                        }
                    }

                    // Mostrar indicador de carga si estamos asignando un pedido
                    if (assignOrderState is PendingOrdersViewModel.AssignOrderState.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is PendingOrdersViewModel.UiState.Error -> {
                    val errorMessage = (uiState as PendingOrdersViewModel.UiState.Error).message
                    ErrorView(
                        errorMessage = errorMessage,
                        onRetry = { viewModel.retry() }
                    )
                }
                is PendingOrdersViewModel.UiState.Empty -> {
                    EmptyView(
                        onRefresh = { viewModel.loadPendingOrders(refresh = true) }
                    )
                }
            }
        }
    }
}

@Composable
fun PendingOrderItem(
    order: Order,
    onAssignClicked: () -> Unit,
    onItemClicked: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var borderColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked()
                borderColor = Color(0xFFFFCE00) // Amarillo al hacer clic (opcional)
            }
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent) // Fondo transparente
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Transparent) // Fondo transparente dentro del contenido
        ) {
            // Cliente y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.userInfo.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Texto blanco
                )

                Text(
                    text = dateFormatter.format(order.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White // Texto blanco
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dirección
            Text(
                text = order.address,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White, // Texto blanco
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Notas del pedido
            Text(
                text = "Notas: ${order.notes}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White, // Texto blanco
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para tomar el pedido
            Button(
                onClick = onAssignClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Coincide con CustomButton
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCE00),
                    contentColor = Color(0xFF242A2D)
                )
            ) {
                Text("Tomar Pedido")
            }
        }
    }
}


@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error al cargar pedidos",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
fun EmptyView(
    onRefresh: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
        isPlaying = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No hay pedidos pendientes",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRefresh) {
            Text("Actualizar")
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
