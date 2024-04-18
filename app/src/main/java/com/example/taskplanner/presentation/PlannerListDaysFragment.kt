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
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentPlannerListDaysBinding
import com.example.taskplanner.view.adapter.CalendarAdapter
import com.example.taskplanner.view.viewmodelfactory.ViewModelsFactory
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
    private val myAdapter = CalendarAdapter(
        onClickNoteDelete = { note -> deleteNoteClick(note) },
        onClickNoteChange = { note -> changeNoteClick(note) },
        onClickNoteChangeFinished = { note -> changeFinishNote(note) },
        onClickProductChangeFinished = { note, position -> changeFinishProduct(note, position) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerListDaysBinding.inflate(inflater)
        return binding.root
    }

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

    private fun showDays() {
        viewModel.getPageDay.onEach {
            myAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun refreshMyAdapter() {
        myAdapter.refresh()
    }

    override fun deleteNoteClick(note: TypeNotes) {
        lifecycleScope.launch {
            viewModel.deleteNote(note)
            Toast.makeText(requireContext(), getString(R.string.note_deleting), Toast.LENGTH_LONG)
                .show()
            refreshMyAdapter()
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
            refreshMyAdapter()
        }
    }

    override fun changeFinishProduct(note: TypeNotes, position: Int) {

    }

    override fun onResume() {
        super.onResume()
        refreshMyAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
