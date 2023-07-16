package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScorePageActivity extends AppCompatActivity {

    TextView correct,wrong;
    Button playAgain,exit;
    String userCorrect,userWrong;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=database.getReference().child("scores");

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        correct=findViewById(R.id.textViewCorrectAnswer);
        wrong=findViewById(R.id.textViewWrongAnswer);
        playAgain=findViewById(R.id.buttonPlayAgain);
        exit=findViewById(R.id.buttonExit);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUID=user.getUid();
                userCorrect=snapshot.child(userUID).child("correct").getValue().toString();
                userWrong=snapshot.child(userUID).child("wrong").getValue().toString();
                correct.setText(userCorrect);
                wrong.setText(userWrong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScorePageActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}