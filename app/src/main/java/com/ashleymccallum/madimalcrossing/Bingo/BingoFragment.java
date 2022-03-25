package com.ashleymccallum.madimalcrossing.Bingo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashleymccallum.madimalcrossing.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BingoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BingoFragment.
     */
    public static BingoFragment newInstance(String param1, String param2) {
        BingoFragment fragment = new BingoFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bingo, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.bingoRecycler);
        //TODO: get 25 random villagers for the bingo card & prompt user to choose mode
//        BingoGame game = new BingoGame();
//        recyclerView.setAdapter(new BingoRecyclerViewAdapter(getContext(), game));
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        //TODO: if the game has been won, just do: game = new BingoGame() with the new selection and list of villagers??? (probably not, probably need a method for this)
        return view;
    }
}