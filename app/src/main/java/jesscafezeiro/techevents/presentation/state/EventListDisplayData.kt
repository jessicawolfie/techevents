package jesscafezeiro.techevents.presentation.state

import androidx.paging.PagingData
import jesscafezeiro.techevents.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class EventListDisplayData(
    val recommendedEvents: List<Event> = emptyList(),
    val allEvents: Flow<PagingData<Event>> = emptyFlow()
)
