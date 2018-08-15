package com.example.michellebiol.sampleapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.AccountsActivity;
import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.HomeActivity;
import com.example.michellebiol.sampleapp.Interfaces.ILoginUserApi;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.Models.AccountsItem;
import com.example.michellebiol.sampleapp.Models.TokenRequest;
import com.example.michellebiol.sampleapp.Models.TokenResponse;
import com.example.michellebiol.sampleapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountsAdapter  extends RecyclerView.Adapter<AccountsAdapter.ViewHolder>{
    private List<AccountsItem> accountsItems;
    private Context context;
    private static String current_user_id;
    private View v;


    public AccountsAdapter(List<AccountsItem> accountsItems, Context context) {
        this.accountsItems = accountsItems;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accounts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AccountsItem accountsItem = accountsItems.get(position);
        SharedPreferences sharedPref2 = context.getSharedPreferences("tokens", Context.MODE_PRIVATE);
        current_user_id = sharedPref2
                .getString("user_id","");
        holder.usernameHead.setText(accountsItem.getUsername());
        if (current_user_id.equals(String.valueOf(accountsItem.getId()))) {
            holder.userId.setText("Current use");
         } else {
            holder.accountLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    current_user_id = String.valueOf(accountsItem.getId());
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                    alertBuilder.setTitle("Are you sure?");
                    alertBuilder.setMessage("Do you want to use this account?");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loginRequest(accountsItem.getUsername());
                            //get the life of the user in the shared preferences
                        }
                    });
                    alertBuilder.setNegativeButton("Choose again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            });
        }


    }
    @Override
    public int getItemCount() {
        return accountsItems.size();
    }

    private void loginRequest(String player_name)
    {
        //process of login request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ILoginUserApi services = retrofit.create(ILoginUserApi.class);
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setUsername(player_name);
        tokenRequest.setPassword(player_name);
        Call<TokenResponse> tokenResponseCall = services.loginRequest(tokenRequest);
        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()){
                    TokenResponse tokenResponse = response.body();
                    String token_type = tokenResponse.getToken_type();
                    String token = tokenResponse.getAccess_token();
                    String user_id = tokenResponse.getId();
                    SharedPreferences sharedPref = context.getSharedPreferences("tokens",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token_type",token_type);
                    editor.putString("token",token);
                    editor.putString("user_id",user_id);
                    editor.apply();
                    Intent intent = new Intent(context , HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                 }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) { }
        });
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView usernameHead;
        public TextView userId;
        public LinearLayout accountLinearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            usernameHead = (TextView) itemView.findViewById(R.id.usernameHead);
            userId = (TextView) itemView.findViewById(R.id.userId);
            accountLinearLayout = (LinearLayout) itemView.findViewById(R.id.accountsLinearLayout);
        }
    }

}
