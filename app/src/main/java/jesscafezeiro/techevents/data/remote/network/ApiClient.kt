package jesscafezeiro.techevents.data.remote.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val BASE_URL = "http://10.0.2.2:8000/"
    
    // O interceptor: vai imprimir no logcat do android studio o JSON exato da API
    private val loggingInterceptor = HttpLoggingInterceptor().apply { 
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // O motor(OkHttp): acoplando o interceptor de logs à API
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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