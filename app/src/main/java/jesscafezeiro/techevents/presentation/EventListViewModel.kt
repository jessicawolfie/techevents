package jesscafezeiro.techevents.presentation

import android.app.usage.UsageEventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jesscafezeiro.techevents.domain.model.Event
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import jesscafezeiro.techevents.presentation.state.EventListDisplayData
import jesscafezeiro.techevents.presentation.state.UiState
import kotlinx.coroutines.launch

// Recebe o repository via construtor (injeção de dependência manual)
class EventListViewModel(
    private val repository: EventRepository
) : ViewModel() {
    // Estado mutável, escondido da View (encapsulamento)
    private val _uiState = MutableLiveData<UiState<EventListDisplayData>>()

    // Estado imutável, que a View pode observar
    val uiState: LiveData<UiState<EventListDisplayData>> get() = _uiState
    init {
        fetchEvents()
    }

    // Busca eventos e prepara as listas (geral e recomendação)
    fun fetchEvents(query: String = "") {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val eventFromApi = repository.getEvents(query)

                if (eventFromApi.isEmpty()) {
                    _uiState.value = UiState.Empty
                } else {
                    // lista de eventos recomendados pela IA. por enquanto vamos simular 2 eventos
                    val recommendadosMock = eventFromApi.take(2)

                    val displayData = EventListDisplayData(
                        recommendedEvents = recommendadosMock,
                        allEvents = eventFromApi
                    )

                    _uiState.value = UiState.Success(displayData)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Não foi possível carregar os eventos", e)
            }
        }
    }
}


