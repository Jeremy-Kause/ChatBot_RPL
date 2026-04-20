package com.demikopi;

import com.demikopi.sistemAdmin.AdminAuth;
import com.demikopi.sistemAdmin.AdminController;
import com.demikopi.model.Menu;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;

import java.util.List;
import java.util.Scanner;

public class AdminTestFitur {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==================================");
        System.out.println("===     TEST ADMIN (CRUD)      ===");
        System.out.println("==================================");

        testAdmin(scanner);

        scanner.close();
    }

    private static void testAdmin(Scanner scanner) {
        System.out.println("\n--- Login Admin ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        AdminAuth adminAuth = new AdminAuth();
        if (!adminAuth.login(username, password)) {
            System.out.println("Login gagal. Username atau password salah.");
            return;
        }

        System.out.println("Login berhasil!\n");
        AdminController adminController = new AdminController();

        while (true) {
            System.out.println("==================================");
            System.out.println("===       DASHBOARD ADMIN      ===");
            System.out.println("==================================");
            System.out.println("1. Menu");
            System.out.println("2. Info");
            System.out.println("3. Fasilitas");
            System.out.println("4. Exit");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("1")) {
                menuSistemAdminMenu(scanner, adminController);
            } else if (pilihan.equals("2")) {
                menuSistemAdminInfo(scanner, adminController);
            } else if (pilihan.equals("3")) {
                menuSistemAdminFasilitas(scanner, adminController);
            } else if (pilihan.equals("4")) {
                System.out.println("Keluar dari dashboard admin.");
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void menuSistemAdminMenu(Scanner scanner, AdminController controller) {
        while (true) {
            System.out.println("\n--- Manajemen Menu ---");
            System.out.println("1. Lihat menu");
            System.out.println("2. Tambah menu");
            System.out.println("3. Update menu");
            System.out.println("4. Hapus menu");
            System.out.println("5. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("1")) {
                List<Menu> menuList = controller.getAllMenu();
                System.out.println("\nDaftar Menu:");
                for (Menu m : menuList) {
                    System.out.println("- [" + m.getIdMenu() + "] " + m.getNamaMenu() + " (Rp" + m.getHarga() + ")");
                }
            } else if (pilihan.equals("2")) {
                try {
                    System.out.print("Kategori (Kopi/Non-Kopi/Cemilan/dll): ");
                    String kategori = scanner.nextLine();
                    System.out.print("Nama Menu: ");
                    String namaMenu = scanner.nextLine();
                    System.out.print("Profil Rasa: ");
                    String profilRasa = scanner.nextLine();
                    System.out.print("Suhu Sajian (Panas/Dingin): ");
                    String suhuSajian = scanner.nextLine();
                    System.out.print("Bestseller (true/false): ");
                    boolean isBestseller = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Harga: ");
                    int harga = Integer.parseInt(scanner.nextLine());
                    System.out.print("Deskripsi: ");
                    String deskripsi = scanner.nextLine();
                    System.out.print("Tersedia (true/false): ");
                    boolean statusTersedia = Boolean.parseBoolean(scanner.nextLine());

                    Menu menuBaru = new Menu(kategori, namaMenu, profilRasa, suhuSajian, isBestseller, harga, deskripsi,
                            statusTersedia);
                    if (controller.tambahMenu(menuBaru)) {
                        System.out.println("Menu berhasil ditambahkan.");
                    } else {
                        System.out.println("Gagal menambahkan menu. Periksa inputan atau koneksi database.");
                    }
                } catch (Exception e) {
                    System.out.println("Input tidak valid! Pastikan tipe data benar (misal harga harus angka).");
                }
            } else if (pilihan.equals("3")) {
                try {
                    System.out.print("ID Menu yang ingin diupdate: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Kategori Baru: ");
                    String kategori = scanner.nextLine();
                    System.out.print("Nama Menu Baru: ");
                    String namaMenu = scanner.nextLine();
                    System.out.print("Profil Rasa Baru: ");
                    String profilRasa = scanner.nextLine();
                    System.out.print("Suhu Sajian Baru (Panas/Dingin): ");
                    String suhuSajian = scanner.nextLine();
                    System.out.print("Bestseller Baru (true/false): ");
                    boolean isBestseller = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Harga Baru: ");
                    int harga = Integer.parseInt(scanner.nextLine());
                    System.out.print("Deskripsi Baru: ");
                    String deskripsi = scanner.nextLine();
                    System.out.print("Tersedia Baru (true/false): ");
                    boolean statusTersedia = Boolean.parseBoolean(scanner.nextLine());

                    Menu menuUpdate = new Menu(id, kategori, namaMenu, profilRasa, suhuSajian, isBestseller, harga,
                            deskripsi, statusTersedia);
                    if (controller.updateMenu(menuUpdate)) {
                        System.out.println("Menu berhasil diupdate.");
                    } else {
                        System.out.println("Gagal mengupdate menu.");
                    }
                } catch (Exception e) {
                    System.out.println("Input tidak valid!");
                }
            } else if (pilihan.equals("4")) {
                try {
                    System.out.print("ID Menu yang ingin dihapus: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Menu menuHapus = new Menu(id, "", "", "", "", false, 0, "", false);
                    if (controller.hapusMenu(menuHapus)) {
                        System.out.println("Menu berhasil dihapus.");
                    } else {
                        System.out.println("Gagal menghapus menu.");
                    }
                } catch (Exception e) {
                    System.out.println("Input tidak valid!");
                }
            } else if (pilihan.equals("5")) {
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void menuSistemAdminInfo(Scanner scanner, AdminController controller) {
        while (true) {
            System.out.println("\n--- Manajemen Info ---");
            System.out.println("1. Lihat Info");
            System.out.println("2. Update Info");
            System.out.println("3. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("1")) {
                InfoKedai info = controller.getInfoKedai();
                if (info != null) {
                    System.out.println("\nInfo Kedai:");
                    System.out.println("ID               : " + info.getIdInfo());
                    System.out.println("Jam Operasional  : " + info.getJamOperasional());
                    System.out.println("Lokasi           : " + info.getLokasi());
                    System.out.println("Kontak           : " + info.getKontak());
                } else {
                    System.out.println("Gagal mengambil info kedai.");
                }
            } else if (pilihan.equals("2")) {
                System.out.print("ID Info (misal 'INF1'): ");
                String id = scanner.nextLine();
                System.out.print("Jam Operasional Baru: ");
                String jam = scanner.nextLine();
                System.out.print("Lokasi Baru: ");
                String lokasi = scanner.nextLine();
                System.out.print("Kontak Baru: ");
                String kontak = scanner.nextLine();

                InfoKedai infoUpdate = new InfoKedai(id, jam, lokasi, kontak);
                if (controller.updateInfoKedai(infoUpdate)) {
                    System.out.println("Info berhasil diupdate.");
                } else {
                    System.out.println("Gagal mengupdate info.");
                }
            } else if (pilihan.equals("3")) {
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void menuSistemAdminFasilitas(Scanner scanner, AdminController controller) {
        while (true) {
            System.out.println("\n--- Manajemen Fasilitas ---");
            System.out.println("1. Lihat Fasilitas");
            System.out.println("2. Tambah Fasilitas");
            System.out.println("3. Update Fasilitas");
            System.out.println("4. Hapus Fasilitas");
            System.out.println("5. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("1")) {
                List<Fasilitas> fasilitas = controller.getAllFasilitas();
                System.out.println("\nDaftar Fasilitas:");
                for (Fasilitas f : fasilitas) {
                    System.out.println("- [" + f.getIdFasilitas() + "] " + f.getNamaFasilitas() + " : "
                            + f.getDeskripsiFasilitas());
                }
            } else if (pilihan.equals("2")) {
                System.out.print("ID Fasilitas: ");
                String id = scanner.nextLine();
                System.out.print("Nama Fasilitas: ");
                String nama = scanner.nextLine();
                System.out.print("Deskripsi Fasilitas: ");
                String deskripsi = scanner.nextLine();

                Fasilitas fasBaru = new Fasilitas(id, nama, deskripsi);
                if (controller.tambahFasilitas(fasBaru)) {
                    System.out.println("Fasilitas berhasil ditambahkan.");
                } else {
                    System.out.println("Gagal menambahkan fasilitas.");
                }
            } else if (pilihan.equals("3")) {
                System.out.print("ID Fasilitas yang akan diupdate: ");
                String id = scanner.nextLine();
                System.out.print("Nama Fasilitas Baru: ");
                String nama = scanner.nextLine();
                System.out.print("Deskripsi Fasilitas Baru: ");
                String deskripsi = scanner.nextLine();

                Fasilitas fasUpdate = new Fasilitas(id, nama, deskripsi);
                if (controller.updateFasilitas(fasUpdate)) {
                    System.out.println("Fasilitas berhasil diupdate.");
                } else {
                    System.out.println("Gagal mengupdate fasilitas.");
                }
            } else if (pilihan.equals("4")) {
                System.out.print("ID Fasilitas yang ingin dihapus: ");
                String id = scanner.nextLine();
                if (controller.hapusFasilitas(id)) {
                    System.out.println("Fasilitas berhasil dihapus.");
                } else {
                    System.out.println("Gagal menghapus fasilitas.");
                }
            } else if (pilihan.equals("5")) {
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }
}