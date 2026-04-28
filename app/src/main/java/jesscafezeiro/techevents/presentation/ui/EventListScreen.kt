package jesscafezeiro.techevents.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jesscafezeiro.techevents.domain.model.Event

@Composable
fun EventListScreen(
    events: List<Event>,
    selectedType: String?,
    onTypeSelected: (String?) -> Unit
) {
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events) { evento ->
                EventCard(event = evento)
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

    Surface(
        modifier = Modifier.clickable { onClick() },
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
