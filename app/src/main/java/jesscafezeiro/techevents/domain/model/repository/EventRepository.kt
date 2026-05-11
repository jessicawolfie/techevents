package jesscafezeiro.techevents.domain.model.repository

import androidx.paging.PagingData
import jesscafezeiro.techevents.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(query: String, tipo: String?): Flow<PagingData<Event>>
    suspend fun getEvent(id: String): Event?

    suspend fun getEventRecommendations(eventId: String): List<Event>
}