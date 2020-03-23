package ca.ulaval.ima.tp3.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {
    private int id;
    private Model model;
    private String image;
    private int year;
    private boolean owner;
    private int kilometers;
    private int price;
    private String created;


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.model, flags);
        dest.writeString(this.image);
        dest.writeInt(this.year);
        dest.writeByte((byte) (this.owner ? 1 : 0));
        dest.writeInt(this.kilometers);
        dest.writeInt(this.price);
        dest.writeString(this.created);

    }


    public static final Parcelable.Creator<Offer> CREATOR
            = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public Offer(Parcel in){
        this.id = in.readInt();
        this.model = in.readParcelable(Model.class.getClassLoader());
        this.image = in.readString();
        this.year = in.readInt();
        this.owner = in.readByte() != 0;
        this.kilometers = in.readInt();
        this.price = in.readInt();
        this.created = in.readString();
    }


    public Offer(int id, Model model, String image, int year, boolean owner, int kilometers, int price, String created) {
        this.id = id;
        this.model = model;
        this.image = image;
        this.year = year;
        this.owner = owner;
        this.kilometers = kilometers;
        this.price = price;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}