package com.ashleymccallum.madimalcrossing.pojos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;

import java.util.ArrayList;

/**
 * VillagerList Class
 * @author Ashley McCallum
 */
public class VillagerList {

    public static final int EDIT_KEY = 1;
    public static final int ADD_KEY = 2;
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
