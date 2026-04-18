package com.demikopi.model;

public class InfoKedai {
    private String idInfo;
    private String jamOperasional; // FIX: typo 'jamOperasiona' → 'jamOperasional'
    private String lokasi;

    public InfoKedai(String idInfo, String jamOperasional, String lokasi) {
        this.idInfo = idInfo;
        this.jamOperasional = jamOperasional; // FIX: sesuaikan dengan nama field
        this.lokasi = lokasi;
    }

    public String getIdInfo() {
        return idInfo;
    }

    public String getJamOperasional() { // FIX: typo pada nama getter
        return jamOperasional;
    }

    public String getLokasi() {
        return lokasi;
    }
}
// Done