package jesscafezeiro.techevents.presentation.navigation

import kotlinx.serialization.Serializable

// Rota sem argumentos (tela inicial)
@Serializable
object EventListRoutes

// Rota com argumentos (tela de detalhes)
@Serializable
data class EventDetailRoute(
    val eventId: String
)
