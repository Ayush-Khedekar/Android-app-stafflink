package com.example.stafflink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView welcomeText, companyNameText, companyCodeText;
    private CardView employeesCard, payrollCard, attendanceCard, leavesCard, settingsCard, profileCard;
    private ImageView logout;

    private String adminNodeKey, companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Bind UI
        welcomeText = findViewById(R.id.adminWelcomeText);
        companyNameText = findViewById(R.id.companyNameText);
        companyCodeText = findViewById(R.id.companyCodeText);
        employeesCard = findViewById(R.id.employeesCard);
        payrollCard = findViewById(R.id.payrollCard);
        attendanceCard = findViewById(R.id.attendanceCard);
        leavesCard = findViewById(R.id.leavesCard);
        settingsCard = findViewById(R.id.settingsCard);
        profileCard = findViewById(R.id.profileCard);
        logout = findViewById(R.id.logoutIcon);

        // Load session
        SharedPreferences sp = getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE);
        adminNodeKey = sp.getString("adminNode", null);
        companyCode = sp.getString("COMPANY_CODE", null);

        if (adminNodeKey == null || companyCode == null) {
            Toast.makeText(this, "No admin session found!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Admin_page.class));
            finish();
            return;
        }

        if(companyCode != null){
            companyCodeText.setText("Code: " + companyCode);
        }

        // Fetch company name from admin node
        DatabaseReference adminRef = FirebaseDatabase.getInstance()
                .getReference("Stafflink/admins")
                .child(adminNodeKey)
                .child("companyInfo")
                .child("companyName");

        adminRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String companyName = snapshot.getValue(String.class);
                companyNameText.setText("Company: " + companyName);
            } else {
                companyNameText.setText("Company: Not Found");
            }
        }).addOnFailureListener(e -> {
            companyNameText.setText("Company: Error");
            Toast.makeText(this, "Failed to load company name", Toast.LENGTH_SHORT).show();
        });

        welcomeText.setText("Welcome, Admin (" + adminNodeKey + ")");

        // Logout
        logout.setOnClickListener(v -> {
            sp.edit().clear().apply();
            Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Admin_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Card click actions
        employeesCard.setOnClickListener(v -> openEmployeeManagement());
        payrollCard.setOnClickListener(v -> openActivity(PayrollActivity.class));
        attendanceCard.setOnClickListener(v -> openAttendance());
        leavesCard.setOnClickListener(v -> openLeaveActivity());
        settingsCard.setOnClickListener(v -> openActivity(SettingsActivity.class));
        profileCard.setOnClickListener(v -> openActivity(ProfileActivity.class));
    }

    private void openEmployeeManagement() {
        Intent i = new Intent(this, EmployeeManagementActivity.class);
        i.putExtra("COMPANY_CODE", companyCode);
        i.putExtra("ADMIN_NODE", adminNodeKey);
        startActivity(i);
    }

    private void openAttendance() {
        Intent i = new Intent(this, AttendanceActivity.class);
        i.putExtra("COMPANY_CODE", companyCode);
        i.putExtra("ADMIN_NODE", adminNodeKey);
        startActivity(i);
    }

    private void openLeaveActivity() {
        Intent i = new Intent(this, LeavesActivity.class);
        i.putExtra("COMPANY_CODE", companyCode);
        i.putExtra("ADMIN_NODE", adminNodeKey);
        startActivity(i);
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}
