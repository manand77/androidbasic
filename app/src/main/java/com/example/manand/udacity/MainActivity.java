package com.example.manand.udacity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
  private TextView assessedResultTextView;
  private TextView[] textViewArray;
  private EditText resultProvidedEditBox;
  private int result, randomNumberCount = 5, questionCount = 0, correctCount = 0;
  private int[] randomNumbers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    showSoftKeyboard();
    setContentView(R.layout.activity_main);

    Context context = this.getApplicationContext();
    textViewArray = new TextView[randomNumberCount];
    assessedResultTextView = findViewById(R.id.assessedResult);
    resultProvidedEditBox =  findViewById(R.id.result);

    for (int count = 0; count < randomNumberCount; count++) {
      String identifier = "operandTextView" + String.valueOf(count);
      int resourceId = getResources().getIdentifier(identifier, "id", getPackageName());
      textViewArray[count] = findViewById(resourceId);
    }

    final Button submitButton = findViewById(R.id.nextQuestion);
    submitButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String str = resultProvidedEditBox.getText().toString();
        if (str.trim().length() == 0) {
          assessedResultTextView.setText(R.string.emptyResponse);
        } else {
          questionCount++;

          boolean isCorrectResult = Integer.parseInt(str) == result;
          if (isCorrectResult) correctCount++;

          String msg = new StringBuilder(String.valueOf(correctCount)).append("/").append(String.valueOf(questionCount)).toString();
          assessedResultTextView.setText(msg);
          resultProvidedEditBox.setText("");
          provideQuestions();
        }
      }
    });

    provideQuestions();

    resultProvidedEditBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Boolean gotUserInput = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          String str = resultProvidedEditBox.getText().toString();

          // If empty string is entered or all characters are removed from the
          // EditText, the keypad disappears. In this case, we would want to
          // invoke that programmatically.
          if (str.trim().length() == 0) {
            assessedResultTextView.setText(R.string.emptyResponse);
            showSoftKeyboard();
          } else {
            boolean isCorrectResult = Integer.parseInt(str) == result;
            String msg = isCorrectResult ? "Correct" : "Incorrect";
            assessedResultTextView.setText(msg);
            gotUserInput = true;
          }
        }
        return gotUserInput;
      }
    });
  }

  public void showSoftKeyboard() {

    // Invoke the keypad when empty string is provided as response
    Handler mHandler = new Handler();
    mHandler.post(
        new Runnable() {
          public void run() {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(resultProvidedEditBox.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            resultProvidedEditBox.requestFocus();
          }
        });
  }

  public int[] getRandomNumbers(int desiredNumbers, int minNumber, int maxNumber) {

    desiredNumbers = (desiredNumbers < 1) ? 5 : desiredNumbers;
    minNumber = (minNumber <= -1) ? 0 : minNumber;
    maxNumber = (maxNumber <= 0 || maxNumber >= 100000) ? 99999 : maxNumber;

    randomNumbers = new int[desiredNumbers];
    Random rand = new Random();

    for (int count = 0; count < desiredNumbers; count++) {
      randomNumbers[count] = minNumber + rand.nextInt((maxNumber - minNumber) + 1);
    }
    return randomNumbers;
  }

  public void provideQuestions() {

    if (questionCount == 10) {
      // Display the Score
      Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
      intent.putExtra("result", correctCount);
      startActivity(intent);
    }
    randomNumbers = getRandomNumbers(randomNumberCount, 1000, 9999);

    result = 0;
    for (int count = 0; count < randomNumberCount; count++) {
      textViewArray[count].setText(Integer.toString(randomNumbers[count]));
      result += randomNumbers[count];
    }
    // System.out.println(result);
  }
}

