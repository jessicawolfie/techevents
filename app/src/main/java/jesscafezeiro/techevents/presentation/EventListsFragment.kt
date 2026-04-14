package jesscafezeiro.techevents.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jesscafezeiro.techevents.databinding.FragmentEventListBinding
import jesscafezeiro.techevents.domain.model.Event
import jesscafezeiro.techevents.domain.model.repository.EventRepository
import jesscafezeiro.techevents.presentation.state.UiState
import kotlinx.coroutines.delay

class EventListsFragment : Fragment() {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    // Instanciando a viewmodel (injeção de dependência provisória)
    private val viewModel: EventListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val mockRepository = object : EventRepository {
                    override suspend fun getEvents(query: String): List<Event> {
                        delay(2000)
                        return emptyList()
                    }

                    override suspend fun getEvent(id: String): Event? = null
                }
                @Suppress("UNCHECKED_CAST")
                return EventListViewModel(mockRepository) as T
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        
        val adapter = EventListAdapter{ eventClicado ->
            println("Usuário clicou no evento: ${eventClicado.title}")
        }
        binding.rvEvents.adapter = adapter
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState is UiState.Success) {
                adapter.submitList(uiState.data.allEvents)
            }
        }    
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
