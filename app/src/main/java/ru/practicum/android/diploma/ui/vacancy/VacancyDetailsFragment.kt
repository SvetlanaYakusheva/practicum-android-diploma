package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModels()

    private var vacancyId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacancyId = requireArguments().getString(KEY_VACANCY_ID)

        // todo: пока заглушка
        viewModel.setInitialFavoriteState(false)

        setupFavoriteButton()
        observeFavoriteState()
    }

    private fun setupFavoriteButton() {
        binding.favoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }

    private fun observeFavoriteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collectLatest { isFavorite ->
                    binding.favoritesButton.isSelected = isFavorite
                    binding.favoritesButton.setImageResource(
                        if (isFavorite) {
                            R.drawable.favorites_tab_icon
                        } else {
                            R.drawable.favorites_icon
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_VACANCY_ID = "KEY_VACANCY_ID"

        fun createArgs(id: String): Bundle =
            bundleOf(KEY_VACANCY_ID to id)
    }
}
