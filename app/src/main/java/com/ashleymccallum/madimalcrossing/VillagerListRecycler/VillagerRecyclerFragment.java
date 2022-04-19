package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.listID;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.viewModel;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.MainActivity;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.databinding.FragmentVillagerListBinding;
import com.ashleymccallum.madimalcrossing.databinding.VillagerListContentBinding;

import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Villagers. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link VillagerDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VillagerRecyclerFragment extends Fragment {

    private RecyclerView recyclerView;
    private VillagerListRecyclerAdapter adapter;
    private View.OnClickListener onClickListener;
    private FragmentVillagerListBinding binding;
    private HashMap<String, Set<String>> filters;
    private ArrayList<Villager> villagers;

    /**
     * Creates and presents the dialog box listing the filter options
     * @author Ashley McCallum
     */
    private void presentFilters() {
        AlertDialog.Builder filterDialog = new AlertDialog.Builder(getContext());
        filterDialog.setTitle(getString(R.string.filter_title));
        filterDialog.setMessage(getString(R.string.filter_message));
        //add custom layout
        LayoutInflater alertInflater = filterDialog.create().getLayoutInflater();
        View alertView = alertInflater.inflate(R.layout.filter_dialog, null);
        filterDialog.setView(alertView);

        TextView filterOp1 = alertView.findViewById(R.id.filterOp1);
        TextView filterOp2 = alertView.findViewById(R.id.filterOp2);
        TextView filterOp3 = alertView.findViewById(R.id.filterOp3);
        TextView filterOp4 = alertView.findViewById(R.id.filterOp4);
        TextView filterOp5 = alertView.findViewById(R.id.filterOp5);
        TextView[] filterOptions = new TextView[] {filterOp1, filterOp2, filterOp3, filterOp4, filterOp5};
        AppDatabase db = new AppDatabase(getContext());

        Button resetBtn = alertView.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on click, reset the filters and set the villagers back to all in the viewModel
                filters = new HashMap<>();
                //get all the villagers from the view model
                villagers = viewModel.getVillagers();
                //set the filtered villagers in the view model back to null
                viewModel.setFilteredVillagers(null);
            }
        });

        for(TextView filter : filterOptions) {
            //for each textview add an on click listener
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentFilterOptions(filter.getText().toString());
                }
            });
        }

        filterDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //if there are filters in the hashmap
                if(!filters.isEmpty()) {
                    //for each key in the hashmap
                    for(String key : filters.keySet()) {
                        //if the key has no values, remove the key
                        if(filters.get(key).size() == 0) {
                            filters.remove(key);
                        }
                    }
                    //set the villagers to be the return value from the filtering method
                    villagers = new ArrayList<>(db.getFilteredVillagers(filters));
                    //retain only the villagers that were already present in the current list
                    villagers.retainAll(viewModel.getVillagers());
                    //set the filtered villagers in the view model
                    viewModel.setFilteredVillagers(villagers);
                }
                //refresh adapter and recyclerview
                adapter = new VillagerListRecyclerAdapter(villagers, onClickListener, getContext());
                recyclerView.setAdapter(adapter);
            }
        });
        filterDialog.setNegativeButton(getString(R.string.cancel_label), null);
        filterDialog.show();
        db.close();
    }

    /**
     * Creates and presents a dialog box with specific options for a chosen filter in a ListView
     * @param filter the property that the user wants to filter by (species, hobby, etc)
     * @author Ashley McCallum
     */
    private void presentFilterOptions(String filter) {
        AppDatabase db = new AppDatabase(getContext());
        List<String> options = new ArrayList<>(db.getVillagerProperty(filter));

        AlertDialog.Builder optionDialog = new AlertDialog.Builder(getContext());
        optionDialog.setTitle(getString(R.string.selection_title, filter));
        optionDialog.setMessage(getString(R.string.selection_message, filter.toLowerCase()));
        //add custom layout
        LayoutInflater alertInflater = optionDialog.create().getLayoutInflater();
        View alertView = alertInflater.inflate(R.layout.add_listview, null);
        optionDialog.setView(alertView);

        ListView optionList = alertView.findViewById(R.id.listList);
        FilterListViewAdapter adapter = new FilterListViewAdapter(getContext(), options, filters.get(filter));
        optionList.setAdapter(adapter);

        optionDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filters.put(filter, adapter.getSelection());
            }
        });
        optionDialog.setNegativeButton(getString(R.string.cancel_label), null);
        optionDialog.show();
        db.close();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVillagerListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.villagerList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        filters = new HashMap<>();

        Button filterBtn = binding.filterBtn;
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentFilters();
            }
        });

        Button emptyBtn = binding.emptyBtn;
        if(listID.equals(ALL_VILLAGER_KEY)) {
            emptyBtn.setVisibility(View.INVISIBLE);
        }
        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.empty_title))
                        .setMessage(getString(R.string.empty_message))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppDatabase db = new AppDatabase(getContext());
                                db.emptyList(Integer.parseInt(listID));
                                db.close();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                try {
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_label), null)
                        .show();
            }
        });

        //find the view used for the tablet layout
        View itemDetailFragmentContainer = view.findViewById(R.id.villager_detail_nav_container);
        onClickListener = itemView -> {
            Bundle arguments = new Bundle();
            arguments.putString(VillagerDetailFragment.ARG_ITEM_ID, itemView.getTag().toString());
            //if the tablet layout is not null, navigate to that, else navigate to the detail fragment for phones
            if (itemDetailFragmentContainer != null) {
                Navigation.findNavController(itemDetailFragmentContainer).navigate(R.id.fragment_villager_detail, arguments);
            } else {
                Navigation.findNavController(itemView).navigate(R.id.show_villager_detail, arguments);
            }
        };

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_load);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int animToggle = Integer.parseInt(sharedPreferences.getString(getString(R.string.animations_key), "1"));
        //only play animation on phone layout
        if(animToggle == 1 && itemDetailFragmentContainer == null) {
            recyclerView.setAnimation(animation);
        }

        //if the viewmodel has filtered villagers
        if(viewModel.getFilteredVillagers() != null) {
            //load the filtered villagers
            villagers = viewModel.getFilteredVillagers();
        } else {
            //otherwise load all the villagers
            villagers = viewModel.getVillagers();
        }

        adapter = new VillagerListRecyclerAdapter(villagers, onClickListener, getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class VillagerListRecyclerAdapter extends RecyclerView.Adapter<VillagerListRecyclerAdapter.ViewHolder> {

        private final ArrayList<Villager> villagers;
        private final View.OnClickListener listener;
        private final Context context;

        VillagerListRecyclerAdapter(ArrayList<Villager> villagers, View.OnClickListener onClickListener, Context context) {
            this.villagers = villagers;
            listener = onClickListener;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            VillagerListContentBinding binding = VillagerListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Villager villager = villagers.get(position);
            holder.villagerName.setText(villager.getName());
            Picasso.get().load(villager.getIconURI()).into(holder.villagerImg);
            holder.villagerImg.setContentDescription(getString(R.string.villager_desc, villager.getGender(), villager.getSpecies()));
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(listener);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(!listID.equals(ALL_VILLAGER_KEY)) {
                        new AlertDialog.Builder(context)
                                .setTitle(context.getString(R.string.remove_villager))
                                .setMessage(context.getString(R.string.delete_villager, villager.getName()))
                                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AppDatabase db = new AppDatabase(context);
                                        db.removeVillagerFromList(villager.getId(), Integer.parseInt(listID));
                                        viewModel.removeVillager(villager);
                                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                                        db.close();
                                        if(villagers.isEmpty()) {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            try {
                                                startActivity(intent);
                                            } catch (ActivityNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton(context.getString(R.string.cancel_label), null)
                                .show();
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return villagers.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            protected ImageView villagerImg;
            protected TextView villagerName;

            ViewHolder(VillagerListContentBinding binding) {
                super(binding.getRoot());
                villagerImg = binding.villagerListImg;
                villagerName = binding.villagerListName;
            }
        }
    }
}