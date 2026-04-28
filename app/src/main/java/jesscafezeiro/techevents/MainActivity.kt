package jesscafezeiro.techevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jesscafezeiro.techevents.data.remote.network.ApiClient
import jesscafezeiro.techevents.data.repository.EventRepositoryImpl
import jesscafezeiro.techevents.presentation.ui.EventListScreen
import jesscafezeiro.techevents.presentation.EventListViewModel
import jesscafezeiro.techevents.presentation.state.UiState

class MainActivity : ComponentActivity() {
    
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
                    containerColor = Color(0xFF121212) 
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.Transparent
                    ) {
                        val uiState by viewModel.uiState.collectAsState()
                        val searchQuery by viewModel.searchQuery.collectAsState()
                        val selectedType by viewModel.selectedType.collectAsState()
                        
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Barra de busca
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { novotexto -> viewModel.onSearchQueryChanged(novotexto)},
                                label = { Text("Buscar eventos...")},
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

                            // Reação dos dados
                            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                when (val state = uiState) {
                                    is UiState.Loading -> {
                                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                    }
                                    is UiState.Success -> {
                                        EventListScreen(
                                            events = state.data.allEvents,
                                            selectedType = selectedType,
                                            onTypeSelected = { novoTipo -> viewModel.onTypeSelected(novoTipo)}
                                        )
                                    }
                                    is UiState.Error -> {
                                        Text(
                                            text = "Erro: ${state.message}", 
                                            modifier = Modifier.padding(16.dp).align(Alignment.Center),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    is UiState.Empty -> {
                                        Text(
                                            text = "Nenhum evento encontrado", 
                                            modifier = Modifier.align(Alignment.Center),
                                            color = Color(0xFFECEFF4)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
