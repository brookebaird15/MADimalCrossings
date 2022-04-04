package com.ashleymccallum.madimalcrossing.pojos;

import java.util.ArrayList;

/**
 * VillagerList Class
 * @author Ashley McCallum
 */
public class VillagerList {

    private final int id;
    private String name;

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
}
