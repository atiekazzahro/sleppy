package com.example.sleppy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvDetailInfo = findViewById<TextView>(R.id.tvDetailInfo)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val tidur = intent.getStringExtra("JAM_TIDUR") ?: "-"
        val bangun = intent.getStringExtra("JAM_BANGUN") ?: "-"

        tvDetailInfo.text = "Jam Tidur: $tidur WIB\nJam Bangun: $bangun WIB\nStatus: Catatan Berhasil Disimpan 🌙"

        btnKembali.setOnClickListener {
            finish()
        }
    }
}