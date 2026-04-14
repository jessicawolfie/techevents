package jesscafezeiro.techevents.domain.model
data class Event (
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val isOnline: Boolean,
    val tags: List<String>,
    val imageUrl: String? = null
)