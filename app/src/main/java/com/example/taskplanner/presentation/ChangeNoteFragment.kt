package com.example.taskplanner.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.data.model.CustomTextInputLayoutMultiLine
import com.example.taskplanner.data.model.CustomTextInputLayoutOneLine
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.StateType
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentChangeNoteBinding
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
class ChangeNoteFragment : Fragment() {
    private lateinit var paramNote: TypeNotes

    private var _binding: FragmentChangeNoteBinding? = null
    private val binding: FragmentChangeNoteBinding
        get() {
            return _binding!!
        }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory
    private val viewModel: ChangeNoteViewModel by viewModels { viewModelsFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                paramNote = it.getParcelable(ARG_PARAM_NOTE, TypeNotes::class.java)!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackingStateTypeNote()
        saveNote()
    }

    private fun trackingStateTypeNote() {
        lifecycleScope.launch {
            with(binding) {
                when (paramNote) {
                    is Note -> {
                        binding.typeNote.setText(StateType.NOTE.value)
                        val text = CustomTextInputLayoutMultiLine(requireContext())
                        binding.linearForInputText.addView(
                            text
                        )
                        text.binding.editTitleTask.setText(paramNote.title)
                        text.binding.editTitleTask.addTextChangedListener {
                            paramNote.title = it.toString()
                        }
                        creatureFieldDataNote(paramNote.date)
                    }

                    is Reminder -> {
                        binding.typeNote.setText(StateType.REMINDER.value)
                        val text = CustomTextInputLayoutMultiLine(requireContext())
                        binding.linearForInputText.addView(
                            text
                        )
                        text.binding.editTitleTask.setText(paramNote.title)
                        text.binding.editTitleTask.addTextChangedListener {
                            paramNote.title = it.toString()
                        }
                        creatureFieldDataNote(paramNote.date)
                        creatureFieldTime((paramNote as Reminder).time)
                    }

                    is Products -> {
                        binding.typeNote.setText(StateType.PRODUCTS.value)
                        buttonPlus.visibility = View.VISIBLE
                        creatureFieldDataNote(paramNote.date)
                        showAllProduct(linearForInputText, (paramNote as Products).listProducts)
                        buttonPlus.setOnClickListener {
                            addViewProduct(linearForInputText)
                        }
                    }

                    is Medications -> {
                        binding.typeNote.setText(StateType.MEDICATIONS.value)
                        val text = CustomTextInputLayoutMultiLine(requireContext())
                        binding.linearForInputText.addView(text)
                        text.binding.editTitleTask.setText(paramNote.title)
                        text.binding.editTitleTask.addTextChangedListener {
                            paramNote.title = it.toString()
                        }
                        creatureFieldDataNote(paramNote.date)
                        creatureFieldTime((paramNote as Medications).time)
                    }
                }
            }
        }
    }

    private fun showAllProduct(linearForInputText: LinearLayout, list: MutableList<String>) {
        list.forEach {
            linearForInputText.addView(
                CustomTextInputLayoutOneLine(
                    linearForInputText.childCount + 1,
                    requireContext()
                ).apply {
                    binding.editTitleTask.setText(it)
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

    private fun creatureFieldDataNote(date: LocalDate) {
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
                    paramNote.date = LocalDate.of(y, m + 1, d)
                    binding.dataNote.setText(paramNote.date.toString())
                },
                paramNote.date.year,
                paramNote.date.monthValue - 1,
                paramNote.date.dayOfMonth
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
                .setTitleText("Select Appointment time")
                .build()
        picker.show(childFragmentManager, "")
        picker.addOnPositiveButtonClickListener {
            when (paramNote) {
                is Medications -> {
                    (paramNote as Medications).time = LocalTime.of(picker.hour, picker.minute)
                    binding.containerTimeNote.editText?.setText((paramNote as Medications).time.toString())
                }

                is Reminder -> {
                    (paramNote as Reminder).time = LocalTime.of(picker.hour, picker.minute)
                    binding.containerTimeNote.editText?.setText((paramNote as Reminder).time.toString())
                }

                else -> {}
            }
        }
    }

    private fun saveNote() {
        binding.buttonSave.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                when (paramNote) {
                    is Products -> {
                        (paramNote as Products).apply {
                            listProducts.clear()
                            listProducts.addAll(extractListProducts())
                        }
                        viewModel.saveNote(paramNote)
                    }

                    else -> {
                        viewModel.saveNote(paramNote)
                    }
                }
                findNavController().navigate(R.id.action_changeNoteFragment_to_plannerFragment)
            }
        }
    }

    private fun extractListProducts(): MutableList<String> {
        val list = mutableListOf<String>()
        val index = binding.linearForInputText.childCount.minus(1)
        for (i in 0..index) {
            (binding.linearForInputText.getChildAt(i) as CustomTextInputLayoutOneLine).apply {
                val product = this.binding.editTitleTask.text.toString()
                if (product.trim().isNotEmpty()) {
                    list.add(product)
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
        const val ARG_PARAM_NOTE = "ARG_PARAM_NOTE"
    }
}
