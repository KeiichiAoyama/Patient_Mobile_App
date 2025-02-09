package com.example.patientmobileapp;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String nik;
    public String nama;
    public String tempat_lahir;
    public String tanggal_lahir;
    public String bulan_lahir;
    public String tahun_lahir;
    public String jenis_kelamin;
    public String golongan_darah;
    public String alamat;
    public String rt;
    public String rw;
    public String kelurahan;
    public String kecamatan;
    public String agama;
    public String pekerjaan;
    public String no_telp;
    public String foto_pasien;

    public User() {}

    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();
        if (json.has("nik")) user.nik = json.getString("nik");
        if (json.has("nama")) user.nama = json.getString("nama");
        if (json.has("tempat_lahir")) user.tempat_lahir = json.getString("tempat_lahir");
        if (json.has("tanggal_lahir")) user.tanggal_lahir = json.getString("tanggal_lahir");
        if (json.has("bulan_lahir")) user.bulan_lahir = json.getString("bulan_lahir");
        if (json.has("tahun_lahir")) user.tahun_lahir = json.getString("tahun_lahir");
        if (json.has("jenis_kelamin")) user.jenis_kelamin = json.getString("jenis_kelamin");
        if (json.has("golongan_darah")) user.golongan_darah = json.getString("golongan_darah");
        if (json.has("alamat")) user.alamat = json.getString("alamat");
        if (json.has("rt")) user.rt = json.getString("rt");
        if (json.has("rw")) user.rw = json.getString("rw");
        if (json.has("kelurahan")) user.kelurahan = json.getString("kelurahan");
        if (json.has("kecamatan")) user.kecamatan = json.getString("kecamatan");
        if (json.has("agama")) user.agama = json.getString("agama");
        if (json.has("pekerjaan")) user.pekerjaan = json.getString("pekerjaan");
        if (json.has("no_telp")) user.no_telp = json.getString("no_telp");
        if (json.has("foto_pasien")) user.foto_pasien = json.getString("foto_pasien");

        return user;
    }
}
