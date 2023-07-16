package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizPageActivity extends AppCompatActivity {

    TextView time,correct,wrong,question,a,b,c,d;
    Button next,finish;

    String userAnswer;
   FirebaseDatabase database=FirebaseDatabase.getInstance();
   DatabaseReference databaseReference= database.getReference().child("Questions");

    String quizQuestion,quizAnswerA,quizAnswerB,quizAnswerC,quizAnswerD,quizCorrectAnswer;

    int userCorrect=0;
    int userWrong=0;
    int questionCount;
    int questionNumber=1;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME=25000;
    Boolean timerContinue;
    long leftTime=TOTAL_TIME;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();
    DatabaseReference databaseReferenceSecond=database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        time = findViewById(R.id.textViewTime);
        correct = findViewById(R.id.textViewCorrect);
        wrong = findViewById(R.id.textViewWrong);

        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewA);
        b = findViewById(R.id.textViewB);
        c= findViewById(R.id.textViewC);
        d= findViewById(R.id.textViewD);


        next = findViewById(R.id.buttonNext);
        finish=findViewById(R.id.buttonFinish);

        game();



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restTimer();
                game();

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendScore();
                Intent i=new Intent(QuizPageActivity.this,ScorePageActivity.class);
                startActivity(i);
                finish();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer="a";
                if (quizCorrectAnswer.equals(userAnswer)) {

                    a.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(""+userCorrect);

                }else{
                    a.setBackgroundColor(Color.RED);
                    userWrong++;
                    wrong.setText(""+userWrong);
                    findAnswer();

                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer="b";
                if (quizCorrectAnswer.equals(userAnswer)) {

                    b.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(""+userCorrect);

                }else{
                    b.setBackgroundColor(Color.RED);
                    userWrong++;
                    wrong.setText(""+userWrong);
                    findAnswer();

                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer="c";
                if (quizCorrectAnswer.equals(userAnswer)) {

                    c.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(""+userCorrect);

                }else{
                    c.setBackgroundColor(Color.RED);
                    userWrong++;
                    wrong.setText(""+userWrong);
                    findAnswer();
                }
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer="d";
                if (quizCorrectAnswer.equals(userAnswer)) {

                    d.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(""+userCorrect);

                }else{
                    d.setBackgroundColor(Color.RED);
                    userWrong++;
                    wrong.setText(""+userWrong);
                    findAnswer();

                }
            }
        });

    }

    public void game(){

        startTimer();

        a.setBackgroundColor(Color.WHITE);
        b.setBackgroundColor(Color.WHITE);
        c.setBackgroundColor(Color.WHITE);
        d.setBackgroundColor(Color.WHITE);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                questionCount=(int) dataSnapshot.getChildrenCount();

                quizQuestion=dataSnapshot.child(String.valueOf(questionNumber)).child("Q").getValue().toString();
                quizAnswerA=dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                quizAnswerB=dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                quizAnswerC=dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                quizAnswerD=dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                quizCorrectAnswer=dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();

                question.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);

                if(questionNumber < questionCount){
                    questionNumber++;
                }else {
                    Toast.makeText(QuizPageActivity.this, "You answered all questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QuizPageActivity.this,"There is an error.",Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void findAnswer(){
        if(quizCorrectAnswer.equals("a")){
            a.setBackgroundColor(Color.GREEN);
        } else if (quizCorrectAnswer.equals("b")) {
            b.setBackgroundColor(Color.GREEN);

        } else if (quizCorrectAnswer.equals("c")) {
            c.setBackgroundColor(Color.GREEN);

        } else if (quizCorrectAnswer.equals("d")) {
            d.setBackgroundColor(Color.GREEN);

        }
    }

    public void startTimer(){
        countDownTimer=new CountDownTimer(leftTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTime=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                    timerContinue=false;
                    pauseTimer();
                    question.setText("Sorry, Time is up!");
            }
        }.start();
            timerContinue=true;
    }

    public void restTimer(){
        leftTime=TOTAL_TIME;
        updateCountDownText();
    }
    public void updateCountDownText(){
        int second=(int) (leftTime/1000) %60;
        time.setText(""+second);
    }
    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue=false;
    }

    public void sendScore(){
        String userUID=user.getUid();
        databaseReferenceSecond.child("scores").child(userUID).child("correct").setValue(userCorrect)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(QuizPageActivity.this,"scores set successfully.",Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReferenceSecond.child("scores").child(userUID).child("wrong").setValue(userWrong);

    }
}