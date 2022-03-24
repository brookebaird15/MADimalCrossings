package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VillagerViewPagerAdapter extends FragmentStateAdapter {

    public VillagerViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return VillagerListFragment.newInstance("All villagers", "");
            case 1: return VillagerListFragment.newInstance("My villagers", "");
            case 2: return VillagerListFragment.newInstance("My dreamies", "");
            default: return VillagerListFragment.newInstance("Issue Loading", "");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
