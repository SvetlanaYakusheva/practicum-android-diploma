package ru.practicum.android.diploma.ui.location

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentLocationBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.location.LocationViewModel
import ru.practicum.android.diploma.ui.filter.FilterUiState
import kotlin.getValue

class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<LocationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.countryValue.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_countryFragment)
        }
        binding.country.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_countryFragment)
        }

        binding.regionValue.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_regionFragment)
        }
        binding.region.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_regionFragment)
        }
    }

    private fun render(state: FilterUiState) {
        when (state) {
            FilterUiState.Empty -> emptyScreen()
            is FilterUiState.Filled -> filterScreen(state.filter)
        }

        renderConfirmButtons()
    }

    private fun renderConfirmButtons() {
        binding.selectButton.isVisible = viewModel.currentFilterChanged()

    }

    private fun filterScreen(filter: Filter) {
        binding.countryValue.setText(filter.area?.name)

//        fillIndustry()
//        if (filter.salary.isNullOrEmpty() or (filter.salary == "")) {
//            binding.salaryValue.setText(R.string.empty_string)
//            binding.salaryFrame.defaultHintTextColor = hintColorStates().first
//        } else {
//            binding.salaryValue.setText(filter.salary)
//            binding.salaryFrame.setEndIconDrawable(R.drawable.ic_close_icon_24)
//            binding.salaryFrame.defaultHintTextColor = hintColorStates().second
//        }
        binding.selectButton.isVisible = true
    }

//    private fun fillIndustry() {
//        binding.industry.defaultHintTextColor = setGrayColor()
//        if (binding.industryValue.text.toString().isNotEmpty()) {
//            binding.industry.setEndIconDrawable(R.drawable.ic_close_icon_24)
//            binding.industry.defaultHintTextColor = setHintOnValueColor()
//            binding.industry.setEndIconOnClickListener {
//                viewModel.clearIndustry()
//                renderConfirmButtons()
//                binding.industryValue.setText(getString(R.string.empty_string))
//                binding.industry.setEndIconDrawable(R.drawable.ic_arrow_forward)
//                binding.industry.defaultHintTextColor = setGrayColor()
//                binding.industry.setEndIconOnClickListener {
//                    findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
//                }
//            }
//        } else {
//            binding.industry.setEndIconDrawable(R.drawable.ic_arrow_forward)
//            binding.industry.setEndIconOnClickListener {
//                renderConfirmButtons()
//                findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
//            }
//        }
//    }

    private fun emptyScreen() {
        with(binding) {
            country.defaultHintTextColor = setGrayColor()
            region.defaultHintTextColor = setGrayColor()

            selectButton.isVisible = false
            countryValue.setText(getString(R.string.empty_string))
            regionValue.setText(getString(R.string.empty_string))

            country.setEndIconDrawable(R.drawable.ic_arrow_forward)
            region.setEndIconDrawable(R.drawable.ic_arrow_forward)
        }
    }

    private fun setGrayColor(): ColorStateList {
        val grayColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.gray))
        )
        return grayColor
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFilter()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
