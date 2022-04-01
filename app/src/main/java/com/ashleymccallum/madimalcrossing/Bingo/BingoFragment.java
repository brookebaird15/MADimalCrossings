package com.ashleymccallum.madimalcrossing.Bingo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoFragment extends Fragment implements OnGameWinListener {

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

    private BingoGame game;
    private TextView modeText;
    private KonfettiView konfettiView;
    private Party party;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bingo, container, false);
        AppDatabase db = new AppDatabase(getContext());
        game = BingoGame.getInstance();
        game.startNew(db.getBingoVillagers());
        RecyclerView recyclerView = view.findViewById(R.id.bingoRecycler);
        recyclerView.setAdapter(new BingoRecyclerViewAdapter(game, this));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        //configure confetti effect
        konfettiView = view.findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(50);
        party = new PartyFactory(emitterConfig) .spread(360)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(new Position.Relative(0.5, 0.3))
                .build();

        modeText = view.findViewById(R.id.bingoModeText);
        modeText.setText(game.currentMode);
        TextView selectModeText = view.findViewById(R.id.bingoModeSelector);
        selectModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentModeSelectDialog(game, modeText);
            }
        });

        //generate a new bingo card when the dice button is clicked
        ImageView bingoButton = view.findViewById(R.id.bingoCardButton);
        bingoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.startNew(db.getBingoVillagers());
                recyclerView.setAdapter(new BingoRecyclerViewAdapter(game, BingoFragment.this));
            }
        });

        //TODO: handle game win
        return view;
    }

    @Override
    public void onGameWin(BingoGame game) {
        Log.d("----------------", "tile click listener");
//
//        if(game.canChangeMode(game.currentMode)) {
//            game.startNew();
//        }

        //TODO: stop confetti animation in settings
        konfettiView.start(party);
        presentModeSelectDialog(game, modeText);

    }

    /**
     *
     * @param game the BingoGame
     * @param modeText the TextView displaying the current mode on screen
     * @author Ashley McCallum
     */
    private void presentModeSelectDialog(BingoGame game, TextView modeText) {
        AlertDialog.Builder modeDialog = new AlertDialog.Builder(getContext());
        modeDialog.setTitle(getString(R.string.bingo_selector));
        //add custom layout
        LayoutInflater alertInflater = modeDialog.create().getLayoutInflater();
        View alertView = alertInflater.inflate(R.layout.bingo_mode_select, null);
        modeDialog.setView(alertView);

        RadioGroup radioGroup = alertView.findViewById(R.id.bingoGroup);
        RadioButton rowBtn = alertView.findViewById(R.id.rowBtn);
        RadioButton xBtn = alertView.findViewById(R.id.xBtn);
        RadioButton blackoutBtn = alertView.findViewById(R.id.blackoutBtn);
        RadioButton ringBtn = alertView.findViewById(R.id.ringBtn);
        RadioButton cornerBtn = alertView.findViewById(R.id.cornerBtn);
        RadioButton[] buttons = new RadioButton[] {rowBtn, xBtn, blackoutBtn, ringBtn, cornerBtn};
        selectDefault(buttons, game.currentMode, game.isWon());

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

    /**
     * Sets the button representing the current game mode to checked on load
     * @param buttons an array containing the radiobuttons used
     * @param mode the String representation of the current game mode
     * @param gameWon a boolean representing if the current game has been won
     *                    if true, the player may not select the same mode they just won
     * @author Ashley McCallum
     */
    public void selectDefault(RadioButton[] buttons, String mode, boolean gameWon) {
        for (RadioButton button : buttons) {
            if (button.getTag().toString().toLowerCase().equals(mode.toLowerCase())) {
                if(gameWon) {
                    button.setEnabled(false);
                } else {
                    button.setChecked(true);
                }
                return;
            }
        }
    }
}