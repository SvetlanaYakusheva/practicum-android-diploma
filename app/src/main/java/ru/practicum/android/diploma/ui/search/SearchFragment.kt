package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val vacancyAdapter by lazy {
        VacancyAdapter { vacancyId ->
            openVacancyDetails(vacancyId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vacancyRecycler.adapter = vacancyAdapter

        observeViewModel()

        binding.searchFilter.setOnClickListener {
            findNavController().navigate(R.id.action_search_fragment_to_filterFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: SearchUiState) {
        when (state) {
            is SearchUiState.Initial -> {
                binding.progressBar.visibility = View.GONE
                binding.vacancyRecycler.visibility = View.GONE
                binding.emptyPlaceholder.visibility = View.VISIBLE
            }

            is SearchUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.vacancyRecycler.visibility = View.GONE
                binding.emptyPlaceholder.visibility = View.GONE
            }

            is SearchUiState.Content -> {
                binding.progressBar.visibility = View.GONE
                binding.emptyPlaceholder.visibility = View.GONE
                binding.vacancyRecycler.visibility = View.VISIBLE
                vacancyAdapter.updateList(state.vacancies)
            }
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        findNavController().navigate(
            R.id.action_search_fragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancyId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
