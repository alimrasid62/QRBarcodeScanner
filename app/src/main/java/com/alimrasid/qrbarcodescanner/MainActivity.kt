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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ScanFragment())
            .commit()

        val nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_scan -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ScanFragment())
                        .commit()
                    true
                }
                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HistoryFragment())
                        .commit()
                    true
                }
                else -> false
            }
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