package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Penawaran() {
    }

    public Penawaran(int id) {
        this.id = id;
    }

    protected Penawaran(Parcel in) {
        id = in.readInt();
        idTender = in.readInt();
        idUser = in.readInt();
        nama = in.readString();
        deskripsi = in.readString();
        foto = in.readString();
        harga = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idTender);
        parcel.writeInt(idUser);
        parcel.writeString(nama);
        parcel.writeString(deskripsi);
        parcel.writeString(foto);
        parcel.writeInt(harga);
    }
}
