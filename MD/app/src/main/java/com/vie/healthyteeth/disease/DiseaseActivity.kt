package com.vie.healthyteeth.disease

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vie.healthyteeth.R
import com.vie.healthyteeth.databinding.ActivityDiseaseBinding

class DiseaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseBinding
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<DiseaseData>
    lateinit var imageId : Array<Int>
    lateinit var heading : Array<String>
    lateinit var desc : Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        imageId = arrayOf(
            R.drawable.caries_cavity,
            R.drawable.gingivitis,
            R.drawable.discolored_tooth,
            R.drawable.periodontitis
        )

        heading = arrayOf(
            getString(R.string.disease_cavities_title),
            getString(R.string.disease_gingivitis_title),
            getString(R.string.disease_tooth_discoloration_title),
            getString(R.string.disease_tooth_periodontitis_title),
        )

        desc = arrayOf(
            getString(R.string.disease_cavities_description),
            getString(R.string.disease_gingivitis_description),
            getString(R.string.disease_tooth_discoloration_description),
            getString(R.string.disease_tooth_periodontitis_description),
        )

        newRecyclerView = binding.recyclerView
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        getUserData()

    }

    private fun getUserData() {

        for (i in imageId.indices) {

            val desc = DiseaseData(imageId[i], heading[i])
            newArrayList.add(desc)
        }

        var adapter = MyAdapter(newArrayList)
        newRecyclerView.adapter = adapter
        adapter.setOnClickListener(object : MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@DiseaseActivity,DiseaseDetailActivity::class.java)
                intent.putExtra("heading",newArrayList[position].heading)
                intent.putExtra("imageId",newArrayList[position].titleImage)
                intent.putExtra("desc",desc[position])
                startActivity(intent)
            }

        })
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