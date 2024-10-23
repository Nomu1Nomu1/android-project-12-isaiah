package com.example.signupsignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    EditText editname, editemail, editusername, editpassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editname = findViewById(R.id.editName);
        editemail = findViewById(R.id.editEmail);
        editusername = findViewById(R.id.editUsername);
        editpassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChange() || isEmailChange() || isPasswordChange()) {
                    Toast.makeText(EditProfile.this, "Data has been saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, Profile.class);
                    intent.putExtra("name", nameUser);
                    intent.putExtra("email", emailUser);
                    intent.putExtra("username", usernameUser);
                    intent.putExtra("password", passwordUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditProfile.this, "No data has been changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNameChange() {
        if (!nameUser.equals(editname.getText().toString())) {
            reference.child(usernameUser).child("name").setValue(editname.getText().toString());
            nameUser = editname.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailChange() {
        if (!emailUser.equals(editemail.getText().toString())) {
            reference.child(usernameUser).child("email").setValue(editemail.getText().toString());
            emailUser = editemail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPasswordChange() {
        if (!passwordUser.equals(editpassword.getText().toString())) {
            reference.child(usernameUser).child("password").setValue(editpassword.getText().toString());
            passwordUser = editpassword.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public void showData() {
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editname.setText(nameUser);
        editemail.setText(emailUser);
        editusername.setText(usernameUser);
        editpassword.setText(passwordUser);
    }
}