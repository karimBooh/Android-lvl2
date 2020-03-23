package ca.ulaval.ima.tp3.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ca.ulaval.ima.tp3.DescriptionActivity;
import ca.ulaval.ima.tp3.R;

public class PublicationFragment extends Fragment implements Observer {

    private ArrayList<Offer> offers;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RequestService requestService;

    public void observe(Observable o) {
        o.addObserver(this);
    }

    public static PublicationFragment newInstance(int index) {
        PublicationFragment fragment = new PublicationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestService = RequestService.getInstance(getContext());
        this.observe(requestService);
    }

    public void getMyOffer() {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.GET, requestService.url + "offer/mine/", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray objs = response.getJSONArray("content");
                            System.out.println(objs.toString());
                            for (int i = 0; i < objs.length(); i++) {
                                JSONObject modelObj = objs.getJSONObject(i).getJSONObject("model");
                                Brand brand = new Brand(modelObj.getJSONObject("brand").getInt("id"), modelObj.getJSONObject("brand").getString("name"));
                                Model newModel = new Model(modelObj.getInt("id"), modelObj.getString("name"), brand);
                                String image = objs.getJSONObject(i).getString("image");
                                int year = objs.getJSONObject(i).getInt("year");
                                boolean owner = objs.getJSONObject(i).getBoolean("from_owner");
                                int kilometer = objs.getJSONObject(i).getInt("kilometers");
                                int price = objs.getJSONObject(i).getInt("price");
                                String created = objs.getJSONObject(i).getString("created");
                                Offer offer = new Offer(objs.getJSONObject(i).getInt("id"), newModel, image, year, owner, kilometer, price, created);
                                offers.add(offer);
                            }

                            // specify an adapter (see also next example)
                            mAdapter = new OfferAdapter(offers);
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            // TODO
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("################" + error.getMessage());

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map params = new HashMap();

                params.put("Authorization", requestService.getToken());
                System.out.println(params.toString());
                return params;
            }
        };

        requestService.addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_publication, container, false);

        offers = new ArrayList<>();

        recyclerView = root.findViewById(R.id.publicationRecyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getMyOffer();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                Offer offer = offers.get(position);


                Intent intent = new Intent(getContext(), DescriptionActivity.class);
                intent.putExtra("Offer", offer);
                startActivity(intent);
            }

            @Override public void onLongItemClick(View view, int position) {
            }
        }));

        return root;
    }

    @Override
    public void update(Observable observable, Object o) {
        getMyOffer();
    }
}
