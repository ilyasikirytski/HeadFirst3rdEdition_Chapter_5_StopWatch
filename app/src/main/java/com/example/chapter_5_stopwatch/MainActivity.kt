package com.example.chapter_5_stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter_5_stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var running = false
    private var offset: Long = 0

    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState != null) {
            binding.textView.text = savedInstanceState.getInt("answer").toString()
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                binding.chronometer.base = savedInstanceState.getLong(BASE_KEY)
                binding.chronometer.start()
            } else setBaseTime()
        }

        binding.startButton.setOnClickListener() {
            if (!running) {
                setBaseTime()
                binding.chronometer.start()
                running = true
            }
        }

        binding.pauseButton.setOnClickListener() {
            if (running) {
                saveOffset()
                binding.chronometer.stop()
                running = false
            }
        }

        binding.resetButton.setOnClickListener() {
            offset = 0
            setBaseTime()
        }
    }

    override fun onPause() {
        super.onPause()
        if (running){
            saveOffset()
            binding.chronometer.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running){
            setBaseTime()
            binding.chronometer.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("answer", 42)
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY,  binding.chronometer.base)
        super.onSaveInstanceState(outState)
    }

    private fun setBaseTime() {
        binding.chronometer.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() -  binding.chronometer.base
    }
}