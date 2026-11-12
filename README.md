# 🌙 Sleppy Tracker — Tugas 2 Pemrograman Mobile

Aplikasi pelacak waktu dan analisis kualitas tidur berbasis Android (Kotlin). Proyek ini merupakan pengembangan dari Tugas 1 dengan menambahkan **logika interaktif, validasi input multi-kondisi, pengolahan data durasi tidur, serta navigasi antar-layar menggunakan Intent eksplisit dan implisit**.

---

## 📱 Daftar Layar (Activities)

1. **MainActivity (Layar Beranda & Formulir Input)**
   - Formulir input nama pengguna, jam mulai tidur, dan jam bangun.
   - Fitur bantuan pemilih jam waktu nyata (`TimePickerDialog`) atau input teks manual format 24 jam (`JJ:MM`).
   - Validasi input dengan pesan error langsung (`setError`).
   - Tombol **Analisis Kualitas Tidur** untuk memproses data dan bernavigasi ke layar hasil.
   - Tombol **Reset Formulir** untuk mengosongkan isian.

2. **SecondActivity (Layar Ringkasan & Hasil Evaluasi)**
   - Menampilkan salam personal kepada pengguna.
   - Menampilkan highlight **Total Durasi Tidur** (dalam Jam dan Menit).
   - Badge status tidur dinamis (*Tidur Ideal & Optimal*, *Cukup Baik*, *Kurang Tidur*, atau *Kelebihan Tidur*).
   - Kartu rincian jam tidur dan jam bangun.
   - Kartu catatan & rekomendasi medis terpersonalisasi.
   - Fitur **Bagikan Hasil Tidur** menggunakan *Implicit Intent* (`ACTION_SEND`).
   - Tombol **Kembali & Hitung Ulang** menggunakan `finish()`.

---

## ⚙️ Logika Interaktif & Pemrosesan Data (Ketentuan Tugas 2)

### 1. Validasi Input
Aplikasi dilengkapi validasi multi-kolom yang ketat untuk memastikan integritas data:
- **Validasi Kolom Kosong**: Memastikan kolom Nama, Jam Tidur, dan Jam Bangun tidak kosong. Jika kosong, sistem memunculkan error pada EditText terkait dan memindahkan fokus (`requestFocus`).
- **Validasi Format Jam (24 Jam)**: Menggunakan Regular Expression `^([01]?[0-9]|2[0-3]):[0-5][0-9]$` untuk memastikan input berupa waktu valid antara `00:00` hingga `23:59`.
- **Validasi Logika Waktu**: Mencegah jam bangun sama persis dengan jam tidur karena durasi istirahat malam 0 menit tidak realistis.

### 2. Logika Perhitungan Durasi Tidur
Perhitungan menangani kondisi normal maupun kondisi **melewati tengah malam (pergantian hari)**:
```kotlin
val menitMulai = jamTidur * 60 + menitTidur
val menitSelesai = jamBangun * 60 + menitBangun

var selisihMenit = menitSelesai - menitMulai
if (selisihMenit < 0) {
    // Menangani pergantian hari (misal tidur 22:30, bangun esok pagi 06:15)
    selisihMenit += 24 * 60
}
val durasiJam = selisihMenit / 60
val durasiMenit = selisihMenit % 60
```

### 3. Klasifikasi Kualitas Tidur & Saran Kesehatan
Berdasarkan standar rekomendasi durasi tidur dewasa:
- **< 6 Jam**: *Kurang Tidur (Sleep Deprivation) ⚠️* (Badge Merah) — Rekomendasi istirahat lebih awal untuk menjaga imunitas dan fokus.
- **6 s.d < 7 Jam**: *Cukup Baik 💤* (Badge Biru) — Rekomendasi menambah 30–60 menit waktu tidur.
- **7 s.d 9 Jam**: *Tidur Ideal & Optimal 🌟* (Badge Hijau) — Pola tidur optimal untuk regenerasi energi.
- **> 9 Jam**: *Kelebihan Tidur (Oversleeping) 🛌* (Badge Oranye/Kuning) — Mencegah rasa lemas akibat oversleeping.

### 4. Navigasi Data (Intent Eksplisit & Implisit)
- **Intent Eksplisit**: Mengirim data olahan (`EXTRA_NAMA`, `EXTRA_JAM_TIDUR`, `EXTRA_JAM_BANGUN`, `EXTRA_DURASI_TEKS`, `EXTRA_STATUS`, `EXTRA_SARAN`, `EXTRA_KATEGORI`) ke `SecondActivity`.
- **Intent Implisit**: Menggunakan `Intent.ACTION_SEND` untuk membagikan ringkasan analisis ke aplikasi pihak ketiga (WhatsApp, Catatan, Telegram, dll.).

---

## 💡 Jawaban Pertanyaan Refleksi (Pertemuan 6)

### 1. Mengapa input EditText dibaca di dalam listener, bukan di `onCreate`?
> **Jawaban:**  
> Method `onCreate()` dijalankan sekali saja pada saat siklus hidup Activity pertama kali dibuat dan diinisialisasi (*layout inflation*). Jika `editText.text.toString()` dibaca langsung di dalam `onCreate()`, nilainya pasti masih kosong (`""`) karena pengguna belum sempat mengetikkan apa pun di layar.  
> Oleh karena itu, pembacaan nilai input **wajib diletakkan di dalam event listener** (seperti `setOnClickListener` milik tombol), agar nilai teks diambil tepat pada saat tombol ditekan setelah pengguna selesai mengisi formulir.

