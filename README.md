# Sleppy - Night Sleep Tracker App 🌙

**Sleppy** adalah aplikasi pemantau waktu tidur berbasis Android yang dirancang dengan antarmuka pengguna (*UI*) bernuansa *Dark Mode*, bergaya minimalis, bersih, dan nyaman di mata untuk penggunaan malam hari[cite: 3].

Aplikasi ini dikembangkan menggunakan **Kotlin** dan **XML Layout** di VS Code & Android Studio untuk memberikan pengalaman pengguna (*UX*) yang responsif dalam mencatat serta melihat ringkasan waktu tidur[cite: 3].

---

## 📱 Fitur Utama Aplikasi

1. **Layar Beranda**
   * Formulir input waktu untuk mencatat jam tidur dan jam bangun[cite: 3].
   * Tombol aksi dengan aksen warna Indigo untuk menyimpan catatan[cite: 3].
   * Antarmuka serba gelap yang nyaman untuk mata di malam hari[cite: 3].

2. **Layar Ringkasan**
   * Menerima dan menampilkan data pengiriman jam tidur serta jam bangun secara presisi[cite: 3].
   * Kartu status ringkasan (*card container*) untuk menampilkan konfirmasi catatan berhasil disimpan[cite: 3].
   * Tombol navigasi kembali ke halaman utama[cite: 3].

---

## 📸 Tampilan Aplikasi (Screenshots)

<table>
  <tr>
    <td align="center"><b>Layar Beranda</b></td>
    <td align="center"><b>Layar Ringkasan</b></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/b81bbf3f-eb9f-4620-b460-33324c71b5bb" width="250" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/33e077b2-5860-4bb8-9c7c-f5d26aa9a9d1" width="225" /></td>
  </tr>
</table>

---

## 🛠️ Teknologi & Resource yang Digunakan

* **Language:** Kotlin
* **UI Layout:** XML (`ScrollView`, `LinearLayout`, `TextView`, `EditText`, `Button`, `ImageView`)[cite: 3]
* **Design Theme:** Custom Dark Palette (`#0F172A`, `#1E293B`, `#6366F1`)[cite: 3]
* **Navigasi Data:** Explicit `Intent` (`putExtra`, `getStringExtra`)[cite: 3]
* **Target Device:** Android[cite: 25]
