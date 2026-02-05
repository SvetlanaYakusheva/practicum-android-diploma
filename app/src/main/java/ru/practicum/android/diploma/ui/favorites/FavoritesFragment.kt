package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.VacancySource
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.ui.adapters.VacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsFragment

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()

    private val vacancyAdapter by lazy {
        VacancyAdapter { vacancyId ->
            openVacancyDetails(vacancyId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fillData()
        viewModel.observeFavoritesState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoritesUiState) {
        binding.apply {
            when (state) {
                is FavoritesUiState.Loading -> {
                    vacancyRecycler.isVisible = false
                    emptyPlaceholder.isVisible = false
                    emptyText.isVisible = false
                }

                is FavoritesUiState.Error -> {
                    vacancyRecycler.isVisible = false
                    emptyPlaceholder.isVisible = true
                    emptyText.isVisible = true
                    emptyPlaceholder.setImageResource(R.drawable.empty_list_icon)
                    emptyText.setText(R.string.error_no_vacancies_found)
                }

                is FavoritesUiState.Empty -> {
                    vacancyRecycler.isVisible = false
                    emptyPlaceholder.isVisible = true
                    emptyText.isVisible = true
                    emptyPlaceholder.setImageResource(R.drawable.placeholder_nothing)
                    emptyText.setText(R.string.favorites_list_is_empty)
                }

                is FavoritesUiState.Content -> {
                    vacancyRecycler.isVisible = true
                    emptyPlaceholder.isVisible = false
                    emptyText.isVisible = false
                    binding.vacancyRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.vacancyRecycler.adapter = vacancyAdapter
                    vacancyAdapter.setData(state.vacancies)
                }
            }
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        findNavController().navigate(
            R.id.action_favorites_fragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancyId, VacancySource.FAVORITES)
        )
    }
}
