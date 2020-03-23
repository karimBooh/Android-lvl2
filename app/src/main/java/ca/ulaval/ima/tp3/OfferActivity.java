package ca.ulaval.ima.tp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.ulaval.ima.tp3.ui.main.Brand;
import ca.ulaval.ima.tp3.ui.main.Model;
import ca.ulaval.ima.tp3.ui.main.Offer;
import ca.ulaval.ima.tp3.ui.main.OfferAdapter;
import ca.ulaval.ima.tp3.ui.main.RecyclerItemClickListener;
import ca.ulaval.ima.tp3.ui.main.RequestService;

public class OfferActivity extends AppCompatActivity {
    private Model model;
    private ArrayList<Offer> offers;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        offers = new ArrayList<>();
        this.model = getIntent().getParcelableExtra("Model");
        getSupportActionBar().hide();
        ImageView back  = findViewById(R.id.backImage);
        TextView title = findViewById(R.id.toolbarTitle);

        title.setText(model.getName());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RequestService requestService = RequestService.getInstance(this);

        recyclerView =  findViewById(R.id.offerRecyclerView);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.GET, requestService.url + "offer/", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray objs = response.getJSONArray("content");

                            for(int i = 0; i < objs.length(); i++)
                            {
                                JSONObject modelObj = objs.getJSONObject(i).getJSONObject("model");
                                Brand brand = new Brand(modelObj.getJSONObject("brand").getInt("id"), modelObj.getJSONObject("brand").getString("name"));
                                Model newModel = new Model(modelObj.getInt("id"), modelObj.getString("name"), brand);
                                if (newModel.getId() == model.getId()){

                                    String image = objs.getJSONObject(i).getString("image");
                                    int year = objs.getJSONObject(i).getInt("year");
                                    boolean owner = objs.getJSONObject(i).getBoolean("from_owner");
                                    int kilometer = objs.getJSONObject(i).getInt("kilometers");
                                    int price = objs.getJSONObject(i).getInt("price");
                                    String created = objs.getJSONObject(i).getString("created");
                                    Offer offer = new Offer(objs.getJSONObject(i).getInt("id"), newModel, image, year, owner, kilometer, price, created);
                                    offers.add(offer);
                                }
                            }

                            // specify an adapter (see also next example)
                            mAdapter = new OfferAdapter(offers);
                            recyclerView.setAdapter(mAdapter);


                        }
                        catch (JSONException e)
                        {
                            // TODO
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error.getMessage());

                    }
                });

        requestService.addToRequestQueue(jsonArrayRequest);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                Offer offer = offers.get(position);


                Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                intent.putExtra("Offer", offer);
                startActivity(intent);
            }

            @Override public void onLongItemClick(View view, int position) {
            }
        }));

    }
}
