package com.ashleymccallum.madimalcrossing.NewsRecycler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.api.RequestSingleton;
import com.ashleymccallum.madimalcrossing.pojos.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        String url = "https://newsapi.org/v2/everything?q=animal%20crossing&language=en&pageSize=15";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                NewsItem item = new NewsItem();
                try {
                    JSONArray array = response.getJSONArray("articles");
//                    Log.d("---------------", String.valueOf(array.length()));
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject article = array.getJSONObject(i);
                        JSONObject source = article.getJSONObject("source");
                        item.setPublisherName(source.getString("name"));
                        item.setAuthorName(article.getString("author"));
                        item.setTitle(article.getString("title"));
                        item.setDescription(article.getString("description"));
                        item.setArticleURL(article.getString("url"));
                        item.setImgURL(article.getString("urlToImage"));
                        item.setTimestamp(article.getString("publishedAt"));
                        newsItems.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        RequestSingleton.getInstance(getContext()).getRequestQueue().add(request);

        RecyclerView recyclerView = view.findViewById(R.id.newsRecycler);
        recyclerView.setAdapter(new NewsRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
}