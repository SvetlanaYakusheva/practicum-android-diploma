package ru.practicum.android.diploma.ui.vacancy

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.UtilFunctions.formatSalary

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

        viewModel.fillData()
        viewModel.observeVacancyDetailsState().observe(viewLifecycleOwner) {
            render(it)
        }
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
    private fun render (state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()
            is VacancyDetailsState.Content -> showContent(state.vacancy)
            is VacancyDetailsState.VacancyNotFoundError -> showVacancyNotFound()
            is VacancyDetailsState.VacancyServerError -> showServerError()

        }
    }

    private fun showLoading() {
        binding.apply {
            vacancyDetailsScroll.isVisible = false
            emptyPlaceholder.isVisible = false
            progressBar.isVisible = true
        }
    }
    private fun showContent(vacancy: Vacancy) {
        binding.apply {
            vacancyDetailsScroll.isVisible = true
            emptyPlaceholder.isVisible = false
            progressBar.isVisible = false

            Glide.with(requireContext())
                .load(Uri.parse(vacancy.employerLogoPath))
                .centerCrop()
                .placeholder(R.drawable.ic_employer_logo_placeholder_48)
                .into(companyLogo)

            vacancyName.text = vacancy.name
            salary.text = formatSalary(vacancy, requireContext())
            companyName.text = vacancy.employerName
            companyAddress.text = vacancy.addressFull ?: vacancy.areaName
            experience.text = vacancy.experienceName
            schedule.text = vacancy.schedule
            responsibilities.text = vacancy.description
            skillsList.text = vacancy.skills.toString()
            email.text = vacancy.contactsEmail
            phone.text = vacancy.contactsPhones
            comment.text = vacancy.contactsName

        }
    }

    private fun showVacancyNotFound() {
        binding.apply {
            vacancyDetailsScroll.isVisible = false
            emptyPlaceholder.isVisible = true
            progressBar.isVisible = false
        }
    }
    private fun showServerError() {
        binding.apply {
            vacancyDetailsScroll.isVisible = false
            emptyPlaceholder.isVisible = true
            progressBar.isVisible = false
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
