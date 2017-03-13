package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * model untuk tender
 * mewakili atribut-atribut yang dimiliki tender
 */
public class Tender implements Parcelable {

    private int id;
    private int iduser;
    private String name;
    private String foto;
    private String deskripsi;
    private int anggaran;
    private String waktu;

    public Tender() {
    }

    public Tender(int id) {
        this.id = id;
    }

    protected Tender(Parcel in) {
        id = in.readInt();
        iduser = in.readInt();
        name = in.readString();
        foto = in.readString();
        deskripsi = in.readString();
        anggaran = in.readInt();
        waktu = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(iduser);
        parcel.writeString(name);
        parcel.writeString(foto);
        parcel.writeString(deskripsi);
        parcel.writeInt(anggaran);
        parcel.writeString(waktu);
    }

    @Override
    public String toString() {
        return "Tender{" +
                "id=" + id +
                ", iduser=" + iduser +
                ", name='" + name + '\'' +
                ", foto='" + foto + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", anggaran=" + anggaran +
                ", waktu='" + waktu + '\'' +
                '}';
    }
}
