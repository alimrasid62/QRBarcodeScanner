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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottom_navigation)

        viewPager.adapter = MainPagerAdapter(this)
        viewPager.isUserInputEnabled = true // Swipe aktif

        // Sinkronisasi BottomNav -> ViewPager
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_scan -> viewPager.currentItem = 0
                R.id.nav_history -> viewPager.currentItem = 1
            }
            true
        }

        // Sinkronisasi ViewPager -> BottomNav
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

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