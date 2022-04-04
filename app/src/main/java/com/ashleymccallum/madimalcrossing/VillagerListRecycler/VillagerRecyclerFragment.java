package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.viewModel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.databinding.FragmentVillagerListBinding;
import com.ashleymccallum.madimalcrossing.databinding.VillagerListContentBinding;

import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        recyclerView.setAdapter(new VillagerListRecyclerAdapter(viewModel.getVillagers(), onClickListener));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class VillagerListRecyclerAdapter extends RecyclerView.Adapter<VillagerListRecyclerAdapter.ViewHolder> {

        private final ArrayList<Villager> villagers;
        private final View.OnClickListener listener;

        VillagerListRecyclerAdapter(ArrayList<Villager> villagers, View.OnClickListener onClickListener) {
            this.villagers = villagers;
            listener = onClickListener;
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
        }

        @Override
        public int getItemCount() {
            return villagers.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
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