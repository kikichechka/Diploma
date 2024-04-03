package com.example.taskplanner.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskplanner.domain.Day
import com.example.taskplanner.view.viewmodel.PlannerViewModel
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentAddNewNoteBinding
import java.time.LocalDate

class AddNewNoteFragment : Fragment() {
    private lateinit var viewModel: PlannerViewModel
    private val date = Day(LocalDate.now())
    private var _binding: FragmentAddNewNoteBinding? = null
    private val binding: FragmentAddNewNoteBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance() = AddNewNoteFragment()
    }

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
        viewModel = ViewModelProvider(this)[PlannerViewModel::class.java]
        creatureFieldTypeNote()
        creatureFieldDataNote()
    }

    private fun creatureFieldTypeNote() {
        val listTypesNote = resources.getStringArray(R.array.list_note_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_type_note, listTypesNote)
        binding.typeNote.setAdapter(arrayAdapter)

        binding.typeNote.setOnItemClickListener { _, _, position, id ->
            if (position == 0) {
                binding.editTitleTask.setText("первый")
            }
            if (position == 1) {
                binding.editTitleTask.setText("второй")
            }
        }
    }



    private fun creatureFieldDataNote() {
        binding.containerDataNote.editText?.setText(date.toString())
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
                    binding.dataNote.setText(date.toString())
                },
                date.date.year,
                date.date.monthValue - 1,
                date.date.dayOfMonth
            )
        datePicker.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}