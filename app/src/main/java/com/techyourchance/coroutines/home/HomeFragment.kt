package com.techyourchance.coroutines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.techyourchance.coroutines.R
import com.techyourchance.coroutines.common.BaseFragment

class HomeFragment : BaseFragment(), HomeArrayAdapter.Listener {

    override val screenTitle get() = "Coroutines Course"

    private lateinit var listScreensReachableFromHome: ListView
    private lateinit var adapterScreensReachableFromHome: HomeArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapterScreensReachableFromHome = HomeArrayAdapter(requireContext(), this)

        listScreensReachableFromHome = view.findViewById(R.id.list_screens)
        listScreensReachableFromHome.adapter = adapterScreensReachableFromHome

        adapterScreensReachableFromHome.addAll(*ScreenReachableFromHome.values())
        adapterScreensReachableFromHome.notifyDataSetChanged()

        return view
    }

    override fun onScreenClicked(screenReachableFromHome: ScreenReachableFromHome) {
        when (screenReachableFromHome) {
            ScreenReachableFromHome.UI_THREAD_DEMONSTRATION -> screensNavigator.toUiThreadDemonstration()
            ScreenReachableFromHome.BACKGROUND_THREAD_DEMO -> screensNavigator.toBackGroundThreadDemo()
            ScreenReachableFromHome.BASIC_COROUTINES_DEMO -> screensNavigator.toBasicCoroutinesDemo()
            ScreenReachableFromHome.EXERCISE_1 -> screensNavigator.toExercise1()
            ScreenReachableFromHome.EXERCISE_2 -> screensNavigator.toExercise2()
            ScreenReachableFromHome.COROUTINES_CANCELLATION_DEMO -> screensNavigator.toCoroutinesCancellationDemo()
            ScreenReachableFromHome.CONCURRENT_COROUTINES_DEMO -> screensNavigator.toConcurrentCoroutinesDemo()
        }
    }

    companion object {
        fun newInstance(): Fragment {
            return HomeFragment()
        }
    }
}