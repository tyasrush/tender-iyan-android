package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * model untuk kategori
 * mewakili atribut-atribut yang dimiliki kategori
 */
public class Kategori implements Parcelable {
    private int id;
    private String nama;

    public Kategori() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override public String toString() {
        return "Kategori{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                '}';
    }

    protected Kategori(Parcel in) {
        id = in.readInt();
        nama = in.readString();
    }

    public static final Creator<Kategori> CREATOR = new Creator<Kategori>() {
        @Override
        public Kategori createFromParcel(Parcel in) {
            return new Kategori(in);
        }

        @Override
        public Kategori[] newArray(int size) {
            return new Kategori[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
    }
}
