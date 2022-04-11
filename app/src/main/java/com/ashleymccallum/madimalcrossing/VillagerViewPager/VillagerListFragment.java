package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.LIST_ID;
import static com.ashleymccallum.madimalcrossing.pojos.VillagerList.EDIT_KEY;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;
import com.google.android.material.snackbar.Snackbar;
import com.royrodriguez.transitionbutton.TransitionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VillagerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagerListFragment extends Fragment {
    private TransitionButton listButton;
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
        listButton = view.findViewById(R.id.villagerListButton);

        if(mParam1 != null && mParam2 != null) {
            listName.setText(mParam1);
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listButton.startAnimation();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccessful = true;

                            if (isSuccessful) {
                                listButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                    @Override
                                    public void onAnimationStopEnd() {
                                        Intent i = new Intent(getActivity(), VillagerDetailHostActivity.class);
                                        //TODO: button no longer loads correct list on click, always loads full villager list
                                        i.putExtra(LIST_ID, mParam2);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        try {
                                            startActivity(i);
                                        } catch (ActivityNotFoundException e) {
                                            Snackbar.make(view, getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                                        }
                                        listButton.clearAnimation();
                                        listButton.setText(R.string.goto_villagers);


                                    }
                                });
                            } else {
                                listButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);

                            }
                        }
                    }, 2000);
                }
            });
        }

        return view;
    }
}