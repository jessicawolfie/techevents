package jesscafezeiro.techevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jesscafezeiro.techevents.data.remote.network.ApiClient
import jesscafezeiro.techevents.data.repository.EventRepositoryImpl
import jesscafezeiro.techevents.presentation.EventListViewModel
import jesscafezeiro.techevents.presentation.ui.EventListScreen

class MainActivity : ComponentActivity() {

    // Injeção de dependência manual temporária
    private val viewModel: EventListViewModel by viewModels {
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
                        val searchQuery by viewModel.searchQuery.collectAsState()
                        val selectedType by viewModel.selectedType.collectAsState()
                        val eventsPagingFlow = viewModel.eventsPagingFlow

                        Column(modifier = Modifier.fillMaxSize()) {
                            // Barra de busca
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { novoTexto -> viewModel.onSearchQueryChanged(novoTexto) },
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
                                onTypeSelected = { novoTipo -> viewModel.onTypeSelected(novoTipo) }
                            )
                        }
                    }
                }
            }
        }
    }
}