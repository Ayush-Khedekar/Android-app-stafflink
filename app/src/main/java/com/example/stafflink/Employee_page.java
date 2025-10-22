package com.example.stafflink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Employee_page extends AppCompatActivity {

    TextView signuptextview;

    Button employeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_page);

        signuptextview = findViewById(R.id.signuptextview);
        employeeButton = findViewById(R.id.employeeButton);

        signuptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee_page.this, Employee_Signup_page.class);
                startActivity(intent);
                finish();
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee_page.this, User_profile_page.class);
                startActivity(intent);
            }
        });


    }
}