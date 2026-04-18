package com.demikopi.sistemUser;

// TODO: Tambahkan import berikut saat mulai implementasi:
// import com.demikopi.dataAccess.MenuDAO;
// import com.demikopi.dataAccess.InfoDAO;
// import com.demikopi.model.Menu;
// import com.demikopi.model.InfoKedai;
// import com.demikopi.model.Fasilitas;
// import com.demikopi.sistemUser.NLPService.Intent;
// import java.util.List;

import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.MenuDAO;

/**
 * ChatEngine — Otak Utama Chatbot DEMIKOPI
 *
 * Kelas ini adalah pusat koordinasi seluruh alur percakapan chatbot.
 * Tugasnya adalah menerima input teks dari user (melalui UI), lalu
 * mendelegasikan pemrosesan ke kelas-kelas yang tepat, dan mengembalikan
 * teks jawaban yang siap ditampilkan.
 *
 * ============================================================
 * ALUR KERJA UTAMA (Pipeline Chatbot):
 * ============================================================
 *
 *   [UI / ChatbotUI]
 *        │
 *        ▼
 *   ChatEngine.getResponse(inputUser)
 *        │
 *        ├─► NLPService.preprocess()     → bersihkan teks input
 *        ├─► NLPService.detectIntent()   → tentukan maksud user (Intent enum)
 *        ├─► NLPService.extractKeyword() → ambil kata kunci (rasa/kategori)
 *        │
 *        ▼
 *   [Switch berdasarkan Intent]
 *        │
 *        ├─► SALAM              → buildResponseSalam()
 *        ├─► TANYA_MENU         → MenuDAO.getAllMenu() → format teks
 *        ├─► TANYA_KATEGORI     → MenuDAO.getMenuByKategori() → format teks
 *        ├─► TANYA_REKOMENDASI  → RecomendationHandler.getRekomendasi()
 *        ├─► TANYA_DETAIL_MENU  → MenuDAO.getMenuByName() → format teks
 *        ├─► TANYA_JAM_BUKA     → InfoDAO.getJamBuka() → format teks
 *        ├─► TANYA_LOKASI       → InfoDAO.getLokasi() → format teks
 *        ├─► TANYA_FASILITAS    → InfoDAO.getFasilitas() → format teks
 *        └─► TIDAK_DIKENAL      → buildResponseFallback()
 *        │
 *        ▼
 *   return String (respons siap tampil)
 *        │
 *        ▼
 *   [UI / ChatbotUI menampilkan ke user]
 *
 * ============================================================
 * KELAS-KELAS YANG BERINTERAKSI DENGAN ChatEngine:
 * ============================================================
 *   - NLPService         : memproses & menganalisis input user
 *   - RecomendationHandler : mengelola logika rekomendasi menu
 *   - MenuDAO            : mengambil data menu dari database
 *   - InfoDAO            : mengambil data info kedai & fasilitas
 */
public class ChatEngine {

    // =========================================================
    // FIELD / ATRIBUT
    // =========================================================

    // TODO: Deklarasikan objek DAO sebagai field agar tidak dibuat ulang tiap request.
    //       Contoh:
    //       private MenuDAO menuDAO;
    //       private InfoDAO infoDAO;

    // TODO: Deklarasikan RecomendationHandler sebagai field.
    //       Contoh:
    //       private RecomendationHandler rekomendasi;
    private MenuDAO menuDAO = new MenuDAO();
    private InfoDAO infoDAO = new InfoDAO();


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /**
     * Constructor default — inisialisasi semua dependensi di sini.
     *
     * TODO: Inisialisasi semua field DAO dan handler di constructor ini.
     *       Contoh:
     *         this.menuDAO  = new MenuDAO();
     *         this.infoDAO  = new InfoDAO();
     *         this.rekomendasi = new RecomendationHandler();
     */
    public ChatEngine() {
        // TODO: Isi constructor ini
    }


    // =========================================================
    // METHOD UTAMA: getResponse()
    // =========================================================

