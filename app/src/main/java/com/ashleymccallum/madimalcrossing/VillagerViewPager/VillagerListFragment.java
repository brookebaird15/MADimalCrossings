package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.LIST_ID;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.listID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity;
import com.google.android.material.snackbar.Snackbar;

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

        AppDatabase db = new AppDatabase(getContext());
        List<String> imgResources = db.getVillagerImages(mParam2);
        if(imgResources.isEmpty()) {
            //TODO: refresh images after villagers have been added to the list
            //if there are no villagers for that list, use a default image
            imgResources.add("https://upload.wikimedia.org/wikipedia/commons/5/58/Animal_Crossing_Leaf.png");
        }
        //add a null value to the end of the list so looping can occur
        imgResources.add(null);

        ViewPager2 slideshow = view.findViewById(R.id.imgSlideshow);
        SlideshowViewPagerAdapter slideshowAdapter = new SlideshowViewPagerAdapter(getActivity(), imgResources);
        slideshow.setAdapter(slideshowAdapter);
        slideshow.setPageTransformer(slideshowAdapter);
        slideshow.setUserInputEnabled(false);

        //imageCount is the total number of images
        int imageCount = imgResources.size();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                slideshow.post(new Runnable() {
                    @Override
                    public void run() {
                        //move to the next slideshow image
                        slideshow.setCurrentItem((slideshow.getCurrentItem() + 1), false);
                        //if the images are at the end of the list
                        if (slideshow.getCurrentItem() == imageCount - 1) {
                            //return to image at 0
                            slideshow.setCurrentItem(0, false);
                        }
                    }
                });
            }
        };

        //timer controls the speed of the timerTask
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);


        return view;
    }
}