package com.example.taskplanner.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentPlannerCalendarBinding
import com.example.taskplanner.view.adapter.AllNotesListAdapter
import com.example.taskplanner.view.viewmodelfactory.ViewModelsFactory
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDay()

        binding.calendar.setOnDateChangeListener { _, year, month, day ->
            selectedDay = LocalDate.of(year, month + 1, day)
            showDay()
        }
    }

    private fun showDay() {
        lifecycleScope.launch {
            val day = viewModel.getDay(selectedDay)

            with(binding) {
                date.text = day.date.format(formatter)

                if (day.list.isNotEmpty()) {
                    notTask.isVisible = false
                    if (day.date == LocalDate.now()) {
                        thisDate.text = getString(R.string.today)
                    } else {
                        thisDate.text = ""
                    }
                    scrollItemTask.adapter = AllNotesListAdapter(
                        onDeleteNote = { note -> deleteNoteClick(note) },
                        onClickChangeFinishedProduct = { prod ->
                            changeFinishProduct(
                                prod
                            )
                        },
                        onClickChangeFinishedNote = { note -> changeFinishNote(note) },
                        onChangeNote = { note -> changeNoteClick(note) },
                        day = day
                    )
                } else {
                    notTask.isVisible = true
                    notTask.text = root.context.getString(R.string.not_task)
                    scrollItemTask.adapter = null
                    thisDate.text = ""

                }
            }
        }
    }

    override fun deleteNoteClick(note: TypeNotes) {
        lifecycleScope.launch {
            viewModel.deleteNote(note)
            Toast.makeText(requireContext(), getString(R.string.note_deleting), Toast.LENGTH_LONG).show()
            showDay()
        }
    }

    override fun changeNoteClick(note: TypeNotes) {
        val bundle = Bundle().apply {
            putParcelable(ChangeNoteFragment.ARG_PARAM_NOTE, note)
        }
        findNavController().navigate(R.id.action_plannerFragment_to_changeNoteFragment, bundle)
    }

    override fun changeFinishNote(note: TypeNotes) {
        lifecycleScope.launch {
            viewModel.changeFinishNote(note)
            showDay()
        }
    }

    override fun changeFinishProduct(product: Product) {
        lifecycleScope.launch {
            viewModel.changeFinishProduct(product)
            showDay()
        }
    }

    override fun onResume() {
        super.onResume()
        showDay()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
