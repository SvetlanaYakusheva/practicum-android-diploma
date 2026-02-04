package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.adapters.SearchVacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsFragment

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

        setDefaultState()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) { toast ->
            showToast(toast)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = vacancyAdapter
        binding.recyclerView.animation = null

        binding.searchInput.doAfterTextChanged { action ->
            viewModel.searchDebounce(action.toString())
            if (action?.isNotEmpty() == true) {
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_close_icon_24)
            } else {
                viewModel.clearSearch()
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_search_24)
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = vacancyAdapter.itemCount
                    itemsCount.let {
                        if (pos >= it - 2) {
                            viewModel.onLastItemReached()
                        }
                    }
                }
            }
        })

        binding.searchTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    findNavController().navigate(R.id.action_search_fragment_to_filterFragment)
                    true
                }

                else -> false
            }
        }

        binding.searchTopAppBar.menu.findItem(R.id.filter).setOnMenuItemClickListener { _ ->
            runFilter()
        }

        binding.searchInputLayout.setEndIconOnClickListener {
            binding.searchInput.setText(getString(R.string.empty_string))
            viewModel.clearSearch()
        }

    }

    private fun runFilter(): Boolean {
        findNavController().navigate(R.id.action_search_fragment_to_filterFragment)
        return true
    }

    private fun openVacancyDetails(vacancyId: String) {
        findNavController().navigate(
            R.id.action_search_fragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancyId)
        )
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> state.countOfVacancies?.let { showContent(state.vacanciesList, it) }
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.ServerError -> showError(state.errorMessage)
            is SearchState.LoadingNewExpression -> showLoading()
            is SearchState.InternetNotAvailable -> showLooseInternetConnection(state.errorMessage)
            is SearchState.Default -> setDefaultState()
            is SearchState.NextPageLoading -> vacancyAdapter.showLoading(true)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showLooseInternetConnection(errorMessage: String) {
        showImageAndTextState()
        binding.vacancyCountTextView.isVisible = false
        binding.emptyPlaceholder.setImageResource(R.drawable.nointernet_icon)
        binding.stateTextView.text = errorMessage
    }

    private fun showContent(vacanciesList: List<Vacancy>, countOfVacancies: Int) {
        binding.progressBar.isVisible = false
        binding.emptyPlaceholder.isVisible = false
        binding.recyclerView.isVisible = true
        binding.stateTextView.isVisible = false
        binding.vacancyCountTextView.isVisible = true
        binding.vacancyCountTextView.text = buildString {
            append(getString(R.string.vacancy_found))
            append(getString(R.string.empty_space))
            append(
                context?.resources?.getQuantityString(
                    R.plurals.vacancy_of_vacancies, countOfVacancies, countOfVacancies
                )
            )
        }
        vacancyAdapter.showLoading(false)
        vacancyAdapter.setData(vacanciesList)

    }

    private fun showError(errorMessage: String) {
        showImageAndTextState()
        binding.vacancyCountTextView.isVisible = false
        binding.emptyPlaceholder.setImageResource(R.drawable.error_icon)
        binding.stateTextView.text = errorMessage
    }

    private fun setDefaultState() {
        binding.vacancyCountTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.emptyPlaceholder.isVisible = true
        binding.recyclerView.isVisible = false
        binding.stateTextView.isVisible = false
        binding.emptyPlaceholder.setImageResource(R.drawable.empty_icon)
    }

    private fun showEmpty(message: String) {
        showImageAndTextState()
        binding.vacancyCountTextView.isVisible = true
        binding.vacancyCountTextView.text = getString(R.string.no_vacancies)
        binding.emptyPlaceholder.setImageResource(R.drawable.empty_list_icon)
        binding.stateTextView.text = message
    }

    private fun showImageAndTextState() {
        binding.progressBar.isVisible = false
        binding.emptyPlaceholder.isVisible = true
        binding.recyclerView.isVisible = false
        binding.stateTextView.isVisible = true
    }

    private fun showLoading() {
        binding.vacancyCountTextView.isVisible = false
        binding.progressBar.isVisible = true
        binding.emptyPlaceholder.isVisible = false
        binding.recyclerView.isVisible = false
        binding.stateTextView.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
