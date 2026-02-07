package ru.practicum.android.diploma.ui.region

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.location.CountryViewModel
import ru.practicum.android.diploma.presentation.location.RegionViewModel
import ru.practicum.android.diploma.ui.adapters.AreaAdapter
import ru.practicum.android.diploma.ui.country.CountryUiState
import ru.practicum.android.diploma.ui.filter.FilterUiState
import ru.practicum.android.diploma.util.ErrorType
import kotlin.getValue

class RegionFragment : Fragment() {
    private var _binding: FragmentRegionBinding? = null
    private val binding
        get() = _binding!!

    private val regionViewModel by viewModel<RegionViewModel>()
    private val areaAdapter = AreaAdapter { area ->
        if (area != null) {
            regionViewModel.setCountryToFilter(area)
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

//        searchFrame.setEndIconOnClickListener {
//            if (inputEditText.text.isNullOrEmpty()) {
//                inputEditText.requestFocus()
//                showKeyboard(inputEditText)
//            } else {
//                inputEditText.text?.clear()
//                it.hideKeyboard()
//            }
//        }

    }

    private fun setupObservers() {
        regionViewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }


    private fun render(state: RegionUiState) {
        //textWatcher?.let { binding.salaryValue.removeTextChangedListener(it) }
        when (state) {
            is RegionUiState.Error -> renderError(state.error)
            is RegionUiState.Filtered -> showContent(state.regionsList)
            is RegionUiState.NotFiltered -> showContent(state.regionsList)
        }
//        textWatcher?.let { binding.salaryValue.addTextChangedListener(it) }
//        renderConfirmButtons()
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
    private fun setGrayColor(): ColorStateList {
        val grayColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.gray))
        )
        return grayColor
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
