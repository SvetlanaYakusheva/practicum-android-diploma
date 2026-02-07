package ru.practicum.android.diploma.ui.region

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
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.location.RegionViewModel
import ru.practicum.android.diploma.ui.adapters.AreaAdapter
import ru.practicum.android.diploma.util.ErrorType

class RegionFragment : Fragment() {
    private var _binding: FragmentRegionBinding? = null
    private val binding
        get() = _binding!!

    private val regionViewModel by viewModel<RegionViewModel>()
    private val areaAdapter = AreaAdapter { area ->
        if (area != null) {
            regionViewModel.setRegionToFilter(area)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
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
            adapter = areaAdapter
            itemAnimator = null
        }

        inputEditText.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty()
            updateSearchIcon(query.isNotEmpty())
            regionViewModel.searchDebounce(query)
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

    private fun setupObservers() {
        regionViewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: RegionUiState) {
        when (state) {
            is RegionUiState.Error -> renderError(state.error)
            is RegionUiState.Content -> showContent(state.regionsList)
            is RegionUiState.Loading -> showLoading()
            is RegionUiState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() = with(binding) {
        progressBar.isVisible = false
        centralImageHolder.isVisible = true
        stateTextView.isVisible = true
        recyclerView.isVisible = false
        centralImageHolder.setImageResource(R.drawable.empty_list_icon)
        stateTextView.setText(R.string.region_not_found)
    }

    private fun showContent(regionList: List<Area>) = with(binding) {
        progressBar.isVisible = false
        centralImageHolder.isVisible = false
        stateTextView.isVisible = false
        recyclerView.isVisible = true
        areaAdapter.setData(regionList)
    }

    private fun showLoading() = with(binding) {
        progressBar.isVisible = true
        centralImageHolder.isVisible = false
        recyclerView.isVisible = false
        stateTextView.isVisible = false
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

        centralImageHolder.isVisible = true
        stateTextView.isVisible = true

        centralImageHolder.setImageResource(imageRes)
        stateTextView.setText(textRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
