package com.demikopi;

import com.demikopi.dataAccess.AdminDAO;
import com.demikopi.model.Admin;
import com.demikopi.sistemUser.ChatEngine;

import java.util.Scanner;

public class DemiKopi {
    public static void main(String[] args) {
        System.out.println("\n==================================");
        System.out.println("=== TEST CHATBOT CHAT ENGINE   ===");
        System.out.println("==================================");

        ChatEngine chatEngine = new ChatEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Chatbot DEMIKOPI sudah siap dijalankan di Console!");
        System.out.println("Ketik 'exit' atau 'keluar' untuk mengakhiri program.\n");

        while (true) {
            System.out.print("Kamu:> ");
            String inputUser = scanner.nextLine();

            if (inputUser.equalsIgnoreCase("exit") || inputUser.equalsIgnoreCase("keluar")) {
                System.out.println("Bot:> Sampai jumpa!");
                break;
            }

            String responseBot = chatEngine.getResponse(inputUser);
            System.out.println("Bot:> \n" + responseBot + "\n");
        }

        scanner.close();
    }
}
