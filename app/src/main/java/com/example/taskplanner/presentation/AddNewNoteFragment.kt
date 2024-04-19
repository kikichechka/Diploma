package com.example.taskplanner.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.R
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
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class AddNewNoteFragment : Fragment() {
    private val date = Day(LocalDate.now())
    private var time = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)

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

        trackingStateTypeNote()
        creatureFieldTypeNote()
        creatureFieldDataNote()
        creatureFieldTimeNote()
        saveNote()
    }

    private fun trackingStateTypeNote() {
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
        binding.scrollForLinearLayout.postDelayed(Runnable {
            binding.scrollForLinearLayout.fullScroll(
                View.FOCUS_DOWN
            )
        }, 0)
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

    private fun creatureFieldTypeNote() {
        binding.typeNote.text = Editable.Factory.getInstance().newEditable(stateTypeNote.value)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.item_type_note, viewModel.listTypesNote)
        (binding.containerTypeNote.editText as AutoCompleteTextView).setAdapter(arrayAdapter)
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
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                        val note =
                            Note(date = date.date, title = title)
                        viewModel.saveNote(note)
                    }

                    StateType.REMINDER -> {
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                        val reminder = Reminder(
                            date = date.date,
                            title = title,
                            time = time
                        )
                        viewModel.saveNote(reminder)
                    }

                    StateType.PRODUCTS -> {
                        val title = resources.getString(R.string.product)
                        val products = Products(
                            dateProducts = date.date,
                            titleProducts = title
                        )
                        val listProducts = extractListProducts()
                        viewModel.saveProducts(products, listProducts)
                    }

                    StateType.MEDICATIONS -> {
                        val title =
                            (binding.linearForInputText.getChildAt(0) as CustomTextInputLayoutMultiLine).binding.editTitleTask.text.toString()
                        val medications = Medications(
                            date = date.date,
                            title = title,
                            time = time
                        )
                        viewModel.saveNote(medications)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
