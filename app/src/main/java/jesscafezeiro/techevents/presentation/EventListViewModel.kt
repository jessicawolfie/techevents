package jesscafezeiro.techevents.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import jesscafezeiro.techevents.domain.model.Event
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import jesscafezeiro.techevents.presentation.state.EventListDisplayData
import jesscafezeiro.techevents.presentation.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

// @HiltViewModel // Descomente quando configurar Injeção de Dependência
@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModel  (
    private val repository: EventRepository
) : ViewModel() {

    // 1. Estado da Query de busca
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Estado do filtro selecionado
    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType = _selectedType.asStateFlow()

    // 3. O Fluxo de PagingData (Isolado e reativo)
    val eventsPagingFlow: Flow<PagingData<Event>> = combine(
        _searchQuery,
        _selectedType
    ) { query, tipo ->
        Pair(query, tipo)
    }.flatMapLatest { (query, tipo) ->
        repository.getEvents(query = query, tipo = tipo)
    }.cachedIn(viewModelScope)

    // 4. Estado da UI exclusivo para outros dados (como banners, header, validações)
    private val _uiState = MutableStateFlow<UiState<EventListDisplayData>>(UiState.Loading)
    val uiState: StateFlow<UiState<EventListDisplayData>> = _uiState.asStateFlow()

    init {
        // Inicializa dados extras REAIS aqui (ex: buscar perfil do usuário, banners)
        loadAdditionalScreenData()
    }

    private fun loadAdditionalScreenData() {
    }

    // Ações do Usuário (Intentions)
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onTypeSelected(tipo: String?) {
        _selectedType.value = tipo
    }
}