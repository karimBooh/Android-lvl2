package ca.ulaval.ima.tp3.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ca.ulaval.ima.tp3.R;

public class BrandAdapter extends ArrayAdapter<Brand> {


    public BrandAdapter(Context context, ArrayList<Brand> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        Brand brand = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.brand_items, parent, false);
        }

        TextView model = convertView.findViewById(R.id.model);

        model.setText(brand.getName());

        return convertView;
    }

}
