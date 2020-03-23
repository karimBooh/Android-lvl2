package ca.ulaval.ima.tp3.ui.main;


import android.os.Parcel;
import android.os.Parcelable;

public class Description implements Parcelable {
    private int id;
    private Offer offer;
    private String description;
    private String transmission;
    private Seller seller;


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.offer, flags);
        dest.writeParcelable(this.seller, flags);
        dest.writeString(this.description);
        dest.writeString(this.transmission);
    }


    public static final Parcelable.Creator<Description> CREATOR
            = new Parcelable.Creator<Description>() {
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        public Description[] newArray(int size) {
            return new Description[size];
        }
    };

    public Description(Parcel in){
        this.id = in.readInt();
        this.offer = in.readParcelable(Offer.class.getClassLoader());
        this.seller = in.readParcelable(Seller.class.getClassLoader());
        this.description = in.readString();
        this.transmission = in.readString();
    }

    public Description(int id, Offer offer, String description, String transmission, Seller seller) {
        this.id = id;
        this.offer = offer;
        this.description = description;
        this.transmission = transmission;
        this.seller = seller;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
