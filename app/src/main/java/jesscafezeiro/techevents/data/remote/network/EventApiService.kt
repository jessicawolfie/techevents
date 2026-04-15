package jesscafezeiro.techevents.data.remote.network

import jesscafezeiro.techevents.data.remote.model.EventDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApiService {
    // Endpoint para buscar todos os eventos
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    // Endpoint para buscar um evento específico (id dinâmico)
    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto
}