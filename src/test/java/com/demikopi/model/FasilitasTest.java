package com.demikopi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FasilitasTest {

    @Test
    void constructor_populatesAllFacilityFields() {
        Fasilitas fasilitas = new Fasilitas("4", "Area Kerja", "Meja panjang dengan stop kontak");

        assertEquals("4", fasilitas.getIdFasilitas());
        assertEquals("Area Kerja", fasilitas.getNamaFasilitas());
        assertEquals("Meja panjang dengan stop kontak", fasilitas.getDeskripsiFasilitas());
    }
}
