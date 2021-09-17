package com.example.qr_code_kotlin_android

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private val SELECT_PHOTO = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_qrCode.setOnClickListener {
            val scanner = IntentIntegrator(this)
            //scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) //bar code block only scan qr code
            //scanner.setBeepEnabled(false) // if not want beep sound
            scanner.initiateScan()

        }
        btn_qrCode_from_gallery.setOnClickListener {
            val photoPic = Intent(Intent.ACTION_PICK)
            photoPic.type = "image/*"
            startActivityForResult(photoPic, SELECT_PHOTO)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        when {
            //from camera
            result != null -> {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                tv_qrCode.text = result.contents
            }
            //from gallery
            resultCode == RESULT_OK -> {
                try {
                    val imageUri: Uri? = data?.data
                    val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    try {
                        val contents: String
                        val intArray = IntArray(selectedImage.width * selectedImage.height)
                        selectedImage.getPixels(
                            intArray,
                            0,
                            selectedImage.width,
                            0,
                            0,
                            selectedImage.width,
                            selectedImage.height
                        )
                        val source: LuminanceSource = RGBLuminanceSource(
                            selectedImage.width,
                            selectedImage.height,
                            intArray
                        )
                        val bitmap = BinaryBitmap(HybridBinarizer(source))
                        val reader: Reader = MultiFormatReader()
                        val results = reader.decode(bitmap)
                        contents = results.text
                        Toast.makeText(applicationContext, "Scanned: $contents", Toast.LENGTH_LONG)
                            .show()
                        tv_qrCode.text = contents
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun funGenerateQR(view: android.view.View) {
        startActivity(Intent(this, GenerateQRcodeActivity::class.java))
    }
}