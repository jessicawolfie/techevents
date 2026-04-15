package jesscafezeiro.techevents.data.repository

import jesscafezeiro.techevents.data.remote.model.toDomain
import jesscafezeiro.techevents.data.remote.network.EventApiService
import jesscafezeiro.techevents.domain.model.Event
import jesscafezeiro.techevents.domain.model.repository.EventRepository

class EventRepositoryImpl(
    private val eventApiService: EventApiService
) : EventRepository {
    
    override suspend fun getEvents(query: String): List<Event> {
        // Extração: vai na API e busca a lista de DTOs
        val dtoList = eventApiService.getEvents()
        
        // Transformação: DTO -> Domain (garantindo que a UI não dependa da estrutura da API)
        return dtoList.map { it.toDomain() }
    }

    override suspend fun getEvent(id: String): Event? {
        return try {
            eventApiService.getEvent(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}