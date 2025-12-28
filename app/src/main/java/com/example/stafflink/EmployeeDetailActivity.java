package com.example.stafflink;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeDetailActivity extends AppCompatActivity {

    // Profile Views
    private TextView tvDetailInitial, tvDetailName, tvDetailEmail, tvDetailPhone, tvDetailPosition,
            tvDetailDepartment, tvDetailStatus, tvDetailRole, tvDetailJoinedAt;

    // Salary Views
    private TextView tvDetailBaseSalary, tvDetailFinalSalary, tvDetailDeductions, tvDetailOvertime;

    // Leaves Views
    private TextView tvDetailHalfDays, tvDetailFullDays, tvDetailPaidLeaves;

    private ImageButton btnBack, btnLogout;

    private String empId, companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        // Initialize profile views
        tvDetailInitial = findViewById(R.id.tvDetailInitial);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailEmail = findViewById(R.id.tvDetailEmail);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailPosition = findViewById(R.id.tvDetailPosition);
        tvDetailDepartment = findViewById(R.id.tvDetailDepartment);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailRole = findViewById(R.id.tvDetailRole);
        tvDetailJoinedAt = findViewById(R.id.tvDetailJoinedAt);

        // Salary views
        tvDetailBaseSalary = findViewById(R.id.tvDetailBaseSalary);
        tvDetailFinalSalary = findViewById(R.id.tvDetailFinalSalary);
        tvDetailDeductions = findViewById(R.id.tvDetailDeductions);
        tvDetailOvertime = findViewById(R.id.tvDetailOvertime);

        // Leaves views
        tvDetailHalfDays = findViewById(R.id.tvDetailHalfDays);
        tvDetailFullDays = findViewById(R.id.tvDetailFullDays);
        tvDetailPaidLeaves = findViewById(R.id.tvDetailPaidLeaves);

        // Buttons
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);

        btnBack.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            // Optional: Add logout logic here
        });

        // Get intent data
        empId = getIntent().getStringExtra("EMP_ID");
        companyCode = getIntent().getStringExtra("COMPANY_CODE");

        if (empId == null || empId.isEmpty() || companyCode == null || companyCode.isEmpty()) {
            Toast.makeText(this, "Invalid employee data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchEmployeeDetails();
    }

    private void fetchEmployeeDetails() {
        DatabaseReference profileRef = FirebaseDatabase.getInstance()
                .getReference("Stafflink")
                .child("companies")
                .child(companyCode)
                .child("employees")
                .child(empId)
                .child("profile");

        DatabaseReference payrollRef = FirebaseDatabase.getInstance()
                .getReference("Stafflink")
                .child("companies")
                .child(companyCode)
                .child("employees")
                .child(empId)
                .child("payroll");

        DatabaseReference leavesRef = FirebaseDatabase.getInstance()
                .getReference("Stafflink")
                .child("companies")
                .child(companyCode)
                .child("employees")
                .child(empId)
                .child("leaves");

        // Fetch Profile
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(EmployeeDetailActivity.this, "Employee not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String position = snapshot.child("position").getValue(String.class);
                String department = snapshot.child("department").getValue(String.class);
                String status = snapshot.child("status").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
                String joinedAt = snapshot.child("joinedAt").getValue(String.class);
                Long baseSalary = snapshot.child("baseSalary").getValue(Long.class);

                // Profile
                tvDetailName.setText(name != null ? name : "Unknown");
                tvDetailEmail.setText(email != null ? email : "-");
                tvDetailPhone.setText(phone != null ? phone : "-");
                tvDetailPosition.setText(position != null ? position : "-");
                tvDetailDepartment.setText(department != null ? department : "-");
                tvDetailStatus.setText(status != null ? status : "-");
                tvDetailRole.setText(role != null ? role : "-");
                tvDetailJoinedAt.setText(joinedAt != null ? "Joined: " + joinedAt : "-");

                // Initial
                if (name != null && !name.isEmpty()) {
                    tvDetailInitial.setText(name.substring(0, 1).toUpperCase());
                } else {
                    tvDetailInitial.setText("A");
                }

                tvDetailBaseSalary.setText("Base Salary: ₹" + (baseSalary != null ? baseSalary : 0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeDetailActivity.this, "Profile DB error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch Payroll
        payrollRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long finalSalary = snapshot.child("finalSalary").getValue(Long.class);
                Long deductions = snapshot.child("deductions").getValue(Long.class);
                Long overtime = snapshot.child("overtime").getValue(Long.class);

                tvDetailFinalSalary.setText("Final Salary: ₹" + (finalSalary != null ? finalSalary : 0));
                tvDetailDeductions.setText("Deductions: ₹" + (deductions != null ? deductions : 0));
                tvDetailOvertime.setText("Overtime: ₹" + (overtime != null ? overtime : 0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeDetailActivity.this, "Payroll DB error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch Leaves
        leavesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long halfDays = snapshot.child("halfDaysUsed").getValue(Long.class);
                Long fullDays = snapshot.child("fullDaysUsed").getValue(Long.class);
                Long paidLeaves = snapshot.child("paidLeavesUsed").getValue(Long.class);

                tvDetailHalfDays.setText("Half Days Used: " + (halfDays != null ? halfDays : 0));
                tvDetailFullDays.setText("Full Days Used: " + (fullDays != null ? fullDays : 0));
                tvDetailPaidLeaves.setText("Paid Leaves Used: " + (paidLeaves != null ? paidLeaves : 0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeDetailActivity.this, "Leaves DB error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
