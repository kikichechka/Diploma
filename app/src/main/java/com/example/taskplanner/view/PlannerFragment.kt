package com.example.taskplanner.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.diplomaproject.view.PlannerCalendarFragment
import com.example.taskplanner.view.adapter.VpAdapter
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentPlannerBinding
import com.google.android.material.tabs.TabLayoutMediator

class PlannerFragment : Fragment() {

    private val listFragmentPlanner =
        listOf(
            PlannerListFragment.newInstance(),
            PlannerCalendarFragment.newInstance()
        )

    private val tabIcons = listOf(
        R.drawable.baseline_checklist_24,
        R.drawable.baseline_calendar_month_24
    )
    private var _binding: FragmentPlannerBinding? = null
    private val binding: FragmentPlannerBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance() = PlannerFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        creatingVpAdapter()
    }
    private fun creatingVpAdapter() {
        val vpAdapter = VpAdapter(this, listFragmentPlanner)
        binding.containerViewPager2.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.containerViewPager2) { tab, pos ->
            tab.text = resources.getStringArray(R.array.list_name_fragment_for_planner)[pos]
            tab.icon = ResourcesCompat.getDrawable(resources, tabIcons[pos], null)
        }.attach()

        binding.addNewTask.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, AddNewNoteFragment.newInstance())
                .addToBackStack("AddNewNoteFragment")
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}