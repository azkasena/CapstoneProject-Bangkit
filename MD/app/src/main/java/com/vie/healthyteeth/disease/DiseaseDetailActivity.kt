package com.vie.healthyteeth.disease

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.vie.healthyteeth.databinding.ActivityDiseaseDetailBinding

class DiseaseDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDiseaseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val headingDisease : TextView = binding.diseaseDetailTitle
        val descDisease : TextView = binding.diseaseDetailDescription
        val imageDisease : ImageView = binding.diseaseDetailImage

        val bundle : Bundle?= intent.extras
        val heading = bundle!!.getString("heading")
        val imageId = bundle.getInt("imageId")
        val desc = bundle.getString("desc")

        headingDisease.text = heading
        descDisease.text = desc
        imageDisease.setImageResource(imageId)
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