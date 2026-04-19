package jesscafezeiro.techevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import jesscafezeiro.techevents.domain.model.Event

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // lista falsa para testar visual dos cards
            val mockEvents = listOf(
                Event(
                    id = "1",
                    title = "Google I/O 2026",
                    date = "2026-05-10",
                    time = "13:00",
                    location = "Mountain View, CA",
                    isOnline = true,
                    tags = listOf("android", "ai", "compose")
                ),
                Event(
                    id = "2",
                    title = "Apple WWDC",
                    date = "2026-06-05",
                    time = "14:00",
                    location = "Cupertino, CA",
                    isOnline = true,
                    tags = listOf("ios", "swift", "mobile")
                ),
                Event(
                    id = "3",
                    title = "Web Summit Rio",
                    date = "2026-04-15",
                    time = "09:00",
                    location = "Rio de Janeiro, RJ",
                    isOnline = false,
                    tags = listOf("startups", "tecnologia", "networking")
                )
            )

            EventListScreen(events = mockEvents)
        }
    }
}