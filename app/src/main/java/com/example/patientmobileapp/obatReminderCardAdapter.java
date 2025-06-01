package com.example.patientmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class obatReminderCardAdapter extends RecyclerView.Adapter<obatReminderCardAdapter.ViewHolder> {
    private final List<obat_reminder_card> reminderList;
    private final Context context;
    private final OnItemClickListener listener;

    public obatReminderCardAdapter(Context context, List<obat_reminder_card> reminderList, OnItemClickListener listener) {
        this.context = context;
        this.reminderList = reminderList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(obat_reminder_card item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.obat_reminder_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        obat_reminder_card reminder = reminderList.get(position);

        holder.namaReminder.setText(reminder.title);
        holder.dosisReminder.setText(reminder.subtitle1);
        holder.waktuReminder.setText(reminder.subtitle2);
        holder.timeReminder.setText(reminder.subtitle3);
        holder.checkboxReminder.setChecked(reminder.isChecked);

        holder.checkboxReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reminder.isChecked = isChecked;
        });

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(reminder);

            if (context instanceof ResumeMedisActivity &&
                    context instanceof FragmentActivity &&
                    ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.resumeMedisFrameLayout) instanceof RawatJalanFragment) {

                Intent intent = new Intent(context, ResumeMedisDetailActivity.class);
                intent.putExtra("reminder_record", reminder.record.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaReminder, dosisReminder, waktuReminder, timeReminder;
        CheckBox checkboxReminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaReminder = itemView.findViewById(R.id.namaReminder);
            dosisReminder = itemView.findViewById(R.id.dosisReminder);
            waktuReminder = itemView.findViewById(R.id.waktuReminder);
            timeReminder = itemView.findViewById(R.id.timeReminder);
            checkboxReminder = itemView.findViewById(R.id.checkboxReminder);
        }
    }
}
