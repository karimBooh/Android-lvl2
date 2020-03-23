package ca.ulaval.ima.tp3.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ca.ulaval.ima.tp3.R;

public class LoginDialog extends AlertDialog {

    public EditText nipInput;
    public EditText emailInput;
    public TextView okButton;
    public RequestService requestService;


    public LoginDialog(Context a) {
        super(a);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        setContentView(getLayoutInflater().inflate(R.layout.login_dialog, null));
        nipInput = findViewById(R.id.nipInput);
        emailInput = findViewById(R.id.emailInput);
        okButton = findViewById(R.id.okButton);

        setCancelable(false);

        emailInput.setText("karim.boulaich.1@ulaval.ca");
        nipInput.setText("111274529");

        requestService = RequestService.getInstance(getContext());

        final JSONObject object = new JSONObject();
        try {
            object.put("email", emailInput.getText().toString());
            object.put("identification_number", Integer.parseInt(nipInput.getText().toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, requestService.url + "account/login/", object, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    System.out.println(response.toString());
                                    JSONObject objs = response.getJSONObject("content");
                                    requestService.setToken("Basic " + objs.getString("token"));
                                    System.out.println(objs);
                                    dismiss();
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
                        });

                requestService.addToRequestQueue(jsonObjectRequest);
            }
        });
    }

}
