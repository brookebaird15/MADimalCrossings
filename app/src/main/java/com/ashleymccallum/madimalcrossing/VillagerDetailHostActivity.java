package com.ashleymccallum.madimalcrossing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ashleymccallum.madimalcrossing.databinding.ActivityVillagerDetailBinding;
import com.ashleymccallum.madimalcrossing.pojos.Villager;

import java.util.ArrayList;

public class VillagerDetailHostActivity extends AppCompatActivity {

    public static final String LIST_ID = "list_id";
    public static final String ALL_VILLAGER_KEY = "all";
    public static VillagerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityVillagerDetailBinding binding = ActivityVillagerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db = new AppDatabase(this);
        Intent intent = getIntent();
        String listID = intent.getStringExtra(LIST_ID);

        //if the key is "all" get all the villagers from the db
        if(listID.equals(ALL_VILLAGER_KEY)) {
            viewModel = new VillagerViewModel(db.getAllVillagers());
        } else {
            //otherwise, get the villagers for the list ID passed
            viewModel = new VillagerViewModel(db.getAllVillagersForList(Integer.parseInt(listID)));
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_villager_detail);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.
                Builder(navController.getGraph())
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_villager_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}