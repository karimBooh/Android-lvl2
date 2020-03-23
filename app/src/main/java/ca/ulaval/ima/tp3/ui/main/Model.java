package ca.ulaval.ima.tp3.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Model implements Parcelable {
    private int id;
    private String name;
    private Brand brand;


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.brand, flags);
    }

    public static final Parcelable.Creator<Model> CREATOR
            = new Parcelable.Creator<Model>() {
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public Model(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.brand = in.readParcelable(Brand.class.getClassLoader());
    }

    public Model(int id, String name, @Nullable Brand brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}