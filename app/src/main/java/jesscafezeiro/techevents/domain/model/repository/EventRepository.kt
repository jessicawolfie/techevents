package jesscafezeiro.techevents.domain.model.repository

import jesscafezeiro.techevents.domain.model.Event

// Contrato do Repositório de Eventos.
// Fica na camda de domain para que a ViewModel saiba o que pode pedir, sem precisar saber comos os dados são buscados.
interface EventRepository {
    suspend fun getEvents(query: String = "", tipo: String? = null): List<Event>
    suspend fun getEvent(id: String): Event?
}