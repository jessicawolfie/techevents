package jesscafezeiro.techevents.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel (
    private val repository: EventRepository,
    private val id: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    init {
        loadEventDetails()
    }

    private fun loadEventDetails() {
        viewModelScope.launch {
            _uiState.value = EventDetailsUiState.Loading

            try {
                // Dispara as duas buscas ao mesmo tempo
                val eventDeferred = async { repository.getEvent(id) }
                val recommendationsDeferred = async { repository.getEventRecommendations(id) }

                // Aguarda os dois resultados
                val event = eventDeferred.await()
                val recommendations = recommendationsDeferred.await()

                if (event != null) {
                    // passa tanto o evento quanto a lista da IA para o estado de Success
                    _uiState.value = EventDetailsUiState.Success(
                        event = event,
                        recommendations = recommendations
                    )
                } else {
                    _uiState.value = EventDetailsUiState.Error("Evento não encontrado.")
                }
            } catch (e: Exception) {
                _uiState.value = EventDetailsUiState.Error("Erro ao carregar dados: ${e.message}")
            }
        }
    }
}