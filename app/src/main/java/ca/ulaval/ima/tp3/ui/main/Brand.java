package ca.ulaval.ima.tp3.ui.main;

import android.os.Parcel;
import android.os.Parcelable;


public class Brand implements Parcelable {
    private int id;
    private String name;


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }


    public static final Parcelable.Creator<Brand> CREATOR
            = new Parcelable.Creator<Brand>() {
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    public Brand(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
    }


    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
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
}