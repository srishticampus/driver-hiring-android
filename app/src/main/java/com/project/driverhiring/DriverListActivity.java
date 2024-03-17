package com.project.driverhiring;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.driverhiring.adpters.DriversListAdapter;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);
        initView();


        SharedPreferences sharedPref = getSharedPreferences("userPref", MODE_PRIVATE);

        String vehicleType=sharedPref.getString("vehicleType","");
        String driverPreference=sharedPref.getString("drivePref","");
        String destinationLat=sharedPref.getString("destination_lat","");
        String destinationLongitude=sharedPref.getString("destination_longitude","");
        String userLatitude=sharedPref.getString("user_latitude","");
        String userLongitude=sharedPref.getString("user_longitude","");
        String userId=sharedPref.getString("userId","");


        // Toast.makeText(this, getIntent().getStringExtra("vehicleType"), Toast.LENGTH_SHORT).show();
        driverListApiCall(userLatitude,userLongitude,destinationLat,destinationLongitude,vehicleType,driverPreference);

    }

    private void driverListApiCall(String userLatitude,String userLongitude,String destinationLat,String destinationLongitude,String vehicleType,String driverPreference) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        //TODO change hardcorded values
        api.DRIVER_LIST_CALL(userLatitude, userLongitude, destinationLat, destinationLongitude, vehicleType, driverPreference).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        DriversListAdapter driversListAdapter = new DriversListAdapter(getApplicationContext(), root);
                        recyclerView.setAdapter(driversListAdapter);
                    }else {
                        Toast.makeText(DriverListActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(DriverListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(DriverListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }
}