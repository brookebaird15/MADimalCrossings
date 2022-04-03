package com.ashleymccallum.madimalcrossing;

import androidx.lifecycle.ViewModel;

import com.ashleymccallum.madimalcrossing.pojos.Villager;

import java.util.ArrayList;

public class VillagerViewModel extends ViewModel {
    private final ArrayList<Villager> villagers;

    public VillagerViewModel(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }
}
