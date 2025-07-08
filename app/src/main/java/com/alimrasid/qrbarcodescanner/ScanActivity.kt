package com.alimrasid.qrbarcodescanner

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import android.Manifest
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScanActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 101
    private lateinit var scannerView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        scannerView = findViewById(R.id.zxing_barcode_scanner)

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            startScanner()
        }
    }

    private fun startScanner() {
        scannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text?.let { scannedText ->
                    scannerView.pause()

                    // Cek apakah hasilnya adalah URL
                    if (scannedText.startsWith("http://") || scannedText.startsWith("https://")) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(scannedText))
                        startActivity(browserIntent)
                    } else {
                        // Jika bukan URL, tetap kirim balik ke MainActivity
                        val intent = Intent()
                        intent.putExtra("RESULT", scannedText)
                        setResult(RESULT_OK, intent)
                    }

                    finish()
                }

            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::scannerView.isInitialized) {
            scannerView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::scannerView.isInitialized) {
            scannerView.pause()
        }
    }
}
