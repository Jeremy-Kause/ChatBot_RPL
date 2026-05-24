package com.demikopi.sistemUser;

import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserFallbackDataTest {

    @Test
    void getMenu_returnsNonEmptyFallbackMenuList() {
        List<Menu> menus = UserFallbackData.getMenu();

        assertEquals(26, menus.size());
        assertEquals("Espresso", menus.get(0).getNamaMenu());
        assertTrue(menus.stream().anyMatch(Menu::isBestseller));
        assertTrue(menus.stream().allMatch(Menu::isStatusTersedia));
    }

    @Test
    void getInfo_returnsConfiguredFallbackStoreInfo() {
        InfoKedai info = UserFallbackData.getInfo();

        assertNotNull(info);
        assertEquals("1", info.getIdInfo());
        assertTrue(info.getJamOperasional().contains("Senin - Jumat"));
        assertTrue(info.getLokasi().contains("Jl. Bima"));
        assertEquals("0812-3456-7890", info.getKontak());
    }

    @Test
    void getFasilitas_returnsKnownFallbackFacilities() {
        List<Fasilitas> fasilitas = UserFallbackData.getFasilitas();

        assertEquals(7, fasilitas.size());
        assertEquals("WiFi Gratis", fasilitas.get(0).getNamaFasilitas());
        assertTrue(fasilitas.stream().anyMatch(item -> item.getNamaFasilitas().equals("Parkir")));
        assertFalse(fasilitas.stream().anyMatch(item -> item.getNamaFasilitas().isBlank()));
    }
}
