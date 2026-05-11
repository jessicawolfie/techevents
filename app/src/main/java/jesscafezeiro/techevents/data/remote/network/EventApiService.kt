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

    /**
     * Busca os detalhes completos de um evento específico.
     * @param id O identificador único do evento (UUID ou Hash).
     * @return O objeto [EventDto] serializado a partir do JSON da API.
     * @throws retrofit2.HttpException se o servidor retornar erro (ex: 404, 500)
     */
    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto

    // Endpoint de Machine Learning
    @GET("events/{id}/recommendations")
    suspend fun getEventRecommendations(
        @Path("id") eventId: String
    ): List<EventDto>
}
