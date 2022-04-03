package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import java.util.ArrayList;

public class VillagerViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<VillagerList> lists;

    //TODO: populate view pager with lists from db + all villager list
    public VillagerViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<VillagerList> lists) {
        super(fragmentActivity);
        this.lists = lists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        VillagerList list = lists.get(position);
        //TODO: make first page default all villagers
        //TODO: use bundle to pass list ID to activity for viewing list
        return VillagerListFragment.newInstance(list.getName(), "");
//        switch (position) {
//            case 0: return VillagerListFragment.newInstance("All villagers", "");
//            case 1: return VillagerListFragment.newInstance("My villagers", "");
//            case 2: return VillagerListFragment.newInstance("My dreamies", "");
//            default: return VillagerListFragment.newInstance("Issue Loading", "");
//        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
