package com.demikopi.model;

public class Menu {

    private int idMenu;
    private String namaMenu;
    private String kategori;
    private String profilRasa;
    private String suhuSajian;
    private boolean isBestseller;
    private int harga;
    private String deskripsiMenu;
    private boolean statusTersedia;

    public Menu(int idMenu, String kategori, String namaMenu, String profilRasa,
            String suhuSajian, boolean isBestseller, int harga,
            String deskripsiMenu, boolean statusTersedia) {
        this.idMenu = idMenu;
        this.kategori = kategori;
        this.namaMenu = namaMenu;
        this.profilRasa = profilRasa;
        this.suhuSajian = suhuSajian;
        this.isBestseller = isBestseller;
        this.harga = harga;
        this.deskripsiMenu = deskripsiMenu;
        this.statusTersedia = statusTersedia;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public String getKategori() {
        return kategori;
    }

    public String getProfilRasa() {
        return profilRasa;
    }

    public String getSuhuSajian() {
        return suhuSajian;
    }

    public boolean isBestseller() {
        return isBestseller;
    }

    public int getHarga() {
        return harga;
    }

    public String getDeskripsiMenu() {
        return deskripsiMenu;
    }

    public boolean isStatusTersedia() {
        return statusTersedia;
    }
}
 // Done