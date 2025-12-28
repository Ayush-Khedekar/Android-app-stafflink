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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Employee_Signup_page extends AppCompatActivity {

    private EditText companycodeInput, employeeEmailInput, passwordInput, contactInput;
    private TextView loginTextView;
    private Button signupButton;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_signup_page);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        adminsRef = FirebaseDatabase.getInstance().getReference("admins");

        companycodeInput = findViewById(R.id.companycodeInput);
        employeeEmailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        contactInput = findViewById(R.id.contactInput);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.logintextview);

        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(Employee_Signup_page.this, Employee_page.class));
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String Ccode = companycodeInput.getText().toString().trim();
            String employeeEmail = employeeEmailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();

            if (Ccode.isEmpty() || employeeEmail.isEmpty() || password.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            Query adminQuery = adminsRef.orderByChild("Company code").equalTo(Ccode);
            adminQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Admin found
                        mAuth.createUserWithEmailAndPassword(employeeEmail, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();

                                            HashMap<String, Object> employeeData = new HashMap<>();
                                            employeeData.put("UserEmail", employeeEmail);
                                            employeeData.put("UserContact", contact);
                                            employeeData.put("UserId", uid);
                                            employeeData.put("Cmpany Code", Ccode);

                                            usersRef.child(uid).setValue(employeeData)
                                                    .addOnCompleteListener(dbTask -> {
                                                        if (dbTask.isSuccessful()) {
                                                            Toast.makeText(Employee_Signup_page.this, "Employee registered successfully!", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(Employee_Signup_page.this, Employee_page.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Employee_Signup_page.this, "Failed to save data.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(Employee_Signup_page.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Employee_Signup_page.this, "Admin email not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Employee_Signup_page.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
