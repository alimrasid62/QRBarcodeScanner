package com.alimrasid.qrbarcodescanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)

        findViewById<Button>(R.id.btnScan).setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivityForResult(intent, 100)
        }

        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val result = data?.getStringExtra("RESULT") ?: return

            // Simpan ke DB
            lifecycleScope.launch {
                db.scanHistoryDao().insert(
                    ScanHistory(content = result, date = System.currentTimeMillis())
                )
            }

            // Tampilkan hasil
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }
    }
}