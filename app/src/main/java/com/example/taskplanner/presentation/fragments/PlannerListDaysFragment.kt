package com.example.taskplanner.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.databinding.FragmentPlannerListDaysBinding
import com.example.taskplanner.presentation.ListDaysClickable
import com.example.taskplanner.presentation.viewmodel.PlannerListDaysViewModel
import com.example.taskplanner.presentation.adapters.CalendarAdapter
import com.example.taskplanner.presentation.viewmodel.ViewModelsFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlannerListDaysFragment : Fragment(), ListDaysClickable {
    private var _binding: FragmentPlannerListDaysBinding? = null
    private val binding: FragmentPlannerListDaysBinding
        get() {
            return _binding!!
        }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory

    private val viewModel: PlannerListDaysViewModel by viewModels { viewModelsFactory }
    @RequiresApi(Build.VERSION_CODES.S)
    private val myAdapter = CalendarAdapter(
        onClickTaskDelete = { note -> deleteTaskClick(note) },
        onClickTaskChange = { note -> changeTaskClick(note) },
        onClickTaskChangeFinished = { note -> changeFinishTask(note) },
        onClickProductChangeFinished = { product -> changeFinishProduct(product) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerListDaysBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollViewFragmentPlanner.adapter = myAdapter
        showDays()

        lifecycleScope.launch {
            binding.scrollViewFragmentPlanner.isVisible = false
            while (true) {
                delay(10)
                if (myAdapter.itemCount >= 25){
                    binding.scrollViewFragmentPlanner.scrollToPosition(myAdapter.itemCount / 2)
                    binding.scrollViewFragmentPlanner.isVisible = true
                    cancel()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showDays() {
        viewModel.getPageDay.onEach {
            myAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun refreshMyAdapter() {
        myAdapter.refresh()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun deleteTaskClick(task: TypeTask) {
        lifecycleScope.launch {
            viewModel.deleteTask(task, requireContext())
            Toast.makeText(requireContext(), getString(R.string.note_deleting), Toast.LENGTH_LONG)
                .show()
            refreshMyAdapter()
        }
    }

    override fun changeTaskClick(task: TypeTask) {
        val bundle = Bundle().apply {
            putParcelable(ChangeTaskFragment.ARG_PARAM_TASK, task)
        }
        findNavController().navigate(R.id.action_plannerFragment_to_changeNoteFragment, bundle)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun changeFinishTask(task: TypeTask) {
        lifecycleScope.launch {
            viewModel.changeFinishTask(task, requireContext())
            refreshMyAdapter()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun changeFinishProduct(product: Product) {
        lifecycleScope.launch {
            viewModel.changeFinishProduct(product)
            refreshMyAdapter()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        refreshMyAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
