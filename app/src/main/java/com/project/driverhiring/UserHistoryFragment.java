package com.project.driverhiring;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.driverhiring.adpters.UserHistoryAdapter;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserHistoryFragment extends Fragment {


    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_history, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");


        apiCall(userId);

        return view;
    }

    private void apiCall(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.USER_RIDE_HISTORY(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {

                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter(getContext(), root);
                        recyclerView.setAdapter(userHistoryAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Error Occurred While Fetching User History", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}