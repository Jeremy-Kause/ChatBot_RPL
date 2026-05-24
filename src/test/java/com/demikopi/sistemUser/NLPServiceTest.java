package com.demikopi.sistemUser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NLPServiceTest {

    @Test
    void detectIntent_returnsSalam_forGreetingInput() {
        NLPService service = new NLPService("Halo, selamat pagi");

        assertEquals(NLPService.Intent.SALAM, service.detectIntent());
    }

    @Test
    void detectIntent_returnsJamBuka_forOperationalQuestion() {
        NLPService service = new NLPService("Jam buka kedai hari ini?");

        assertEquals(NLPService.Intent.TANYA_JAM_BUKA, service.detectIntent());
    }

    @Test
    void detectIntent_returnsKategori_forCategoryQuestion() {
        NLPService service = new NLPService("Ada menu non kopi apa saja?");

        assertEquals(NLPService.Intent.TANYA_KATEGORI, service.detectIntent());
        assertEquals("non-kopi", service.extractKeyword(NLPService.Intent.TANYA_KATEGORI));
    }

    @Test
    void detectIntent_returnsDetailMenu_andExtractsMenuName() {
        NLPService service = new NLPService("Info menu cappuccino dong");

        assertEquals(NLPService.Intent.TANYA_DETAIL_MENU, service.detectIntent());
        assertEquals("cappuccino", service.extractKeyword(NLPService.Intent.TANYA_DETAIL_MENU));
    }

    @Test
    void detectIntent_returnsRecommendation_forTasteAndGoalQuery() {
        NLPService service = new NLPService("Aku mau kopi yang manis buat belajar");

        assertEquals(NLPService.Intent.TANYA_REKOMENDASI, service.detectIntent());
    }

    @Test
    void extractRecommendationQuery_readsCategoryTasteTemperatureAndGoal() {
        NLPService service = new NLPService("Rekomendasi kopi manis dingin buat belajar");

        NLPService.RecommendationQuery query = service.extractRecommendationQuery();

        assertEquals("kopi", query.getKategori());
        assertEquals(List.of("manis"), query.getRasa());
        assertEquals("dingin", query.getSuhuSajian());
        assertEquals(List.of("belajar"), query.getTujuanPengguna());
        assertFalse(query.isBestseller());
    }

    @Test
    void extractRecommendationQuery_mapsWeatherContextToServingTemperature() {
        NLPService service = new NLPService("Cuaca hujan enaknya minum apa?");

        NLPService.RecommendationQuery query = service.extractRecommendationQuery();

        assertEquals("dingin", query.getKonteksCuaca());
        assertEquals("panas", query.getSuhuSajian());
    }

    @Test
    void extractRecommendationQuery_marksBestsellerRequest() {
        NLPService service = new NLPService("Tolong rekomendasi best seller dong");

        NLPService.RecommendationQuery query = service.extractRecommendationQuery();

        assertTrue(query.isBestseller());
        assertNull(query.getKategori());
        assertTrue(query.getRasa().isEmpty());
    }
}
