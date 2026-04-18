package com.demikopi.sistemUser;

// TODO: Tambahkan import berikut saat mulai implementasi:
// import com.demikopi.dataAccess.MenuDAO;
// import com.demikopi.model.Menu;
// import java.util.List;

/**
 * RecomendationHandler — Handler Khusus Rekomendasi Menu
 *
 * Kelas ini bertanggung jawab menangani satu intent spesifik:
 * TANYA_REKOMENDASI — yaitu saat user meminta saran menu dari chatbot.
 *
 * Alasan dipisahkan dari ChatEngine:
 *   - Logika rekomendasi cukup kompleks (ada beberapa skenario)
 *   - Memudahkan pengembangan dan testing secara mandiri
 *   - Prinsip Single Responsibility — satu kelas, satu tanggung jawab
 *
 * ============================================================
 * SKENARIO REKOMENDASI YANG HARUS DITANGANI:
 * ============================================================
 *
 *   Skenario 1 — Berdasarkan Profil Rasa
 *     Contoh: "aku mau yang manis" / "ada yang pahit?"
 *     Keyword: manis, pahit, asam, gurih, creamy, fruity
 *     → Panggil menuDAO.getMenuByKriteria(keyword)
 *     → Tampilkan daftar menu yang cocok
 *
 *   Skenario 2 — Berdasarkan Suhu Sajian
 *     Contoh: "aku mau yang dingin" / "ada yang panas?"
 *     Keyword: panas, dingin, iced, hot
 *     → Filter dari menuDAO.getAllMenu() berdasarkan suhuSajian
 *     → Tampilkan daftar menu yang cocok
 *
 *   Skenario 3 — Bestseller / Tanpa Preferensi Spesifik
 *     Contoh: "rekomendasiin dong" / "menu terpopuler apa?"
 *     → Panggil menuDAO.getMBestSeller()  ← PERHATIAN: method ini belum selesai!
 *     → Tampilkan daftar menu bestseller
 *
 * ============================================================
 * ALUR KERJA INTERNAL:
 * ============================================================
 *
 *   ChatEngine memanggil:
 *   rekomendasi.getRekomendasi(keyword)
 *        │
 *        ├─► Jika keyword = rasa (manis/pahit/dll)   → getRekomendasiByRasa(keyword)
 *        ├─► Jika keyword = suhu (dingin/panas/dll)  → getRekomendasiBySuhu(keyword)
 *        └─► Jika keyword = null / tidak dikenal     → getRekomendasiBestseller()
 *        │
 *        ▼
 *   return String (daftar rekomendasi yang diformat)
 */
public class RecomendationHandler {

    // =========================================================
    // FIELD / ATRIBUT
    // =========================================================

    // TODO: Deklarasikan MenuDAO sebagai field untuk mengakses database.
    //       Contoh:
    //       private MenuDAO menuDAO;

    // Daftar kata kunci rasa yang dikenali oleh sistem rekomendasi
    // TODO: Gunakan array atau List ini saat melakukan pengecekan keyword di getRekomendasi()
    //       private static final String[] KATA_KUNCI_RASA = {"manis", "pahit", "asam", "gurih", "creamy", "fruity"};

    // Daftar kata kunci suhu yang dikenali oleh sistem rekomendasi
    //       private static final String[] KATA_KUNCI_SUHU = {"panas", "dingin", "iced", "hot"};


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /**
     * TODO: Inisialisasi MenuDAO di sini.
     *       Contoh: this.menuDAO = new MenuDAO();
     */
    public RecomendationHandler() {
        // TODO: Isi constructor ini
    }


    // =========================================================
    // METHOD UTAMA: getRekomendasi()
    // =========================================================

    /**
     * Entry point rekomendasi — dipanggil oleh ChatEngine.
     *
     * Method ini menentukan SKENARIO mana yang relevan berdasarkan keyword
     * yang diekstrak oleh NLPService, lalu mendelegasikan ke method yang tepat.
     *
     * @param keyword kata kunci dari NLPService (bisa null jika tidak ada preferensi)
     * @return String berisi daftar rekomendasi yang sudah diformat
     *
     * TODO: Implementasikan logika routing ini:
     *   if (keyword ada di KATA_KUNCI_RASA) → return getRekomendasiByRasa(keyword)
     *   if (keyword ada di KATA_KUNCI_SUHU) → return getRekomendasiBySuhu(keyword)
     *   else                                → return getRekomendasiBestseller()
     */
    public String getRekomendasi(String keyword) {
        // TODO: Cek apakah keyword termasuk kata kunci rasa
        // TODO: Cek apakah keyword termasuk kata kunci suhu
        // TODO: Jika tidak keduanya (keyword null atau tidak dikenal) → tampilkan bestseller

        return ""; // TODO: Hapus baris ini setelah implementasi selesai
    }


