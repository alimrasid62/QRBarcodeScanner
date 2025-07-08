package com.alimrasid.qrbarcodescanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView


class ScanFragment : Fragment() {


    private lateinit var scannerView: DecoratedBarcodeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        scannerView = view.findViewById(R.id.zxing_barcode_scanner)
        return view
    }

    override fun onResume() {
        super.onResume()
        scannerView.resume()

        scannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text?.let { scanned ->
                    scannerView.pause()

                    // Buka di browser jika URL
                    if (scanned.startsWith("http://") || scanned.startsWith("https://")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scanned))
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Hasil: $scanned", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        scannerView.pause()
    }


}