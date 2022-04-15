package com.ashleymccallum.madimalcrossing.VillagerViewPager;

import static com.ashleymccallum.madimalcrossing.pojos.VillagerList.ADD_KEY;
import static com.ashleymccallum.madimalcrossing.pojos.VillagerList.EDIT_KEY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import nl.dionsegijn.konfetti.core.Party;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VillagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagerFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public VillagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VillagerFragment.
     */
    public static VillagerFragment newInstance(String param1, String param2) {
        VillagerFragment fragment = new VillagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                viewPagerIndex = viewPager2.getCurrentItem();
                nameText = view.findViewById(R.id.listName);

                if(viewPagerIndex == 0) {
                    editList.setVisibility(View.INVISIBLE);
                    deleteList.setVisibility(View.INVISIBLE);
                } else {
                    editList.setVisibility(View.VISIBLE);
                    deleteList.setVisibility(View.VISIBLE);
                }
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditListName(ADD_KEY, getContext(), null);
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
        if(fabClick){
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