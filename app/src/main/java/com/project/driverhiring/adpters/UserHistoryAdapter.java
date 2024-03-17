package com.project.driverhiring.adpters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.project.driverhiring.R;
import com.project.driverhiring.UserTripDetailsActivity;
import com.project.driverhiring.model.Root;

import java.util.List;
import java.util.Locale;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.MyViewHolder> {

    Context context;
    Root root;


    public UserHistoryAdapter(Context context, Root root) {
        this.context = context;
        this.root = root;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_user_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.workshopName.setText(root.workshopDetails.get(position).workshop_name);
//        holder.workshopDistanceTv.setText(root.workshopDetails.get(position).distance);
//        Glide.with(context).load(root.workshopDetails.get(position).file).into(holder.workshopImg);

        holder.tvDriverName.setText(root.userHistory.get(position).driver_name);
        holder.tvTripAmount.setText(String.valueOf(root.userHistory.get(position).fare));
        holder.tripKm.setText(String.valueOf(root.userHistory.get(position).distance));
        Glide.with(context).load(root.userHistory.get(position).driver_photo).into(holder.ivDriverProPic);
        holder.tripStatus.setText(root.userHistory.get(position).ride_status);
        try {
            holder.ratingDriver.setRating(Float.valueOf(String.valueOf(root.userHistory.get(position).driver_rating)));
            holder.tvNoOfReviews.setText(String.valueOf(root.userHistory.get(position).no_of_ratings));
        } catch (Exception e) {

        }



        Geocoder geocoder;
        List<Address> startingAddresses;
        List<Address> destinationAddresses;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            startingAddresses = geocoder.getFromLocation(Double.parseDouble(root.userHistory.get(position).start_point_lat),
                    Double.parseDouble(root.userHistory.get(position).start_point_long), 1);
            String startingAddress = startingAddresses.get(0).getAddressLine(0);

            destinationAddresses = geocoder.getFromLocation(Double.parseDouble(root.userHistory.get(position).start_point_lat),
                    Double.parseDouble(root.userHistory.get(position).start_point_long), 1);
            String destinationAddress = destinationAddresses.get(0).getAddressLine(0);

            holder.tvFrom.setText(startingAddress);
            holder.tvTo.setText(destinationAddress);

            //   destinationAddresses=geocoder.getFromLocation()

        } catch (Exception e) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserTripDetailsActivity.class);

                intent.putExtra("driverName", root.userHistory.get(position).driver_name);
                intent.putExtra("driverImg", root.userHistory.get(position).driver_photo);
                intent.putExtra("startLat", root.userHistory.get(position).start_point_lat);
                intent.putExtra("startLong", root.userHistory.get(position).start_point_long);
                intent.putExtra("destLat", root.userHistory.get(position).dest_lat);
                intent.putExtra("destLong", root.userHistory.get(position).dest_long);
                intent.putExtra("fare", String.valueOf(root.userHistory.get(position).fare));
                intent.putExtra("distance", root.userHistory.get(position).distance);
                intent.putExtra("rideId", root.userHistory.get(position).id);
                intent.putExtra("reviewStatus", root.userHistory.get(position).review_status);
                intent.putExtra("vehicleType", root.userHistory.get(position).vehicle_type);
                intent.putExtra("vehicleNumber", root.userHistory.get(position).vehicle_no);
                intent.putExtra("date", root.userHistory.get(position).date);
                intent.putExtra("time", root.userHistory.get(position).time);
                intent.putExtra("comments", root.userHistory.get(position).comments);
                intent.putExtra("rideStatus", root.userHistory.get(position).ride_status);
                intent.putExtra("paymentStatus", root.userHistory.get(position).payment_status);
                intent.putExtra("no_of_reviews",root.userHistory.get(position).no_of_ratings);
                intent.putExtra("driverRating",root.userHistory.get(position).driver_rating);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.userHistory.size();
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
        private TextView tvTo;

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
        }
    }
}
