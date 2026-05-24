package com.demikopi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    @Test
    void constructorWithIdAndImagePath_populatesAllFields() {
        Menu menu = new Menu(
                10,
                "Kopi",
                "Cappuccino",
                "creamy, lembut",
                "panas",
                true,
                25000,
                "Espresso dengan foam susu",
                "asset/menu/cappuccino.jpg",
                true
        );

        assertEquals(10, menu.getIdMenu());
        assertEquals("Kopi", menu.getKategori());
        assertEquals("Cappuccino", menu.getNamaMenu());
        assertEquals("creamy, lembut", menu.getProfilRasa());
        assertEquals("panas", menu.getSuhuSajian());
        assertTrue(menu.isBestseller());
        assertEquals(25000, menu.getHarga());
        assertEquals("Espresso dengan foam susu", menu.getDeskripsiMenu());
        assertEquals("asset/menu/cappuccino.jpg", menu.getImagePath());
        assertTrue(menu.isStatusTersedia());
    }

    @Test
    void constructorWithoutId_usesDefaultIdAndKeepsOtherValues() {
        Menu menu = new Menu(
                "Non-Kopi",
                "Lemon Tea",
                "asam, segar",
                "dingin",
                false,
                18000,
                "Teh lemon segar",
                false
        );

        assertEquals(0, menu.getIdMenu());
        assertEquals("Non-Kopi", menu.getKategori());
        assertEquals("Lemon Tea", menu.getNamaMenu());
        assertEquals("asam, segar", menu.getProfilRasa());
        assertEquals("dingin", menu.getSuhuSajian());
        assertFalse(menu.isBestseller());
        assertEquals(18000, menu.getHarga());
        assertEquals("Teh lemon segar", menu.getDeskripsiMenu());
        assertNull(menu.getImagePath());
        assertFalse(menu.isStatusTersedia());
    }
}
