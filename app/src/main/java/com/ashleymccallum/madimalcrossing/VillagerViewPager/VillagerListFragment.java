package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.LIST_ID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VillagerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagerListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private int mParam3;

    public VillagerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VillagerListFragment.
     */
    public static VillagerListFragment newInstance(String param1, String param2, int param3) {
        VillagerListFragment fragment = new VillagerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villager_viewpager, container, false);
        TextView listName = view.findViewById(R.id.listName);
        Button listButton = view.findViewById(R.id.villagerListButton);

        ImageView background = view.findViewById(R.id.bgImage);
        background.setAlpha(0.8f);

        switch (mParam3 % 3) {
            case 0:
                background.setImageResource(R.drawable.beach);
                break;
            case 1:
                background.setImageResource(R.drawable.day);
                break;
            case 2:
                background.setImageResource(R.drawable.night);
                listName.setTextColor(Color.WHITE);
                break;
        }

        if(mParam1 != null && mParam2 != null) {
            listName.setText(mParam1);
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), VillagerDetailHostActivity.class);
                    i.putExtra(LIST_ID, mParam2);
                    try {
                        startActivity(i);
                    }catch (ActivityNotFoundException e) {
                        Snackbar.make(view, getString(R.string.list_error), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }
}