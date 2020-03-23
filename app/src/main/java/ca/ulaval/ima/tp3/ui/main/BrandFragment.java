package ca.ulaval.ima.tp3.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.ulaval.ima.tp3.ModelActivity;
import ca.ulaval.ima.tp3.R;

public class BrandFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RequestService requestService = RequestService.getInstance(this.getContext());

    public static BrandFragment newInstance(int index) {
        BrandFragment fragment = new BrandFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        final ListView listView = root.findViewById(R.id.model_list);

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, requestService.url + "brand/", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray obj = response.getJSONArray("content");
                            ArrayList<Brand> brands = new ArrayList<Brand>();
                            System.out.println(obj.length());

                            for(int i = 0; i < obj.length(); i++)
                            {
                                Brand brand = new Brand(obj.getJSONObject(i).getInt("id"), obj.getJSONObject(i).getString("name"));
                                brands.add(brand);
                            }
                            BrandAdapter Brands = new BrandAdapter(getContext(), brands);


                            listView.setAdapter(Brands);
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

        requestService.addToRequestQueue(jsonObjRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand brand = (Brand) parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), ModelActivity.class);
                intent.putExtra("Brand", brand);
                intent.putExtra("SellFragment", false);
                startActivity(intent);
            }
        });

        return root;
    }
}