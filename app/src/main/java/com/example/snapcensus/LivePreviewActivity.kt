// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.snapcensus

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snapcensus.common.CameraSource
import com.example.snapcensus.common.CameraSourcePreview
import com.example.snapcensus.common.GraphicOverlay
import com.example.snapcensus.textrecognition.TextRecognitionProcessor
import com.google.android.gms.common.annotation.KeepName
import timber.log.Timber
import java.io.IOException

/** Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source.  */
@KeepName
class LivePreviewActivity : AppCompatActivity() {

    private var cameraSource: CameraSource? = null
    private val textRecognitionProcessor = TextRecognitionProcessor()
    lateinit var fireFaceOverlay : GraphicOverlay
    lateinit var firePreview : CameraSourcePreview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        setContentView(R.layout.activity_live_preview)
        //firebase init

        initview()
        createCameraSource()
        observeText()

    }
    fun initview(){
        fireFaceOverlay = findViewById(R.id.fireFaceOverlay)
        firePreview = findViewById(R.id.firePreview)
    }

    @SuppressLint("MissingPermission")
    private fun observeText() {
        textRecognitionProcessor.readingText{
            if (it.isNotEmpty()) {
                cameraSource?.stop()
                cameraSource?.start()
            }
        }
    }


    private fun createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, fireFaceOverlay)
            cameraSource?.setMachineLearningFrameProcessor(textRecognitionProcessor)
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        cameraSource?.let {
            try {
                if (firePreview == null) {
                    Timber.d("resume: Preview is null")
                }
                if (fireFaceOverlay == null) {
                    Timber.d("resume: graphOverlay is null")
                }
                firePreview?.start(it, fireFaceOverlay)
            } catch (e: IOException) {
                Timber.e(e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        startCameraSource()
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        firePreview?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }


}
