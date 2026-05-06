package jesscafezeiro.techevents.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jesscafezeiro.techevents.data.repository.EventRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel (
    private val repository: EventRepositoryImpl,
    private val id: String
) : ViewModel() {

    // Estado mutável privado (apenas a viewlModel pode alterar)
    private val _uiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)

    // Estado imutável público (a View só pode observar)
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    init {
        loadEventDetails()
    }

    private fun loadEventDetails() {
        viewModelScope.launch {
            _uiState.value = EventDetailsUiState.Loading
            val event = repository.getEvent(id)
            if (event != null) {
                _uiState.value = EventDetailsUiState.Success(event)
            } else {
                _uiState.value = EventDetailsUiState.Error("Não foi possível carregar os detalhes deste evento. Verifique sua conexão ou tente novamente.")
            }
        }
    }
}