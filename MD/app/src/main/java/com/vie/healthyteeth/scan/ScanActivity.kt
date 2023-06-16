package com.vie.healthyteeth.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vie.healthyteeth.api.ApiConfig
import com.vie.healthyteeth.api.FileUploadResponse
import com.vie.healthyteeth.databinding.ActivityScanBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var getFile: String? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        setupView()
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = getFile

            val title = "carries"
            val image = file

            val apiService = ApiConfig().getApiService()
            val uploadImageRequest = apiService.uploadImage(title, image!!)
            uploadImageRequest.timeout()
            uploadImageRequest.enqueue(object : retrofit2.Callback<FileUploadResponse> {
                override fun onResponse(
                    call: retrofit2.Call<FileUploadResponse>,
                    response: retrofit2.Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Toast.makeText(
                                this@ScanActivity,
                                responseBody.diagnosis,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@ScanActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<FileUploadResponse>, t: Throwable) {
                    Toast.makeText(this@ScanActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(
                this@ScanActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToBase64(uri)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)

            }
        }
    }

    fun uriToBase64(uri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val b64 = Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT)
        bitmap.recycle()
        return b64
    }

    fun base64ToFile(base64Str: String, dir: File, filename: String): File {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        val file = File(dir, filename)
        file.writeBytes(decodedBytes)
        return file
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