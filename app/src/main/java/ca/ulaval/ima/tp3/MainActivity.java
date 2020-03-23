package ca.ulaval.ima.tp3;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.ima.tp3.ui.main.LoginDialog;
import ca.ulaval.ima.tp3.ui.main.RequestService;
import ca.ulaval.ima.tp3.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestService = RequestService.getInstance(this.getApplicationContext());
        requestService.getRequestQueue().start();


        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                requestService.setPosition(position);
                if (position == 2) {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, requestService.url + "account/me/",
                                    null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        System.out.println(response.toString());
                                        JSONObject objs = response.getJSONObject("content");
                                        System.out.println(objs);
                                    } catch (JSONException e) {
                                        // TODO
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    //System.out.println(error.getMessage());
                                    System.out.println("bande de fdp");
                                    LoginDialog loginDialog = new LoginDialog(MainActivity.this);
                                    loginDialog.show();
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
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }
}