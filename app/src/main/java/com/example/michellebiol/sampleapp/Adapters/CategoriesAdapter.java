package com.example.michellebiol.sampleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.Models.CategoriesItem;
import com.example.michellebiol.sampleapp.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private List<CategoriesItem> categoriesItems;
    private Context context;

    public CategoriesAdapter(List<CategoriesItem> categoriesItems, Context context) {
        this.categoriesItems = categoriesItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View v = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.categories,parent,false);

         return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategoriesItem categoriesItem = categoriesItems.get(position);
        holder.textViewId.setText(categoriesItem.getId());
        holder.textViewHead.setText(categoriesItem.getHead());
        holder.textViewDesc.setText(categoriesItem.getDesc());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , CategoryQuestion.class);
                intent.putExtra("category_id",categoriesItem.getId());
                intent.putExtra("category",categoriesItem.getHead());
                intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewHead;
        public TextView textViewId;
        public TextView textViewDesc;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
