package com.example.trianglefvrvgames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.isVisible
import com.example.trianglefvrvgames.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            it.isVisible = false
            binding.pointsTv.text = "Score: 0"
            binding.gameOverAnimationView.isVisible = false
            binding.trigonGameView.start()
        }

        binding.trigonGameView.gameInterface = object : TriangleView2.GameInterface {

            override fun stop() {
                binding.gameOverAnimationView.isVisible = true
                binding.startBtn.isVisible = true
            }

            override fun pause() {
                binding.startBtn.isVisible = true
            }

            override fun resume() {
                binding.startBtn.isVisible = true
            }

            override fun points(points: String) {
                binding.pointsTv.text = "Score: $points"
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (event.x < binding.trigonGameView.width / 2) {
                binding.trigonGameView.previousColor() // Left half of the screen
            } else {
                binding.trigonGameView.nextColor() // Right half of the screen
            }
            return true
        }
        return super.onTouchEvent(event)
    }
}