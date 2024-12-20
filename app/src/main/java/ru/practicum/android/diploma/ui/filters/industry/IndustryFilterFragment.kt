package ru.practicum.android.diploma.ui.filters.industry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.vacancydetails.BadRequestError
import ru.practicum.android.diploma.data.vacancydetails.ErrorType
import ru.practicum.android.diploma.data.vacancydetails.NoInternetError
import ru.practicum.android.diploma.data.vacancydetails.ServerInternalError
import ru.practicum.android.diploma.databinding.FragmentIndustryChooserBinding
import ru.practicum.android.diploma.domain.filters.industry.model.IndustryFilterErrorType
import ru.practicum.android.diploma.domain.filters.industry.model.IndustryFilterModel
import ru.practicum.android.diploma.presentation.filters.industry.IndustryFilterViewModel
import ru.practicum.android.diploma.presentation.filters.industry.model.ChosenStatesFilter
import ru.practicum.android.diploma.presentation.filters.industry.model.IndustryFilterStates
import ru.practicum.android.diploma.util.debounce

class IndustryFilterFragment : Fragment() {
    private var _binding: FragmentIndustryChooserBinding? = null
    private val binding get() = _binding!!
    private val industryFilterViewModel: IndustryFilterViewModel by viewModel()
    private val industriesAdapter: IndustryFilterAdapter by lazy {
        IndustryFilterAdapter { industry: IndustryFilterModel ->
            industryFilterViewModel.selectIndustry(industry)
            industriesAdapter.selectIndustry(industry)
        }
    }

    private val searchDebounce by lazy {
        debounce<String>(
            delayMillis = DELAY_1000_MILLS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query ->
            industryFilterViewModel.searchIndustries(query)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentIndustryChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvIndustry.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIndustry.adapter = industriesAdapter

        industryFilterViewModel.observeIndustryState().observe(viewLifecycleOwner) { state: IndustryFilterStates ->
            when (state) {
                is IndustryFilterStates.Content -> {
                    binding.industryProgressBar.visibility = View.GONE
                    binding.errorIndustryCl.visibility = View.GONE
                    binding.errorPlaceholderIv.visibility = View.GONE
                    binding.errorPlaceholderTv.visibility = View.GONE
                    binding.rvIndustry.visibility = View.VISIBLE
                    val selectedIndustry = industryFilterViewModel.getSelectedIndustry()
                    industriesAdapter.setItems(state.industries, selectedIndustry)
                }
                is IndustryFilterStates.Error -> {
                    showTypeErrorOrEmpty(state.errorType)
                }
                is IndustryFilterStates.Empty -> {
                    showTypeErrorOrEmpty(IndustryFilterErrorType())
                }
                is IndustryFilterStates.Loading -> {
                    binding.industryProgressBar.visibility = View.VISIBLE
                    binding.errorPlaceholderIv.visibility = View.GONE
                    binding.errorPlaceholderTv.visibility = View.GONE
                    binding.errorIndustryCl.visibility = View.GONE
                    binding.rvIndustry.visibility = View.GONE
                }
            }
        }
        observeIndustryStateFilterChosenForVM()
    }
    private fun observeIndustryStateFilterChosenForVM() {
        industryFilterViewModel.observeIndustryStateFilterChosen()
            .observe(viewLifecycleOwner) { state: ChosenStatesFilter ->
                when (state) {
                    is ChosenStatesFilter.Chosen -> binding.btApply.visibility = View.VISIBLE
                    is ChosenStatesFilter.NotChosen -> binding.btApply.visibility = View.GONE
                }
            }
        industryFilterViewModel.getIndustry()
        setBindings()
    }

    private fun setBindings() {
        binding.btApply.setOnClickListener {
            industryFilterViewModel.saveSelectedIndustry()
            findNavController().navigateUp()
        }

        binding.backFromIndustryChooser.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce(s.toString())
                if (s.isNullOrEmpty()) {
                    binding.searchLayoutField.endIconDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.search_icon)
                } else {
                    binding.searchLayoutField.endIconDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.close_icon)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                println()
            }
        })
        binding.searchLayoutField.setEndIconOnClickListener {
            if (!binding.searchEditText.text.isNullOrEmpty()) {
                binding.searchEditText.text?.clear()
                industryFilterViewModel.searchIndustries(EMPTY_STRING)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTypeErrorOrEmpty(errorType: ErrorType) {
        binding.rvIndustry.visibility = View.GONE
        binding.industryProgressBar.visibility = View.GONE
        binding.errorIndustryCl.visibility = View.VISIBLE
        binding.errorPlaceholderIv.visibility = View.VISIBLE
        binding.errorPlaceholderTv.visibility = View.VISIBLE
        when (errorType) {
            is ServerInternalError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.server_error_search_image)
                binding.errorPlaceholderTv.setText(R.string.server_throwable_tv)
            }

            is NoInternetError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.no_internet_image)
                binding.errorPlaceholderTv.setText(R.string.internet_throwable_tv)
            }

            is IndustryFilterErrorType, is BadRequestError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.empty_list_image)
                binding.errorPlaceholderTv.setText(R.string.load_list_throwable_tv)
            }

            else -> {}
        }
    }
    private companion object {
        const val EMPTY_STRING = ""
        const val DELAY_1000_MILLS = 1000L
    }
}
