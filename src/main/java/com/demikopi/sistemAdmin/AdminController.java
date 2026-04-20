package com.demikopi.sistemAdmin;

import com.demikopi.dataAccess.FasilitasDAO;
import com.demikopi.dataAccess.InfoDAO;
import com.demikopi.dataAccess.KategoriDAO;
import com.demikopi.dataAccess.MenuDAO;
import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Kategori;
import com.demikopi.model.Menu;

import java.util.List;

public class AdminController {

    private MenuDAO menuDAO = new MenuDAO();
    private InfoDAO infoDAO = new InfoDAO();
    private FasilitasDAO fasilitasDAO = new FasilitasDAO();
    private KategoriDAO kategoriDAO = new KategoriDAO();

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public List<Menu> getAllMenu() {
        return menuDAO.getAllMenu();
    }

    public boolean tambahMenu(Menu menu) {
        if (menu == null) {
            return false;
        }

        if (isBlank(menu.getNamaMenu())
                || menu.getHarga() <= 0
                || isBlank(menu.getKategori())
                || isBlank(menu.getDeskripsiMenu())) {
            return false;
        }

        return menuDAO.tambahMenu(menu);
    }

    public boolean updateMenu(Menu menu) {
        if (menu == null) {
            return false;
        }

        if (menu.getIdMenu() <= 0
                || isBlank(menu.getNamaMenu())
                || menu.getHarga() <= 0
                || isBlank(menu.getKategori())) {
            return false;
        }

        return menuDAO.updateMenu(menu);
    }

    public boolean hapusMenu(Menu menu) {
        if (menu == null) {
            return false;
        }

        if (menu.getIdMenu() <= 0) {
            return false;
        }

        return menuDAO.hapusMenu(menu.getIdMenu());
    }

    public InfoKedai getInfoKedai() {
        return infoDAO.getInfo();
    }

    public boolean updateInfoKedai(InfoKedai infoKedai) {
        if (infoKedai == null) {
            return false;
        }

        if (isBlank(infoKedai.getJamOperasional())
                || isBlank(infoKedai.getLokasi())
                || isBlank(infoKedai.getKontak())) {
            return false;
        }

        return infoDAO.updateInfo(infoKedai);
    }

    public List<Fasilitas> getAllFasilitas() {
        return fasilitasDAO.getAllFasilitas();
    }

    public boolean tambahFasilitas(Fasilitas fasilitas) {
        if (fasilitas == null) {
            return false;
        }

        if (isBlank(fasilitas.getIdFasilitas())
                || isBlank(fasilitas.getNamaFasilitas())
                || isBlank(fasilitas.getDeskripsiFasilitas())) {
            return false;
        }

        return fasilitasDAO.tambahFasilitas(fasilitas);
    }

    public boolean updateFasilitas(Fasilitas fasilitas) {
        if (fasilitas == null) {
            return false;
        }

        if (isBlank(fasilitas.getIdFasilitas())
                || isBlank(fasilitas.getNamaFasilitas())) {
            return false;
        }

        return fasilitasDAO.updateFasilitas(fasilitas);
    }

    public boolean hapusFasilitas(String idFasilitas) {
        if (isBlank(idFasilitas)) {
            return false;
        }

        return fasilitasDAO.hapusFasilitas(idFasilitas);
    }

    public List<Kategori> getAllKategori() {
        return kategoriDAO.getAllKategori();
    }
}
