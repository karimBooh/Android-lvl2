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
import ca.ulaval.ima.tp3.ui.main.ModelAdapter;
import ca.ulaval.ima.tp3.ui.main.RecyclerItemClickListener;
import ca.ulaval.ima.tp3.ui.main.RequestService;

public class ModelActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Model> models = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);

        final Brand brand = getIntent().getParcelableExtra("Brand");
        final boolean isCalledBySellFragment  = getIntent().getBooleanExtra("SellFragment", true);
        getSupportActionBar().hide();
        ImageView back  = findViewById(R.id.backImage);
        TextView title = findViewById(R.id.toolbarTitle);

        title.setText(brand.getName());

        recyclerView =  findViewById(R.id.recyclerViewModels);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestService requestService = RequestService.getInstance(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.GET, requestService.url + "brand/" + brand.getId() + "/models", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray objs = response.getJSONArray("content");

                            System.out.println(objs.length());

                            for(int i = 0; i < objs.length(); i++)
                            {
                                Model model = new Model(objs.getJSONObject(i).getInt("id"), objs.getJSONObject(i).getString("name"), brand);
                                models.add(model);
                            }

                            mAdapter = new ModelAdapter(models);
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
                Model model = models.get(position);
                Intent intent = new Intent(getApplicationContext(), OfferActivity.class);
                intent.putExtra("Model", model);
                if (!isCalledBySellFragment){
                    startActivity(intent);
                }
                else
                {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override public void onLongItemClick(View view, int position) {
            }
        }));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
