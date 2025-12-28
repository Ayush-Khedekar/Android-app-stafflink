package com.example.stafflink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class Employee_page extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_page);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.employeeButton);
        signupTextView = findViewById(R.id.signuptextview);

        signupTextView.setOnClickListener(v -> {
            startActivity(new Intent(Employee_page.this, Employee_Signup_page.class));
            finish();
        });

        // If already logged in, skip login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkIfEmployee(currentUser.getUid());
            return;
        }

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkIfEmployee(user.getUid());
                            }
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void checkIfEmployee(String uid) {
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("UserEmail").getValue(String.class);

                    if (email != null) {
                        // User found in "Users" node → it's an employee
                        Toast.makeText(Employee_page.this, "Welcome Employee!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Employee_page.this, User_profile_page.class));
                        finish();
                    } else {
                        // Not an employee, sign out
                        Toast.makeText(Employee_page.this, "Access denied: Not an Employee account", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                } else {
                    Toast.makeText(Employee_page.this, "User record not found", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Employee_page.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
