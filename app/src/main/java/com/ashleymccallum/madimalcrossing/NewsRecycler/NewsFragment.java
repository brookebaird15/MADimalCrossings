package com.ashleymccallum.madimalcrossing.NewsRecycler;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.google.android.material.snackbar.Snackbar;


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
        AppDatabase db = new AppDatabase(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.largeNewsRecycler);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView = view.findViewById(R.id.newsRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.setAdapter(new NewsRecyclerViewAdapter(db.getArticles(), getContext()));
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.recycler_anim);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int animToggle = Integer.parseInt(sharedPreferences.getString(getString(R.string.animations_key), "1"));
        if(animToggle == 1) {
            recyclerView.setAnimation(animation);
        }


        ImageView facebook = view.findViewById(R.id.logoFacebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri site = Uri.parse("https://www.facebook.com/animalcrossingcommunity/");
                Intent i = new Intent(Intent.ACTION_VIEW, site);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view, getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ImageView twitter = view.findViewById(R.id.logoTwitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri site = Uri.parse("https://twitter.com/animalcrossing");
                Intent i = new Intent(Intent.ACTION_VIEW, site);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view, getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ImageView instagram = view.findViewById(R.id.logoInstagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri site = Uri.parse("https://www.instagram.com/animalcrossing_official/");
                Intent i = new Intent(Intent.ACTION_VIEW, site);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view, getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        db.close();

        return view;
    }
}