package ru.practicum.android.diploma.ui.filters.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryFilterBinding
import ru.practicum.android.diploma.domain.filters.area.model.Region
import ru.practicum.android.diploma.presentation.filters.area.CountryFilterViewModel

class CountryFilterFragment : Fragment() {
    private var _binding: FragmentCountryFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountryFilterViewModel by viewModel()
    private var countryList = mutableListOf<Region>()
    private val adapter: AreaListAdapter by lazy {
        AreaListAdapter(countryList, viewModel::setCountry)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCountry.layoutManager = LinearLayoutManager(context)

        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.pbCountry.visibility = View.VISIBLE
        binding.errorNoList.visibility = View.GONE

        viewModel.getAreaList()

        viewModel.getCountryListState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.getScreenExitTrigger().observe(viewLifecycleOwner) { flag ->
            if (flag) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun renderState(pair: Pair<List<Region>?, Int?>) {
        binding.pbCountry.visibility = View.GONE
        when (pair.first) {
            null -> {
                binding.rvCountry.visibility = View.GONE
                binding.errorNoList.visibility = View.VISIBLE

            }

            else -> {
                binding.rvCountry.visibility = View.VISIBLE
                binding.rvCountry.adapter = adapter
                countryList.clear()
                countryList.addAll(pair.first as MutableList<Region>)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