    /**
     * Menerima input mentah dari user dan mengembalikan respons chatbot sebagai String.
     *
     * Ini adalah satu-satunya method yang akan dipanggil oleh UI (ChatbotUI).
     *
     * Langkah-langkah implementasi:
     *   1. Buat objek NLPService dengan inputUser.
     *   2. Panggil nlp.detectIntent() untuk mendapatkan Intent.
     *   3. Panggil nlp.extractKeyword() untuk mendapatkan kata kunci (jika ada).
     *   4. Gunakan switch/if-else berdasarkan Intent untuk memanggil method yang tepat.
     *   5. Kembalikan hasil String respons.
     *
     * @param inputUser teks yang diketik oleh user
     * @return String respons chatbot yang siap ditampilkan di UI
     *
     * TODO: Implementasikan method ini sesuai langkah di atas.
     */
    public String getResponse(String inputUser) {
        // TODO: Langkah 1 — Buat NLPService
        // NLPService nlp = new NLPService(inputUser);

        // TODO: Langkah 2 — Deteksi intent user
        // Intent intent = nlp.detectIntent();

        // TODO: Langkah 3 — Ekstrak kata kunci (dipakai untuk rekomendasi & kategori)
        // String keyword = nlp.extractKeyword();

        // TODO: Langkah 4 — Routing berdasarkan intent
        // switch (intent) {
        //     case SALAM:
        //         return buildResponseSalam();
        //     case TANYA_MENU:
        //         return buildResponseSemuaMenu();
        //     case TANYA_KATEGORI:
        //         return buildResponseMenuKategori(keyword);
        //     case TANYA_REKOMENDASI:
        //         return rekomendasi.getRekomendasi(keyword);
        //     case TANYA_DETAIL_MENU:
        //         return buildResponseDetailMenu(keyword);
        //     case TANYA_JAM_BUKA:
        //         return buildResponseJamBuka();
        //     case TANYA_LOKASI:
        //         return buildResponseLokasi();
        //     case TANYA_FASILITAS:
        //         return buildResponseFasilitas();
        //     default:
        //         return buildResponseFallback();
        // }

        return ""; // TODO: Hapus baris ini setelah implementasi selesai
    }


    // =========================================================
    // METHOD BUILDER — Menyusun teks respons untuk tiap intent
    // =========================================================
    // Setiap method di bawah bertugas mengambil data dari DAO,
    // lalu memformatnya menjadi kalimat yang ramah dan mudah dibaca.
    // Gunakan StringBuilder untuk merangkai teks panjang.

    /**
     * Membangun respons untuk sapaan user (Intent: SALAM).
     * Tidak memerlukan akses database.
     *
     * Contoh output:
     *   "Halo! Selamat datang di DEMIKOPI ☕
     *    Ada yang bisa aku bantu? Kamu bisa tanya soal menu, rekomendasi,
     *    jam buka, lokasi, atau fasilitas kami!"
     *
     * TODO: Implementasikan method ini dengan teks sambutan yang hangat dan informatif.
     */
    private String buildResponseSalam() {
        // TODO: Return String sapaan selamat datang
        return "";
    }

    /**
     * Membangun respons daftar SEMUA menu yang tersedia (Intent: TANYA_MENU).
     * Ambil data dari menuDAO.getAllMenu(), lalu kelompokkan per kategori.
     *
     * Contoh output:
     *   "Berikut menu-menu kami:\n
     *    ☕ KOPI\n
     *    - Americano — Rp 18.000\n
     *    - Latte — Rp 22.000\n
     *    🍵 NON-KOPI\n
     *    - Matcha Latte — Rp 25.000\n ..."
     *
     * TODO: Implementasikan pengambilan data dan format output per kategori.
     */
    private String buildResponseSemuaMenu() {
        // TODO: Panggil menuDAO.getAllMenu()
        // TODO: Iterasi List<Menu>, kelompokkan berdasarkan menu.getKategori()
        // TODO: Format tiap item: "- {namaMenu} — Rp {harga}\n"
        // TODO: Kembalikan sebagai String
        return "";
    }

