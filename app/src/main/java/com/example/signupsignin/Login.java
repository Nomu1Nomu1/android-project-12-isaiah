package com.example.signupsignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signUpRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.signInUsername);
        loginPassword = findViewById(R.id.signInPassword);
        loginButton = findViewById(R.id.signInButton);
        signUpRedirectText = findViewById(R.id.loginRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUser();
                }
            }
        });

        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Field cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Field cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()){
                        String passwordFromDb = snapshot.child(userUsername).child("password").getValue(String.class);

                        if (passwordFromDb != null && passwordFromDb.equals(userPassword)) {
                            loginUsername.setError(null);

                            String nameFromDb = snapshot.child(userUsername).child("name").getValue(String.class);
                            String emailFromDb = snapshot.child(userUsername).child("email").getValue(String.class);
                            String usernameFromDb = snapshot.child(userUsername).child("username").getValue(String.class);

                            Intent intent = new Intent(Login.this, Profile.class);

                            intent.putExtra("name", nameFromDb);
                            intent.putExtra("email", emailFromDb);
                            intent.putExtra("username", usernameFromDb);
                            intent.putExtra("password", passwordFromDb);

                            startActivity(intent);
                            finish();
                        } else {
                            loginPassword.setError("Wrong password");
                            loginPassword.requestFocus();
                        }
                    }
                } else {
                    loginUsername.setError("No such user exists");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}