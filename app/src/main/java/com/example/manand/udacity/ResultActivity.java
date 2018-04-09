package com.example.manand.udacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    final Button replayButton = findViewById(R.id.replay);
    replayButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      }
    });

    TextView resultTextView = findViewById(R.id.assessedResult);
    Bundle bundle = getIntent().getExtras();
    int score = bundle.getInt("result");
    resultTextView.setText(String.valueOf(score));
  }
}

