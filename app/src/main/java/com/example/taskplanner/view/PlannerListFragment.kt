package com.example.taskplanner.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.taskplanner.domain.Day
import com.example.taskplanner.view.adapter.CalendarAdapter
import com.example.taskplanner.view.viewmodel.PlannerViewModel
import com.example.taskplanner.databinding.FragmentPlannerListBinding
import com.example.taskplanner.view.viewmodel.PlannerViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PlannerListFragment : Fragment(), OnItemClickDay {
    private var _binding: FragmentPlannerListBinding? = null
    private val binding: FragmentPlannerListBinding
    get() {
        return _binding!!
    }

    @Inject
    lateinit var plannerViewModelFactory: PlannerViewModelFactory

    private val viewModel: PlannerViewModel by viewModels { plannerViewModelFactory }
    private val myAdapter = CalendarAdapter(this)

    companion object{
        fun newInstance() = PlannerListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[PlannerViewModel::class.java]
//        viewModel.getLiveData().observe(viewLifecycleOwner) {
////            Toast.makeText(requireContext(), it.getCalendar().toString(), Toast.LENGTH_LONG).show()
//            show(it.getCalendar())
//        }
        binding.scrollViewFragmentPlanner.adapter = myAdapter
        viewModel.pageDay.onEach {
            Log.d("@@@" , "$it")
            myAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

//        viewModel.sentRequest()
    }

//    private fun show(it: List<Day>) {
//        binding.scrollViewFragmentPlanner.adapter = CalendarAdapter(it, this)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDayClick(day: Day) {

    }
}

