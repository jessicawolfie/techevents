package jesscafezeiro.techevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import jesscafezeiro.techevents.data.remote.network.ApiClient
import jesscafezeiro.techevents.data.repository.EventRepositoryImpl
import jesscafezeiro.techevents.presentation.EventListViewModel
import jesscafezeiro.techevents.presentation.details.EventDetailScreen
import jesscafezeiro.techevents.presentation.details.EventDetailsViewModel
import jesscafezeiro.techevents.presentation.navigation.EventDetailRoute
import jesscafezeiro.techevents.presentation.navigation.EventListRoute
import jesscafezeiro.techevents.presentation.ui.EventListScreen

class MainActivity : ComponentActivity() {

    private val eventListViewModel: EventListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val service = ApiClient.eventService
                val repository = EventRepositoryImpl(service)
                return EventListViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    containerColor = Color(0xFF121212),
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.Transparent
                    ) {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = EventListRoute
                        ) {

                            composable<EventListRoute> {
                                val searchQuery by eventListViewModel.searchQuery.collectAsState()
                                val selectedType by eventListViewModel.selectedType.collectAsState()
                                val eventsPagingFlow = eventListViewModel.eventsPagingFlow

                                Column(modifier = Modifier.fillMaxSize()) {
                                    OutlinedTextField(
                                        value = searchQuery,
                                        onValueChange = { eventListViewModel.onSearchQueryChanged(it) },
                                        label = { Text("Buscar eventos...") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .padding(top = 16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = Color(0xFFECEFF4).copy(alpha = 0.3f),
                                            focusedBorderColor = Color(0xFFB48EAD),
                                            unfocusedTextColor = Color(0xFFECEFF4),
                                            focusedTextColor = Color(0xFFECEFF4),
                                            cursorColor = Color(0xFFB48EAD)
                                        )
                                    )

                                    EventListScreen(
                                        eventsFlow = eventsPagingFlow,
                                        selectedType = selectedType,
                                        onTypeSelected = { eventListViewModel.onTypeSelected(it) },
                                        onEventClick = { eventId ->
                                            navController.navigate(EventDetailRoute(id = eventId))
                                        }
                                    )
                                }
                            }

                            composable<EventDetailRoute> { backStackEntry ->
                                val route = backStackEntry.toRoute<EventDetailRoute>()
                                val detailsViewModel: EventDetailsViewModel = viewModel(
                                    factory = object : ViewModelProvider.Factory {
                                        @Suppress("UNCHECKED_CAST")
                                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                            val repository = EventRepositoryImpl(ApiClient.eventService)
                                            return EventDetailsViewModel(repository, route.id) as T
                                        }
                                    }
                                )

                                val uiState by detailsViewModel.uiState.collectAsState()

                                EventDetailScreen(
                                    uiState = uiState,
                                    onBackClick = {
                                        navController.popBackStack()
                                    },
                                    onRecommendationClick = { eventId ->
                                        navController.navigate(EventDetailRoute(id = eventId))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
