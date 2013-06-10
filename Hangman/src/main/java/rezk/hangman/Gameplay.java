package rezk.hangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Rezk on 6-6-13.
 */
public class Gameplay extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String letter = intent.getStringExtra("letter");
        char letterChar = letter.charAt(0);
        String wordGuess = intent.getStringExtra("wordGuess");
        String guessingList = intent.getStringExtra("guessingList");
        String word = intent.getStringExtra("word");

        /* Check for empty letter input */
        if (letterChar == '\0') {
            intent = new Intent();
            intent.putExtra("methodName","emptyInput");
            setResult(RESULT_OK, intent);
            finish();
        }
        /* Check for duplicate letter */
        else if (guessingList.contains(letter) || wordGuess.contains(letter)) {
            intent = new Intent();
            intent.putExtra("methodName","duplicateLetter");
            setResult(RESULT_OK, intent);
            finish();
        }
            /* Called when correct letter is guessed */
        else if (word.contains(letter)) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("methodName","correctLetter");
            setResult(RESULT_OK, intent);
            finish();
        }
            /* Called when wrong letter is guessed */
        else {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("methodName","wrongLetter");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
