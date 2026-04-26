package jesscafezeiro.techevents.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import jesscafezeiro.techevents.presentation.state.EventListDisplayData
import jesscafezeiro.techevents.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventListViewModel(
    private val repository: EventRepository
) : ViewModel() {
    // Estado da UI
    private val _uiState = MutableStateFlow<UiState<EventListDisplayData>>(UiState.Loading)
    val uiState: StateFlow<UiState<EventListDisplayData>> = _uiState.asStateFlow()

    //Estado da Query de busca
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        fetchEvents()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        fetchEvents(newQuery) // chama a API ou filtra cada letra digitada
    }

    fun fetchEvents(query: String = "") {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val eventFromApi = repository.getEvents(query)

                if (eventFromApi.isEmpty()) {
                    _uiState.value = UiState.Empty
                } else {
                    val recommendedMock = eventFromApi.take(2)
                    val displayData = EventListDisplayData(
                        recommendedEvents = recommendedMock,
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