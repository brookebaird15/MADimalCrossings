package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import androidx.lifecycle.ViewModel;

import com.ashleymccallum.madimalcrossing.pojos.Villager;

import java.util.ArrayList;

/**
 * VillagerViewModel class
 * Contains the ArrayList of Villagers to be loaded
 * @author Ashley McCallum
 */
public class VillagerViewModel extends ViewModel {
    private final ArrayList<Villager> villagers;

    public VillagerViewModel(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }
}