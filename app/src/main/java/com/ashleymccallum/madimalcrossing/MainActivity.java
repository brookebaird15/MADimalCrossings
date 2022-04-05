package com.ashleymccallum.madimalcrossing;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.ashleymccallum.madimalcrossing.NewsRecycler.NewsViewModel;
import com.ashleymccallum.madimalcrossing.api.RequestSingleton;
import com.ashleymccallum.madimalcrossing.pojos.NewsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ashleymccallum.madimalcrossing.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    private ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_villager, R.id.nav_bingo, R.id.nav_news, R.id.nav_song)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        db = new AppDatabase(this);
        if(db.getAllSongs().isEmpty()) {
            loadSongs(this);
        }

        if(db.getAllVillagers().isEmpty()) {
            loadVillagers(this);
        }

        if(!db.getArticles().isEmpty()) {
            if(System.currentTimeMillis() - db.getArticles().get(0).getLastUpdated() > 7200000) {
                Log.d("----NEWS-----", "ARTICLES REFRESHED");
                db.clearArticles();
                loadArticles(this);
            }
        } else {
            loadArticles(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads articles from the API
     * @param context application context
     * @author Ashley McCallum
     */
    private void loadArticles(Context context) {

        String url = "https://newsapi.org/v2/everything?q=animal%20crossing&language=en&pageSize=15";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                db.addArticles(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("news_VOLLEY", error.getLocalizedMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("User-Agent", "Mozilla/5.0");
                params.put("Authorization", "ee0ac4d79099430d8f0b2bf7ef3e9cdf");
                return params;
            }
        };
        RequestSingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Loads the villagers from the API on launch
     * @param context application context
     * @author Ashley McCallum
     */
    private void loadVillagers(Context context) {
        String url = "https://api.nookipedia.com/villagers?nhdetails=true&game=nh";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                db.addAllVillagers(array);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loadVillagers_VOLLEY",  error.getLocalizedMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", "1aa13d13-125c-4eb1-884d-d315dc280ca0");
                params.put("Accept-Version", "1.0.0");
                return params;
            }
        };
        RequestSingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Loads the songs from the API on launch
     * @param context application context
     * @author Ashley McCallum
     */
    private void loadSongs(Context context) {
        String url = "https://acnhapi.com/v1/songs/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                db.addAllSongs(object, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loadSongs_VOLLEY",  error.getLocalizedMessage());
                error.printStackTrace();
            }
        });
        RequestSingleton.getInstance(context).getRequestQueue().add(request);
    }

}