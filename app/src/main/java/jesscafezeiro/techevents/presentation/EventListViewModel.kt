package jesscafezeiro.techevents.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import jesscafezeiro.techevents.presentation.state.EventListDisplayData
import jesscafezeiro.techevents.presentation.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    // Estado do filtro selecionado
    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType = _selectedType.asStateFlow()

    // Variável para controlar o trabalho de busca (Debounce)
    private var searchJob: Job? = null

    init {
        fetchEvents()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery

        // Cancela a busca anterior se o usuário digitar rápido
        searchJob?.cancel()

        // Inicia uma nova contagem
        searchJob = viewModelScope.launch {
            delay(500)
            fetchEvents()
        }
    }

    //Função para alterar o filtro
    fun onTypeSelected(tipo: String?) {
        _selectedType.value = tipo
        fetchEvents()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // Pega os valores atuais da busca e do filtro
                val currentQuery = _searchQuery.value
                val currentType = _selectedType.value

                // Passa o filtro (tipo) para o repositório
                val eventFromApi = repository.getEvents(query = currentQuery, tipo = currentType)

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