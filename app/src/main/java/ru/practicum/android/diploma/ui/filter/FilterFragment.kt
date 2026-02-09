package ru.practicum.android.diploma.ui.filter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.presentation.filter.FilterViewModel

class FilterFragment : Fragment() {
    private var textWatcher: TextWatcher? = null
    private var _binding: FragmentFilterBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<FilterViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
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
        binding.workPlaceValue.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_locationFragment)
        }
        binding.workPlace.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_locationFragment)
        }

        binding.industryValue.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }
        binding.industry.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }

        setupSalaryField()

        binding.resetButton.setOnClickListener { viewModel.clearFilter() }
        binding.salaryIsRequiredCheck.setOnClickListener {
            viewModel.setSalaryIsRequired(binding.salaryIsRequiredCheck.isChecked)
            renderConfirmButtons()
        }
        binding.saveButton.setOnClickListener {
            viewModel.applyFilter()
            findNavController().popBackStack()
        }
    }

    private fun setupSalaryField() {
        val (emptyHintColor, blackHintColor, blueHintColor) = hintColorStates()

        binding.salaryValue.setOnFocusChangeListener { _, hasFocus ->
            updateSalaryHintOnFocus(
                hasFocus = hasFocus,
                emptyHintColor = emptyHintColor,
                blackHintColor = blackHintColor,
                blueHintColor = blueHintColor
            )
            updateSalaryEndIcon(binding.salaryValue.text)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                updateSalaryEndIcon(s)
                updateSalaryHintOnTextChange(text = s, emptyHintColor = emptyHintColor, blueHintColor = blueHintColor)
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setSalary(s.toString())
                renderConfirmButtons()
            }
        }

        binding.salaryValue.addTextChangedListener(textWatcher!!)
        binding.salaryFrame.setEndIconOnClickListener {
            clearSalaryField()
            it.hideKeyboard()
        }
    }

    private fun updateSalaryHintOnFocus(
        hasFocus: Boolean,
        emptyHintColor: ColorStateList,
        blackHintColor: ColorStateList,
        blueHintColor: ColorStateList
    ) {
        binding.salaryFrame.defaultHintTextColor = when {
            hasFocus && binding.salaryValue.text.isNullOrEmpty() -> emptyHintColor
            hasFocus -> blueHintColor
            binding.salaryValue.text.isNullOrEmpty() -> emptyHintColor
            else -> blackHintColor
        }
    }

    private fun updateSalaryEndIcon(text: CharSequence?) {
        val isVisible = !text.isNullOrEmpty()
        binding.salaryFrame.isEndIconVisible = isVisible

        if (isVisible) {
            binding.salaryFrame.setEndIconDrawable(R.drawable.ic_close_icon_24)
        } else {
            binding.salaryFrame.endIconDrawable = null
        }
    }

    private fun updateSalaryHintOnTextChange(
        text: CharSequence?,
        emptyHintColor: ColorStateList,
        blueHintColor: ColorStateList
    ) {
        binding.salaryFrame.defaultHintTextColor =
            if (text.isNullOrEmpty()) emptyHintColor else blueHintColor
    }

    private fun clearSalaryField() {
        binding.salaryValue.setText(getString(R.string.empty_string))
        binding.salaryFrame.isEndIconVisible = false
        viewModel.clearSalary()
        renderConfirmButtons()
    }

    private fun renderConfirmButtons() {
        binding.saveButton.isVisible = viewModel.currentFilterChanged()
        binding.resetButton.isVisible = !viewModel.currentFilterIsEmpty()
    }

    private fun render(state: FilterUiState) {
        textWatcher?.let { binding.salaryValue.removeTextChangedListener(it) }
        when (state) {
            FilterUiState.Empty -> emptyScreen()
            is FilterUiState.Filled -> filterScreen(state.filter)
        }
        textWatcher?.let { binding.salaryValue.addTextChangedListener(it) }
        renderConfirmButtons()
    }

    private fun filterScreen(filter: Filter) {
        val workplaceText = buildString {
            append(filter.country?.name ?: "")
            append(if (!filter.region?.name.isNullOrEmpty()) requireContext().getString(R.string.comma_space) else "")
            append(filter.region?.name ?: "")
        }
        binding.workPlaceValue.setText(workplaceText)
        fillLocation(workplaceText.isNotEmpty())

        val industryText = filter.industry?.name ?: ""
        binding.industryValue.setText(industryText)
        fillIndustry(industryText.isNotEmpty())

        binding.salaryIsRequiredCheck.isChecked = filter.onlyWithSalary

        val (emptyHintColor, blackHintColor, blueHintColor) = hintColorStates()
        if (filter.salary.isNullOrEmpty()) {
            binding.salaryValue.setText(R.string.empty_string)
            binding.salaryFrame.defaultHintTextColor = emptyHintColor
            binding.salaryFrame.isEndIconVisible = false
        } else {
            binding.salaryValue.setText(filter.salary)
            binding.salaryFrame.setEndIconDrawable(R.drawable.ic_close_icon_24)
            binding.salaryFrame.isEndIconVisible = true
            binding.salaryFrame.defaultHintTextColor =
                if (binding.salaryValue.hasFocus()) blueHintColor else blackHintColor
        }
    }

    private fun fillLocation(isNotEmpty: Boolean) {
        if (isNotEmpty) {
            binding.workPlace.setEndIconDrawable(R.drawable.ic_close_icon_24)
            binding.workPlace.isEndIconVisible = true
            binding.workPlace.defaultHintTextColor = setHintOnValueColor()
            binding.workPlace.setEndIconOnClickListener {
                viewModel.clearWorkplace()
                binding.workPlaceValue.setText(getString(R.string.empty_string))
                fillLocation(false)
                renderConfirmButtons()
            }
        } else {
            binding.workPlace.setEndIconDrawable(R.drawable.ic_arrow_forward)
            binding.workPlace.isEndIconVisible = true
            binding.workPlace.defaultHintTextColor = setGrayColor()
            binding.workPlace.setEndIconOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_locationFragment)
            }
        }
    }

    private fun fillIndustry(isNotEmpty: Boolean) {
        if (isNotEmpty) {
            binding.industry.setEndIconDrawable(R.drawable.ic_close_icon_24)
            binding.industry.isEndIconVisible = true
            binding.industry.defaultHintTextColor = setHintOnValueColor()
            binding.industry.setEndIconOnClickListener {
                viewModel.clearIndustry()
                binding.industryValue.setText(getString(R.string.empty_string))
                fillIndustry(false)
                renderConfirmButtons()
            }
        } else {
            binding.industry.setEndIconDrawable(R.drawable.ic_arrow_forward)
            binding.industry.isEndIconVisible = true
            binding.industry.defaultHintTextColor = setGrayColor()
            binding.industry.setEndIconOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
            }
        }
    }

    private fun emptyScreen() {
        with(binding) {
            workPlaceValue.setText(getString(R.string.empty_string))
            industryValue.setText(getString(R.string.empty_string))
            salaryValue.setText(getString(R.string.empty_string))
            salaryIsRequiredCheck.isChecked = false
            fillLocation(false)
            fillIndustry(false)
            salaryFrame.endIconDrawable = null
            salaryFrame.isEndIconVisible = false
            resetButton.isVisible = false
            saveButton.isVisible = false
        }
    }

    private fun hintColorStates(): Triple<ColorStateList, ColorStateList, ColorStateList> {
        val emptyHintColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.text_hint_color))
        )
        val blackHintColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.text_hint_color_after))
        )
        val blueHintColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.text_hint_color_blue))
        )
        return Triple(emptyHintColor, blackHintColor, blueHintColor)
    }

    private fun setGrayColor(): ColorStateList = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_enabled)),
        intArrayOf(ContextCompat.getColor(requireContext(), R.color.gray))
    )

    private fun setHintOnValueColor(): ColorStateList = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_enabled)),
        intArrayOf(ContextCompat.getColor(requireContext(), R.color.text_hint_country_industry))
    )

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
