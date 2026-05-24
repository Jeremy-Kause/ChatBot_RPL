package com.demikopi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest {

    @Test
    void constructor_populatesAllAdminFields() {
        Admin admin = new Admin("Jeremy", "admin1", "Jeremy Kause");

        assertEquals("Jeremy", admin.getUsername());
        assertEquals("admin1", admin.getPassword());
        assertEquals("Jeremy Kause", admin.getNamaLengkap());
    }
}
