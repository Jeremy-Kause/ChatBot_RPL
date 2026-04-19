package com.demikopi.model;

public class InfoKedai {
    private String idInfo;
    private String jamOperasional;
    private String lokasi;
    private String kontak;

    public InfoKedai(String idInfo, String jamOperasional, String lokasi, String kontak) {
        this.idInfo = idInfo;
        this.jamOperasional = jamOperasional;
        this.lokasi = lokasi;
        this.kontak = kontak;
    }

    public String getIdInfo() {
        return idInfo;
    }
    public String getJamOperasional() {
        return jamOperasional;
    }
    public String getLokasi() {
        return lokasi;
    }
    public String getKontak() {
        return kontak;
    }
}
// Done