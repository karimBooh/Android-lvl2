package ca.ulaval.ima.tp3.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ca.ulaval.ima.tp3.DescriptionActivity;
import ca.ulaval.ima.tp3.ModelActivity;
import ca.ulaval.ima.tp3.R;

public class SellFragment extends Fragment implements Observer {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RequestService requestService;

    private Button buttonModel;
    private Spinner spinYear;
    private Spinner spinnerTransmission;
    private EditText kilometer;
    private EditText price;
    private CheckBox checkBox;
    private int index;
    private Offer offer;
    private Model model;

    public static SellFragment newInstance(int index) {
        SellFragment fragment = new SellFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void observe(Observable o) {
        o.addObserver(this);
    }

    private void postAddOffer()
    {
        final JSONObject object = new JSONObject();
        try {
            object.put("from_owner", checkBox.isChecked());
            object.put("kilometers", Integer.parseInt(kilometer.getText().toString()));
            object.put("year", Integer.parseInt(spinYear.getSelectedItem().toString()));
            object.put("price", Integer.parseInt(price.getText().toString()));
            if (spinnerTransmission.getSelectedItem().toString().equals("Automatique")) {
                object.put("transmission", "AT");
            } else if (spinnerTransmission.getSelectedItem().toString().equals("Semi automatique")) {
                object.put("transmission", "SM");
            } else
                object.put("transmission", "MA");
            if (model != null)
                object.put("model", model.getId());
            else
                object.put("model", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, requestService.url + "offer/add/", object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject objs = response.getJSONObject("content");

                            JSONObject modelObj = objs.getJSONObject("model");
                            Brand brand = new Brand(modelObj.getJSONObject("brand").getInt("id"), modelObj.getJSONObject("brand").getString("name"));
                            Model newModel = new Model(modelObj.getInt("id"), modelObj.getString("name"), brand);
                            String image = objs.getString("image");
                            int year = objs.getInt("year");
                            boolean owner = objs.getBoolean("from_owner");
                            int kilometer = objs.getInt("kilometers");
                            int price = objs.getInt("price");
                            String created = objs.getString("created");
                            offer = new Offer(objs.getInt("id"), newModel, image, year, owner, kilometer, price, created);

                            Intent intent = new Intent(getContext(), DescriptionActivity.class);

                            intent.putExtra("Offer", offer);

                            startActivity(intent);
                        } catch (JSONException e) {
                            // TODO
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        if (error != null) {
                            System.out.println(error.networkResponse.statusCode);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (requestService.getToken() != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", requestService.getToken());
                    return params;
                }
                return super.getHeaders();
            }
        };

        requestService.addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sell, container, false);
        if (getArguments() != null)
            index = getArguments().getInt(ARG_SECTION_NUMBER);

        buttonModel = root.findViewById(R.id.buttonModel);
        spinYear = root.findViewById(R.id.yearSpinner);
        spinnerTransmission = root.findViewById(R.id.spinner2);
        kilometer = root.findViewById(R.id.editText);
        price = root.findViewById(R.id.editText2);
        checkBox = root.findViewById(R.id.checkBox);
        Button submit = root.findViewById(R.id.submitButton);


        buttonModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ModelActivity.class);
                Brand brand = new Brand(1, "Acura");
                intent.putExtra("Brand", brand);
                startActivityForResult(intent, 0);
            }
        });

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(0, Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);

        spinYear.setAdapter(adapter);


        ArrayList<String> transmissions = new ArrayList<>();
        transmissions.add("Automatique");
        transmissions.add("Manuel");
        transmissions.add("Semi automatique");
        ArrayAdapter<String> adapterTransmission = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, transmissions);

        spinnerTransmission.setAdapter(adapterTransmission);

        requestService = RequestService.getInstance(getContext());

        observe(requestService);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestService.getToken() == null)
                {
                    LoginDialog loginDialog = new LoginDialog(getActivity());
                    loginDialog.show();
                }
                else {
                    postAddOffer();
                }
            }
        });;

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            model = data.getParcelableExtra("Model");
            buttonModel.setText("Acura " + model.getName());
        }

    }

    @Override
    public void update(Observable observable, Object o) {
        if (requestService.getPosition() == index)
            postAddOffer();
    }
}
