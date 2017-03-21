package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * model untuk tender
 * mewakili atribut-atribut yang dimiliki tender
 */
public class Tender implements Parcelable {
    private int id;
    private int iduser;
    private int idKategori;
    private String name;
    private String foto;
    private String deskripsi;
    private int anggaran;
    private String waktu;
    @SerializedName("kategori")
    private Kategori kategori;

    public Tender() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getAnggaran() {
        return anggaran;
    }

    public void setAnggaran(int anggaran) {
        this.anggaran = anggaran;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    @Override public String toString() {
        return "Tender{" +
                "id=" + id +
                ", iduser=" + iduser +
                ", idKategori=" + idKategori +
                ", name='" + name + '\'' +
                ", foto='" + foto + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", anggaran=" + anggaran +
                ", waktu='" + waktu + '\'' +
                ", kategori=" + kategori +
                '}';
    }

    protected Tender(Parcel in) {
        id = in.readInt();
        iduser = in.readInt();
        idKategori = in.readInt();
        name = in.readString();
        foto = in.readString();
        deskripsi = in.readString();
        anggaran = in.readInt();
        waktu = in.readString();
        kategori = in.readParcelable(Kategori.class.getClassLoader());
    }

    public static final Creator<Tender> CREATOR = new Creator<Tender>() {
        @Override
        public Tender createFromParcel(Parcel in) {
            return new Tender(in);
        }

        @Override
        public Tender[] newArray(int size) {
            return new Tender[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(iduser);
        dest.writeInt(idKategori);
        dest.writeString(name);
        dest.writeString(foto);
        dest.writeString(deskripsi);
        dest.writeInt(anggaran);
        dest.writeString(waktu);
        dest.writeParcelable(kategori, flags);
    }
}
