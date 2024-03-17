package com.project.driverhiring;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class UserTripDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvOne;
    private RelativeLayout layoutOne;
    private ShapeableImageView ivDriverProPic;
    private TextView tvDriverName;
    private TextView tvTripAmount;
    private AppCompatRatingBar ratingDriver;
    private TextView tvNoOfReviews;
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
    private RelativeLayout btStopTrip;
    private RelativeLayout startTripLayout;
    private TextView tvMarkStart;
    private RelativeLayout btStartTrip;
    private RelativeLayout paymentLayout;
    private TextView tvMarkPay;
    private RelativeLayout btPayment;
    private RelativeLayout reviewRatingLayout;
    private TextView howTxt;
    private RatingBar ratingBar;
    private EditText userReviewEt;
    private TextView ratingOrReviewSubmitBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout btCompleteTripOrStop;
    private RelativeLayout complaintLayout;
    private TextView tvDispute;
    private RelativeLayout btComplaint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trip_details);

        initView();

        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        // Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        String driverName = getIntent().getStringExtra("driverName");
        String driverImg = getIntent().getStringExtra("driverImg");
        String startLat = getIntent().getStringExtra("startLat");
        String startLong = getIntent().getStringExtra("startLong");
        String destLat = getIntent().getStringExtra("destLat");
        String destLong = getIntent().getStringExtra("startLong");
        String fare = getIntent().getStringExtra("fare");
        String distance = getIntent().getStringExtra("distance");
        String rideId = getIntent().getStringExtra("rideId");
        // String userId = getIntent().getStringExtra("userId");
        String vehicleType = getIntent().getStringExtra("vehicleType");
        String vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String comments = getIntent().getStringExtra("comments");
        String paymentStatus = getIntent().getStringExtra("paymentStatus");
        String rideStatus = getIntent().getStringExtra("rideStatus");
        String reviewStatus = getIntent().getStringExtra("reviewStatus");
        int noOfRating = getIntent().getIntExtra("no_of_reviews", 0);
        String driverRatingCount = getIntent().getStringExtra("driverRating");


        // Toast.makeText(this, startLat+startLong, Toast.LENGTH_SHORT).show();

        tvDriverName.setText(driverName);
        Glide.with(getApplicationContext()).load(driverImg).into(ivDriverProPic);
        tvTripAmount.setText(fare);
        tvKm.setText(distance);
        tvVehicleNumber.setText(vehicleNumber);
        tvTripDate.setText(date);
        tvReview.setText(comments);
        tvVehicleType.setText(vehicleType);
        tripStatus.setText(rideStatus);
        try {
            ratingDriver.setRating(Float.valueOf(String.valueOf(driverRatingCount)));
            tvNoOfReviews.setText(String.valueOf(noOfRating));
        } catch (Exception e) {

        }


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

        //visibility handling

        if (rideStatus.equals("Ride accepted")) {
            startTripLayout.setVisibility(View.VISIBLE);
        } else {
            startTripLayout.setVisibility(View.GONE);
        }
        if (rideStatus.equals("Ride in progress")) {
            finishTripLayout.setVisibility(View.VISIBLE);
        } else {
            finishTripLayout.setVisibility(View.GONE);
        }
        //
        if (reviewStatus.equals("true") && rideStatus.equals("Ride Completed") && paymentStatus.equals("Paid to admin")) {
            reviewLayout.setVisibility(View.VISIBLE);
            reviewRatingLayout.setVisibility(View.GONE);
        }
        if (reviewStatus.equals("false") && rideStatus.equals("Ride Completed") && paymentStatus.equals("Paid to admin")) {
            reviewLayout.setVisibility(View.GONE);
            reviewRatingLayout.setVisibility(View.VISIBLE);
        }
        if (paymentStatus.equals("Not paid") && rideStatus.equals("Ride Completed")) {
            paymentLayout.setVisibility(View.VISIBLE);
        } else {
            paymentLayout.setVisibility(View.GONE);
        }

        btStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideApi(userId, rideId, "start");
                //TODO add refresh
            }
        });
        btStopTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideApi(userId, rideId, "stop");
                //TODO add refresh
            }
        });
        btPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserTripDetailsActivity.this, UserPaymentActivity.class);
                intent.putExtra("rideId", rideId);
                intent.putExtra("fare", fare);
                startActivity(intent);
            }
        });

        ratingOrReviewSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addRatingReviewApi(userId, rideId, String.valueOf(ratingBar.getRating()), userReviewEt.getText().toString());

            }
        });

        btComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(UserTripDetailsActivity.this);
                dialog.setContentView(R.layout.complaint_dialog_box);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView submitComplaint = dialog.findViewById(R.id.complaint_submit_bt);
                EditText complaintEt = dialog.findViewById(R.id.et_complaint);
                submitComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userComplaintApi(rideId, userId, complaintEt.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


    }

    private void userComplaintApi(String rideId, String userId, String userComplaint) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.USER_COMPLAINT(rideId, userId, userComplaint).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(UserTripDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addRatingReviewApi(String userId, String rideId, String rating, String review) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.ADD_RATING_REVIEW_USER(userId, rideId, rating, review).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserTripDetailsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(UserTripDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rideApi(String userId, String rideId, String status) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.USER_RIDE_START_OR_STOP(userId, rideId, status).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserTripDetailsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserTripDetailsActivity.this, root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(UserTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                Toast.makeText(UserTripDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initView() {
        tvOne = findViewById(R.id.tv_one);
        layoutOne = findViewById(R.id.layout_one);
        ivDriverProPic = findViewById(R.id.iv_driver_pro_pic);
        tvDriverName = findViewById(R.id.tv_driver_name);
        tvTripAmount = findViewById(R.id.tv_trip_amount);
        ratingDriver = findViewById(R.id.rating_driver);
        tvNoOfReviews = findViewById(R.id.tv_no_of_reviews);
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
        btStopTrip = findViewById(R.id.bt_complete_trip_or_stop);
        startTripLayout = findViewById(R.id.start_trip_layout);
        tvMarkStart = findViewById(R.id.tv_mark_start);
        btStartTrip = findViewById(R.id.bt_start_trip);
        paymentLayout = findViewById(R.id.payment_layout);
        tvMarkPay = findViewById(R.id.tv_mark_pay);
        btPayment = findViewById(R.id.bt_payment);
        reviewRatingLayout = findViewById(R.id.review_rating_layout);
        howTxt = findViewById(R.id.how_txt);
        ratingBar = findViewById(R.id.rating_bar);
        userReviewEt = findViewById(R.id.user_review_et);
        ratingOrReviewSubmitBtn = findViewById(R.id.rating_or_review_submit_btn);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        complaintLayout = findViewById(R.id.complaint_layout);
        tvDispute = findViewById(R.id.tv_dispute);
        btComplaint = findViewById(R.id.bt_complaint);
    }

    @Override
    public void onRefresh() {
        // Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}