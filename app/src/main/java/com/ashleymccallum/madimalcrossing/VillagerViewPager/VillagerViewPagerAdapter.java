package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import java.util.ArrayList;

public class VillagerViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<VillagerList> lists;
    Context context;

    public VillagerViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<VillagerList> lists, Context context) {
        super(fragmentActivity);
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            //first page is always "all villagers"
            return VillagerListFragment.newInstance(context.getString(R.string.all_villagers), ALL_VILLAGER_KEY);
        }
        //get the list at the position -1 because the first position is always "all villagers"
        VillagerList list = lists.get(position - 1);
        return VillagerListFragment.newInstance(list.getName(), String.valueOf(list.getId()));
    }

    @Override
    public int getItemCount() {
        //if there are lists in the db
        if(lists.size() > 0) {
            //return lists + 1 to account for default "all villagers" list
            return lists.size() + 1;
        }
        //if db empty return 1 for "all villagers" list
        return 1;
    }
}
