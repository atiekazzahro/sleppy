package com.example.sleppy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etJamTidur = findViewById<EditText>(R.id.etJamTidur)
        val etJamBangun = findViewById<EditText>(R.id.etJamBangun)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            val tidur = etJamTidur.text.toString()
            val bangun = etJamBangun.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("JAM_TIDUR", tidur)
            intent.putExtra("JAM_BANGUN", bangun)
            startActivity(intent)
        }
    }
}