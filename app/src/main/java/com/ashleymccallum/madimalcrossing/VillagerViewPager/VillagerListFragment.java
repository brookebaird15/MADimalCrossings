package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.LIST_ID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VillagerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagerListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
    public static VillagerListFragment newInstance(String param1, String param2) {
        VillagerListFragment fragment = new VillagerListFragment();
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

    int index = 0;
    ImageView img1;
    ImageView img2;
    List<String> imgResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villager_viewpager, container, false);
        TextView listName = view.findViewById(R.id.listName);
        Button listButton = view.findViewById(R.id.villagerListButton);

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

        img1 = view.findViewById(R.id.slideshowImg1);
        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Animal_Crossing_Leaf.svg/150px-Animal_Crossing_Leaf.svg.png").into(img1);
        img2 = view.findViewById(R.id.slideshowImg2);
        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Animal_Crossing_Leaf.svg/150px-Animal_Crossing_Leaf.svg.png").into(img2);

        AppDatabase db = new AppDatabase(getContext());

        SwipeRefreshLayout layout = view.findViewById(R.id.swipeRefresh);
        layout.post(new Runnable() {
            @Override
            public void run() {
                imgResources = db.getVillagerImages(mParam2);
                //if there are no images
                if(imgResources.size() == 0) {
                    //if the list is either ALL villagers or a list that HAS villagers
                    if(mParam2.equals(ALL_VILLAGER_KEY) || !db.getAllVillagersForList(Integer.parseInt(mParam2)).isEmpty()) {
                        //refresh
                        Log.d("----", "trying to refresh");
                        layout.setRefreshing(true);
                    } else {
                        //otherwise it is an empty list, add a placeholder image
                        imgResources.add("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Animal_Crossing_Leaf.svg/150px-Animal_Crossing_Leaf.svg.png");
                    }
                }
            }
        });

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("------", "onRefresh: triggered");
                imgResources = db.getVillagerImages(mParam2);

                try {
                    loadImages(imgResources);
                }catch (Exception e) {
                    Snackbar.make(view, getString(R.string.list_img_error), Snackbar.LENGTH_LONG).show();
                }

                layout.setRefreshing(false);
            }
        });

        if(imgResources != null) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    img1.post(new Runnable() {
                        @Override
                        public void run() {
                            //load image on timer
                            loadImages(imgResources);
                        }
                    });
                }
            };

            //timer controls the speed of the timerTask
            Timer timer = new Timer();
            timer.schedule(timerTask, 4000, 4000);
        }

        return view;
    }

    private void loadImages(List<String> imgs) {
        checkIndex(imgs);
        Picasso.get().load(imgs.get(index)).into(img1);
        index++;
        checkIndex(imgs);
        Picasso.get().load(imgs.get(index)).into(img2);
        index++;
    }

    private void checkIndex(List<String> imgs) {
        if(index == imgs.size()) {
            index = 0;
        }
    }
}