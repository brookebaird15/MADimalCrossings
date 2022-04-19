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
    private ArrayList<Villager> filteredVillagers;

    public VillagerViewModel(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }

    public void setFilteredVillagers(ArrayList<Villager> villagers) {
        this.filteredVillagers = villagers;
    }

    public ArrayList<Villager> getFilteredVillagers() {
        return filteredVillagers;
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }

    public void removeVillager(Villager villager) {
        villagers.remove(villager);
        if(filteredVillagers != null) {
            filteredVillagers.remove(villager);
        }
    }
}
