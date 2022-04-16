package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ashleymccallum.madimalcrossing.R;

import java.util.List;

public class SlideshowViewPagerAdapter extends FragmentStateAdapter implements ViewPager2.PageTransformer {
    List<String> imgURIs;
    Context context;

    public SlideshowViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> imgURIs, Context context) {
        super(fragmentActivity);
        this.imgURIs = imgURIs;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String imgURI;
        if(position != imgURIs.size() - 1) {
            imgURI = imgURIs.get(position);
        } else {
            //if the position is at the last image, use the uri for the item at 0
            imgURI = imgURIs.get(0);
        }

        return SlideshowFragment.newInstance(imgURI, null);
    }

    @Override
    public int getItemCount() {
        return imgURIs.size();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        //load animation preference and add animation
        Animation animation = (Animation) AnimationUtils.loadAnimation(page.getContext(), R.anim.slideshow_anim);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(page.getContext());
        int animToggle = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.animations_key), "1"));

        //if 1 animations are on, if 0 animations are off
        if(animToggle == 1) {
            page.setAnimation(animation);
        }
    }
}
