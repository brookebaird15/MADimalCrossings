package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.pojos.VillagerList.ADD_KEY;
import static com.ashleymccallum.madimalcrossing.pojos.VillagerList.EDIT_KEY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass..
 */
public class VillagerFragment extends Fragment {

    int viewPagerIndex;
    VillagerViewPagerAdapter adapter;
    ViewPager2 viewPager2;
    boolean fabClick = false;
    FloatingActionButton addList,editList,deleteList;
    Animation fab_open, fab_close, rotate_forward, rotate_backwards;
    AppDatabase db;
    TextView nameText;
    int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villager, container, false);
        //fab buttons
        addList = view.findViewById(R.id.addList);
        editList = view.findViewById(R.id.editList);
        deleteList = view.findViewById(R.id.deleteList);

        //animations
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backwards = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backwards);

        //database and viewpager
        db = new AppDatabase(getContext());
        viewPager2 = view.findViewById(R.id.villagerViewPager);
        adapter = new VillagerViewPagerAdapter(getActivity(), db.getAllLists(), getContext());
        viewPager2.setAdapter(adapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_load);
        int animToggle = Integer.parseInt(sharedPreferences.getString(getString(R.string.animations_key), "1"));
        if(animToggle == 1) {
            viewPager2.setPageTransformer(adapter);
            viewPager2.setAnimation(animation);
        }

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                viewPagerIndex = viewPager2.getCurrentItem();
                nameText = view.findViewById(R.id.listName);

                if(viewPagerIndex == 0) {
                    animateFAB();
                    deleteList.setVisibility(View.INVISIBLE);
                    editList.setVisibility(View.INVISIBLE);
                    editList.setClickable(false);
                    deleteList.setClickable(false);
                }
            }

        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPagerIndex == 0){
                    addEditListName(ADD_KEY, getContext(), null);
                    deleteList.hide();
                    editList.hide();
                }
                animateFAB();

            }
        });

        editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listName = nameText.getText().toString();
                VillagerList list = db.getList(listName);
                addEditListName(EDIT_KEY, getContext(), list);
            }
        });

        deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listName = nameText.getText().toString();
                VillagerList list = db.getList(listName);
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message, list.getName()))
                        .setPositiveButton(getString(R.string.continue_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteList(list.getId());
                                adapter.updateData(db.getAllLists());
                                viewPager2.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_label), null)
                        .show();
            }
        });
        db.close();

        return view;
    }

    /**
     * Creates and presents the dialog box to allow the user to name a list
     * @param key an int representing if the user is adding or editing
     * @param context application context
     * @param list if editing, the list being edited
     * @author Ashley McCallum
     */
    private void addEditListName(int key, Context context, VillagerList list) {
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = nameDialog.create().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_edit_list, null);
        nameDialog.setView(view);

        EditText nameInput = view.findViewById(R.id.listNameInput);

        if(key == EDIT_KEY) {
            nameDialog.setTitle(getString(R.string.edit_name));
            nameInput.setText(list.getName());
        } else if (key == ADD_KEY) {
            nameDialog.setTitle(getString(R.string.add_list));
        }

        nameDialog.setPositiveButton(getString(R.string.save_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(key == EDIT_KEY) {
                    list.setName(String.valueOf(nameInput.getText()));
                    db.updateList(list);
                    index = viewPagerIndex;
                } else if(key == ADD_KEY) {
                    db.createList(String.valueOf(nameInput.getText()));
                    index = db.getAllLists().size();
                }
                adapter.updateData(db.getAllLists());
                viewPager2.setAdapter(adapter);
                viewPager2.setCurrentItem(index);
            }
        });

        nameDialog.setNegativeButton(getString(R.string.cancel_label), null);
        nameDialog.show();
    }
    /**
     * Plays an animation depending on the state of the fab button
     * @Author Brooke Baird
     */
    public void animateFAB(){
        if(viewPagerIndex == 0) {
            if (fabClick) {
                addList.startAnimation(rotate_backwards);
                editList.startAnimation(fab_close);
                deleteList.startAnimation(fab_close);
                //set edit and delete to non-clickable
                editList.setClickable(false);
                deleteList.setClickable(false);
                fabClick = false;
            }
        }else {
            if (fabClick) {
                addList.startAnimation(rotate_backwards);
                editList.startAnimation(fab_close);
                deleteList.startAnimation(fab_close);
                //set edit and delete to non-clickable
                editList.setClickable(false);
                deleteList.setClickable(false);
                fabClick = false;
            } else {
                addList.startAnimation(rotate_forward);
                editList.startAnimation(fab_open);
                deleteList.startAnimation(fab_open);
                //set edit and delete to clickable
                editList.setClickable(true);
                deleteList.setClickable(true);
                fabClick = true;
            }
        }
    }

}