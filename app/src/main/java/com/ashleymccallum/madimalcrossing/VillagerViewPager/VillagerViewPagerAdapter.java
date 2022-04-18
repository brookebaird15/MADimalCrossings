package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import java.util.ArrayList;

public class VillagerViewPagerAdapter extends FragmentStateAdapter implements ViewPager2.PageTransformer {
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
            return VillagerListFragment.newInstance(context.getString(R.string.all_villagers), ALL_VILLAGER_KEY, position);
        }
        //get the list at the position -1 because the first position is always "all villagers"
        VillagerList list = lists.get(position - 1);
        return VillagerListFragment.newInstance(list.getName(), String.valueOf(list.getId()), position);
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

    public void updateData(ArrayList<VillagerList> lists) {
        this.lists = lists;
        this.notifyDataSetChanged();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        float MIN_SCALE = 0.75f;

        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0f);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            page.setAlpha(1f);
            page.setTranslationX(0f);
            page.setTranslationZ(0f);
            page.setScaleX(1f);
            page.setScaleY(1f);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            page.setAlpha(1 - position);

            // Counteract the default slide transition
            page.setTranslationX(pageWidth * -position);
            // Move it behind the left page
            page.setTranslationZ(-1f);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0f);
        }
    }
}
