package com.demikopi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfoKedaiTest {

    @Test
    void constructor_populatesAllStoreInfoFields() {
        InfoKedai info = new InfoKedai(
                "1",
                "Senin - Jumat: 08.00 - 22.00",
                "Jl. Bima No. 17",
                "0812-3456-7890"
        );

        assertEquals("1", info.getIdInfo());
        assertEquals("Senin - Jumat: 08.00 - 22.00", info.getJamOperasional());
        assertEquals("Jl. Bima No. 17", info.getLokasi());
        assertEquals("0812-3456-7890", info.getKontak());
    }
}
