package com.example.taskplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import com.example.taskplanner.view.PlannerFragment
import com.example.taskplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private val listFragmentPlanner =
//        listOf(
//            PlannerListFragment.newInstance(),
//            PlannerCalendarFragment.newInstance()
//        )
//
//    private val tabIcons = listOf(
//        R.drawable.baseline_checklist_24,
//        R.drawable.baseline_calendar_month_24
//    )

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, PlannerFragment.newInstance())
                .commit()
        }
//        creatingVpAdapter()
    }

//    private fun creatingVpAdapter() {
//        val vpAdapter = VpAdapter(this, listFragmentPlanner)
//        binding.containerViewPager2.adapter = vpAdapter
//        TabLayoutMediator(binding.tabLayout, binding.containerViewPager2) { tab, pos ->
//            tab.text = resources.getStringArray(R.array.list_name_fragment_for_planner)[pos]
//            tab.icon = getDrawable(tabIcons[pos])
//        }.attach()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}