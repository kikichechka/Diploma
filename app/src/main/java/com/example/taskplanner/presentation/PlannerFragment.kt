package com.example.taskplanner.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentPlannerBinding
import com.google.android.material.tabs.TabLayoutMediator

class PlannerFragment : Fragment() {
    private var _binding: FragmentPlannerBinding? = null
    private val binding: FragmentPlannerBinding
        get() {
            return _binding!!
        }

    private lateinit var adapter: MyViewAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewPager()
        addNewTask()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createViewPager() {
        adapter = MyViewAdapter(this)
        viewPager = binding.containerForFragment
        viewPager.adapter = adapter

        TabLayoutMediator(binding.bottomNavigation, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.tabs_list)
                    tab.icon = resources.getDrawable(R.drawable.baseline_checklist_24, null)
                }

                1 -> {
                    tab.text = getString(R.string.tabs_calendar)
                    tab.icon = resources.getDrawable(R.drawable.baseline_calendar_month_24, null)
                }
            }
        }.attach()
    }

    private fun addNewTask() {
        binding.addNewTask.setOnClickListener {
            findNavController().navigate(R.id.action_plannerFragment_to_addNewNoteFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private class MyViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
            override fun getItemCount(): Int = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> PlannerListDaysFragment()
                    1 -> PlannerCalendarFragment()
                    else -> PlannerListDaysFragment()
                }
            }
        }
    }
}
