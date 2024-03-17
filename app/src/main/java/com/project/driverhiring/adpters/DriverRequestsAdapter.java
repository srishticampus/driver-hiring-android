package com.project.driverhiring.adpters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.driverhiring.DriverHomeActivity;
import com.project.driverhiring.DriverRequestsActivity;
import com.project.driverhiring.R;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverRequestsAdapter extends RecyclerView.Adapter<DriverRequestsAdapter.MyViewHolder> {

    Root root;
    Context context;


    public DriverRequestsAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_driver_pending_requests, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("driverPref", MODE_PRIVATE);
        String driverId = sharedPreferences.getString("driverId", "");

        holder.tvDriverName.setText(root.notRideList.get(position).user_name);
        holder.tvTripAmount.setText(String.valueOf(root.notRideList.get(position).fare));
        holder.tripKm.setText(String.valueOf(root.notRideList.get(position).distance));
        holder.timeTv.setText(root.notRideList.get(position).time);
        holder.dateTv.setText(root.notRideList.get(position).date);


        Geocoder geocoder;
        List<Address> startingAddresses;
        List<Address> destinationAddresses;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            startingAddresses = geocoder.getFromLocation(Double.parseDouble(root.notRideList.get(position).start_point_lat),
                    Double.parseDouble(root.notRideList.get(position).start_point_long), 1);
            String startingAddress = startingAddresses.get(0).getAddressLine(0);

            destinationAddresses = geocoder.getFromLocation(Double.parseDouble(root.notRideList.get(position).start_point_lat),
                    Double.parseDouble(root.notRideList.get(position).start_point_long), 1);
            String destinationAddress = destinationAddresses.get(0).getAddressLine(0);

            holder.tvFrom.setText(startingAddress);
            holder.tvTo.setText(destinationAddress);

            //   destinationAddresses=geocoder.getFromLocation()

        } catch (Exception e) {

        }

        holder.userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + root.notRideList.get(position).start_point_lat + ","
                        + root.notRideList.get(position).start_point_long + "?Z=8f");
                // Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?Z=15f");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        holder.destinationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + root.notRideList.get(position).dest_lat + ","
                        + root.notRideList.get(position).dest_long + "?Z=8f");
                // Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?Z=15f");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });


        Glide.with(context).load(root.notRideList.get(position).photo).into(holder.ivDriverProPic);
        holder.acceptReqBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideAprovalApi(root.notRideList.get(position).ride_id, driverId, "true");
                // Toast.makeText(context, driverId, Toast.LENGTH_SHORT).show();
            }
        });
        holder.rejectReqBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideAprovalApi(root.notRideList.get(position).ride_id, driverId, "false");
            }
        });

    }

    private void rideAprovalApi(String rideId, String driverId, String status) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.RIDE_ACCEPT_OR_REJECT(rideId, driverId, status).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(context, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context, DriverHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.notRideList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutOne;
        private ShapeableImageView ivDriverProPic;
        private TextView tvDriverName;
        private TextView tvTripAmount;
        private TextView tripKm;
        private TextView tvKm;
        private RelativeLayout layoutTwo;
        private RelativeLayout layoutThree;
        private TextView tvFrom;
        private Button rejectReqBt;
        private View viewOne;
        private RelativeLayout layoutFour;
        private TextView tvTo, dateTv, timeTv, userLocation, destinationLocation;
        private Button acceptReqBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }

        private void initView(View itemView) {
            layoutOne = itemView.findViewById(R.id.layout_one);
            ivDriverProPic = itemView.findViewById(R.id.iv_driver_pro_pic);
            tvDriverName = itemView.findViewById(R.id.tv_driver_name);
            tvTripAmount = itemView.findViewById(R.id.tv_trip_amount);
            tripKm = itemView.findViewById(R.id.trip_km);
            tvKm = itemView.findViewById(R.id.tv_km);
            layoutTwo = itemView.findViewById(R.id.layout_two);
            layoutThree = itemView.findViewById(R.id.layout_three);
            tvFrom = itemView.findViewById(R.id.tv_from);
            rejectReqBt = itemView.findViewById(R.id.reject_req_bt);
            viewOne = itemView.findViewById(R.id.view_one);
            layoutFour = itemView.findViewById(R.id.layout_four);
            tvTo = itemView.findViewById(R.id.tv_to);
            acceptReqBt = itemView.findViewById(R.id.accept_req_bt);
            dateTv = itemView.findViewById(R.id.requested_date);
            timeTv = itemView.findViewById(R.id.requested_time);
            destinationLocation = itemView.findViewById(R.id.destination_location_on_map);
            userLocation = itemView.findViewById(R.id.user_location_on_map);
        }
    }
}
