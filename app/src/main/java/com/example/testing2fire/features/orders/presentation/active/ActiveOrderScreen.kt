package com.example.testing2fire.features.orders.presentation.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testing2fire.core.ui.components.PrimaryButton
import com.example.testing2fire.core.ui.components.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveOrderScreen(
    orderId: String,
    onBackPressed: () -> Unit,
    onOrderCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ActiveOrderViewModel = viewModel(
        factory = ActiveOrderViewModelFactory(context, orderId)
    )

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedido Activo") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        modifier = Modifier.background(Color(0xFF07203C))
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF07203C)),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SecondaryButton(
                        text = "Reintentar",
                        onClick = { viewModel.loadOrderDetails() }
                    )
                }
            } else if (state.order != null) {
                val order = state.order!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Información del cliente
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF243E57))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Información del Cliente",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )

                            Divider()

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Nombre",
                                    modifier = Modifier.padding(end = 8.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = order.userInfo.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color =Color.White,
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Teléfono",
                                    modifier = Modifier.padding(end = 8.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = order.userInfo.phone,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color =Color.White,
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Dirección",
                                    modifier = Modifier.padding(end = 8.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = order.address,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color =Color.White,
                                )
                            }
                        }
                    }

                    // Detalles del pedido
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF243E57))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Detalles del Pedido",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color =Color.White,
                            )

                            Divider()

                            Text(
                                text = order.notes,
                                style = MaterialTheme.typography.bodyLarge,
                                color =Color.White,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón de completar pedido
                    PrimaryButton(
                        text = "Marcar como Completado",
                        onClick = {
                            viewModel.completeOrder {
                                onOrderCompleted()

                            }
                        },
                        enabled = true,
                        isLoading = viewModel.isCompletingOrder
                    )
                }
            }
        }
    }
}