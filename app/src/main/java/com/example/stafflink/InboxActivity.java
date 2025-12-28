package com.example.stafflink;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {

    // TEMP values (replace with logged-in session later)
    private String companyCode = "TECH001";
    private String empId = "emp_A1";

    private RecyclerView rvInbox;
    private InboxAdapter adapter;
    private List<MessageModel> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        rvInbox = findViewById(R.id.rvInbox);
        rvInbox.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        adapter = new InboxAdapter(this, messageList);
        rvInbox.setAdapter(adapter);

        loadInboxMessages();
    }

    private void loadInboxMessages() {
        FirebaseDatabase.getInstance()
                .getReference("companies")
                .child(companyCode)
                .child("employees")
                .child(empId)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for (DataSnapshot d : snapshot.getChildren()) {
                            MessageModel msg = d.getValue(MessageModel.class);
                            if (msg != null) {
                                messageList.add(msg);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Optional: Toast or Log
                    }
                });
    }
}
