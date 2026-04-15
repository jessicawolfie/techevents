package jesscafezeiro.techevents.data.remote.model

import com.google.gson.annotations.SerializedName

// Utilizado @SerializedName para avisar ao retrofit qual é a chave no JSON.
data class EventDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("isOnline")
    val isOnline: Boolean,
    @SerializedName("tags")
    val tags: List<String>?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)