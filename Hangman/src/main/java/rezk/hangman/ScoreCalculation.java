package rezk.hangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Rezk on 6-6-13.
 */
public class ScoreCalculation extends Activity {
    public static final int maxList = 10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        ArrayList<String> scores = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));

        /* Add to highscores list if list does not yet contain 10 entries */
        if (scores.size() <= maxList) {
            scores.add(score);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor edit = prefs.edit();
            edit.putStringSet("SAVEDATA", new HashSet<String>(scores));
            edit.commit();
        }
        /* Add to highscores list if entry has a higher score than the other entries */
        else {
            int scorie = Integer.valueOf(score);
            for (int i=0; i <= maxList; i++) {
                String scoreLoop = scores.get(i);
                int scoreNow = Integer.valueOf(scoreLoop);
                if (scorie > scoreNow)
                    scores.set(i, scoreLoop);
            }
        }
        Intent start = new Intent(this, HighScoresActivity.class);
        startActivity(start);
        finish();
    }
}

