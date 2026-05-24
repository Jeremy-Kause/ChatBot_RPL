package com.demikopi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KategoriTest {

    @Test
    void constructor_populatesCategoryFields() {
        Kategori kategori = new Kategori(3, "Mix");

        assertEquals(3, kategori.getIdKategori());
        assertEquals("Mix", kategori.getNamaKategori());
    }
}
