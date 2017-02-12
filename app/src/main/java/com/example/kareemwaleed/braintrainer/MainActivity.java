package com.example.kareemwaleed.braintrainer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView scoreTextView;
    TextView timerTextView;
    TextView equationTextView;
    TextView choiceState;
    RelativeLayout finalScoreLayout;
    boolean[] gameFieldState = {false, false, false, false};
    int equationNumber, correctAnswers;
    int lowerBound, upperBound;
    Random randomNumberGenerator;
    boolean gameOn;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepare();
    }

    public void goButton(View view)
    {
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.VISIBLE);
        Button tempButton = (Button) findViewById(R.id.goButton);
        tempButton.setVisibility(View.INVISIBLE);
        timerControl();
        gameFieldControl();
        scoreControl();
    }

    private void timerControl()
    {
        new CountDownTimer(30000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(convert(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mediaPlayer.start();
                timerTextView.setText("00:00");
                choiceState.setVisibility(View.INVISIBLE);
                TextView tempTextView = (TextView) findViewById(R.id.finalScoreTextView);
                tempTextView.setText("Your Score: " + scoreControl());
                tempTextView.setVisibility(View.VISIBLE);
                Button tempButton = (Button) findViewById(R.id.playAgainButton);
                tempButton.setVisibility(View.VISIBLE);
                gameOn = false;
            }
        }.start();
    }

    private void gameFieldControl()
    {
        for(int i=0; i < 4; i++)
            gameFieldState[i] = false;
        double tempRand = randomNumberGenerator.nextDouble();
        int firstNumber = (int) (tempRand * (upperBound - lowerBound) + lowerBound);
        tempRand = randomNumberGenerator.nextDouble();
        int secondNumber = (int) (tempRand * (upperBound - lowerBound) + lowerBound);
        equationTextView.setText(String.valueOf(firstNumber) + " + " + String.valueOf(secondNumber));
        int sum = firstNumber + secondNumber;
        tempRand = randomNumberGenerator.nextDouble();
        int correctAnswerPosition = (int) (tempRand * 3);
        Button tempButton = (Button) findViewById(getResources().getIdentifier("answer" + String.valueOf(correctAnswerPosition), "id", getPackageName()));
        tempButton.setText(String.valueOf(sum));
        gameFieldState[correctAnswerPosition] = true;
        for(int i=0; i < 4; i++)
        {
            if(!gameFieldState[i])
            {
                tempRand = randomNumberGenerator.nextDouble();
                while(((int) (tempRand * (upperBound*2 - lowerBound*2) + lowerBound*2)) == sum)
                    tempRand = randomNumberGenerator.nextDouble();
                tempButton = (Button) findViewById(getResources().getIdentifier("answer" + String.valueOf(i), "id", getPackageName()));
                tempButton.setText(String.valueOf((int) (tempRand * (upperBound*2 - lowerBound*2) + lowerBound*2)));
            }
        }
    }

    private void prepare()
    {
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        equationTextView = (TextView) findViewById(R.id.equationTextView);
        choiceState = (TextView) findViewById(R.id.choiceState);
        finalScoreLayout = (RelativeLayout) findViewById(R.id.finalScoreLayout);
        equationNumber = 0;
        correctAnswers = 0;
        lowerBound = 1;
        upperBound = 20;
        randomNumberGenerator = new Random();
        gameOn = true;
    }

    private String convert(long milliseconds)
    {
        long seconds = milliseconds/1000;
        long minutes = seconds/60;
        seconds = seconds % 60;

        String minutesString = String.valueOf(minutes);
        if(minutesString.length() == 1)
        {
            String temp = "0";
            temp += minutesString;
            minutesString = temp;
        }
        String secondsString = String.valueOf(seconds);
        if(secondsString.length() == 1)
        {
            String temp = "0";
            temp += secondsString;
            secondsString = temp;
        }
        return minutesString + ":" + secondsString;
    }

    private String scoreControl()
    {
        String temp = String.valueOf(correctAnswers) + "/" + String.valueOf(equationNumber);
        scoreTextView.setText(temp);
        return temp;
    }

    public void answerChosen(View view)
    {
        if(!gameOn)
            return;
        Object temp = view.getTag();
        String tempString = temp.toString();
        int tag = Integer.parseInt(tempString);
        choiceState.setVisibility(View.VISIBLE);
        if(gameFieldState[tag])
        {
            correctAnswers++;
            lowerBound*=2;
            upperBound*=2;
            choiceState.setText("Correct !");
        }
        else
            choiceState.setText("Wrong ..");
        equationNumber++;
        scoreControl();
        gameFieldControl();
    }

    public void playAgain(View view)
    {
        Button tempButton = (Button) findViewById(R.id.playAgainButton);
        tempButton.setVisibility(View.INVISIBLE);
        TextView temptextView = (TextView) findViewById(R.id.finalScoreTextView);
        temptextView.setVisibility(View.INVISIBLE);
        choiceState.setVisibility(View.INVISIBLE);
        prepare();
        timerControl();
        scoreControl();
        gameFieldControl();
    }
}