    // =========================================================
    // METHOD HELPER — Masing-masing menangani satu skenario
    // =========================================================

    /**
     * Skenario 1 — Rekomendasi berdasarkan profil rasa.
     * Dipanggil saat keyword adalah: manis, pahit, asam, gurih, creamy, fruity.
     *
     * @param rasa kata kunci rasa (contoh: "manis", "pahit")
     * @return String daftar menu yang cocok dengan rasa tersebut
     *
     * TODO: Panggil menuDAO.getMenuByKriteria(rasa).
     *       Jika list kosong → return "Maaf, tidak ada menu dengan rasa tersebut saat ini."
     *       Jika ada → format hasilnya seperti:
     *         "Rekomendasi menu dengan rasa {rasa}:\n
     *          - {namaMenu} — Rp {harga}\n  {deskripsi}\n"
     */
    private String getRekomendasiByRasa(String rasa) {
        // TODO: Panggil menuDAO.getMenuByKriteria(rasa)
        // TODO: Tangani list kosong
        // TODO: Format output dan kembalikan sebagai String
        return "";
    }

    /**
     * Skenario 2 — Rekomendasi berdasarkan suhu sajian.
     * Dipanggil saat keyword adalah: panas, dingin, iced, hot.
     *
     * Normalisasi keyword:
     *   "iced"  → gunakan "Dingin" saat query ke DB
     *   "hot"   → gunakan "Panas"  saat query ke DB
     *
     * @param suhu kata kunci suhu (contoh: "dingin", "panas")
     * @return String daftar menu yang sesuai suhu tersebut
     *
     * TODO: Ambil semua menu dari menuDAO.getAllMenu().
     *       Filter berdasarkan menu.getSuhuSajian() yang mengandung keyword suhu.
     *       Jika hasil filter kosong → return pesan "tidak ada menu dengan suhu tersebut".
     *       Jika ada → format dan kembalikan hasilnya.
     */
    private String getRekomendasiBySuhu(String suhu) {
        // TODO: Normalisasi keyword suhu ("iced" → "Dingin", "hot" → "Panas")
        // TODO: Panggil menuDAO.getAllMenu()
        // TODO: Filter dengan stream atau loop: menu.getSuhuSajian().contains(suhu)
        // TODO: Tangani hasil kosong
        // TODO: Format output dan kembalikan sebagai String
        return "";
    }

    /**
     * Skenario 3 — Rekomendasi menu bestseller (tanpa preferensi spesifik).
     * Dipanggil saat user hanya berkata "rekomendasiin dong" tanpa detail.
     *
     * ⚠️ PERHATIAN: menuDAO.getMBestSeller() BELUM SELESAI DIIMPLEMENTASI.
     *    Sebelum method ini bisa berjalan, kamu harus:
     *    1. Perbaiki query di MenuDAO.getMBestSeller() → tambahkan kondisi WHERE is_bestseller = true
     *    2. Pastikan result set di-map menjadi objek Menu yang benar
     *    3. Kembalikan List<Menu> yang terisi, bukan list kosong
     *
     * @return String daftar menu bestseller yang diformat
     *
     * TODO (setelah MenuDAO.getMBestSeller() diperbaiki):
     *   Panggil menuDAO.getMBestSeller().
     *   Format hasilnya menjadi daftar rekomendasi yang menarik.
     *   Tambahkan label "⭐ BESTSELLER" atau sejenisnya di header.
     */
    private String getRekomendasiBestseller() {
        // TODO: [PRIORITAS TINGGI] Perbaiki MenuDAO.getMBestSeller() terlebih dahulu!
        //       Query saat ini tidak lengkap (WHERE clause hilang + result set tidak di-map).

        // TODO: Setelah MenuDAO diperbaiki, panggil menuDAO.getMBestSeller()
        // TODO: Format output seperti:
        //         "⭐ Menu Bestseller DEMIKOPI:\n
        //          - {namaMenu} ({kategori}) — Rp {harga}\n  {deskripsi}\n"
        // TODO: Tangani kasus list kosong
        return "";
    }
}

