package jesscafezeiro.techevents.presentation.details

import jesscafezeiro.techevents.domain.model.Event

// Usamos sealed interface para garantir que a tela só possa estar em um desses estados por vez
sealed interface EventDetailsUiState{
    data object Loading: EventDetailsUiState
    data class Success(
        val event: Event,
        val recommendations: List<Event> = emptyList()
        ) : EventDetailsUiState
    data class  Error(val message: String) : EventDetailsUiState
}