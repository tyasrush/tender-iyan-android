package com.tender.iyan.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Deal implements Parcelable {
  private int id;
  private String namaPenawaran;
  private String harga;
  private String foto;

  public Deal() {
  }

  public Deal(int id) {
    this.id = id;
  }

  protected Deal(Parcel in) {
    id = in.readInt();
    namaPenawaran = in.readString();
    harga = in.readString();
    foto = in.readString();
  }

  public static final Creator<Deal> CREATOR = new Creator<Deal>() {
    @Override public Deal createFromParcel(Parcel in) {
      return new Deal(in);
    }

    @Override public Deal[] newArray(int size) {
      return new Deal[size];
    }
  };

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNamaPenawaran() {
    return namaPenawaran;
  }

  public void setNamaPenawaran(String namaPenawaran) {
    this.namaPenawaran = namaPenawaran;
  }

  public String getHarga() {
    return harga;
  }

  public void setHarga(String harga) {
    this.harga = harga;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  @Override public String toString() {
    return "Deal{"
        + "id="
        + id
        + ", namaPenawaran='"
        + namaPenawaran
        + '\''
        + ", harga='"
        + harga
        + '\''
        + ", foto='"
        + foto
        + '\''
        + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(namaPenawaran);
    dest.writeString(harga);
    dest.writeString(foto);
  }
}
