package com.drapp.expensetracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drapp.expensetracker.Entities.CategorySpending;
import com.drapp.expensetracker.R;

import java.util.List;

public class CategorySpendingAdapter extends RecyclerView.Adapter<CategorySpendingAdapter.CategorySpendingViewHolder> {

    private List<CategorySpending> categorySpendings;

    public CategorySpendingAdapter(List<CategorySpending> categorySpendings) {
        this.categorySpendings = categorySpendings;
    }

    @NonNull
    @Override
    public CategorySpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_spending_item, parent, false);
        return new CategorySpendingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorySpendingViewHolder holder, int position) {
        CategorySpending categorySpending = categorySpendings.get(position);
        holder.bind(categorySpending);
    }

    @Override
    public int getItemCount() {
        return categorySpendings != null ? categorySpendings.size() : 0;
    }

    public void setCategorySpendings(List<CategorySpending> categorySpendings) {
        this.categorySpendings = categorySpendings;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }

    static class CategorySpendingViewHolder extends RecyclerView.ViewHolder {

        private TextView textCategory;
        private TextView textSpending;

        public CategorySpendingViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textViewCategory);
            textSpending = itemView.findViewById(R.id.textViewTotalSpent);
        }

        public void bind(CategorySpending categorySpending) {
            // Check if categorySpending is not null
            if (categorySpending != null) {
                // Set text on TextView objects
                textCategory.setText(categorySpending.getCategory());
                textSpending.setText(String.valueOf(categorySpending.getTotalSpent()));
            }
        }
    }
}
