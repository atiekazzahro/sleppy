package com.example.sleppy

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etJamTidur: EditText
    private lateinit var etJamBangun: EditText
    private lateinit var btnPilihJamTidur: ImageButton
    private lateinit var btnPilihJamBangun: ImageButton
    private lateinit var btnSimpan: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inisialisasi View dari Layout XML
        etNama = findViewById(R.id.etNama)
        etJamTidur = findViewById(R.id.etJamTidur)
        etJamBangun = findViewById(R.id.etJamBangun)
        btnPilihJamTidur = findViewById(R.id.btnPilihJamTidur)
        btnPilihJamBangun = findViewById(R.id.btnPilihJamBangun)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnReset = findViewById(R.id.btnReset)

        // 2. Fitur Interaktif Bantuan Pemilih Jam (TimePickerDialog)
        btnPilihJamTidur.setOnClickListener {
            tampilkanTimePicker(etJamTidur, 22, 0)
        }

        btnPilihJamBangun.setOnClickListener {
            tampilkanTimePicker(etJamBangun, 6, 0)
        }

        // 3. Listener Tombol Proses & Analisis (Tugas 2: Logika Interaktif & Navigasi)
        // Catatan Refleksi: Nilai EditText dibaca di dalam listener saat tombol diklik,
        // bukan di onCreate(), karena saat onCreate() pertama kali berjalan, pengguna belum memasukkan teks apa pun.
        btnSimpan.setOnClickListener {
            validasiDanProsesData()
        }

        // 4. Tombol Reset Formulir
        btnReset.setOnClickListener {
            resetFormulir()
        }
    }

    /**
     * Memunculkan TimePickerDialog untuk kemudahan input waktu
     */
    private fun tampilkanTimePicker(targetEditText: EditText, defaultJam: Int, defaultMenit: Int) {
        val dialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                targetEditText.setText(formattedTime)
                targetEditText.error = null // Menghapus pesan error jika waktu sudah dipilih
            },
            defaultJam,
            defaultMenit,
            true // Format 24 jam
        )
        dialog.show()
    }

    /**
     * Memvalidasi input pengguna, mengolah logika durasi tidur, dan bernavigasi ke layar kedua
     */
    private fun validasiDanProsesData() {
        // Membersihkan error sebelumnya
        etNama.error = null
        etJamTidur.error = null
        etJamBangun.error = null

        val namaInput = etNama.text.toString().trim()
        val jamTidurInput = etJamTidur.text.toString().trim()
        val jamBangunInput = etJamBangun.text.toString().trim()

        var isValid = true

        // --- VALIDASI INPUT (Ketentuan Tugas 2: Minimal 1 kolom divalidasi) ---

        // Validasi 1: Nama tidak boleh kosong
        if (namaInput.isEmpty()) {
            etNama.error = getString(R.string.err_nama_empty)
            etNama.requestFocus()
            isValid = false
        }

        // Validasi 2: Jam tidur tidak boleh kosong & format harus sesuai JJ:MM
        if (jamTidurInput.isEmpty()) {
            etJamTidur.error = getString(R.string.err_tidur_empty)
            if (isValid) etJamTidur.requestFocus()
            isValid = false
        } else if (!isFormatWaktuValid(jamTidurInput)) {
            etJamTidur.error = getString(R.string.err_format_tidur)
            if (isValid) etJamTidur.requestFocus()
            isValid = false
        }

        // Validasi 3: Jam bangun tidak boleh kosong & format harus sesuai JJ:MM
        if (jamBangunInput.isEmpty()) {
            etJamBangun.error = getString(R.string.err_bangun_empty)
            if (isValid) etJamBangun.requestFocus()
            isValid = false
        } else if (!isFormatWaktuValid(jamBangunInput)) {
            etJamBangun.error = getString(R.string.err_format_bangun)
            if (isValid) etJamBangun.requestFocus()
            isValid = false
        }

        // Jika salah satu validasi di atas gagal, hentikan eksekusi (syarat demo input tidak valid)
        if (!isValid) {
            Toast.makeText(this, "Mohon lengkapi atau perbaiki input terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi 4: Logika Waktu — Jam tidur dan bangun tidak boleh identik
        if (jamTidurInput == jamBangunInput) {
            etJamBangun.error = getString(R.string.err_waktu_sama)
            etJamBangun.requestFocus()
            Toast.makeText(this, getString(R.string.err_waktu_sama), Toast.LENGTH_SHORT).show()
            return
        }

        // --- PEMROSESAN LOGIKA (Ketentuan Tugas 2: Minimal satu interaksi logis diolah) ---
        val hasilDurasi = hitungDurasiTidur(jamTidurInput, jamBangunInput)
        val hasilAnalisis = evaluasiKualitasTidur(hasilDurasi.totalMenit)

        // --- NAVIGASI EKSPLISIT MEMBAWA HASIL OLAHAN (Ketentuan Tugas 2) ---
        val intentKeHasil = Intent(this, SecondActivity::class.java).apply {
            putExtra(SecondActivity.EXTRA_NAMA, namaInput)
            putExtra(SecondActivity.EXTRA_JAM_TIDUR, jamTidurInput)
            putExtra(SecondActivity.EXTRA_JAM_BANGUN, jamBangunInput)
            putExtra(SecondActivity.EXTRA_DURASI_TEKS, "${hasilDurasi.jam} Jam ${hasilDurasi.menit} Menit")
            putExtra(SecondActivity.EXTRA_STATUS, hasilAnalisis.status)
            putExtra(SecondActivity.EXTRA_SARAN, hasilAnalisis.saran)
            putExtra(SecondActivity.EXTRA_KATEGORI, hasilAnalisis.kategori)
        }

        startActivity(intentKeHasil)
    }

    /**
     * Memeriksa validitas format waktu 24 jam (00:00 s.d 23:59) dengan Regular Expression
     */
    private fun isFormatWaktuValid(waktu: String): Boolean {
        val polaWaktu = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
        return polaWaktu.matches(waktu)
    }

    /**
     * Menghitung selisih durasi tidur dalam jam & menit, menangani pergantian hari (lewat tengah malam)
     */
    private fun hitungDurasiTidur(jamTidurStr: String, jamBangunStr: String): DurasiResult {
        val partsTidur = jamTidurStr.split(":")
        val partsBangun = jamBangunStr.split(":")

        val menitMulai = partsTidur[0].toInt() * 60 + partsTidur[1].toInt()
        val menitSelesai = partsBangun[0].toInt() * 60 + partsBangun[1].toInt()

        var selisihMenit = menitSelesai - menitMulai
        if (selisihMenit < 0) {
            // Jika tidur malam dan bangun esok harinya (misal 22:30 ke 06:15), tambahkan 24 jam (1440 menit)
            selisihMenit += 24 * 60
        }

        val jam = selisihMenit / 60
        val menit = selisihMenit % 60

        return DurasiResult(jam, menit, selisihMenit)
    }

    /**
     * Mengkategorikan kualitas tidur dan merumuskan saran kesehatan berdasarkan total durasi tidur
     */
    private fun evaluasiKualitasTidur(totalMenit: Int): HasilAnalisis {
        val durasiJam = totalMenit / 60.0

        return when {
            durasiJam < 6.0 -> HasilAnalisis(
                status = "Kurang Tidur (Sleep Deprivation) ⚠️",
                saran = "Durasi tidur Anda kurang dari 6 jam. Kurang tidur dapat menurunkan daya fokus, memperlemah sistem imun, dan meningkatkan hormon stres. Usahakan tidur lebih awal malam ini!",
                kategori = "KURANG"
            )
            durasiJam < 7.0 -> HasilAnalisis(
                status = "Cukup Baik 💤",
                saran = "Durasi tidur Anda hampir mencukupi. Menambah 30 - 60 menit waktu istirahat akan membantu regenerasi sel dan pemulihan stamina tubuh dengan lebih optimal.",
                kategori = "CUKUP"
            )
            durasiJam <= 9.0 -> HasilAnalisis(
                status = "Tidur Ideal & Optimal 🌟",
                saran = "Luar biasa! Durasi tidur Anda berada pada rentang ideal kesehatan (7 - 9 jam). Pola tidur ini sangat baik untuk kejernihan berpikir dan vitalitas harian.",
                kategori = "IDEAL"
            )
            else -> HasilAnalisis(
                status = "Kelebihan Tidur (Oversleeping) 🛌",
                saran = "Durasi tidur Anda melebihi 9 jam. Terlalu lama berbaring dan tidur berlebih dapat menimbulkan rasa lemas atau sakit kepala saat bangun. Cobalah bangun lebih pagi dan bergerak aktif.",
                kategori = "LEBIH"
            )
        }
    }

    /**
     * Mengosongkan form input dan membersihkan pesan error
     */
    private fun resetFormulir() {
        etNama.text?.clear()
        etJamTidur.text?.clear()
        etJamBangun.text?.clear()

        etNama.error = null
        etJamTidur.error = null
        etJamBangun.error = null

        Toast.makeText(this, "Formulir telah direset", Toast.LENGTH_SHORT).show()
    }

    // Kelas Data Pembantu (Data Models)
    data class DurasiResult(val jam: Int, val menit: Int, val totalMenit: Int)
    data class HasilAnalisis(val status: String, val saran: String, val kategori: String)
}
