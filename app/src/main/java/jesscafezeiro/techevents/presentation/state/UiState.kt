package jesscafezeiro.techevents.presentation.state

// Contrato de estado genérico para todas as telas do app.
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()

data class Error(
    val message: String,
    val exception: Throwable? = null
) : UiState<Nothing>()
}

