package jesscafezeiro.techevents.data.remote.network

import jesscafezeiro.techevents.data.remote.model.EventDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApiService {
    // Endpoint para buscar todos os eventos com filtro opcional de tipo
    @GET("events")
    suspend fun getEvents(
        @Query("tipo") tipo: String? = null
    ): List<EventDto>

    // Endpoint para buscar um evento específico por ID
    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto
}
