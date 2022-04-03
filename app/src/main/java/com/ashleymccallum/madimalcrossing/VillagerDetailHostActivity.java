package com.ashleymccallum.madimalcrossing;

import android.os.Bundle;

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

    private AppDatabase db;
    private ArrayList<Villager> villagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityVillagerDetailBinding binding = ActivityVillagerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: attach villager list to villagerViewModel and use to populate recyclers
        //TODO: something like: if ARG = 'all' -> db.getAllVillagers, otherwise db.getVillagersForList(ARG value)

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