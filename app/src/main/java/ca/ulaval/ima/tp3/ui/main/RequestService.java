package ca.ulaval.ima.tp3.ui.main;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Observable;

public class RequestService extends Observable {
    private static RequestService instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    public static String url = "http://68.183.207.74/api/v1/";
    private String token;
    private int position;

    private RequestService(Context context)
    {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestService getInstance(Context context)
    {
        if (instance == null) {
            instance = new RequestService(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public  void setToken(String tokens) {
        synchronized (this) {
            token = tokens;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized String getToken() {
        return token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
