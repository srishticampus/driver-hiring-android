package com.project.driverhiring;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverTripDetailsActivity extends AppCompatActivity {

    private TextView tvOne;
    private RelativeLayout layoutOne;
    private ShapeableImageView ivDriverProPic;
    private TextView tvUserName;
    private TextView tvTripAmount;
    private TextView tripKm;
    private TextView tvKm;
    private LinearLayout layoutFive;
    private TextView tvTripDate;
    private RelativeLayout layoutTwo;
    private RelativeLayout layoutThree;
    private TextView tvFrom;
    private TextView tripStatus;
    private View viewOne;
    private RelativeLayout layoutFour;
    private TextView tvTo;
    private View viewTwo;
    private RelativeLayout layoutSix;
    private TextView tvVehicleType;
    private View viewThree;
    private RelativeLayout layoutSeven;
    private TextView tvVehicleNumber;
    private View viewFour;
    private RelativeLayout reviewLayout;
    private TextView tvReview;
    private RelativeLayout finishTripLayout;
    private TextView tvMark;
    private RelativeLayout btConfirmLocation;
    private RelativeLayout btFinishTrip;
    private RelativeLayout startTripLayoutDriver;
    private TextView tvMarkStart;
    private RelativeLayout btStartTripDriver, callBtn;
    private RelativeLayout complaintLayout;
    private TextView tvDispute;
    private RelativeLayout btComplaintDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_details);
        initView();

        String userName = getIntent().getStringExtra("userName");
        String userImg = getIntent().getStringExtra("userImg");
        String startLat = getIntent().getStringExtra("startLat");
        String startLong = getIntent().getStringExtra("startLong");
        String destLat = getIntent().getStringExtra("destLat");
        String destLong = getIntent().getStringExtra("destLong");
        String fare = String.valueOf(getIntent().getIntExtra("fare", 0));
        String distance = String.valueOf(getIntent().getDoubleExtra("distance", 0.0d));
        String rideId = getIntent().getStringExtra("rideId");
        String driverId = getIntent().getStringExtra("driverId");
        String vehicleType = getIntent().getStringExtra("vehicleType");
        String vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String comments = getIntent().getStringExtra("comments");
        String rideStatus = getIntent().getStringExtra("rideStatus");
        String userPhoneNumber = getIntent().getStringExtra("userPhone");


        Glide.with(getApplicationContext()).load(userImg).into(ivDriverProPic);
        tvUserName.setText(userName);
        tvTripAmount.setText(fare);
        tripKm.setText(distance);
        tripStatus.setText(rideStatus);
        tvVehicleType.setText(vehicleType);
        tvVehicleNumber.setText(vehicleNumber);
        tvReview.setText(comments);
        tvTripDate.setText(date);

        Geocoder geocoder;
        List<Address> startingAddresses;
        List<Address> destinationAddresses;

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            startingAddresses = geocoder.getFromLocation(Double.parseDouble(startLat),
                    Double.parseDouble(startLong), 1);
            String startingAddress = startingAddresses.get(0).getAddressLine(0);

            destinationAddresses = geocoder.getFromLocation(Double.parseDouble(destLat),
                    Double.parseDouble(destLong), 1);
            String destinationAddress = destinationAddresses.get(0).getAddressLine(0);

            tvFrom.setText(startingAddress);
            tvTo.setText(destinationAddress);

            //   destinationAddresses=geocoder.getFromLocation()

        } catch (Exception e) {

        }

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + userPhoneNumber));//change the number
                startActivity(callIntent);
            }
        });


        if (rideStatus.equals("Ride accepted")) {
            startTripLayoutDriver.setVisibility(View.VISIBLE);
        } else {
            startTripLayoutDriver.setVisibility(View.GONE);
        }
        if (rideStatus.equals("Ride in progress")) {
            finishTripLayout.setVisibility(View.VISIBLE);
        } else {
            finishTripLayout.setVisibility(View.GONE);
        }

        btStartTripDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideApi(rideId, driverId, "start");
            }
        });
        btFinishTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideApi(rideId, driverId, "stop");
            }
        });

        //TODO start or finish button taking driver to driver home

        btComplaintDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(DriverTripDetailsActivity.this);
                dialog.setContentView(R.layout.complaint_dialog_box);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView submitComplaint = dialog.findViewById(R.id.complaint_submit_bt);
                EditText complaintEt = dialog.findViewById(R.id.et_complaint);
                submitComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        driverComplaintApi(rideId, driverId, complaintEt.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private void driverComplaintApi(String rideId, String driverId, String driverComplaint) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.DRIVER_COMPLAINT(rideId, driverId, driverComplaint).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(DriverTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DriverTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DriverTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(DriverTripDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rideApi(String rideId, String driverId, String status) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.RIDE_START_OR_STOP_DRIVER(rideId, driverId, status).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(DriverTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DriverTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DriverTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                Toast.makeText(DriverTripDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView() {
        tvOne = findViewById(R.id.tv_one);
        layoutOne = findViewById(R.id.layout_one);
        ivDriverProPic = findViewById(R.id.iv_driver_pro_pic);
        tvUserName = findViewById(R.id.tv_user_name);
        tvTripAmount = findViewById(R.id.tv_trip_amount);
        tripKm = findViewById(R.id.trip_km);
        tvKm = findViewById(R.id.tv_km);
        layoutFive = findViewById(R.id.layout_five);
        tvTripDate = findViewById(R.id.tv_trip_date);
        layoutTwo = findViewById(R.id.layout_two);
        layoutThree = findViewById(R.id.layout_three);
        tvFrom = findViewById(R.id.tv_from);
        tripStatus = findViewById(R.id.trip_status);
        viewOne = findViewById(R.id.view_one);
        layoutFour = findViewById(R.id.layout_four);
        tvTo = findViewById(R.id.tv_to);
        viewTwo = findViewById(R.id.view_two);
        layoutSix = findViewById(R.id.layout_six);
        tvVehicleType = findViewById(R.id.tv_vehicle_type);
        viewThree = findViewById(R.id.view_three);
        layoutSeven = findViewById(R.id.layout_seven);
        tvVehicleNumber = findViewById(R.id.tv_vehicle_number);
        viewFour = findViewById(R.id.view_four);
        reviewLayout = findViewById(R.id.review_layout);
        tvReview = findViewById(R.id.tv_review);
        finishTripLayout = findViewById(R.id.finish_trip_layout);
        tvMark = findViewById(R.id.tv_mark);
        btConfirmLocation = findViewById(R.id.bt_confirm_location);
        btFinishTrip = findViewById(R.id.bt_finish_trip);
        startTripLayoutDriver = findViewById(R.id.start_trip_layout_driver);
        tvMarkStart = findViewById(R.id.tv_mark_start);
        btStartTripDriver = findViewById(R.id.bt_start_trip_driver);
        complaintLayout = findViewById(R.id.complaint_layout);
        tvDispute = findViewById(R.id.tv_dispute);
        btComplaintDriver = findViewById(R.id.bt_complaint_driver);
        callBtn = findViewById(R.id.call_btn);
    }
}