### 2. Apa beda Intent eksplisit dan implisit? Beri satu contoh masing-masing.
> **Jawaban:**  
> - **Intent Eksplisit:** Digunakan untuk memanggil komponen tertentu (biasanya Activity di dalam aplikasi yang sama) secara langsung dengan menyebutkan nama kelas targetnya secara spesifik.  
>   *Contoh pada aplikasi ini:*  
>   `val intent = Intent(this, SecondActivity::class.java)` untuk berpindah dari layar utama ke layar hasil.  
> - **Intent Implisit:** Digunakan untuk melakukan suatu aksi (*action*) umum tanpa menyebutkan nama kelas target secara langsung, melainkan menyerahkan kepada sistem Android untuk mencari aplikasi/komponen yang dapat menangani aksi tersebut.  
>   *Contoh pada aplikasi ini:*  
>   `Intent(Intent.ACTION_SEND)` pada tombol "Bagikan Hasil Tidur" untuk membuka lembar share (*chooser*) ke WhatsApp, Gmail, atau aplikasi perpesanan lain.

### 3. Mengapa `getStringExtra` bisa menghasilkan null, dan bagaimana mengantisipasinya?
> **Jawaban:**  
> `getStringExtra` dapat menghasilkan nilai `null` apabila:
> 1. Key (kunci string) yang digunakan untuk mengambil data tidak cocok (typo) dengan key yang dikirimkan pada `putExtra`.
> 2. Activity pemanggil memang tidak pernah menyertakan extra dengan key tersebut.
> 3. Nilai yang dikirimkan oleh pemanggil memang bernilai `null`.
> 
> **Cara Mengantisipasi:**  
> 1. **Menggunakan Operator Elvis (`?:`) di Kotlin:** Menentukan nilai bawaan (*default value*) jika hasil pembacaan null, misalnya:  
>    `val nama = intent.getStringExtra(EXTRA_NAMA) ?: "Pengguna"`  
> 2. **Menggunakan Konstanta (*Companion Object*):** Menghindari kesalahan pengetikan key string dengan mendefinisikan konstanta (misal `const val EXTRA_NAMA = "EXTRA_NAMA"`).  
> 3. **Pengecekan `hasExtra()`:** Memeriksa keberadaan data terlebih dahulu dengan `intent.hasExtra(KEY)` sebelum mengambil datanya.

---

## 🎬 Panduan Perekaman Video Demo (1–2 Menit)

Berikut alur yang disarankan saat merekam video demo untuk memenuhi rubrik penilaian (skala 100):

| Durasi | Bagian | Aksi yang Ditampilkan |
| :--- | :--- | :--- |
| **00:00 – 00:20** | **Pengenalan** | Buka aplikasi Sleppy Tracker di HP/Emulator. Jelaskan nama aplikasi dan antarmuka beranda secara singkat. |
| **00:20 – 00:50** | **Kasus Input Tidak Valid (Wajib)** | 1. Kosongkan kolom lalu klik tombol *"Analisis Kualitas Tidur"* $\rightarrow$ Tunjukkan error *"Nama tidak boleh kosong"* & *"wajib diisi"*.<br>2. Masukkan format salah (misal jam tidur diisi `99:99` atau teks acak) $\rightarrow$ Tunjukkan error validasi format JJ:MM.<br>3. Masukkan jam tidur dan jam bangun yang sama persis (misal `06:00` dan `06:00`) $\rightarrow$ Tunjukkan pesan error waktu tidak boleh identik. |
| **00:50 – 01:25** | **Kasus Input Valid & Logika** | 1. Masukkan Nama: `Atiek Azzahro`.<br>2. Masukkan Jam Tidur: `22:30` (bisa gunakan tombol TimePicker).<br>3. Masukkan Jam Bangun: `06:00`.<br>4. Klik tombol *"Analisis Kualitas Tidur"*. |
| **01:25 – 01:45** | **Navigasi & Hasil Olahan** | Tunjukkan layar kedua terbuka mulus:<br>- Total durasi terhitung: **7 Jam 30 Menit**.<br>- Status: **Tidur Ideal & Optimal 🌟**.<br>- Rekomendasi medis terpersonalisasi.<br>- Klik tombol *"Bagikan"* (muncul share sheet Intent Implisit).<br>- Klik *"Kembali & Hitung Ulang"* untuk kembali ke beranda. |

---

## 🛠️ Cara Menjalankan Proyek

1. **Clone repository ini:**
   ```bash
   git clone <URL_REPOSITORY_ANDA>
   ```
2. Buka **Android Studio** (Koala / Ladybug atau versi terbaru).
3. Pilih menu **File > Open**, lalu arahkan ke direktori proyek `sleppy`.
4. Tunggu proses **Gradle Sync** selesai.
5. Jalankan aplikasi di Emulator Android (API 24+) atau perangkat fisik melalui kabel data USB / Wi-Fi Debugging.

