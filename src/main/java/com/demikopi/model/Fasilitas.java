package com.demikopi.model;

public class Fasilitas {
    private String idFasilitas;
    private String namaFasilitas;
    private String deskripsiFasilitas;

    public Fasilitas(String idFasilitas, String namaFasilitas, String deskripsiFasilitas) {
        this.idFasilitas = idFasilitas;
        this.namaFasilitas = namaFasilitas;
        this.deskripsiFasilitas = deskripsiFasilitas;
    }

    public String getIdFasilitas() {return idFasilitas;}
    public String getNamaFasilitas() {
        return namaFasilitas;
    }
    public String getDeskripsiFasilitas() {
        return deskripsiFasilitas;
    }
}
// Done