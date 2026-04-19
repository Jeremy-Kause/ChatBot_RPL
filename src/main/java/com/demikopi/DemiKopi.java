package com.demikopi;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.model.Admin;
import com.demikopi.sistemUser.ChatEngine;

import java.util.Scanner;

public class DemiKopi {
    public static void main(String[] args) {
        System.out.println("=== TEST ADMIN AUTH ===");
        // Koneksi dibuat otomatis saat AdminDAO pertama kali digunakan
        AdminDAO adao = new AdminDAO();
        Admin admin = adao.getAdmin("Delvin");
        if (admin != null) {
            System.out.println("Berhasil login! Admin: " + admin.getUsername() + " | Nama Lengkap: " + admin.getNamaLengkap());
        } else {
            System.out.println("Admin tidak ditemukan.");
        }

        System.out.println("\n==================================");
        System.out.println("=== TEST CHATBOT CHAT ENGINE   ===");
        System.out.println("==================================");
        
        // Inisialisasi mesin chatbot
        ChatEngine chatEngine = new ChatEngine();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Chatbot DEMIKOPI sudah siap dijalankan di Console!");
        System.out.println("Ketik 'exit' atau 'keluar' untuk mengakhiri program.\n");
        
        // Looping tak terbatas untuk tes percakapan
        while (true) {
            System.out.print("Kamu:> ");
            String inputUser = scanner.nextLine();
            
            if (inputUser.equalsIgnoreCase("exit") || inputUser.equalsIgnoreCase("keluar")) {
                System.out.println("Bot:> Sampai jumpa!");
                break;
            }
            
            // Mengirim input ke ChatEngine dan mencetak respons bot
            String responseBot = chatEngine.getResponse(inputUser);
            System.out.println("Bot:> \n" + responseBot + "\n");
        }
        
        scanner.close();
    }
}
