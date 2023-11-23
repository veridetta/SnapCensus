package com.example.snapcensus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.snapcensus.helper.showSnackbar
import com.example.snapcensus.permissionkit.askPermissions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.marchinram.rxgallery.RxGallery

import timber.log.Timber
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var uriPath: Uri
    lateinit var btnCamera: Button
    lateinit var btnGallery: Button
    lateinit var textoutput:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        initview()
        supportActionBar?.hide()

        btnCamera.setOnClickListener {
            //showBottomView()
            RxGallery.photoCapture(this).subscribe({ uriPhoto ->
                Timber.d(uriPhoto.toString())
                uriPath = uriPhoto
                if (::uriPath.isInitialized) {
                    startOCR()
                } else {
                    Toast.makeText(this@MainActivity, "Mohon pilih gambar", Toast.LENGTH_SHORT).show()
                }

            }, { failed ->
                failed.message?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            })
        }
        btnGallery.setOnClickListener {
            RxGallery.gallery(this, false, RxGallery.MimeType.IMAGE)
                .subscribe({ uriPhoto ->
                    Timber.d(uriPhoto.toString())
                    uriPath = uriPhoto[0]
                    if (::uriPath.isInitialized) {
                        startOCR()
                    } else {
                        Toast.makeText(this@MainActivity, "Mohon pilih gambar", Toast.LENGTH_SHORT).show()
                    }
                }, { error ->
                    // Handle error here
                    Timber.e(error, "Error occurred")
                    // Optionally show a Toast or perform other error handling
                    showSnackbar(btnGallery, error.message ?: "Error occurred")
                })
        }


        reqPermission()

    }
    fun initview(){

        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGalerry)

    }

    private fun reqPermission() {
        askPermissions(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            onGranted {
                Toast.makeText(this@MainActivity, "Granted", Toast.LENGTH_SHORT).show()
            }

            onDenied {
                Toast.makeText(this@MainActivity, "Mohon izinkan", Toast.LENGTH_SHORT).show()
            }

            onShowRationale {
                Toast.makeText(this@MainActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }

            onNeverAskAgain {
                Toast.makeText(this@MainActivity, "Mohon Izinkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startOCR() {
        FirebaseApp.initializeApp(this)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val image = FirebaseVisionImage.fromFilePath(this, uriPath)
        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                for (blockText in firebaseVisionText.textBlocks) {
                    Timber.d(blockText.text)
                    val regexKtpPattern = "[0-9]{8,16}"
                    val pattern = Pattern.compile(regexKtpPattern)
                    val matcher = pattern.matcher(blockText.text)
                    Log.d("MainActivity", "startOCR: ${blockText.text}")
                    if (matcher.find()) {
                        //textoutput.text = matcher.group()
                        //textoutput.text = blockText.text
                        //intent with put extra
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("text", blockText.text)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener {
                Timber.e(it)
                Toast.makeText(this@MainActivity, "Failure {$it}", Toast.LENGTH_SHORT).show()
            }
    }

}
