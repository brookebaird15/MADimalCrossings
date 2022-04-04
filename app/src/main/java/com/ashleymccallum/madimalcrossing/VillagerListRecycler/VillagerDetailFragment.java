package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import static com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerDetailHostActivity.viewModel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.VillagerListRecycler.VillagerRecyclerFragment;
import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.ashleymccallum.madimalcrossing.databinding.FragmentVillagerDetailBinding;
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

        //TODO: this loads the first villager in the list by default, but what if the list is empty?
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            if(getArguments().getString(ARG_ITEM_ID).equals("")) {
                villager = viewModel.getVillagers().get(0);
            } else {
                villager = viewModel.getVillagers().get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVillagerDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        ImageView villagerImg = rootView.findViewById(R.id.villagerDetailImg);
        Picasso.get().load(villager.getImgURI()).into(villagerImg);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}