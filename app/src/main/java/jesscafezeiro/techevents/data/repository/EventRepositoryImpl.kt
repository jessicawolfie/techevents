package jesscafezeiro.techevents.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import jesscafezeiro.techevents.data.remote.model.toDomain
import jesscafezeiro.techevents.data.remote.network.EventApiService
import jesscafezeiro.techevents.data.remote.paging.EventPagingSource
import jesscafezeiro.techevents.domain.model.Event
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(
    private val eventApiService: EventApiService
) : EventRepository {

    // Constante privada para centralizar a configuração de paginação
    companion object{
        private const val PAGE_SIZE = 20
    }

    override fun getEvents(query: String, tipo: String?): Flow<PagingData<Event>>{
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false // Evita a criação de itens nulos na UI antes da carga
            ),
            pagingSourceFactory = {
                EventPagingSource(apiService = eventApiService, query = query, tipo = tipo)
            }
        ).flow
    }

    override suspend fun getEvent(id: String): Event? {
        return try {
            eventApiService.getEvent(id).toDomain()
        } catch (e: Exception) {
            Log.e("TECH_EVENTS_DEBUG", "Erro ao buscar evento ID $id", e)
            null
        }
    }
}