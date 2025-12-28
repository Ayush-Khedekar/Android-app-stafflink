package com.example.stafflink;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.VH> {

    private List<MessageModel> list;
    private Context context;

    public InboxAdapter(Context context, List<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_message, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        MessageModel m = list.get(position);

        holder.title.setText(m.title);
        holder.body.setText(m.body);

        holder.title.setTypeface(
                null,
                m.isRead ? Typeface.NORMAL : Typeface.BOLD
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, body;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.txtTitle);
            body = v.findViewById(R.id.txtBody);
        }
    }
}
