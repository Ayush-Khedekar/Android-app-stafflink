package com.example.stafflink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class CompanyCodeSetupActivity extends AppCompatActivity {

    private EditText companyCodeInput;
    private Button setCompanyButton;
    private DatabaseReference adminsRef;
    private DatabaseReference companiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_code_setup);

        companyCodeInput = findViewById(R.id.companyCodeInput);
        setCompanyButton = findViewById(R.id.setCompanyButton);

        // Get adminID and password from previous login
        String adminID = getIntent().getStringExtra("ADMIN_ID");

        // Firebase references
        adminsRef = FirebaseDatabase.getInstance().getReference("Stafflink/admins").child(adminID);
        companiesRef = FirebaseDatabase.getInstance().getReference("Stafflink/companies");

        setCompanyButton.setOnClickListener(v -> {
            String companyCode = companyCodeInput.getText().toString().trim();

            if (companyCode.isEmpty()) {
                Toast.makeText(this, "Enter Company Code ❌", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1️⃣ Save company code under admin node
            adminsRef.child("companyCode").setValue(companyCode)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 2️⃣ Check if company node exists
                            companiesRef.child(companyCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        // Create company node with empty employees
                                        Map<String, Object> companyData = new HashMap<>();
                                        companyData.put("employees", new HashMap<>()); // empty initially
                                        companiesRef.child(companyCode).setValue(companyData);
                                    }

                                    // 3️⃣ Save session locally
                                    getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE)
                                            .edit()
                                            .putString("ADMIN_ID", adminID)
                                            .putString("COMPANY_CODE", companyCode)
                                            .apply();

                                    Toast.makeText(CompanyCodeSetupActivity.this, "Company Code Set ✅", Toast.LENGTH_SHORT).show();

                                    // 4️⃣ Redirect to Admin Dashboard
                                    Intent i = new Intent(CompanyCodeSetupActivity.this, AdminDashboardActivity.class);
                                    i.putExtra("ADMIN_ID", adminID);
                                    i.putExtra("COMPANY_CODE", companyCode);
                                    startActivity(i);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(CompanyCodeSetupActivity.this, "Firebase Error ❌", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Failed to set company code ❌", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
