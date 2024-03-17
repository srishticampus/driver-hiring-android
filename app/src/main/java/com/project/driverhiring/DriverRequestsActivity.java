package com.project.driverhiring;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.driverhiring.adpters.DriverRequestsAdapter;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRequestsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RelativeLayout noPendingRequestLayout;
    private SwipeRefreshLayout swipeRefreshLayoutOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_requests);
        initView();

        SharedPreferences sharedPreferences = getSharedPreferences("driverPref", MODE_PRIVATE);
        String driverId = sharedPreferences.getString("driverId", "");


        apiCall(driverId);

        swipeRefreshLayoutOne.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiCall(driverId);
                swipeRefreshLayoutOne.setRefreshing(false);
            }
        });


    }

    private void apiCall(String driverId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.PENDING_REQUESTS_DRIVER(driverId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        if (root.message.equals("failed")) {
                            noPendingRequestLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noPendingRequestLayout.setVisibility(View.GONE);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            DriverRequestsAdapter driverHistoryAdapter = new DriverRequestsAdapter(root, getApplicationContext());
                            recyclerView.setAdapter(driverHistoryAdapter);

                        }

                    } else {
                        Toast.makeText(DriverRequestsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DriverRequestsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(DriverRequestsActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        noPendingRequestLayout = findViewById(R.id.no_pending_request_layout);
        swipeRefreshLayoutOne = findViewById(R.id.swipe_refresh_layout_one);
    }
}