    /**
     * Membangun respons daftar menu berdasarkan kategori tertentu (Intent: TANYA_KATEGORI).
     * Contoh: user bertanya "ada kopi apa aja?" → keyword = "Kopi"
     *
     * @param kategori kata kunci kategori yang diekstrak NLPService (mis: "Kopi", "Makanan")
     *
     * TODO: Panggil menuDAO.getMenuByKategori(kategori) lalu format hasilnya.
     *       Tangani kasus jika list kosong (kategori tidak ditemukan).
     */
    private String buildResponseMenuKategori(String kategori) {
        // TODO: Panggil menuDAO.getMenuByKategori(kategori)
        // TODO: Jika list kosong → return "Maaf, kategori tersebut tidak tersedia."
        // TODO: Jika ada isinya → format dan kembalikan sebagai daftar
        return "";
    }

    /**
     * Membangun respons detail satu menu berdasarkan nama (Intent: TANYA_DETAIL_MENU).
     * Contoh: user mengetik "ceritain dong soal Latte"
     *
     * @param namaMenu nama menu spesifik yang diekstrak NLPService
     *
     * TODO: Panggil menuDAO.getMenuByName(namaMenu).
     *       Jika null → kembalikan pesan "menu tidak ditemukan".
     *       Jika ada → tampilkan nama, kategori, rasa, suhu, harga, dan deskripsi.
     */
    private String buildResponseDetailMenu(String namaMenu) {
        // TODO: Panggil menuDAO.getMenuByName(namaMenu)
        // TODO: Jika null → return "Maaf, menu tersebut tidak kami temukan."
        // TODO: Format: nama, kategori, profil rasa, suhu sajian, harga, deskripsi
        return "";
    }

    /**
     * Membangun respons jam operasional kedai (Intent: TANYA_JAM_BUKA).
     *
     * TODO: Panggil InfoDAO untuk mengambil data jam operasional dari database.
     *       InfoDAO belum diimplementasi — ini harus diprioritaskan bersama method ini.
     *       Format output: "DEMIKOPI buka setiap hari pukul {jam}."
     */
    private String buildResponseJamBuka() {
        // TODO: Panggil infoDAO.getJamBuka() — method ini belum ada, buat di InfoDAO dulu
        // TODO: Format dan kembalikan sebagai String
        return "";
    }

    /**
     * Membangun respons lokasi/alamat kedai (Intent: TANYA_LOKASI).
     *
     * TODO: Panggil InfoDAO untuk mengambil data lokasi dari database.
     *       Format output: "Kami berlokasi di {alamat}. Klik link ini untuk peta: ..."
     */
    private String buildResponseLokasi() {
        // TODO: Panggil infoDAO.getLokasi() — method ini belum ada, buat di InfoDAO dulu
        // TODO: Format dan kembalikan sebagai String
        return "";
    }

    /**
     * Membangun respons daftar fasilitas kedai (Intent: TANYA_FASILITAS).
     *
     * TODO: Panggil InfoDAO untuk mengambil List<Fasilitas> dari database.
     *       Format output: daftar fasilitas dengan nama dan deskripsasinya.
     *       Contoh: "✅ WiFi Gratis — Tersedia di seluruh area kedai."
     */
    private String buildResponseFasilitas() {
        // TODO: Panggil infoDAO.getAllFasilitas() — method ini belum ada, buat di InfoDAO dulu
        // TODO: Iterasi List<Fasilitas>, format tiap item
        // TODO: Kembalikan sebagai String
        return "";
    }

    /**
     * Membangun respons fallback — digunakan saat intent tidak dikenali (TIDAK_DIKENAL).
     *
     * TODO: Kembalikan pesan yang sopan dan mengarahkan user.
     *       Contoh: "Maaf, aku belum mengerti maksudmu 😅
     *                Coba tanya soal: menu, rekomendasi, jam buka, lokasi, atau fasilitas."
     */
    private String buildResponseFallback() {
        // TODO: Return String pesan fallback yang informatif
        return "";
    }
}
