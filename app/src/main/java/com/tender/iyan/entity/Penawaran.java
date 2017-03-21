package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * model untuk penawaran
 * mewakili atribut-atribut yang dimiliki penawaran
 */
public class Penawaran implements Parcelable {

    private int id;
    private int idTender;
    private int idUser;
    private String nama;
    private String deskripsi;
    private String foto;
    private int harga;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public Penawaran() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTender() {
        return idTender;
    }

    public void setIdTender(int idTender) {
        this.idTender = idTender;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override public String toString() {
        return "Penawaran{" +
                "id=" + id +
                ", idTender=" + idTender +
                ", idUser=" + idUser +
                ", nama='" + nama + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", foto='" + foto + '\'' +
                ", harga=" + harga +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    protected Penawaran(Parcel in) {
        id = in.readInt();
        idTender = in.readInt();
        idUser = in.readInt();
        nama = in.readString();
        deskripsi = in.readString();
        foto = in.readString();
        harga = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Penawaran> CREATOR = new Creator<Penawaran>() {
        @Override
        public Penawaran createFromParcel(Parcel in) {
            return new Penawaran(in);
        }

        @Override
        public Penawaran[] newArray(int size) {
            return new Penawaran[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idTender);
        dest.writeInt(idUser);
        dest.writeString(nama);
        dest.writeString(deskripsi);
        dest.writeString(foto);
        dest.writeInt(harga);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }
}
