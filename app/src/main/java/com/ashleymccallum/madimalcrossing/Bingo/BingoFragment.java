package com.ashleymccallum.madimalcrossing.Bingo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TextView selectModeText;
    private KonfettiView konfettiView;
    private Party party;
    private RecyclerView recyclerView;
    private BingoRecyclerViewAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bingo, container, false);
        db = new AppDatabase(getContext());
        game = BingoGame.getInstance();
        recyclerView = view.findViewById(R.id.bingoRecycler);
        adapter = new BingoRecyclerViewAdapter(game, this, db);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        //if there are no tiles in the database start a new game, otherwise load the tiles
        if(db.getTiles().isEmpty()) {
            startNewGame();
        } else {
            game.continueGame(db.getTiles());
            recyclerView.setAdapter(adapter);
        }

        //configure confetti effect
        konfettiView = view.findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(3, TimeUnit.SECONDS).perSecond(50);
        party = new PartyFactory(emitterConfig) .spread(360)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(new Position.Relative(0.5, 0.3))
                .build();

        selectModeText = view.findViewById(R.id.bingoModeSelector);
        selectModeText.setText(getString(R.string.bingo_selector, game.currentMode));
        selectModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentModeSelectDialog(game, selectModeText);
            }
        });

        //generate a new bingo card when the dice button is clicked
        Button bingoButton = view.findViewById(R.id.bingoCardButton);
        bingoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
        return view;
    }

    @Override
    public void onGameWin(BingoGame game) {

        //TODO: stop confetti animation & delay in settings
        konfettiView.start(party);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presentGameOver(game);
            }
        }, 3000);
    }

    /**
     * Starts a new game
     * Gets 24 new villagers from the db and generates new tiles
     * Resets the adapter
     * Inserts the new tiles into the db
     */
    private void startNewGame() {
        db.removeAllTiles();
        game.startNew(db.getBingoVillagers());
        recyclerView.setAdapter(adapter);
        db.insertTiles(game.tiles);
    }

    /**
     * Creates and presents the game over dialog box
     * @param game the BingoGame
     * @author Ashley McCallum
     */
    private void presentGameOver(BingoGame game) {
        AlertDialog.Builder gameOverDialog = new AlertDialog.Builder(getContext());
        gameOverDialog.setTitle(getString(R.string.game_over));

        if(game.canChangeMode(game.currentMode)) {
            gameOverDialog.setPositiveButton(getString(R.string.continue_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    presentModeSelectDialog(game, selectModeText);
                }
            });
        }

        gameOverDialog.setNeutralButton(getString(R.string.new_game_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startNewGame();
            }
        });
        gameOverDialog.show();
    }

    /**
     * Creates and presents the dialog box to allow user to change the game mode
     * @param game the BingoGame
     * @param modeText the TextView displaying the current mode on screen
     * @author Ashley McCallum
     */
    private void presentModeSelectDialog(BingoGame game, TextView modeText) {
        AlertDialog.Builder modeDialog = new AlertDialog.Builder(getContext());
        modeDialog.setTitle(getString(R.string.bingo_selector, ""));
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
        modeDialog.setPositiveButton(getString(R.string.save_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //get the ID of the selected button
                int selectedID = radioGroup.getCheckedRadioButtonId();
                //get the current mode the game is in
                String modeSelection = game.currentMode;
                //set the text of the display and the mode selection accordingly
                switch (selectedID) {
                    case R.id.rowBtn:
                        modeSelection = getString(R.string.bingo_row);
                        break;
                    case R.id.xBtn:
                        modeSelection = getString(R.string.bingo_x);
                        break;
                    case R.id.ringBtn:
                        modeSelection = getString(R.string.bingo_ring);
                        break;
                    case R.id.cornerBtn:
                        modeSelection = getString(R.string.bingo_corners);
                        break;
                    case R.id.blackoutBtn:
                        modeSelection = getString(R.string.bingo_blackout);
                        break;
                }
                if(game.canChangeMode(modeSelection)) {
                    modeText.setText(getString(R.string.bingo_selector, modeSelection));
                }
            }
        });
        modeDialog.setNegativeButton(getString(R.string.cancel_label), null);
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
            if (button.getTag().toString().equalsIgnoreCase(mode)) {
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