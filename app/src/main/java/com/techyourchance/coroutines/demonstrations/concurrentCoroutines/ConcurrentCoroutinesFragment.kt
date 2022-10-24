package com.techyourchance.coroutines.demonstrations.basiccoroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techyourchance.coroutines.R
import com.techyourchance.coroutines.common.BaseFragment
import com.techyourchance.coroutines.common.ThreadInfoLogger
import com.techyourchance.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*

class ConcurrentCoroutinesFragment : BaseFragment() {


    private val coroutinesScope = CoroutineScope(Dispatchers.Main.immediate)

    override val screenTitle get() = ScreenReachableFromHome.COROUTINES_CANCELLATION_DEMO.description

    private lateinit var btnStart: Button
    private lateinit var txtRemainingTime: TextView

    private var job: Job? = null
    private var jobCounter: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic_coroutines_demo, container, false)

        txtRemainingTime = view.findViewById(R.id.txt_remaining_time)

        btnStart = view.findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            logThreadInfo("button callback")

            val benchmarkDurationSeconds = 5

            jobCounter = coroutinesScope.launch {

                updateRemainingTime(benchmarkDurationSeconds)
            }

            job = coroutinesScope.launch {
                btnStart.isEnabled = false
                val iterationsCount = executeBenchmark(benchmarkDurationSeconds)
                Toast.makeText(requireContext(), "$iterationsCount", Toast.LENGTH_SHORT).show()
                btnStart.isEnabled = true
            }

        }

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onStop() {
        super.onStop()

        job?.apply {
            cancel()
            btnStart.isEnabled = true
        }

        jobCounter?.apply {
            cancel()
            txtRemainingTime.text = "done!"
        }
    }

    private suspend fun executeBenchmark(benchmarkDurationSeconds: Int): Long {
        return withContext(Dispatchers.Default) {
            logThreadInfo("benchmark started")

            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("benchmark completed")

            iterationsCount

        }

    }

    @SuppressLint("SetTextI18n")
    private suspend fun updateRemainingTime(remainingTimeSeconds: Int) {

        for (times in remainingTimeSeconds downTo 0) {
            logThreadInfo("updateRemainingTime: $times seconds")

            if (times > 0) {
                txtRemainingTime.text = "$times seconds remaining"
                delay(1000)
            } else {
                txtRemainingTime.text = "done!"
            }
        }


    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    companion object {
        fun newInstance(): Fragment {
            return ConcurrentCoroutinesFragment()
        }
    }
}