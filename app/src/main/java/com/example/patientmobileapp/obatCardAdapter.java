package com.example.patientmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class obatCardAdapter extends RecyclerView.Adapter<obatCardAdapter.ViewHolder> implements Filterable {

    private List<obat_card> itemList;
    private List<obat_card> itemListFull;

    public obatCardAdapter(List<obat_card> itemList) {
        this.itemList = itemList;
        this.itemListFull = new ArrayList<>(itemList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle1, subtitle2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            subtitle1 = itemView.findViewById(R.id.textSubtitle);
            subtitle2 = itemView.findViewById(R.id.textSubtitle2);
        }
    }

    @NonNull
    @Override
    public obatCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.obat_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull obatCardAdapter.ViewHolder holder, int position) {
        obat_card item = itemList.get(position);
        holder.title.setText(item.getName());
        holder.subtitle1.setText(item.getForm());
        holder.subtitle2.setText(item.getStrength());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<obat_card> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (obat_card item : itemListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getForm().toLowerCase().contains(filterPattern) ||
                            item.getStrength().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
