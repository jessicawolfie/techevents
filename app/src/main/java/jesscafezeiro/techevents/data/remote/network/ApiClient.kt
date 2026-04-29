package jesscafezeiro.techevents.data.remote.network // Mantenha o seu pacote original aqui

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // <-- Importante para o tempo de espera!

object ApiClient {
    private const val BASE_URL = "https://techevents-api.onrender.com/"

    // O interceptor: vai imprimir no logcat do android studio o JSON exato da API
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // O motor(OkHttp): acoplando o interceptor de logs e adicionando os tempos de espera
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    // O construtor do retrofit: o by lazy garante que ele só vai ser inicializado quando for usado
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eventService: EventApiService by lazy {
        retrofit.create(EventApiService::class.java)
    }
}