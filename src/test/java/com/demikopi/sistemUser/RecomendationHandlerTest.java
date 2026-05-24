package com.demikopi.sistemUser;

import com.demikopi.model.Menu;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecomendationHandlerTest {

    private final RecomendationHandler handler = new RecomendationHandler();

    @Test
    void getMenuRekomendasi_prioritizesBestseller_whenQueryHasNoPreference() {
        List<Menu> menus = List.of(
                menu("Kopi Susu Gula Aren", "Kopi", "manis, caramel, creamy", "dingin", true, 23000),
                menu("Espresso", "Kopi", "pahit, bold", "panas", false, 18000),
                menu("Croissant Butter", "Makanan", "gurih, buttery", "suhu ruang", true, 20000)
        );

        List<Menu> hasil = handler.getMenuRekomendasi(NLPService.RecommendationQuery.empty(), menus, 10);

        assertEquals(2, hasil.size());
        assertTrue(hasil.stream().allMatch(Menu::isBestseller));
        assertEquals("Croissant Butter", hasil.get(0).getNamaMenu());
        assertEquals("Kopi Susu Gula Aren", hasil.get(1).getNamaMenu());
    }

    @Test
    void getMenuRekomendasi_filtersByCategoryTasteAndTemperature() {
        List<Menu> menus = List.of(
                menu("Kopi Susu Gula Aren", "Kopi", "manis, caramel, creamy", "dingin", true, 23000),
                menu("Americano", "Kopi", "pahit, clean", "panas", false, 20000),
                menu("Milo Dinosaur", "Non-Kopi", "manis, cokelat", "dingin", true, 22000)
        );

        NLPService.RecommendationQuery query = new NLPService.RecommendationQuery(
                "kopi",
                List.of("manis"),
                "dingin",
                null,
                List.of(),
                false,
                "kopi manis dingin"
        );

        List<Menu> hasil = handler.getMenuRekomendasi(query, menus, 10);

        assertEquals(1, hasil.size());
        assertEquals("Kopi Susu Gula Aren", hasil.get(0).getNamaMenu());
    }

    @Test
    void getAlasanRekomendasi_includesMatchedSignals() {
        Menu menu = menu("Cappuccino", "Kopi", "creamy, sedikit pahit, lembut", "panas", true, 25000);
        NLPService.RecommendationQuery query = new NLPService.RecommendationQuery(
                "kopi",
                List.of("creamy"),
                "panas",
                "dingin",
                List.of("belajar"),
                true,
                "rekomendasi kopi creamy panas buat belajar saat hujan"
        );

        String alasan = handler.getAlasanRekomendasi(query, menu);

        assertTrue(alasan.contains("masuk pilihan berbasis kopi"));
        assertTrue(alasan.contains("punya profil rasa creamy"));
        assertTrue(alasan.contains("cocok untuk cuaca dingin"));
        assertTrue(alasan.contains("belajar"));
        assertTrue(alasan.contains("best seller"));
    }

    @Test
    void getRekomendasi_buildsReadableTitle_forPreferenceQuery() {
        List<Menu> menus = List.of(
                menu("Lemon Tea", "Non-Kopi", "asam, segar, manis ringan", "dingin", false, 18000),
                menu("Matcha Latte", "Non-Kopi", "creamy, sedikit pahit", "panas/dingin", true, 27000)
        );

        NLPService.RecommendationQuery query = new NLPService.RecommendationQuery(
                "non-kopi",
                List.of("segar"),
                "dingin",
                "panas",
                List.of("santai"),
                false,
                "non kopi segar dingin untuk santai"
        );

        String hasil = handler.getRekomendasi(query, menus, 5);

        assertTrue(hasil.startsWith("Ini 1 rekomendasi non-kopi"));
        assertTrue(hasil.contains("Lemon Tea"));
    }

    private Menu menu(String nama, String kategori, String rasa, String suhu, boolean bestseller, int harga) {
        return new Menu(1, kategori, nama, rasa, suhu, bestseller, harga, nama + " description", true);
    }
}
