package com.ashleymccallum.madimalcrossing;

import static com.ashleymccallum.madimalcrossing.VillagerRecyclerFragment.villagers;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.ashleymccallum.madimalcrossing.placeholder.PlaceholderContent;
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

//    private final View.OnDragListener dragListener = (v, event) -> {
//        if (event.getAction() == DragEvent.ACTION_DROP) {
////            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
////            villager = VillagerRecyclerFragment.villagers.get()
////            mItem = PlaceholderContent.ITEM_MAP.get(clipDataItem.getText().toString());
////            updateContent();
//        }
//        return true;
//    };
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
            int villagerIndex;
            if(getArguments().getString(ARG_ITEM_ID).equals("")) {
                villager = villagers.get(0);
            } else {
                villager = villagers.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
//                villagerIndex = ;
            }


            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

//            mItem = PlaceholderContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVillagerDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        ImageView villagerImg = rootView.findViewById(R.id.villagerDetailImg);
        Picasso.get().load(villager.getImgURI()).into(villagerImg);


        // Show the placeholder content as text in a TextView & in the toolbar if available.
//        updateContent();
//        rootView.setOnDragListener(dragListener);
        return rootView;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

//    private void updateContent() {
//        if (mItem != null) {
//            mTextView.setText(mItem.details);
//            if (mToolbarLayout != null) {
//                mToolbarLayout.setTitle(mItem.content);
//            }
//        }
//    }
}