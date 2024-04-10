package com.example.taskplanner.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.FragmentPlannerListDaysBinding
import com.example.taskplanner.view.adapter.CalendarAdapter
import com.example.taskplanner.view.viewmodelfactory.ViewModelsFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

@AndroidEntryPoint
class PlannerListDaysFragment : Fragment(){
    private var _binding: FragmentPlannerListDaysBinding? = null
    private val binding: FragmentPlannerListDaysBinding
    get() {
        return _binding!!
    }

    @Inject
    lateinit var viewModelsFactory: ViewModelsFactory

    private val viewModel: PlannerListDaysViewModel by viewModels { viewModelsFactory }
    private val myAdapter = CalendarAdapter(onClickNoteDelete = { note -> onClickDeleteNote(note) })

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
    }

    private fun showDays() {
        viewModel.pageDay.onEach {
            it?.collect{pagingData ->
                myAdapter.submitData(pagingData)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onClickDeleteNote(note: TypeNotes) {
        lifecycleScope.launch {
            viewModel.deleteNote(note)
            Log.d("@@@", "myAdapter.itemCount =${myAdapter.itemCount.days}")
            Toast.makeText(requireContext(), "delete", Toast.LENGTH_LONG).show()
        }
    }
}