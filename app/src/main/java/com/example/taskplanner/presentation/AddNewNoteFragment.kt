package com.example.taskplanner.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.R
import com.example.taskplanner.data.model.entity.Note
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
import javax.inject.Inject

@AndroidEntryPoint
class AddNewNoteFragment : Fragment() {
    private val date = Day(LocalDate.now())
    private var time = LocalTime.of(0, 0)

    private var _binding: FragmentAddNewNoteBinding? = null
    private val binding: FragmentAddNewNoteBinding
        get() {
            return _binding!!
        }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory
    private val viewModel: AddNewNoteViewModel by viewModels { viewModelsFactory }

    private lateinit var stateTypeNote: StateType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.stateTypeNote.collect {
                stateTypeNote = it
                when(it) {
                    StateType.NOTE -> binding.containerTimeNote.visibility = View.GONE
                    StateType.REMINDER -> binding.containerTimeNote.visibility = View.VISIBLE
                    StateType.PRODUCTS -> binding.containerTimeNote.visibility = View.GONE
                    StateType.MEDICATIONS -> binding.containerTimeNote.visibility = View.VISIBLE
                }
            }
        }

        creatureFieldTypeNote()
        creatureFieldDataNote()
        creatureFieldTimeNote()
        saveNote()
    }

    private fun creatureFieldTypeNote() {
        val listTypesNote = List(StateType.entries.size) {
            StateType.entries[it].value
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_type_note, listTypesNote)
        binding.typeNote.setAdapter(arrayAdapter)
        binding.typeNote.setOnItemClickListener { _, _, position, _ ->
            viewModel.changeStateTypeNote(StateType.entries[position])
        }
    }

    private fun creatureFieldDataNote() {
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

    private fun creatureFieldTimeNote() {
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
                .setTitleText("Select Appointment time")
                .build()
        picker.show(childFragmentManager, "")
        picker.addOnPositiveButtonClickListener {
            time = LocalTime.of(picker.hour, picker.minute)
            binding.containerTimeNote.editText?.setText(time.toString())
        }
    }

    private fun saveNote() {
        binding.addNewTask.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                when (stateTypeNote) {
                    StateType.NOTE -> {
                        val note =
                            Note(date = date.date, title = binding.editTitleTask.text.toString())
                        viewModel.saveNote(note)
                    }

                    StateType.REMINDER -> {
                        val reminder = Reminder(date = date.date, title = binding.editTitleTask.text.toString(), time = time)
                        viewModel.saveNote(reminder)
                    }

                    StateType.PRODUCTS -> {}
                    StateType.MEDICATIONS -> {}
                }
                findNavController().navigate(R.id.action_addNewNoteFragment_to_plannerFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
