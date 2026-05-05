package jesscafezeiro.techevents.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jesscafezeiro.techevents.data.remote.model.toDomain
import jesscafezeiro.techevents.data.remote.network.EventApiService
import jesscafezeiro.techevents.domain.model.Event
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

/**
 * PagingSource responsável por buscar fatias paginadas de eventos da API.
 * Gerencia o estado da rede e o mapeamento de DTO para Domínio.
 */
class EventPagingSource(
    private val apiService: EventApiService,
    private val query: String,
    private val tipo: String?
) : PagingSource<Int, Event>() {

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        // Encontra a página mais próxima da posição atual do usuário para recarregar
        // os dados de forma suave, sem jogar o scroll pro topo abruptamente.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val responseDto = apiService.getEvents(
                query = query,
                tipo = tipo,
                page = position,
                limit = params.loadSize
            )

            // Transformação isolada na camada de dados
            val events = responseDto.map { it.toDomain() }

            LoadResult.Page(
                data = events,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                // Se a API retornou uma lista vazia, atingimos o fim dos dados (End of Pagination)
                nextKey = if (events.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            // Falha de conectividade (sem internet, timeout)
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // Falha do servidor (ex: erro 404, 500)
            LoadResult.Error(exception)
        }
    }
}