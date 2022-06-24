package com.mg.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button[] buttonAnswers;
    private int questionNumber = 0;
    private TextView textQuestionNumber;
    private TextView textQuestion;
    private TextView textTime;
    private Button buttonStart;
    private int answer;
    private Timer timer;
    private int seconds;
    private boolean is_started = false;
    private int normalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textQuestionNumber = findViewById(R.id.textNumber);
        textQuestionNumber.setText(String.valueOf(0));

        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setText(R.string.buttonStartText);
        buttonStart.setOnClickListener(this::onButtonStartClick);

        textQuestion = findViewById(R.id.textQuestion);
        textQuestion.setText("");
        if (textQuestion.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) textQuestion.getBackground();
            normalColor = cd.getColor();
        }

        textTime = findViewById(R.id.textTime);
        textTime.setText(String.valueOf(0));

        buttonAnswers = new Button[]{
                findViewById(R.id.buttonAnswer0),
                findViewById(R.id.buttonAnswer1),
                findViewById(R.id.buttonAnswer2),
                findViewById(R.id.buttonAnswer3),
                findViewById(R.id.buttonAnswer4),
                findViewById(R.id.buttonAnswer5)
        };
        for (Button btn : buttonAnswers) {
            btn.setText(String.valueOf(0));
            btn.setOnClickListener(this::onButtonAnswerClick);
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               runOnUiThread(() -> {
                                   seconds++;
                                   textTime.setText(String.valueOf(seconds));
                               });
                           }
                       }
                , 1000, 1000);

    }

    private void setQuestionStatus(boolean is_right) {
        if (is_right) {
            textQuestion.setBackgroundColor(Color.GREEN);
        } else {
            textQuestion.setBackgroundColor(Color.RED);
        }
        Timer questionTimer = new Timer();
        questionTimer.schedule(new TimerTask() {
                                   @Override
                                   public void run() {
                                       runOnUiThread(() -> textQuestion.setBackgroundColor(normalColor));
                                   }
                               }
                , is_right ? 200 : 600);

    }

    public void onButtonAnswerClick(View view) {
        if (view instanceof Button) {
            int value = Integer.parseInt(((Button) view).getText().toString());
            System.out.println(value);
            if (value == answer) {
                setQuestionStatus(true);
                newQuestion();
            } else {
                setQuestionStatus(false);
            }
        }
    }

    private void newQuestion() {
        questionNumber++;
        textQuestionNumber.setText(String.valueOf(questionNumber));
        Random r = new Random();
        int x = r.nextInt(8) + 2;
        int y = r.nextInt(8) + 2;

        final int op_sum = 0;
        final int op_sub = 1;
        final int op_mul = 2;
        final int op_div = 3;

        String question;

        final int op = r.nextInt(4);
        switch (op) {
            case op_sum:
                answer = x + y;
                question = String.format(Locale.getDefault(), "%d + %d", x, y);
                break;
            case op_sub:
                answer = x;
                question = String.format(Locale.getDefault(), "%d - %d", x + y, y);
                break;
            case op_mul:
                answer = x * y;
                question = String.format(Locale.getDefault(), "%d * %d", x, y);
                break;
            case op_div:
                answer = x;
                question = String.format(Locale.getDefault(), "%d / %d", x * y, y);
                break;
            default:
                question = "Error";
        }

        textQuestion.setText(question);
        setAnswers(answer);
    }

    private void setAnswers(int answer) {
        Random r = new Random();
        boolean haveAnswer = false;
        for (Button b : buttonAnswers) {
            int randomAnswer = r.nextInt(100);
            b.setText(String.valueOf(randomAnswer));
            if(randomAnswer == answer) {
                haveAnswer = true;
            }
        }
        if(!haveAnswer) {
            buttonAnswers[r.nextInt(buttonAnswers.length)].setText(String.valueOf(answer));
        }
    }

    private void onButtonStartClick(View v) {
        if (is_started) {
            //stop
            buttonStart.setText(R.string.buttonStartText);
            timer.cancel();
        } else {
            //start
            buttonStart.setText(R.string.buttonStopText);
            seconds = 0;
            textTime.setText(String.valueOf(seconds));
            questionNumber = 0;
            startTimer();
            newQuestion();
        }
        is_started = !is_started;
    }
}