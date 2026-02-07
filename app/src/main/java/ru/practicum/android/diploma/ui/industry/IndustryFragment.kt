package ru.practicum.android.diploma.ui.industry

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.industry.IndustryViewModel
import ru.practicum.android.diploma.ui.adapters.IndustryAdapter
import ru.practicum.android.diploma.util.ErrorType

class IndustryFragment : Fragment() {

    private val industryViewModel by viewModel<IndustryViewModel>()
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!

    private val industryAdapter = IndustryAdapter { industry ->
        binding.selectButton.isVisible = industry != null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding) {
        topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = industryAdapter
            itemAnimator = null
        }

        inputEditText.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty()
            updateSearchIcon(query.isNotEmpty())
            industryViewModel.searchDebounce(query)
        }

        searchFrame.setEndIconOnClickListener {
            if (inputEditText.text.isNullOrEmpty()) {
                inputEditText.requestFocus()
                showKeyboard(inputEditText)
            } else {
                inputEditText.text?.clear()
                it.hideKeyboard()
            }
        }

        selectButton.setOnClickListener {
            it.hideKeyboard()
            industryViewModel.setIndustryToFilter(industryAdapter.getSelectedIndustry())
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        industryViewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: IndustryUiState) {
        when (state) {
            is IndustryUiState.Content -> showContent(state.industries)
            is IndustryUiState.Loading -> showLoading()
            is IndustryUiState.Empty -> showPlaceholder(
                R.drawable.empty_list_icon,
                R.string.industry_not_found
            )

            is IndustryUiState.Error -> renderError(state.error)
        }
    }

    private fun showContent(industryList: List<Industry>) = with(binding) {
        progressBar.isVisible = false
        centralImageHolder.isVisible = false
        stateTextView.isVisible = false
        recyclerView.isVisible = true
        industryAdapter.setData(industryList)
    }

    private fun showLoading() = with(binding) {
        progressBar.isVisible = true
        centralImageHolder.isVisible = false
        recyclerView.isVisible = false
        stateTextView.isVisible = false
        selectButton.isVisible = false
    }

    private fun renderError(errorType: ErrorType) {
        val (image, text) = when (errorType) {
            ErrorType.NoConnection -> R.drawable.nointernet_icon to R.string.internet_is_not_available
            ErrorType.ServerError -> R.drawable.servererror2_icon to R.string.server_error_message
            else -> R.drawable.placeholder_nothing to R.string.server_error_message
        }
        showPlaceholder(image, text)
    }

    private fun showPlaceholder(imageRes: Int, textRes: Int) = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        selectButton.isVisible = false

        centralImageHolder.isVisible = true
        stateTextView.isVisible = true

        centralImageHolder.setImageResource(imageRes)
        stateTextView.setText(textRes)
    }

    private fun updateSearchIcon(hasText: Boolean) {
        val iconRes = if (hasText) R.drawable.ic_close_icon_24 else R.drawable.ic_search_24
        binding.searchFrame.setEndIconDrawable(iconRes)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
