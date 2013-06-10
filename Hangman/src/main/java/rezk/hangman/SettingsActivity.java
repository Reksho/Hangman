package rezk.hangman;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by rezk on 5/31/13.
 */
public class SettingsActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
    }
}
