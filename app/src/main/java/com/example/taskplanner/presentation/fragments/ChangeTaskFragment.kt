package com.example.taskplanner.presentation.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.App
import com.example.taskplanner.R
import com.example.taskplanner.data.converters.ConverterPendingIntentRequestCode
import com.example.taskplanner.data.model.AlarmClockManager
import com.example.taskplanner.data.model.CustomTextInputLayoutMultiLine
import com.example.taskplanner.data.model.CustomTextInputLayoutOneLine
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.databinding.FragmentChangeNoteBinding
import com.example.taskplanner.presentation.viewmodel.ChangeTaskViewModel
import com.example.taskplanner.presentation.viewmodel.ViewModelsFactory
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class ChangeTaskFragment : Fragment() {
    private lateinit var paramTask: TypeTask

    private var _binding: FragmentChangeNoteBinding? = null
    private val binding: FragmentChangeNoteBinding
        get() {
            return _binding!!
        }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory
    private val viewModel: ChangeTaskViewModel by viewModels { viewModelsFactory }
    private var requestCode = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                paramTask = it.getParcelable(ARG_PARAM_TASK, TypeTask::class.java)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeNoteBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackingStateTypeTask()
        saveTask()
    }

    private fun trackingStateTypeTask() {
        lifecycleScope.launch {
            with(binding) {
                when (paramTask) {
                    is Note -> {
                        settingTextAndDate(StateType.NOTE.value)
                    }

                    is Reminder -> {
                        settingTextAndDate(StateType.REMINDER.value)
                        creatureFieldTime((paramTask as Reminder).time)
                        requestCode =
                            ConverterPendingIntentRequestCode.convertReminderToRequestCode(paramTask as Reminder)
                    }

                    is ProductsWithList -> {
                        binding.typeNote.setText(StateType.PRODUCTS.value)
                        buttonPlus.visibility = View.VISIBLE
                        creatureFieldDataTask(paramTask.date)
                        (paramTask as ProductsWithList).listProducts?.let {
                            showAllProduct(
                                linearForInputText,
                                it.toMutableList()
                            )
                        }
                        buttonPlus.setOnClickListener {
                            addViewProduct(linearForInputText)
                        }
                    }

                    is Medications -> {
                        settingTextAndDate(StateType.MEDICATIONS.value)
                        creatureFieldTime((paramTask as Medications).time)
                        requestCode =
                            ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(
                                paramTask as Medications
                            )
                    }
                }
            }
        }
    }

    private fun settingTextAndDate(value: String) {
        binding.typeNote.setText(value)
        val text = CustomTextInputLayoutMultiLine(requireContext())
        binding.linearForInputText.addView(
            text
        )
        text.binding.editTitleTask.setText(paramTask.title)
        text.binding.editTitleTask.addTextChangedListener {
            paramTask.title = it.toString()
        }
        creatureFieldDataTask(paramTask.date)
    }

    private fun showAllProduct(linearForInputText: LinearLayout, list: MutableList<Product>) {
        list.forEach {
            linearForInputText.addView(
                CustomTextInputLayoutOneLine(
                    linearForInputText.childCount + 1,
                    requireContext()
                ).apply {
                    binding.editTitleTask.setText(it.title)
                    binding.buttonDelete.setOnClickListener {
                        linearForInputText.removeViewAt(this.index - 1)
                        rewriteIndexChildLinear()
                    }
                }
            )
        }
    }

    private fun addViewProduct(linearForInputText: LinearLayout) {
        linearForInputText.addView(
            CustomTextInputLayoutOneLine(
                linearForInputText.childCount + 1,
                requireContext()
            ).apply {
                setHint(this.index.toString())
                binding.buttonDelete.setOnClickListener {
                    linearForInputText.removeViewAt(this.index - 1)
                    rewriteIndexChildLinear()
                }
            })
        binding.scrollForLinearLayout.postDelayed(Runnable {
            binding.scrollForLinearLayout.fullScroll(
                View.FOCUS_DOWN
            )
        }, 1)
    }

    private fun rewriteIndexChildLinear() {
        val index = binding.linearForInputText.childCount.minus(1)
        for (i in 0..index) {
            (binding.linearForInputText.getChildAt(i) as CustomTextInputLayoutOneLine).apply {
                this.index = i + 1
                this.setHint(this.index.toString())
            }
        }
    }

    private fun creatureFieldDataTask(date: LocalDate) {
        binding.containerDataNote.editText?.setText(date.toString())
        binding.dataNote.setOnClickListener {
            showCalendar()
        }
    }

    private fun showCalendar() {
        val datePicker =
            DatePickerDialog(
                requireContext(),
                { _, y: Int, m: Int, d: Int ->
                    paramTask.date = LocalDate.of(y, m + 1, d)
                    binding.dataNote.setText(paramTask.date.toString())
                },
                paramTask.date.year,
                paramTask.date.monthValue - 1,
                paramTask.date.dayOfMonth
            )
        datePicker.show()
    }

    private fun creatureFieldTime(time: LocalTime) {
        binding.containerTimeNote.isVisible = true
        binding.containerTimeNote.editText?.setText(time.toString())
        binding.timeNote.setOnClickListener {
            showTimePicker(time)
        }
    }

    private fun showTimePicker(time: LocalTime) {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(time.hour)
                .setMinute(time.minute)
                .setTitleText(getString(R.string.select_appointment_time))
                .build()
        picker.show(childFragmentManager, "")
        picker.addOnPositiveButtonClickListener {
            when (paramTask) {
                is Medications -> {
                    (paramTask as Medications).time = LocalTime.of(picker.hour, picker.minute)
                    binding.containerTimeNote.editText?.setText((paramTask as Medications).time.toString())
                }

                is Reminder -> {
                    (paramTask as Reminder).time = LocalTime.of(picker.hour, picker.minute)
                    binding.containerTimeNote.editText?.setText((paramTask as Reminder).time.toString())
                }

                else -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun saveTask() {
        binding.addNewTask.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                when (paramTask) {
                    is ProductsWithList -> {
                        viewModel.changeProductsWithList(
                            paramTask as ProductsWithList,
                            extractListTitle()
                        )
                    }

                    is Medications -> {
                        val oldIntent = AlarmClockManager.createIntent(
                            requestCode,
                            paramTask,
                            requireContext()
                        )
                        (requireContext().applicationContext as App).alarmManager.cancel(oldIntent)
                        viewModel.changeTask(paramTask, requireContext())
                    }

                    is Note -> {
                        viewModel.changeTask(paramTask, requireContext())
                    }

                    is Reminder -> {
                        val oldIntent = AlarmClockManager.createIntent(
                            requestCode,
                            paramTask,
                            requireContext()
                        )
                        (requireContext().applicationContext as App).alarmManager.cancel(oldIntent)
                        viewModel.changeTask(paramTask, requireContext())
                    }
                }
                findNavController().navigate(R.id.action_changeNoteFragment_to_plannerFragment)
            }
        }
    }

    private fun extractListTitle(): MutableList<String> {
        val list = mutableListOf<String>()
        val index = binding.linearForInputText.childCount.minus(1)
        for (i in 0..index) {
            (binding.linearForInputText.getChildAt(i) as CustomTextInputLayoutOneLine).apply {
                val title = this.binding.editTitleTask.text.toString()
                if (this.binding.editTitleTask.text.toString().trim().isNotEmpty()) {
                    list.add(title)
                }
            }
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_PARAM_TASK = "ARG_PARAM_TASK"
    }
}
