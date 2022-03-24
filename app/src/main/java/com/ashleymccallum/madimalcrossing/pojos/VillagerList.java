package com.ashleymccallum.madimalcrossing.pojos;

import java.util.ArrayList;

public class VillagerList {

    private int id;
    private String name;
    private ArrayList<Villager> villagers;

    public VillagerList(int id, String name, ArrayList<Villager> villagers) {
        this.id = id;
        this.name = name;
        this.villagers = villagers;
    }

    public VillagerList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }

    public void setVillagers(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }
}
