package jesscafezeiro.techevents.data.remote.network

import jesscafezeiro.techevents.data.remote.model.EventDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApiService {
    // Endpoint para buscar todos os eventos com suporte a filtros e paginação
    @GET("events")
    suspend fun getEvents(
        @Query("query") query: String? = null,
        @Query("tipo") tipo: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): List<EventDto>

    // Endpoint para buscar um evento específico por ID
    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto
}
