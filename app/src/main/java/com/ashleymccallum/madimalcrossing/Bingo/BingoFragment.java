package com.ashleymccallum.madimalcrossing.Bingo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoFragment extends Fragment implements OnTileClickListener{

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
        //TODO: get 25 random villagers for the bingo card & prompt user to choose mode
        BingoGame game = BingoGame.getInstance();
//        game.startNew();
        TextView modeText = view.findViewById(R.id.bingoModeText);
        TextView selectModeText = view.findViewById(R.id.bingoModeSelector);
        selectModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: select game mode
                AlertDialog.Builder modeDialog = new AlertDialog.Builder(getContext());
                modeDialog.setTitle(getString(R.string.bingo_selector));
                //add custom layout
                LayoutInflater alertInflater = modeDialog.create().getLayoutInflater();
                View alertView = alertInflater.inflate(R.layout.bingo_mode_select, null);
                modeDialog.setView(alertView);

                RadioGroup radioGroup = view.findViewById(R.id.bingoGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        //get selected radio button
                        RadioButton radioButton = radioGroup.findViewById(i);
                    }
                });
                modeDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get the ID of the selected button
                        int selectedID = radioGroup.getCheckedRadioButtonId();
                        //get the current mode the game is in
                        String modeSelection = game.currentMode;
                        //set the text of the display and the mode selection accordingly
                        switch (selectedID) {
                            case R.id.rowBtn:
                                modeText.setText(getString(R.string.bingo_row));
                                modeSelection = BingoGame.BINGO_ROW_KEY;
                                break;
                            case R.id.xBtn:
                                modeText.setText(getString(R.string.bingo_x));
                                modeSelection = BingoGame.BINGO_X_KEY;
                                break;
                            case R.id.ringBtn:
                                modeText.setText(getString(R.string.bingo_ring));
                                modeSelection = BingoGame.BINGO_RING_KEY;
                                break;
                            case R.id.cornerBtn:
                                modeText.setText(getString(R.string.bingo_corners));
                                modeSelection = BingoGame.BINGO_CORNERS_KEY;
                                break;
                            case R.id.blackoutBtn:
                                modeText.setText(getString(R.string.bingo_blackout));
                                modeSelection = BingoGame.BINGO_BLACKOUT_KEY;
                                break;
                        }
//                        if(!game.canChangeMode(modeSelection)) {
////                            Snackbar.make(view, "Cannot c")
//                        }
                    }
                });
                modeDialog.setNegativeButton("Cancel", null);
                modeDialog.show();
            }
        });
        ImageView bingoButton = view.findViewById(R.id.bingoCardButton);
        bingoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                game.startNew();
                //TODO: randomise bingo card on click
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.bingoRecycler);
        recyclerView.setAdapter(new BingoRecyclerViewAdapter(getContext(), game, this));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        //TODO: handle game win
        return view;
    }

    @Override
    public void onTileClick(BingoGame game) {
        //TODO: prompt for new game or continue game
        //TODO: prompt for select new mode
        //TODO: check if methods function as intended
//        if(game.canChangeMode()) {
//            game.startNew();
//        }

    }

}