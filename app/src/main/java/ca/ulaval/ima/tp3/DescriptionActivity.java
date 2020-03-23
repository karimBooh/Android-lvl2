package ca.ulaval.ima.tp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ca.ulaval.ima.tp3.ui.main.Brand;
import ca.ulaval.ima.tp3.ui.main.Description;
import ca.ulaval.ima.tp3.ui.main.Model;
import ca.ulaval.ima.tp3.ui.main.Offer;
import ca.ulaval.ima.tp3.ui.main.RequestService;
import ca.ulaval.ima.tp3.ui.main.Seller;

public class DescriptionActivity extends AppCompatActivity {

    RequestService requestService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        getSupportActionBar().hide();
        Offer offer = getIntent().getParcelableExtra("Offer");

        ImageView back  = findViewById(R.id.backImage);

        final ImageView imagedDescription = findViewById(R.id.imageDescription);
        final TextView brandValue = findViewById(R.id.brandValue);
        final TextView modelValue = findViewById(R.id.modemValuee);
        final TextView yearValue = findViewById(R.id.yearValueDescritpion);
        final TextView kilometerValue = findViewById(R.id.kilometerValueDescritpion);
        final TextView transmissionValue = findViewById(R.id.transmissionValue);
        final TextView priceValue = findViewById(R.id.priceValueDescrition);
        final TextView name = findViewById(R.id.name);
        final TextView emailValue = findViewById(R.id.emailValue);
        final TextView ownerValue = findViewById(R.id.owner);
        final TextView descriptionValue = findViewById(R.id.descriptionValue);
        final Button buttonContact = findViewById(R.id.buttonContact);

        TextView title = findViewById(R.id.toolbarTitle);

        title.setText("Vendre une voiture");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        requestService = RequestService.getInstance(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestService.url + "offer/" + offer.getId() + "/details/", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("content");
                            JSONObject modelObj = obj.getJSONObject("model");
                            Brand brand = new Brand(modelObj.getJSONObject("brand").getInt("id"), modelObj.getJSONObject("brand").getString("name"));
                            Model newModel = new Model(modelObj.getInt("id"), modelObj.getString("name"), brand);

                            String image = obj.getString("image");
                            int year = obj.getInt("year");
                            boolean owner = obj.getBoolean("from_owner");
                            int kilometer = obj.getInt("kilometers");
                            int price = obj.getInt("price");
                            String created = obj.getString("created");

                            Offer offer = new Offer(obj.getInt("id"), newModel, image, year, owner, kilometer, price, created);

                            JSONObject sellerObj = obj.getJSONObject("seller");

                            Seller seller = new Seller(sellerObj.getString("last_name"), sellerObj.getString("first_name"), sellerObj.getString("email"));

                            String description = obj.getString("description");
                            String transmission = obj.getString("transmission");

                            final Description describe = new Description(obj.getInt("id"), offer, description, transmission, seller);

                            Picasso.get().load(describe.getOffer().getImage()).into(imagedDescription);
                            brandValue.setText(describe.getOffer().getModel().getBrand().getName());
                            modelValue.setText(describe.getOffer().getModel().getName());
                            yearValue.setText(String.format("%d", describe.getOffer().getYear()));
                            kilometerValue.setText(describe.getOffer().getKilometers() + "");
                            transmissionValue.setText(describe.getTransmission());
                            priceValue.setText(describe.getOffer().getPrice() + "");
                            name.setText(describe.getSeller().getFirstName() + " " + describe.getSeller().getLastName());
                            emailValue.setText(describe.getSeller().getEmail());
                            ownerValue.setText((describe.getOffer().isOwner()) ? "Oui": "Non" );
                            descriptionValue.setText(describe.getDescription());

                            buttonContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                                    emailIntent.setType("plain/text");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{describe.getSeller().getEmail()});
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                }
                            });


                        } catch (JSONException e) {
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

        requestService.addToRequestQueue(jsonObjectRequest);
    }
}
