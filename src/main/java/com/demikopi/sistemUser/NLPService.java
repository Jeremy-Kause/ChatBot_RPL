package com.demikopi.sistemUser;

/**
 * NLPService (Natural Language Processing Service)
 *
 * Kelas ini bertugas memahami dan memproses teks yang diketik oleh pengguna.
 * Ini adalah "otak" awal dari chatbot — mengubah kalimat bebas menjadi
 * sesuatu yang bisa dimengerti oleh program.
 *
 * Karena ini chatbot sederhana (rule-based), NLP di sini bukan AI/ML,
 * melainkan pencocokan kata kunci (keyword matching).
 *
 * Alur kerjanya:
 *   Input user (String)
 *     → preprocess()       : bersihkan & normalisasi teks
 *     → detectIntent()     : tentukan maksud/tujuan user
 *     → extractKeyword()   : ambil kata kunci penting dari input
 *     → hasil dikirim ke ChatEngine untuk diproses lebih lanjut
 */
public class NLPService {

    // Menyimpan input mentah dari user
    private String inputUser;

    // Menyimpan input setelah dibersihkan (lowercase, trim, dll.)
    // private String processedInput;

    public NLPService(String inputUser) {
        this.inputUser = inputUser;
    }

    // =========================================================
    // ENUM INTENT — Daftar semua kemungkinan maksud user
    // =========================================================
    // Intent adalah "apa yang ingin diketahui/dilakukan user".
    // Setiap intent akan menghasilkan respons yang berbeda dari chatbot.
    //
    // Contoh Intent yang direncanakan:
    //
    //   TANYA_MENU         → user bertanya daftar menu (kata kunci: "menu", "daftar", "ada apa")
    //   TANYA_KATEGORI     → user bertanya menu per kategori (kata kunci: "kopi", "makanan", "minuman")
    //   TANYA_REKOMENDASI  → user minta saran menu (kata kunci: "rekomen", "saran", "enak", "cocok")
    //   TANYA_DETAIL_MENU  → user tanya detail satu menu (kata kunci: nama menu spesifik)
    //   TANYA_JAM_BUKA     → user tanya jam operasional (kata kunci: "jam", "buka", "tutup")
    //   TANYA_LOKASI       → user tanya alamat kedai (kata kunci: "lokasi", "alamat", "di mana")
    //   TANYA_FASILITAS    → user tanya fasilitas (kata kunci: "fasilitas", "wifi", "parkir", "mushola")
    //   SALAM              → user menyapa (kata kunci: "halo", "hi", "hey", "selamat")
    //   TIDAK_DIKENAL      → input tidak cocok dengan intent manapun (fallback response)
    //
    // TODO: Buat enum Intent sebagai inner class atau class terpisah


    // =========================================================
    // METHOD: preprocess()
    // =========================================================
    // Membersihkan dan menormalisasi input sebelum diproses.
    // Yang perlu dilakukan:
    //   - Ubah ke lowercase: "MENU" → "menu"
    //   - Hapus spasi berlebih: "  menu   " → "menu"
    //   - (Opsional) Hapus tanda baca: "menu?" → "menu"
    //   - (Opsional) Normalisasi typo umum: "rekomenasi" → "rekomendasi"
    // Return: String yang sudah bersih
    // private String preprocess() { ... }


    // =========================================================
    // METHOD: detectIntent()
    // =========================================================
    // Menentukan maksud/tujuan dari input user berdasarkan kata kunci.
    // Gunakan if-else atau switch yang mengecek apakah processedInput
    // mengandung kata kunci tertentu (String.contains()).
    //
    // Contoh logika:
    //   if (input mengandung "rekomen" atau "saran") → return Intent.TANYA_REKOMENDASI
    //   if (input mengandung "jam" atau "buka")      → return Intent.TANYA_JAM_BUKA
    //   if (input mengandung "halo" atau "hi")       → return Intent.SALAM
    //   (tidak cocok semua)                          → return Intent.TIDAK_DIKENAL
    //
    // Return: Intent (enum)
    // public Intent detectIntent() { ... }


    // =========================================================
    // METHOD: extractKeyword()
    // =========================================================
    // Mengambil kata kunci spesifik dari input untuk penyaringan data.
    // Digunakan terutama untuk intent TANYA_REKOMENDASI dan TANYA_KATEGORI.
    //
    // Contoh penggunaan:
    //   Input: "aku mau yang manis dan dingin"
    //   extractKeyword() → "manis" (akan dipakai di menuDAO.getMenuByKriteria("manis"))
    //
    //   Input: "ada menu kopi apa aja?"
    //   extractKeyword() → "Kopi" (akan dipakai di menuDAO.getMenuByKategori("Kopi"))
    //
    // Daftar kata kunci rasa yang dikenali: manis, pahit, asam, gurih, creamy, fruity
    // Daftar kata kunci suhu: dingin, panas, iced, hot
    // Daftar kata kunci kategori: kopi, non-kopi, makanan, camilan
    //
    // Return: String kata kunci, atau null jika tidak ditemukan
    // public String extractKeyword() { ... }

}
