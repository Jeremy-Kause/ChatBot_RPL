package com.demikopi.sistemAdmin;

import com.demikopi.model.Admin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminSessionTest {

    @AfterEach
    void tearDown() {
        AdminSession.clear();
    }

    @Test
    void setAdmin_marksSessionAsLoggedIn() {
        Admin admin = new Admin("Nathan", "admin3", "Waraney Mambu");

        AdminSession.setAdmin(admin);

        assertTrue(AdminSession.isLoggedIn());
        assertEquals("Nathan", AdminSession.getUsername());
        assertEquals("Waraney Mambu", AdminSession.getNamaLengkap());
    }

    @Test
    void setAdmin_withNull_clearsSession() {
        AdminSession.setAdmin(new Admin("Justin", "admin2", "Justin William"));

        AdminSession.setAdmin(null);

        assertFalse(AdminSession.isLoggedIn());
        assertNull(AdminSession.getUsername());
        assertNull(AdminSession.getNamaLengkap());
    }

    @Test
    void clear_removesCurrentSessionData() {
        AdminSession.setAdmin(new Admin("Delvin", "admin4", "Delvin Laurens"));

        AdminSession.clear();

        assertFalse(AdminSession.isLoggedIn());
        assertNull(AdminSession.getUsername());
        assertNull(AdminSession.getNamaLengkap());
    }
}
