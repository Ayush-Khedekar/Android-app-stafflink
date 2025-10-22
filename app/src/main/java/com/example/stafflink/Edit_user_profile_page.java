package com.example.stafflink;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Edit_user_profile_page extends AppCompatActivity {

    TextView tvProfileLogo;

    ImageView editButton;

    int[] colors = {
            Color.parseColor("#4CAF50"), // Green
            Color.parseColor("#2196F3"), // Blue
            Color.parseColor("#FF9800"), // Orange
            Color.parseColor("#9C27B0")  // Purple
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user_profile_page);

        tvProfileLogo = findViewById(R.id.tvProfileLogo);
        editButton = findViewById(R.id.editButton);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Edit_user_profile_page.this, User_profile_page.class);
                startActivity(intent);
            }
        });


        String email = "ayushkhedekar07@gmail.com";
        if (email != null || !email.isEmpty()) {
            String firstLetter = String.valueOf(email.charAt(0)).toUpperCase();
            tvProfileLogo.setText(firstLetter);
        }

        Random random = new Random();
        int color = colors[random.nextInt(colors.length)];
        tvProfileLogo.getBackground().setTint(color);

    }
}