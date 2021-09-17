package com.example.qr_code_kotlin_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_generate_qrcode.*

class GenerateQRcodeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qrcode)

        btn_qrCode_create.setOnClickListener {
            getCodes()
        }

    }

    private fun getCodes() {
        try {
            qrCode()
            barCode()
        } catch (e: Exception) {
            Log.d("TAG", "getCodes: $e")
            edt_qrCode.error = "Please Enter number"
        }
    }

    //QR-Code
    private val multiFormant = MultiFormatWriter()
    private fun qrCode() {
        val bitMatrix =
            multiFormant.encode(edt_qrCode.text.toString(), BarcodeFormat.QR_CODE, 300, 300)
        val barcodeEncoder = BarcodeEncoder()
        val bitMap = barcodeEncoder.createBitmap(bitMatrix)
        iv_qr_code.setImageBitmap(bitMap)
    }

    //barCode
    private fun barCode() {
        val bitMatrix =
            multiFormant.encode(edt_qrCode.text.toString(), BarcodeFormat.CODE_128, 300, 120, null)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
        iv_bar_code.setImageBitmap(bitmap)
    }
}