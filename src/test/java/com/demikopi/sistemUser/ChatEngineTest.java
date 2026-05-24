package com.demikopi.sistemUser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatEngineTest {

    private final ChatEngine chatEngine = new ChatEngine();

    @Test
    void getChatResponse_returnsGreetingBlocks_forGreetingInput() {
        ChatResponse response = chatEngine.getChatResponse("Halo");

        assertNotNull(response);
        assertTrue(response.hasBlocks());
        assertTrue(response.getText().contains("Selamat datang"));
    }

    @Test
    void getChatResponse_returnsMenuList_forMenuQuestion() {
        ChatResponse response = chatEngine.getChatResponse("Menu apa saja yang tersedia?");

        assertTrue(response.getText().contains("Berikut menu-menu kami"));
        assertTrue(response.getText().contains("Espresso"));
        assertTrue(response.hasBlocks());
    }

    @Test
    void getChatResponse_returnsMenuDetail_forDirectMenuMention() {
        ChatResponse response = chatEngine.getChatResponse("Aku mau tahu cappuccino");

        assertTrue(response.getText().contains("Detail Menu: Cappuccino"));
        assertTrue(response.getText().contains("Harga: Rp"));
    }

    @Test
    void getChatResponse_returnsLocationInfo_forLocationQuestion() {
        ChatResponse response = chatEngine.getChatResponse("Lokasi DemiKopi dimana?");

        assertTrue(response.getText().contains("Kami berlokasi di"));
        assertTrue(response.getText().contains("Jl. Bima"));
    }

    @Test
    void getChatResponse_returnsRecommendationImages_forBestsellerRequest() {
        ChatResponse response = chatEngine.getChatResponse("Tampilkan rekomendasi best seller");

        assertTrue(response.getText().contains("rekomendasi"));
        assertFalse(response.getImages().isEmpty());
        assertTrue(response.hasImage());
    }
}
