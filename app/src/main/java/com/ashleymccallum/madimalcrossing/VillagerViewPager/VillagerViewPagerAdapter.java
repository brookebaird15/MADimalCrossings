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

    public void updateData(ArrayList<VillagerList> lists) {
        this.lists = lists;
        this.notifyDataSetChanged();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        //TODO: add vp2 transformation
        //load animations
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        int animToggle = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.animations_key), "1"));
//        Animation animRtoL = AnimationUtils.loadAnimation(context, R.anim.slideshow_right_left);
//        Animation animLtoR = AnimationUtils.loadAnimation(context, R.anim.slideshow_left_right);
//
//        ImageView img1 = page.findViewById(R.id.slideshowImg1);
//        ImageView img2 = page.findViewById(R.id.slideshowImg2);
//
//        //if 1 animations are on, if 0 animations are off
//        if(animToggle == 1) {
//            img1.setAnimation(animRtoL);
//            img2.setAnimation(animLtoR);
//        }
    }
}
