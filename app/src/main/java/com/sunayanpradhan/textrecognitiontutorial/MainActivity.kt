package com.sunayanpradhan.textrecognitiontutorial

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.sunayanpradhan.textrecognitiontutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    lateinit var binding: ActivityMainBinding

    private val REQUEST_IMAGE_CAPTURE=1

    private var imageBitmap: Bitmap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.apply {

            captureImage.setOnClickListener {

                takeImage()

                textView.text = ""


            }

            detectTextImageBtn.setOnClickListener {

                processImage()

            }

        }

        
    }


    private fun takeImage(){

        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {

            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)

        }
        catch (e:Exception){



        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){

            val extras: Bundle? = data?.extras

            imageBitmap= extras?.get("data") as Bitmap

            if (imageBitmap!=null) {

                binding.imageView.setImageBitmap(imageBitmap)

            }



        }


    }




    private fun processImage(){

        if (imageBitmap!=null) {

            val image = imageBitmap?.let {

                InputImage.fromBitmap(it, 0)

            }

            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener { visionText ->

                        binding.textView.text = visionText.text

                    }
                    .addOnFailureListener { e ->

                    }
            }

        }

        else{

            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()

        }


    }



}