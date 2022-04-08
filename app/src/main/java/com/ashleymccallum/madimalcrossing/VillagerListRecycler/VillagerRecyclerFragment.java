package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.ALL_VILLAGER_KEY;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.listID;
import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.viewModel;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private FragmentVillagerListBinding binding;
    private HashMap<String, List<String>> filters;

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

        for(TextView filter : filterOptions) {
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentFilterOptions(filter.getText().toString());
                }
            });
        }
        //TODO: add textview to list selected filters? -> get from hashmap

        filterDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: apply filter and reload recyclerview
                Log.d("---------------", "selected filters " + filters.toString());
            }
        });
        filterDialog.setNegativeButton(getString(R.string.cancel_label), null);
        filterDialog.show();
    }

    private void presentFilterOptions(String filter) {
        AppDatabase db = new AppDatabase(getContext());
        List<String> options = new ArrayList<>(db.getVillagerProperty(filter));
        Log.d("-------------", "presentFilterOptions: " + options.toString());

        AlertDialog.Builder optionDialog = new AlertDialog.Builder(getContext());
        optionDialog.setTitle(getString(R.string.selection_title, filter));
        optionDialog.setMessage(getString(R.string.selection_message, filter));
        //add custom layout
        LayoutInflater alertInflater = optionDialog.create().getLayoutInflater();
        View alertView = alertInflater.inflate(R.layout.add_listview, null);
        optionDialog.setView(alertView);

        ListView optionList = alertView.findViewById(R.id.listList);
        FilterListViewAdapter adapter = new FilterListViewAdapter(getContext(), options);
        optionList.setAdapter(adapter);

        optionDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filters.put(filter, adapter.getSelection());

                //re-show previous dialog box
//                presentFilters();
            }
        });
        optionDialog.setNegativeButton(getString(R.string.cancel_label), null);
        optionDialog.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVillagerListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.villagerList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Button filterBtn = binding.filterBtn;
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters = new HashMap<>();
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
        View.OnClickListener onClickListener = itemView -> {
            Bundle arguments = new Bundle();
            arguments.putString(VillagerDetailFragment.ARG_ITEM_ID, itemView.getTag().toString());
            //if the tablet layout is not null, navigate to that, else navigate to the detail fragment for phones
            if (itemDetailFragmentContainer != null) {
                Navigation.findNavController(itemDetailFragmentContainer).navigate(R.id.fragment_villager_detail, arguments);
            } else {
                Navigation.findNavController(itemView).navigate(R.id.show_villager_detail, arguments);
            }
        };

        recyclerView.setAdapter(new VillagerListRecyclerAdapter(viewModel.getVillagers(), onClickListener, getContext()));
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
                                        villagers.remove(holder.getAbsoluteAdapterPosition());
                                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                                        db.close();
                                        if(villagers.isEmpty()) {
                                            //TODO: need some kind of message to tell user why theyre moving back?
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
                    Log.d("-----", "onLongClick: ");
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