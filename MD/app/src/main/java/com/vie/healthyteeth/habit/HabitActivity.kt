package com.vie.healthyteeth.habit

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.vie.healthyteeth.databinding.ActivityHabitBinding

class HabitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        var buttonYes = binding.habitYes
        var buttonNo = binding.habitNo

        buttonYes.setOnClickListener {
            Toast.makeText(this,"You did a great job!",Toast.LENGTH_LONG).show()
        }
        buttonNo.setOnClickListener {
            Toast.makeText(this,"Go brush your teeth!",Toast.LENGTH_LONG).show()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}