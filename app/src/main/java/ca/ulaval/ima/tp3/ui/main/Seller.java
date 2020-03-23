package ca.ulaval.ima.tp3.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

public class Seller implements Parcelable {
    private String lastName;
    private String firstName;
    private String email;


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastName);
        dest.writeString(this.firstName);
        dest.writeString(this.email);

    }


    public static final Parcelable.Creator<Seller> CREATOR
            = new Parcelable.Creator<Seller>() {
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    public Seller(Parcel in) {
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.email = in.readString();
    }


    public Seller(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}