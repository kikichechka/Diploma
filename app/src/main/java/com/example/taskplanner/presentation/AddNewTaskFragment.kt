package com.example.taskplanner.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.App
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.R
import com.example.taskplanner.data.converters.ConverterPendingIntentRequestCode
import com.example.taskplanner.data.model.AlarmClockManager
import com.example.taskplanner.data.model.CustomTextInputLayoutMultiLine
import com.example.taskplanner.data.model.CustomTextInputLayoutOneLine
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.databinding.FragmentAddNewNoteBinding
import com.example.taskplanner.view.viewmodelfactory.ViewModelsFactory
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AddNewTaskFragment : Fragment() {
    private val date = Day(LocalDate.now())
    private var time = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)

    private var _binding: FragmentAddNewNoteBinding? = null
    private val binding: FragmentAddNewNoteBinding
        get() {
            return _binding!!
        }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory
    private val viewModel: AddNewTaskViewModel by viewModels { viewModelsFactory }

    private lateinit var stateTypeNote: StateType
    private lateinit var calendar: Calendar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewNoteBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        trackingStateTypeTask()
        creatureFieldTypeTask()
        creatureFieldDataTask()
        creatureFieldTimeTask()
        saveTask()
    }

    private fun trackingStateTypeTask() {
        lifecycleScope.launch {
            viewModel.stateTypeNote.collect {
                stateTypeNote = it

                with(binding) {
                    containerTimeNote.visibility = View.GONE
                    buttonPlus.visibility = View.GONE
                    linearForInputText.removeAllViews()
                    when (it) {
                        StateType.NOTE -> {
                            linearForInputText.addView(
                                CustomTextInputLayoutMultiLine(
                                    requireContext()
                                )
                            )
                        }

                        StateType.REMINDER -> {
                            containerTimeNote.visibility = View.VISIBLE
                            linearForInputText.addView(
                                CustomTextInputLayoutMultiLine(
                                    requireContext()
                                )
                            )
                        }

                        StateType.PRODUCTS -> {
                            buttonPlus.visibility = View.VISIBLE
                            addViewProduct(linearForInputText)
                            buttonPlus.setOnClickListener {
                                addViewProduct(linearForInputText)
                            }
                        }

                        StateType.MEDICATIONS -> {
                            containerTimeNote.visibility = View.VISIBLE
                            linearForInputText.addView(
                                CustomTextInputLayoutMultiLine(
                                    requireContext()
                                )
                            )
                        }
                    }
                }
            }
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

    private fun creatureFieldTypeTask() {
        binding.typeNote.text = Editable.Factory.getInstance().newEditable(stateTypeNote.value)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.item_type_note, viewModel.listTypesNote)
        (binding.containerTypeNote.editText as AutoCompleteTextView).setAdapter(arrayAdapter)
        binding.typeNote.setOnItemClickListener { _, _, position, _ ->
            viewModel.changeStateTypeTask(StateType.entries[position])
        }
    }

    private fun creatureFieldDataTask() {
        binding.containerDataNote.editText?.setText(date.date.toString())
        binding.dataNote.setOnClickListener {
            showCalendar()
        }
    }

    private fun showCalendar() {
        val datePicker =
            DatePickerDialog(
                requireContext(),
                { _, year: Int, month: Int, day: Int ->
                    date.changeDate(year, month + 1, day)
                    binding.dataNote.setText(date.date.toString())
                },
                date.date.year,
                date.date.monthValue - 1,
                date.date.dayOfMonth
            )
        datePicker.show()
    }

    private fun creatureFieldTimeTask() {
        binding.containerTimeNote.editText?.setText(time.toString())
        binding.timeNote.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(time.hour)
                .setMinute(time.minute)
                .setTitleText(getString(R.string.select_appointment_time))
                .build()
        picker.show(childFragmentManager, "")
        picker.addOnPositiveButtonClickListener {
            time = LocalTime.of(picker.hour, picker.minute)
            binding.containerTimeNote.editText?.setText(time.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ScheduleExactAlarm")
    private fun saveTask() {
        binding.addNewTask.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                calendarSet()
                when (stateTypeNote) {
                    StateType.NOTE -> {
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                                .trim()
                        if (title.isNotEmpty()) {
                            val note =
                                Note(date = date.date, title = title)
                            viewModel.saveTask(note)
                        }
                    }

                    StateType.REMINDER -> {
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                                .trim()

                        if (title.isNotEmpty()) {
                            val reminder = Reminder(
                                date = date.date,
                                title = title,
                                time = time
                            )
                            when {
                                (requireContext().applicationContext as App).alarmManager.canScheduleExactAlarms() -> {

                                    if (calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                                        (requireContext().applicationContext as App).alarmManager.setExact(
                                            AlarmManager.RTC_WAKEUP,
                                            calendar.timeInMillis,
                                            AlarmClockManager.createIntent(
                                                ConverterPendingIntentRequestCode.convertReminderToRequestCode(
                                                    reminder
                                                ),
                                                reminder,
                                                requireContext()
                                            )
                                        )
                                    }
                                    viewModel.saveTask(reminder)
                                }

                                else -> {
                                    startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                                }
                            }
                        }
                    }

                    StateType.PRODUCTS -> {
                        val title = resources.getString(R.string.product)
                        val products = Products(
                            dateProducts = date.date,
                            titleProducts = title
                        )
                        val listProducts = extractListProducts()
                        if (listProducts.isNotEmpty()) {
                            viewModel.saveProducts(products, listProducts)
                        }
                    }

                    StateType.MEDICATIONS -> {
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                                .trim()
                        if (title.isNotEmpty()) {
                            val medications = Medications(
                                date = date.date,
                                title = title,
                                time = time
                            )
                            when {
                                (requireContext().applicationContext as App).alarmManager.canScheduleExactAlarms() -> {
                                    if (calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                                        (requireContext().applicationContext as App).alarmManager.setExact(
                                            AlarmManager.RTC_WAKEUP,
                                            calendar.timeInMillis,
                                            AlarmClockManager.createIntent(
                                                ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(
                                                    medications
                                                ),
                                                medications,
                                                requireContext()
                                            )
                                        )
                                    }
                                    viewModel.saveTask(medications)
                                }

                                else -> {
                                    startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                                }
                            }
                        }
                    }
                }
                findNavController().navigate(R.id.action_addNewNoteFragment_to_plannerFragment)
            }
        }
    }

    private fun extractListProducts(): MutableList<String> {
        val list = mutableListOf<String>()
        val index = binding.linearForInputText.childCount.minus(1)
        for (i in 0..index) {
            (binding.linearForInputText.getChildAt(i) as CustomTextInputLayoutOneLine).apply {
                if (this.binding.editTitleTask.text?.isNotEmpty() == true) {
                    list.add(binding.editTitleTask.text.toString())
                }
            }
        }
        return list
    }

    private fun calendarSet() {
        calendar.set(
            date.date.year,
            date.date.monthValue - 1,
            date.date.dayOfMonth,
            time.hour,
            time.minute,
            0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
