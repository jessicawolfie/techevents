package jesscafezeiro.techevents.data.remote.model

import jesscafezeiro.techevents.domain.model.Event

// Função de extensão que transforma o DTO no modelo de domínio
fun EventDto.toDomain(): Event {
    return Event(
        id = id ?: "",
        title = title ?: "Evento sem título",
        date = date ?: "Data a definir",
        time = time ?: "",
        location = location ?: "Local a definir",
        isOnline = isOnline?: false,
        tags = tags ?: emptyList(),
        imageUrl = imageUrl
    )
}