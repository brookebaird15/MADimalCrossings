package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.viewModel;
import static com.ashleymccallum.madimalcrossing.pojos.Villager.FEMALE;
import static com.ashleymccallum.madimalcrossing.pojos.Villager.MALE;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.MainActivity;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerRecyclerFragment;
import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.ashleymccallum.madimalcrossing.databinding.FragmentVillagerDetailBinding;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Villager detail screen.
 * This fragment is either contained in a {@link VillagerRecyclerFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class VillagerDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Villager villager;
    private FragmentVillagerDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VillagerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //if there is no selected item
            if(getArguments().getString(ARG_ITEM_ID).equals("")) {
                //if there are items that can be selected
                if(viewModel.getVillagers().size() > 0) {
                    //select the first item by default
                    villager = viewModel.getVillagers().get(0);
                } else {
                    //otherwise, notify user list is empty and navigate back to main activity
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.empty_list_title))
                            .setMessage(getString(R.string.empty_list_message))
                            .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }
            } else {
                //otherwise, display the selected item
                villager = viewModel.getVillagers().get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVillagerDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        if(villager != null) {
            //TODO: use int spotted property
            TextView name = rootView.findViewById(R.id.villagerName);
            TextView personality = rootView.findViewById(R.id.villagerPersonality);
            TextView species = rootView.findViewById(R.id.villagerSpecies);
            TextView hobby = rootView.findViewById(R.id.villagerHobby);
            TextView catchphrase = rootView.findViewById(R.id.villagerCatchphrase);
            TextView day = rootView.findViewById(R.id.villagerDay);
            TextView month = rootView.findViewById(R.id.villagerMonth);
            TextView sign = rootView.findViewById(R.id.villagerSign);

            name.setText(villager.getName());
            personality.setText(villager.getPersonality());
            species.setText(villager.getSpecies());
            hobby.setText(villager.getHobby());
            catchphrase.setText(getString(R.string.catchphrase, villager.getCatchphrase()));
            day.setText(villager.getBirthDay());
            month.setText(villager.getBirthMonth());
            sign.setText(villager.getSign());

            ImageView gender = rootView.findViewById(R.id.villagerGender);
            if (villager.getGender().equals(MALE)) {
                gender.setImageResource(R.drawable.ic_baseline_male_24);
                gender.setContentDescription(getString(R.string.gender_m));
            } else if (villager.getGender().equals(FEMALE)) {
                gender.setImageResource(R.drawable.ic_baseline_female_24);
                gender.setContentDescription(getString(R.string.gender_f));
            }

            ImageView villagerImg = rootView.findViewById(R.id.villagerDetailImg);
            Picasso.get().load(villager.getImgURI()).into(villagerImg);
            ImageView houseExt = rootView.findViewById(R.id.villagerHouse);
            Picasso.get().load(villager.getHouseExtURI()).into(houseExt);

            ImageView siteBtn = rootView.findViewById(R.id.villagerSiteBtn);
            siteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(villager.getUrl()));
                    try {
                        startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        Snackbar.make(rootView, getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            FloatingActionButton fab = rootView.findViewById(R.id.villagerFAB);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToList();
                }
            });
            return rootView;
        } else {
            return null;
        }
    }

    /**
     * Creates and displays the dialog box to allow a user to add a Villager to their lists
     * @author Ashley McCallum
     */
    private void addToList() {
        AppDatabase db = new AppDatabase(getContext());
        AlertDialog.Builder addDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = addDialog.create().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_listview, null);
        addDialog.setView(view);

        addDialog.setTitle(getString(R.string.select_list));
        ListView listView = view.findViewById(R.id.listList);
        AddingListViewAdapter adapter = new AddingListViewAdapter(getContext(), db.getAllLists());
        listView.setAdapter(adapter);

        addDialog.setPositiveButton(getString(R.string.save_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(VillagerList list : adapter.getAddingList()) {
                    db.createOneVillagerListRelation(list, villager);
                }
            }
        });

        addDialog.setNegativeButton(getString(R.string.cancel_label), null);
        addDialog.show();
    }
}