package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySource
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.adapters.SearchVacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsFragment
import ru.practicum.android.diploma.util.Constant.Companion.PER_PAGE_SIZE
import ru.practicum.android.diploma.util.ErrorType

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val vacancyAdapter by lazy {
        SearchVacancyAdapter { vacancyId ->
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

        setupUI()
        setupObservers()
        updateFilterIcon(viewModel.hasFilter())
    }

    private fun setupUI() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacancyAdapter
            itemAnimator = null
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = vacancyAdapter.itemCount
                    itemsCount.let {
                        if (pos >= it - 2) {
                            if (itemsCount > PER_PAGE_SIZE) {
                                viewModel.onLastItemReached()
                            }
                        }
                    }
                }
            }
        })

        searchInput.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty()
            viewModel.searchDebounce(query)
            updateSearchIcon(query.isNotEmpty())
        }

        searchInputLayout.setEndIconOnClickListener {
            searchInput.text?.clear()
            it.hideKeyboard()
            viewModel.clearSearch()
        }

        searchTopAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.filter) {
                findNavController().navigate(R.id.action_search_fragment_to_filterFragment)
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeShowToast().observe(viewLifecycleOwner) { toast -> showToast(toast) }
    }

    private fun updateSearchIcon(hasText: Boolean) {
        binding.searchInputLayout.endIconDrawable = if (hasText) {
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_close_icon_24)
        } else {
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_search_24)
        }
    }

    private fun render(state: SearchUiState) {
        when (state) {
            is SearchUiState.Content -> showContent(state.vacanciesList, state.countOfVacancies)
            is SearchUiState.EmptyQuery -> showPlaceholder(
                R.drawable.empty_list_icon,
                R.string.error_no_vacancies_found,
                showCount = true
            )

            is SearchUiState.ServerError -> showPlaceholder(R.drawable.error_icon, R.string.server_error_message)
            is SearchUiState.InternetNotAvailable -> showPlaceholder(
                R.drawable.nointernet_icon,
                R.string.internet_is_not_available
            )

            is SearchUiState.LoadingNewQuery -> showLoading()
            is SearchUiState.Default -> showDefault()
            is SearchUiState.NextPageLoading -> {
                binding.progressBar.isVisible = false
                vacancyAdapter.showLoading(true)
            }
        }
        updateFilterIcon(viewModel.hasFilter())
    }

    private fun showContent(vacancies: List<Vacancy>, count: Int?) = with(binding) {
        progressBar.isVisible = false
        emptyPlaceholder.isVisible = false
        stateTextView.isVisible = false
        recyclerView.isVisible = true

        vacancyCountTextView.isVisible = count != null
        count?.let {
            vacancyCountTextView.text = resources.getQuantityString(R.plurals.vacancy_of_vacancies, it, it)
        }

        vacancyAdapter.showLoading(false)
        vacancyAdapter.setData(vacancies)
    }

    private fun showPlaceholder(imageRes: Int, textRes: Int, showCount: Boolean = false) = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        emptyPlaceholder.isVisible = true
        stateTextView.isVisible = true

        emptyPlaceholder.setImageResource(imageRes)
        stateTextView.setText(textRes)

        vacancyCountTextView.isVisible = showCount
        if (showCount) vacancyCountTextView.setText(R.string.no_vacancies)
    }

    private fun showLoading() = with(binding) {
        recyclerView.isVisible = false
        emptyPlaceholder.isVisible = false
        stateTextView.isVisible = false
        vacancyCountTextView.isVisible = false
        progressBar.isVisible = true
    }

    private fun showDefault() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        stateTextView.isVisible = false
        vacancyCountTextView.isVisible = false
        emptyPlaceholder.isVisible = true
        emptyPlaceholder.setImageResource(R.drawable.empty_icon)
    }

    private fun updateFilterIcon(isFilterApplied: Boolean) {
        val iconRes = if (isFilterApplied) R.drawable.ic_filter_on_24 else R.drawable.ic_filter_off_24
        binding.searchTopAppBar.menu.findItem(R.id.filter).setIcon(iconRes)
    }

    private fun openVacancyDetails(vacancyId: String) {
        findNavController().navigate(
            R.id.action_search_fragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancyId, VacancySource.SEARCH)
        )
    }

    private fun showToast(errorType: ErrorType) {
        val messageRes = when (errorType) {
            ErrorType.NoConnection -> R.string.internet_is_not_available
            ErrorType.ServerError -> R.string.server_error_message
            else -> return
        }
        Toast.makeText(requireContext(), getString(messageRes), Toast.LENGTH_LONG).show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFilters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
