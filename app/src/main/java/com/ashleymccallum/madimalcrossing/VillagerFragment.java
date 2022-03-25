package com.ashleymccallum.madimalcrossing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashleymccallum.madimalcrossing.VillagerViewPager.VillagerViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VillagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagerFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public VillagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VillagerFragment.
     */
    public static VillagerFragment newInstance(String param1, String param2) {
        VillagerFragment fragment = new VillagerFragment();
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
        View view = inflater.inflate(R.layout.fragment_villager, container, false);
        ViewPager2 viewPager2 = view.findViewById(R.id.villagerViewPager);
        viewPager2.setAdapter(new VillagerViewPagerAdapter(getActivity()));
        return view;
    }
}