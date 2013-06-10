package rezk.hangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    Resources res;
    String word;
    String hyphen;
    List<Character> letters;
    List<Character> guessed;
    StringBuilder builder;
    int score;
    int scoreNegative;
    String printScore;
    String guessPref;
    int wordPreference;
    int guessPreference;
    String[] words;
    char letterChar;
    String letter;
    int placeholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Load dictionary */
        res = getResources();
        words = res.getStringArray(R.array.test_array);

        /* Start new game */
        newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present */
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.highscores:
                highscores();
                return true;
            case R.id.settings:
                settings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Called when the user clicks the settings menu button */
    public void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /* Called when the user clicks the highscores menu button */
    public void highscores() {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    /* Called when the user clicks the new game menu button */
    public void newGame() {
        /* Load settings */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String wordPref = sharedPref.getString("listPref", "4");
        guessPref = sharedPref.getString("listPref2", "10");
        wordPreference = Integer.parseInt(wordPref);
        guessPreference = Integer.parseInt(guessPref);
        TextView chances = (TextView) findViewById(R.id.textView7);
        chances.setText(guessPref);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.hangman1);

        /* Load word from dictionary */
        Random r = new Random();
        word = words[r.nextInt(words.length)];
        while (word.length() != wordPreference)
            word = words[r.nextInt(words.length)];

        /* Display hyphens */
        TextView textView = (TextView) findViewById(R.id.textView2);
        placeholder = word.length();
        builder = new StringBuilder();
        for (int i=0; i<placeholder; i++)
            builder.append("-");
        hyphen = builder.toString();
        textView.setText(hyphen);

        /* Empty guessed letters and score */
        TextView input = (TextView) findViewById(R.id.textView5);
        input.setText("");
        score = 0;
        scoreNegative = 0;
        printScore = String.valueOf(score);
        TextView scoreView = (TextView) findViewById(R.id.textView6);
        scoreView.setText(printScore);

        /* Arrays to hold inputted letters */
        letters = new ArrayList<Character>();
        guessed = new ArrayList<Character>();
    }

    /* Called when the user inputs a letter */
    public void checkLetter(View v) {
        /* Get current information on screen */
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setHint("");
        letter = null;
        letterChar = '\0';
        /* Check for empty letter */
        try {
            letter = editText.getText().toString();
            letterChar = letter.charAt(0);
        }
        catch (Exception e) {
            letter = "\0";
        }
        TextView guessingWord = (TextView) findViewById(R.id.textView2);
        String wordGuess = guessingWord.getText().toString();
        TextView textView = (TextView) findViewById(R.id.textView5);
        String guessingList = textView.getText().toString();

        Intent intent = new Intent(this, Gameplay.class);
        intent.putExtra("letter", letter);
        intent.putExtra("wordGuess", wordGuess);
        intent.putExtra("guessingList", guessingList);
        intent.putExtra("word", word);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("methodName");
                if (result.equals("emptyInput"))
                    emptyInput();
                else if (result.equals("duplicateLetter"))
                    duplicateLetter();
                else if (result.equals("correctLetter"))
                    correctLetter();
                else if (result.equals("wrongLetter"))
                    wrongLetter();
            }
        }
    }

    public void emptyInput() {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.empty);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -225);
        toast.show();
    }
    public void duplicateLetter() {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.double_letter);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -225);
        toast.show();

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");
    }

    public void correctLetter() {
        /* Add to list of guessed letters */
        guessed.add(letterChar);
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(letters.toString().replaceAll("\\[|\\]", ""));
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");

        /* Change hyphen to letter */
        hyphenChanger();

        /* Display score */
        printScore = String.valueOf(score);
        TextView scoreView = (TextView) findViewById(R.id.textView6);
        scoreView.setText(printScore);

        if (guessed.size() == placeholder)
            checkWin();
    }

    public void checkWin() {
        /* Check for win */
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.won_text);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -100);
        toast.show();

        Intent intent = new Intent(this, ScoreCalculation.class);
        String scoreString = Integer.toString(score);
        intent.putExtra("score", scoreString);
        startActivity(intent);
        newGame();
    }

    public void wrongLetter() {
        /* Add to list of tried letters */
        letters.add(letterChar);
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(letters.toString().replaceAll("\\[|\\]", ""));
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");

        /* Display message for wrong letter */
        CharSequence text = getString(R.string.wrong_letter) + letter + getString(R.string.wrong_letter2);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -225);
        toast.show();

        /* Check to make sure no negative score is displayed */
        if (score > 1) {
            score -= 2;
            scoreNegative -= 2;
            printScore = String.valueOf(score);
            TextView scoreView = (TextView) findViewById(R.id.textView6);
            scoreView.setText(printScore);
        }

        /* Update amount of chances left */
        guessPreference -= 1;
        TextView chances = (TextView) findViewById(R.id.textView7);
        chances.setText(Integer.toString(guessPreference));

        /* Draw Hangman image */
        int startPref = Integer.parseInt(guessPref);
        int imageScore = (guessPreference*100) / startPref;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if (imageScore >= 81 && imageScore <= 90)
            imageView.setImageResource(R.drawable.hangman2);
        else if (imageScore >= 71 && imageScore <= 80)
            imageView.setImageResource(R.drawable.hangman3);
        else if (imageScore >= 61 && imageScore <= 70)
            imageView.setImageResource(R.drawable.hangman4);
        else if (imageScore >= 51 && imageScore <= 60)
            imageView.setImageResource(R.drawable.hangman5);
        else if (imageScore >= 41 && imageScore <= 50)
            imageView.setImageResource(R.drawable.hangman6);
        else if (imageScore >= 31 && imageScore <= 40)
            imageView.setImageResource(R.drawable.hangman7);
        else if (imageScore >= 21 && imageScore <= 30)
            imageView.setImageResource(R.drawable.hangman8);
        else if (imageScore >= 11 && imageScore <= 20)
            imageView.setImageResource(R.drawable.hangman9);

        if (guessPreference == 0) {
            imageView.setImageResource(R.drawable.hangman10);
            checkLose();
        }
    }

    public void checkLose() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.lose_text));
        alertDialog.setMessage(getString(R.string.new_text));
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                newGame();
            }
        });
        alertDialog.show();
    }

    public void hyphenChanger() {
        builder = new StringBuilder();
        placeholder = word.length();
        score = scoreNegative;
        for (int i = 0; i < placeholder; i++) {
            char c = word.charAt(i);
            if (guessed.contains(c)) {
                builder.append(c);
                score += 10;
            } else
                builder.append("-");
        }
        hyphen = builder.toString();
        TextView guessingWord = (TextView) findViewById(R.id.textView2);
        guessingWord.setText(hyphen);
    }
}