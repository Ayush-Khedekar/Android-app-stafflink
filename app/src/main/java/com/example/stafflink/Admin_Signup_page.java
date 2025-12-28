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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Admin_Signup_page extends AppCompatActivity {

    private TextView loginTextView;
    private Button signupButton;
    private EditText emailInput, passwordInput, contactInput;

    private FirebaseAuth mAuth;
    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup_page);

        mAuth = FirebaseAuth.getInstance();
        adminsRef = FirebaseDatabase.getInstance().getReference("admins");

        loginTextView = findViewById(R.id.logintextview);
        signupButton = findViewById(R.id.signupButton);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        contactInput = findViewById(R.id.contactInput);

        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(Admin_Signup_page.this, Admin_page.class));
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();

                                HashMap<String, Object> adminData = new HashMap<>();
                                adminData.put("adminEmail", email);
                                adminData.put("adminContact", contact);
                                adminData.put("adminId", uid);

                                adminsRef.child(uid).setValue(adminData)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(this, "Admin registered successfully!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Admin_Signup_page.this, Admin_page.class));
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Failed to save admin data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
