package com.ashleymccallum.madimalcrossing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference creditsPref = findPreference(getString(R.string.credits_title));
        creditsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_settings_to_nav_credits);
                return false;
            }
        });

        Preference resetPref = findPreference(getString(R.string.clear_key));
        resetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.reset_title))
                        .setMessage(R.string.reset_message)
                        .setPositiveButton(getString(R.string.reset_btn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppDatabase db = new AppDatabase(getContext());
                                db.clearDatabase();
                                db.close();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_label), null)
                        .show();

                return false;
            }
        });
    }
}