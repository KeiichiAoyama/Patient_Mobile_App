package com.example.patientmobileapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    public User(Map<String, Object> newUser) {
        this.nik = (String) newUser.get("nik");
        this.nama = (String) newUser.get("nama");
        this.tempat_lahir = (String) newUser.get("tempat_lahir");
        this.tanggal_lahir = (String) newUser.get("tanggal_lahir");
        this.bulan_lahir = (String) newUser.get("bulan_lahir");
        this.tahun_lahir = (String) newUser.get("tahun_lahir");
        this.jenis_kelamin = (String) newUser.get("jenis_kelamin");
        this.golongan_darah = (String) newUser.get("golongan_darah");
        this.alamat = (String) newUser.get("alamat");
        this.rt = (String) newUser.get("rt");
        this.rw = (String) newUser.get("rw");
        this.kelurahan = (String) newUser.get("kelurahan");
        this.kecamatan = (String) newUser.get("kecamatan");
        this.agama = (String) newUser.get("agama");
        this.pekerjaan = (String) newUser.get("pekerjaan");
        this.no_telp = (String) newUser.get("no_telp");
    }

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

    @Override
    public String toString() {
        return "User{" +
                "nik='" + nik + '\'' +
                ", nama='" + nama + '\'' +
                ", tempat_lahir='" + tempat_lahir + '\'' +
                ", tanggal_lahir='" + tanggal_lahir + '\'' +
                ", bulan_lahir='" + bulan_lahir + '\'' +
                ", tahun_lahir='" + tahun_lahir + '\'' +
                ", jenis_kelamin='" + jenis_kelamin + '\'' +
                ", golongan_darah='" + golongan_darah + '\'' +
                ", alamat='" + alamat + '\'' +
                ", rt='" + rt + '\'' +
                ", rw='" + rw + '\'' +
                ", kelurahan='" + kelurahan + '\'' +
                ", kecamatan='" + kecamatan + '\'' +
                ", agama='" + agama + '\'' +
                ", pekerjaan='" + pekerjaan + '\'' +
                ", no_telp='" + no_telp + '\'' +
                ", foto_pasien='" + foto_pasien + '\'' +
                '}';
    }

    public static String buildPublishPayload(Map<String, Object> newUser) throws JSONException {
        JSONObject root = new JSONObject();

        root.put("emr_stream", "patient-" + newUser.get("nik") + "-record");
        root.put("identity_stream", newUser.get("email"));

        JSONObject credentials = new JSONObject();
        credentials.put("email", newUser.get("email"));

        String hashedPassword = BCrypt.withDefaults().hashToString(10, newUser.get("password").toString().toCharArray());
        credentials.put("password", hashedPassword);

        root.put("credentials", credentials);

        JSONObject patientData = new JSONObject();
        for (String key : new String[]{
                "nik", "nama", "tempat_lahir", "tanggal_lahir", "bulan_lahir", "tahun_lahir",
                "jenis_kelamin", "golongan_darah", "alamat", "rt", "rw",
                "kelurahan", "kecamatan", "agama", "pekerjaan", "no_telp"
        }) {
            patientData.put(key, newUser.get(key));
        }

        root.put("patient_data", patientData);

        return root.toString();
    }
}
