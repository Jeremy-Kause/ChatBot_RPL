package com.demikopi.sistemUser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatResponseTest {

    @Test
    void text_createsPlainResponseWithoutBlocksOrImages() {
        ChatResponse response = ChatResponse.text("Halo");

        assertEquals("Halo", response.getText());
        assertTrue(response.getBlocks().isEmpty());
        assertTrue(response.getImages().isEmpty());
        assertFalse(response.hasBlocks());
        assertFalse(response.hasImage());
        assertNull(response.getImagePath());
    }

    @Test
    void formatted_keepsSuppliedBlocks() {
        List<ChatResponse.ChatBlock> blocks = List.of(
                ChatResponse.ChatBlock.title("Judul"),
                ChatResponse.ChatBlock.paragraph("Isi paragraf")
        );

        ChatResponse response = ChatResponse.formatted("Konten", blocks);

        assertEquals("Konten", response.getText());
        assertEquals(2, response.getBlocks().size());
        assertTrue(response.hasBlocks());
        assertEquals(ChatResponse.BlockType.TITLE, response.getBlocks().get(0).getType());
        assertEquals("Judul", response.getBlocks().get(0).getTitle());
        assertEquals(ChatResponse.BlockType.PARAGRAPH, response.getBlocks().get(1).getType());
    }

    @Test
    void withImage_setsSingleImageAndMarksResponseAsHavingImage() {
        ChatResponse response = ChatResponse.withImage("Detail menu", "asset/menu/espresso.jpg");

        assertEquals("Detail menu", response.getText());
        assertEquals(1, response.getImages().size());
        assertTrue(response.hasImage());
        assertEquals("asset/menu/espresso.jpg", response.getImagePath());
        assertEquals("", response.getImages().get(0).getTitle());
        assertEquals("", response.getImages().get(0).getSubtitle());
    }

    @Test
    void withImages_ignoresBlankImagePath_whenCheckingHasImage() {
        List<ChatResponse.ChatImage> images = List.of(
                new ChatResponse.ChatImage("Espresso", "Kopi", "asset/menu/espresso.jpg"),
                new ChatResponse.ChatImage("Rusak", "Tanpa path", " ")
        );

        ChatResponse response = ChatResponse.withImages("Rekomendasi", images);

        assertTrue(response.hasImage());
        assertEquals(2, response.getImages().size());
        assertEquals("asset/menu/espresso.jpg", response.getImagePath());
        assertNull(response.getImages().get(1).getImagePath());
    }

    @Test
    void withImageAndBlocks_keepsBothPresentationChannels() {
        List<ChatResponse.ChatBlock> blocks = List.of(
                ChatResponse.ChatBlock.detailRow("Harga", "Rp 25.000"),
                ChatResponse.ChatBlock.note("Best seller")
        );

        ChatResponse response = ChatResponse.withImage("Cappuccino", "asset/menu/cappuccino.jpg", blocks);

        assertEquals("Cappuccino", response.getText());
        assertTrue(response.hasBlocks());
        assertTrue(response.hasImage());
        assertEquals(2, response.getBlocks().size());
        assertEquals(ChatResponse.BlockType.DETAIL_ROW, response.getBlocks().get(0).getType());
        assertEquals("Harga", response.getBlocks().get(0).getTitle());
        assertEquals("Rp 25.000", response.getBlocks().get(0).getValue());
    }
}
