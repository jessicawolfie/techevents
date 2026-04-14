package jesscafezeiro.techevents.presentation.state

import jesscafezeiro.techevents.domain.model.Event


// Essa classe é o packege de dados que a UI de lista vai consumir.
// Ela separa os eventos que a IA recomendou dos eventos gerais da API.
data class EventListDisplayData(
    // Lista gerada pelo modelo de ML.
    val recommendedEvents: List<Event> = emptyList(),

    // Lista completa vinda da API.
    val allEvents: List<Event> = emptyList()
)