package com.project.driverhiring.adpters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.driverhiring.DriverTripDetailsActivity;
import com.project.driverhiring.R;
import com.project.driverhiring.model.Root;

import java.util.List;
import java.util.Locale;

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.MyViewHolder> {

    Context context;
    Root root;


    public DriverHistoryAdapter(Context context, Root root) {
        this.context = context;
        this.root = root;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_driver_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.workshopName.setText(root.workshopDetails.get(position).workshop_name);
//        holder.workshopDistanceTv.setText(root.workshopDetails.get(position).distance);
//        Glide.with(context).load(root.workshopDetails.get(position).file).into(holder.workshopImg);

        holder.tvDriverName.setText(root.driverHistory.get(position).user_name);
        holder.tvTripAmount.setText(String.valueOf(root.driverHistory.get(position).fare));
        holder.tripKm.setText(String.valueOf(root.driverHistory.get(position).distance));
        holder.tripStatus.setText(root.driverHistory.get(position).status);
        Glide.with(context).load(root.driverHistory.get(position).photo).into(holder.ivDriverProPic);

        Geocoder geocoder;
        List<Address> startingAddresses;
        List<Address> destinationAddresses;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            startingAddresses = geocoder.getFromLocation(Double.parseDouble(root.driverHistory.get(position).start_point_lat),
                    Double.parseDouble(root.driverHistory.get(position).start_point_long), 1);
            String startingAddress = startingAddresses.get(0).getAddressLine(0);

            destinationAddresses = geocoder.getFromLocation(Double.parseDouble(root.driverHistory.get(position).start_point_lat),
                    Double.parseDouble(root.driverHistory.get(position).start_point_long), 1);
            String destinationAddress = destinationAddresses.get(0).getAddressLine(0);

            holder.tvFrom.setText(startingAddress);
            holder.tvTo.setText(destinationAddress);

            //   destinationAddresses=geocoder.getFromLocation()

        } catch (Exception e) {

        }

        holder.viewUserOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + root.driverHistory.get(position).start_point_lat + ","
                        + root.driverHistory.get(position).start_point_long + "?Z=8f");
                // Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?Z=15f");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DriverTripDetailsActivity.class);
                intent.putExtra("userName", root.driverHistory.get(position).user_name);
                intent.putExtra("userImg", root.driverHistory.get(position).photo);
                intent.putExtra("startLat", root.driverHistory.get(position).start_point_lat);
                intent.putExtra("startLong", root.driverHistory.get(position).start_point_long);
                intent.putExtra("destLat", root.driverHistory.get(position).dest_lat);
                intent.putExtra("destLong", root.driverHistory.get(position).dest_long);
                intent.putExtra("fare", root.driverHistory.get(position).fare);
                intent.putExtra("distance", root.driverHistory.get(position).distance);
                intent.putExtra("rideId", root.driverHistory.get(position).id);
                intent.putExtra("driverId", root.driverHistory.get(position).driver_id);
                intent.putExtra("vehicleType", root.driverHistory.get(position).vehicle_type);
                intent.putExtra("vehicleNumber", root.driverHistory.get(position).vehicle_no);
                intent.putExtra("date", root.driverHistory.get(position).date);
                intent.putExtra("time", root.driverHistory.get(position).time);
                intent.putExtra("comments", root.driverHistory.get(position).comments);
                intent.putExtra("rideStatus", root.driverHistory.get(position).status);
                intent.putExtra("userPhone", root.driverHistory.get(position).user_phoneNumber);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.driverHistory.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutOne;
        private ShapeableImageView ivDriverProPic;
        private TextView tvDriverName;
        private TextView tvTripAmount;
        private AppCompatRatingBar ratingDriver;
        private TextView tvNoOfReviews;
        private TextView tripKm;
        private TextView tvKm;
        private RelativeLayout layoutTwo;
        private RelativeLayout layoutThree;
        private TextView tvFrom;
        private TextView tripStatus;
        private View viewOne;
        private RelativeLayout layoutFour;
        private TextView tvTo, viewUserOnMap;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);


        }

        private void initView(View itemView) {
            layoutOne = itemView.findViewById(R.id.layout_one);
            ivDriverProPic = itemView.findViewById(R.id.iv_driver_pro_pic);
            tvDriverName = itemView.findViewById(R.id.tv_driver_name);
            tvTripAmount = itemView.findViewById(R.id.tv_trip_amount);
            ratingDriver = itemView.findViewById(R.id.rating_driver);
            tvNoOfReviews = itemView.findViewById(R.id.tv_no_of_reviews);
            tripKm = itemView.findViewById(R.id.trip_km);
            tvKm = itemView.findViewById(R.id.tv_km);
            layoutTwo = itemView.findViewById(R.id.layout_two);
            layoutThree = itemView.findViewById(R.id.layout_three);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tripStatus = itemView.findViewById(R.id.trip_status);
            viewOne = itemView.findViewById(R.id.view_one);
            layoutFour = itemView.findViewById(R.id.layout_four);
            tvTo = itemView.findViewById(R.id.tv_to);
            viewUserOnMap = itemView.findViewById(R.id.view_user_on_map);


        }
    }
}
