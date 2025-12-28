package com.example.stafflink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<EmployeeModel> employeeList;

    public EmployeeAdapter(List<EmployeeModel> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_card, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        EmployeeModel e = employeeList.get(position);

        holder.tvName.setText(e.getName());
        holder.tvRole.setText(
                e.getPosition() + (e.getDepartment().isEmpty() ? "" : " • " + e.getDepartment())
        );
        holder.tvSalary.setText("₹" + e.getBaseSalary());
        holder.tvAvatar.setText(
                e.getName().isEmpty() ? "?" : e.getName().substring(0, 1).toUpperCase()
        );

        holder.tvEmail.setText("Email: " + e.getEmail());
        holder.tvPhone.setText("Phone: " + e.getPhone());
        holder.tvPosition.setText("Position: " + e.getPosition());
        holder.tvDepartment.setText("Department: " + e.getDepartment());
        holder.tvBaseSalary.setText("Base Salary: ₹" + e.getBaseSalary());
        holder.tvFinalSalary.setText("Final Salary: ₹" + e.getFinalSalary());
        holder.tvDeductions.setText("Deductions: ₹" + e.getDeductions());
        holder.tvOvertime.setText("Overtime: ₹" + e.getOvertime());
        holder.tvHalfDaysUsed.setText("Half Days: " + e.getHalfDaysUsed());
        holder.tvFullDaysUsed.setText("Full Days: " + e.getFullDaysUsed());
        holder.tvPaidLeavesUsed.setText("Paid Leaves: " + e.getPaidLeavesUsed());

        holder.expandableLayout.setVisibility(e.isExpanded() ? View.VISIBLE : View.GONE);
        holder.btnExpand.setRotation(e.isExpanded() ? 180f : 0f);

        holder.btnExpand.setOnClickListener(v -> {
            e.setExpanded(!e.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void updateList(List<EmployeeModel> newList) {
        employeeList = newList;
        notifyDataSetChanged();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvRole, tvAvatar, tvSalary;
        TextView tvEmail, tvPhone, tvPosition, tvDepartment;
        TextView tvBaseSalary, tvFinalSalary, tvDeductions, tvOvertime;
        TextView tvHalfDaysUsed, tvFullDaysUsed, tvPaidLeavesUsed;

        ImageButton btnExpand;
        LinearLayout expandableLayout;

        EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvEmployeeName);
            tvRole = itemView.findViewById(R.id.tvEmployeeRole);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvSalary = itemView.findViewById(R.id.tvEmployeeSalary);

            tvEmail = itemView.findViewById(R.id.tvEmployeeEmail);
            tvPhone = itemView.findViewById(R.id.tvEmployeePhone);
            tvPosition = itemView.findViewById(R.id.tvEmployeePosition);
            tvDepartment = itemView.findViewById(R.id.tvEmployeeDepartment);
            tvBaseSalary = itemView.findViewById(R.id.tvEmployeeBaseSalary);
            tvFinalSalary = itemView.findViewById(R.id.tvEmployeeFinalSalary);
            tvDeductions = itemView.findViewById(R.id.tvEmployeeDeductions);
            tvOvertime = itemView.findViewById(R.id.tvEmployeeOvertime);
            tvHalfDaysUsed = itemView.findViewById(R.id.tvEmployeeHalfDays);
            tvFullDaysUsed = itemView.findViewById(R.id.tvEmployeeFullDays);
            tvPaidLeavesUsed = itemView.findViewById(R.id.tvEmployeePaidLeaves);

            btnExpand = itemView.findViewById(R.id.btnExpand);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
        }
    }
}
