package com.example.sleppy

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_JAM_TIDUR = "EXTRA_JAM_TIDUR"
        const val EXTRA_JAM_BANGUN = "EXTRA_JAM_BANGUN"
        const val EXTRA_DURASI_TEKS = "EXTRA_DURASI_TEKS"
        const val EXTRA_STATUS = "EXTRA_STATUS"
        const val EXTRA_SARAN = "EXTRA_SARAN"
        const val EXTRA_KATEGORI = "EXTRA_KATEGORI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Inisialisasi komponen UI
        val tvNamaPengguna = findViewById<TextView>(R.id.tvNamaPengguna)
        val tvDurasi = findViewById<TextView>(R.id.tvDurasi)
        val tvBadgeStatus = findViewById<TextView>(R.id.tvBadgeStatus)
        val tvJamTidur = findViewById<TextView>(R.id.tvJamTidur)
        val tvJamBangun = findViewById<TextView>(R.id.tvJamBangun)
        val tvSaran = findViewById<TextView>(R.id.tvSaran)
        val tvDetailInfo = findViewById<TextView>(R.id.tvDetailInfo)
        val btnBagikan = findViewById<Button>(R.id.btnBagikan)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        // Mengambil data dari Intent dengan antisipasi null (Refleksi Pertemuan 6 No. 3)
        // Jika extra bernilai null atau key tidak ditemukan, fallback ke nilai default
        val nama = intent.getStringExtra(EXTRA_NAMA) ?: "Pengguna"
        val jamTidur = intent.getStringExtra(EXTRA_JAM_TIDUR) 
            ?: intent.getStringExtra("JAM_TIDUR") 
            ?: "22:00"
        val jamBangun = intent.getStringExtra(EXTRA_JAM_BANGUN) 
            ?: intent.getStringExtra("JAM_BANGUN") 
            ?: "06:00"
        val durasiTeks = intent.getStringExtra(EXTRA_DURASI_TEKS) ?: "8 Jam 0 Menit"
        val status = intent.getStringExtra(EXTRA_STATUS) ?: "Tidur Cukup 🌙"
        val saran = intent.getStringExtra(EXTRA_SARAN) 
            ?: "Durasi tidur yang cukup sangat penting untuk menjaga konsentrasi dan kesehatan fisik."
        val kategori = intent.getStringExtra(EXTRA_KATEGORI) ?: "IDEAL"

        // Menampilkan data olahan ke tampilan UI
        tvNamaPengguna.text = "Analisis pola istirahat untuk: $nama"
        tvDurasi.text = durasiTeks
        tvJamTidur.text = "$jamTidur WIB"
        tvJamBangun.text = "$jamBangun WIB"
        tvBadgeStatus.text = status
        tvSaran.text = saran

        // Kompatibilitas dengan TextView Tugas 1 jika diperlukan
        tvDetailInfo.text = "Jam Tidur: $jamTidur WIB\nJam Bangun: $jamBangun WIB\nDurasi: $durasiTeks\nStatus: $status"

        // Mengatur warna badge secara dinamis berdasarkan kategori hasil olahan
        aturWarnaBadge(tvBadgeStatus, kategori)

        // 1. Logika Navigasi Implisit (Implicit Intent): Bagikan Hasil Analisis
        btnBagikan.setOnClickListener {
            val pesanShare = """
                🌙 *Hasil Evaluasi Sleppy Tracker*
                👤 Nama: $nama
                ⏰ Mulai Tidur: $jamTidur WIB
                ☀️ Bangun Pagi: $jamBangun WIB
                ⏱️ Total Durasi: $durasiTeks
                📊 Status: $status
                
                💡 Rekomendasi:
                $saran
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Laporan Kualitas Tidur Sleppy")
                putExtra(Intent.EXTRA_TEXT, pesanShare)
            }
            startActivity(Intent.createChooser(shareIntent, "Bagikan Hasil Evaluasi Tidur via:"))
        }

        // 2. Navigasi Kembali ke Layar Utama
        btnKembali.setOnClickListener {
            finish()
        }
    }

    /**
     * Mengatur warna visual badge status berdasarkan kategori kesehatan tidur
     */
    private fun aturWarnaBadge(badge: TextView, kategori: String) {
        val (bgColorRes, textColor) = when (kategori) {
            "IDEAL" -> Pair(R.color.status_ideal_bg, ContextCompat.getColor(this, R.color.status_ideal))
            "CUKUP" -> Pair(R.color.status_cukup_bg, ContextCompat.getColor(this, R.color.status_cukup))
            "KURANG" -> Pair(R.color.status_kurang_bg, ContextCompat.getColor(this, R.color.status_kurang))
            "LEBIH" -> Pair(R.color.status_lebih_bg, ContextCompat.getColor(this, R.color.status_lebih))
            else -> Pair(R.color.surface_card, Color.WHITE)
        }

        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 48f
            setColor(ContextCompat.getColor(this@SecondActivity, bgColorRes))
            setStroke(2, textColor)
        }

        badge.background = backgroundDrawable
        badge.setTextColor(textColor)
    }
}
