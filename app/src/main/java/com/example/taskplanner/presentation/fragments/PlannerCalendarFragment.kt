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
import com.example.taskplanner.databinding.FragmentPlannerCalendarBinding
import com.example.taskplanner.presentation.ListDaysClickable
import com.example.taskplanner.presentation.viewmodel.PlannerCalendarViewModel
import com.example.taskplanner.presentation.adapters.AllNotesListAdapter
import com.example.taskplanner.presentation.viewmodel.ViewModelsFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class PlannerCalendarFragment : Fragment(), ListDaysClickable {
    private var _binding: FragmentPlannerCalendarBinding? = null
    private val binding: FragmentPlannerCalendarBinding
        get() {
            return _binding!!
        }

    @IgnoredOnParcel
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory

    private val viewModel: PlannerCalendarViewModel by viewModels { viewModelsFactory }

    private var selectedDay = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerCalendarBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDay()

        binding.calendar.setOnDateChangeListener { _, year, month, day ->
            selectedDay = LocalDate.of(year, month + 1, day)
            showDay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showDay() {
        lifecycleScope.launch {
            val day = viewModel.getDay(selectedDay)

            with(binding) {
                date.text = day.date.format(formatter)
                if (day.date == LocalDate.now()) {
                    thisDate.text = getString(R.string.today)
                } else {
                    thisDate.text = ""
                }

                if (day.list.isNotEmpty()) {
                    notTask.isVisible = false
                    scrollItemTask.adapter = AllNotesListAdapter(
                        onDeleteTask = { note -> deleteTaskClick(note) },
                        onClickChangeFinishedProduct = { prod ->
                            changeFinishProduct(
                                prod
                            )
                        },
                        onClickChangeFinishedTask = { note -> changeFinishTask(note) },
                        onChangeTask = { note -> changeTaskClick(note) },
                        day = day
                    )
                } else {
                    notTask.isVisible = true
                    notTask.text = root.context.getString(R.string.not_task)
                    scrollItemTask.adapter = null
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun deleteTaskClick(task: TypeTask) {
        lifecycleScope.launch {
            viewModel.deleteTask(task, requireContext())
            Toast.makeText(requireContext(), getString(R.string.note_deleting), Toast.LENGTH_SHORT).show()
            showDay()
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
            showDay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun changeFinishProduct(product: Product) {
        lifecycleScope.launch {
            viewModel.changeFinishProduct(product)
            showDay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        showDay()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
