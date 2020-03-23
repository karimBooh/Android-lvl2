package ca.ulaval.ima.tp3.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.ulaval.ima.tp3.R;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MyViewHolder> {
    private ArrayList<Model> models;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView modelName;


        public MyViewHolder(View view) {
            super(view);
            modelName = view.findViewById(R.id.modelName);
        }
    }

    public ModelAdapter(ArrayList<Model> myDataset) {
        models = myDataset;
    }

    @Override
    public ModelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.modelName.setText(models.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}