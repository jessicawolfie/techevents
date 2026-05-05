package jesscafezeiro.techevents.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import jesscafezeiro.techevents.domain.model.Event
import kotlinx.coroutines.flow.Flow

@Composable
fun EventListScreen(
    eventsFlow: Flow<PagingData<Event>>,
    selectedType: String?,
    onTypeSelected: (String?) -> Unit
) {
    val pagingItems = eventsFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        // Filtros (Horizontal)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    text = "Todos",
                    isSelected = selectedType == null,
                    onClick = { onTypeSelected(null) }
                )
            }
            item {
                FilterChip(
                    text = "Online",
                    isSelected = selectedType == "online",
                    onClick = { onTypeSelected("online") }
                )
            }
            item {
                FilterChip(
                    text = "Presencial",
                    isSelected = selectedType == "presencial",
                    onClick = { onTypeSelected("presencial") }
                )
            }
        }

        // Lista de Eventos (Vertical)
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = { index ->
                        val event = pagingItems.peek(index)
                        if (event != null) {
                            "${event.id}_$index"
                        } else {
                            "placeholder_$index"
                        }
                    },
                    contentType = pagingItems.itemContentType { "events" }
                ) { index ->
                    val evento = pagingItems[index]
                    if (evento != null) {
                        EventCard(event = evento)
                    }
                }

                // Carregando mais itens (Append)
                if (pagingItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                // Erro ao carregar mais itens (Append)
                if (pagingItems.loadState.append is LoadState.Error) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Falha ao carregar mais eventos.",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { pagingItems.retry() }) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
            }

            // Loading Inicial (Refresh)
            if (pagingItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Caso Vazio (NotLoading e itemCount == 0)
            if (pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0) {
                Text(
                    text = "Nenhum evento encontrado",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Erro Inicial (Refresh)
            if (pagingItems.loadState.refresh is LoadState.Error) {
                val error = pagingItems.loadState.refresh as LoadState.Error
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Erro ao carregar: ${error.error.localizedMessage}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { pagingItems.retry() }) {
                        Text("Tentar novamente")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    // Uso do parâmetro onClick nativo do Surface para melhor acessibilidade e ripple effect
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}