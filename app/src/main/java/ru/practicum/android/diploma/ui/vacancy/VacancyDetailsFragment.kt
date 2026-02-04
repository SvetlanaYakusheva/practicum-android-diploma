package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySource
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.adapters.PhoneAdapter
import ru.practicum.android.diploma.util.UtilFunctions.formatSalary

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModel {
        parametersOf(vacancyId, sourceFragment)
    }

    private val phoneAdapter by lazy {
        PhoneAdapter { number ->
            viewModel.callPhone(number)
        }
    }
    private var vacancyId: String? = null
    private var sourceFragment: VacancySource? = null

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
        val sourceString = arguments?.getString(KEY_SOURCE_FRAGMENT)
        sourceFragment = sourceString?.let { VacancySource.valueOf(it) } ?: VacancySource.SEARCH

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.fillData()
        viewModel.observeVacancyDetailsState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.shareButton.setOnClickListener {
            viewModel.shareVacancy()
        }

        setupFavoriteButton()
        observeFavoriteState()
    }

    private fun setupFavoriteButton() {
        binding.favoritesButton.setOnClickListener {
            viewModel.onFavoriteButtonClicked()
        }
    }

    private fun observeFavoriteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collectLatest { isFavorite ->
                    binding.favoritesButton.isSelected = isFavorite
                    binding.favoritesButton.setImageResource(
                        if (isFavorite) {
                            R.drawable.ic_favorite_on_48
                        } else {
                            R.drawable.ic_favorite_off_48
                        }
                    )
                }
            }
        }
    }

    private fun render(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()
            is VacancyDetailsState.Content -> showContent(state.vacancy)
            is VacancyDetailsState.VacancyNotFoundError -> showVacancyNotFound()
            is VacancyDetailsState.VacancyServerError -> showServerError()
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            notFoundPlaceholder.isVisible = false
            notFoundText.isVisible = false
            vacancyDetailsScroll.isVisible = false
        }
    }

    private fun showContent(vacancy: Vacancy) {
        binding.apply {
            progressBar.isVisible = false
            notFoundPlaceholder.isVisible = false
            notFoundText.isVisible = false
            vacancyDetailsScroll.isVisible = true

            Glide.with(requireContext())
                .load(vacancy.employerLogoPath?.toUri())
                .placeholder(R.drawable.ic_employer_logo_placeholder_48)
                .into(companyLogo)

            vacancyName.text = vacancy.name
            salary.text = formatSalary(vacancy, requireContext())
            companyName.text = vacancy.employerName
            companyAddress.text = vacancy.addressFull ?: vacancy.addressCity
            experience.text = vacancy.experienceName
            schedule.text = buildString {
                append(vacancy.schedule)
                append(requireContext().getString(R.string.comma_space))
                append(vacancy.employment)
            }
            vacancyDescription.text = vacancy.description
            showSkills(vacancy.skills)
            showContacts(vacancy.contactsEmail, vacancy.contactsPhones, vacancy.name)
        }
    }

    private fun showVacancyNotFound() {
        binding.apply {
            progressBar.isVisible = false
            vacancyDetailsScroll.isVisible = false
            notFoundPlaceholder.isVisible = true
            notFoundText.isVisible = true
            notFoundPlaceholder.setImageResource(R.drawable.notfound2_icon)
            notFoundText.setText(R.string.vacancy_not_found)
        }
    }

    private fun showServerError() {
        binding.apply {
            progressBar.isVisible = false
            vacancyDetailsScroll.isVisible = false
            notFoundPlaceholder.isVisible = true
            notFoundText.isVisible = true
            notFoundPlaceholder.setImageResource(R.drawable.servererror2_icon)
            notFoundText.setText(R.string.server_error_message)
        }
    }

    private fun showSkills(skills: List<String>?) {
        binding.apply {
            if (skills.isNullOrEmpty()) {
                skillsTitle.isVisible = false
                skillsList.isVisible = false
            } else {
                skillsTitle.isVisible = true
                skillsList.isVisible = true
                skillsList.text = listToUI(skills)
            }
        }
    }

    private fun showContacts(contactsEmail: String?, contactsPhones: List<Phone>?, vacancyName: String) {
        binding.apply {
            if (contactsEmail.isNullOrEmpty()) {
                emailTitle.isVisible = false
                email.isVisible = false
            } else {
                emailTitle.isVisible = true
                email.isVisible = true
                email.text = contactsEmail
                email.setOnClickListener {
                    viewModel.openEmail(mailTo = contactsEmail, vacancyName = vacancyName)
                }
            }
            if (contactsPhones.isNullOrEmpty()) {
                phoneTitle.isVisible = false
                phoneRecyclerView.isVisible = false
            } else {
                phoneTitle.isVisible = true
                phoneRecyclerView.isVisible = true
                phoneRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                phoneRecyclerView.adapter = phoneAdapter
                phoneAdapter.updateList(contactsPhones)
            }
        }
    }

    private fun listToUI(skills: List<String>): String {
        var result = ""
        for (skill in skills) {
            result += "\n ${Typography.bullet} " + skill
        }
        return result.drop(1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_VACANCY_ID = "KEY_VACANCY_ID"
        const val KEY_SOURCE_FRAGMENT = "KEY_PREVIOUS_FRAGMENT"

        fun createArgs(id: String, sourceFragment: VacancySource): Bundle =
            bundleOf(KEY_VACANCY_ID to id, KEY_SOURCE_FRAGMENT to sourceFragment.name)
    }
}
