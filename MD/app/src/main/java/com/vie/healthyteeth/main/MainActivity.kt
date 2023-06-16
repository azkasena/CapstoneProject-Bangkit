package com.vie.healthyteeth.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.vie.healthyteeth.R
import com.vie.healthyteeth.databinding.ActivityMainBinding
import com.vie.healthyteeth.habit.HabitActivity
import com.vie.healthyteeth.model.UserPreference
import com.vie.healthyteeth.insight.InsightActivity
import com.vie.healthyteeth.disease.DiseaseActivity
import com.vie.healthyteeth.scan.ScanActivity
import com.vie.healthyteeth.view.ViewModelFactory
import com.vie.healthyteeth.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanButton.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        binding.diseaseButton.setOnClickListener {
            startActivity(Intent(this, DiseaseActivity::class.java))
        }

        binding.habitButton.setOnClickListener {
            startActivity(Intent(this, HabitActivity::class.java))
        }

        binding.insightButton.setOnClickListener {
            startActivity(Intent(this, InsightActivity::class.java))
        }

        setupView()
        setupViewModel()
        setupAction()

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

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]


        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            mainViewModel.logout()
        }
    }

}
