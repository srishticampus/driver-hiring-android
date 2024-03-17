package com.project.driverhiring.adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.driverhiring.DriverDetailsActivity;
import com.project.driverhiring.R;
import com.project.driverhiring.model.Root;

public class DriversListAdapter extends RecyclerView.Adapter<DriversListAdapter.MyViewHolder> {

    Context context;
    Root root;


    public DriversListAdapter(Context context, Root root) {
        this.context = context;
        this.root = root;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_driver_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.workshopName.setText(root.workshopDetails.get(position).workshop_name);
//        holder.workshopDistanceTv.setText(root.workshopDetails.get(position).distance);
        Glide.with(context).load(root.driverList.get(position).profilepic).into(holder.ivDriverProPic);
        holder.tvDriverName.setText(root.driverList.get(position).name);
        holder.tvDriverExp.setText(root.driverList.get(position).experience);

        try {
            holder.ratingDriver.setRating(Float.valueOf(String.valueOf(root.driverList.get(position).driver_rating)));
            holder.tvNoOfReviews.setText(String.valueOf(root.userHistory.get(position).no_of_ratings));
        } catch (Exception e) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SharedPreferences sharedPref = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
//                SharedPreferences.Editor myEditor = sharedPref.edit();
//                myEditor.putString("driverId", root.driverList.get(position).address);

                Intent intent = new Intent(context, DriverDetailsActivity.class);
                intent.putExtra("driver_id",root.driverList.get(position).id);
                intent.putExtra("driver_name",root.driverList.get(position).name);
                intent.putExtra("driver_rating", String.valueOf(root.driverList.get(position).driver_rating));
                intent.putExtra("driver_no_of_rating",root.driverList.get(position).number_of_rating);
                intent.putExtra("driver_phone",root.driverList.get(position).phone);
                intent.putExtra("driver_mail",root.driverList.get(position).email);
                intent.putExtra("driver_address",root.driverList.get(position).address);
                intent.putExtra("driver_district",root.driverList.get(position).district);
                intent.putExtra("driver_state",root.driverList.get(position).state);
                intent.putExtra("driverImage",root.driverList.get(position).profilepic);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.driverList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView ivDriverProPic;
        private TextView tvDriverName;
        private TextView tvDriverExp;
        private AppCompatRatingBar ratingDriver;
        private TextView tvNoOfReviews;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }

        private void initView(View itemView) {
            ivDriverProPic = itemView.findViewById(R.id.iv_driver_pro_pic);
            tvDriverName = itemView.findViewById(R.id.tv_driver_name);
            tvDriverExp = itemView.findViewById(R.id.tv_driver_exp);
            ratingDriver = itemView.findViewById(R.id.rating_driver);
            tvNoOfReviews = itemView.findViewById(R.id.tv_no_of_reviews);
        }
    }
}
