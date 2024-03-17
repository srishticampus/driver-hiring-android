package com.project.driverhiring;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverHomeFragment extends Fragment {

    private RelativeLayout startBtn;
    private RelativeLayout stopBtn;
    private RelativeLayout viewRequestBtn;
    private RelativeLayout workingStatusTxt;
    private TextView workingStatusTv;
    private SwitchMaterial driverStatusSwitch;
    private LottieAnimationView lottieWorking,lottieNotWorking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("driverPref", Context.MODE_PRIVATE);
        String driverId = sharedPreferences.getString("driverId", "");

        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, 101);
            }

        }catch (Exception e){

        }


        viewDriverAvailabilityApiCall(driverId);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCall("true", driverId);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCall("false", driverId);
            }
        });

        viewRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DriverRequestsActivity.class);
                startActivity(intent);
            }
        });

        driverStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    apiCall("true", driverId);
                }else {
                    apiCall("false", driverId);
                }
            }
        });


        return view;
    }

    private void viewDriverAvailabilityApiCall(String driverId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_DRIVER_AVAILABILITY(driverId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        if (root.message.equals("Available")) {
                            //  Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                            driverStatusSwitch.setChecked(true);
                            workingStatusTv.setText("Working");
                            lottieWorking.setVisibility(View.VISIBLE);
                            lottieNotWorking.setVisibility(View.GONE);
                        } else {
                            driverStatusSwitch.setChecked(false);
                            workingStatusTv.setText("Not Working");
                            lottieWorking.setVisibility(View.GONE);
                            lottieNotWorking.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apiCall(String online, String driverId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.UPDATE_DRIVER_AVAILABILITY(online, driverId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {

                        switch (online) {
                            case "true":
                                driverStatusSwitch.setChecked(true);
                                workingStatusTv.setText("Working");
                                lottieWorking.setVisibility(View.VISIBLE);
                                lottieNotWorking.setVisibility(View.GONE);
                                break;
                            case "false":
                                driverStatusSwitch.setChecked(false);
                                workingStatusTv.setText("Not Working");
                                lottieWorking.setVisibility(View.GONE);
                                lottieNotWorking.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Failed To Activate", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), "Failed To Activate", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to check and request permission.
    public void checkPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView(View view) {
        startBtn = view.findViewById(R.id.start_working_bt);
        stopBtn = view.findViewById(R.id.stop_working_bt);
        viewRequestBtn = view.findViewById(R.id.view_requestBtn);
        workingStatusTxt = view.findViewById(R.id.working_status_layout);
        workingStatusTv = view.findViewById(R.id.working_status_tv);
        driverStatusSwitch = view.findViewById(R.id.driver_status_switch);
        lottieWorking=view.findViewById(R.id.lottie_working);
        lottieNotWorking=view.findViewById(R.id.lottie_not_working);

    }
}