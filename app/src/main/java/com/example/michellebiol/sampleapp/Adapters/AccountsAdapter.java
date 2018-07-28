package com.example.michellebiol.sampleapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Models.AccountsItem;
import com.example.michellebiol.sampleapp.R;

import java.util.List;

public class AccountsAdapter  extends RecyclerView.Adapter<AccountsAdapter.ViewHolder>{
    private List<AccountsItem> accountsItems;
    private Context context;

    public AccountsAdapter(List<AccountsItem> accountsItems, Context context) {
        this.accountsItems = accountsItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accounts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AccountsItem accountsItem = accountsItems.get(position);
        holder.usernameHead.setText(accountsItem.getUsername());
        holder.accountLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You click " + accountsItem.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView usernameHead;
        public LinearLayout accountLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameHead = (TextView) itemView.findViewById(R.id.usernameHead);
            accountLinearLayout = (LinearLayout) itemView.findViewById(R.id.accountsLinearLayout);
        }
    }
}
