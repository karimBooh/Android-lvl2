package ca.ulaval.ima.tp3.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.ulaval.ima.tp3.R;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private ArrayList<Offer> offers;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView yearValue;
        public TextView kilometerValue;
        public TextView priceValue;
        public TextView modelTitle;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            yearValue = view.findViewById(R.id.yearValue);
            kilometerValue = view.findViewById(R.id.kilometerValue);
            priceValue = view.findViewById(R.id.priceValue);
            modelTitle = view.findViewById(R.id.modelTitle);

        }
    }

    public OfferAdapter(ArrayList<Offer> myDataset) {
        offers = myDataset;
    }

    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_view, parent, false);


        OfferAdapter.MyViewHolder vh = new OfferAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OfferAdapter.MyViewHolder holder, int position) {
        Offer offer = offers.get(position);

        Picasso.get().load(offer.getImage()).into(holder.imageView);
        holder.priceValue.setText( offer.getPrice() + "$");
        holder.kilometerValue.setText( offer.getKilometers()+ "km");
        holder.yearValue.setText(offer.getYear() + "");
        holder.modelTitle.setText(offer.getModel().getBrand().getName() + offer.getModel().getName());
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }
}
