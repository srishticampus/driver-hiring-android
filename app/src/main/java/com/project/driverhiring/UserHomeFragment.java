package com.project.driverhiring;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;


public class UserHomeFragment extends Fragment {


    private LinearLayout tvHomeUser;
    private RadioGroup radioGroupPreference;
    private RadioButton rbUser;
    private RadioButton rbDriver;
    private RadioGroup radioGroupVehicleType;
    private RadioButton rbLmv;
    private RadioButton rbHgmv;
    private RelativeLayout btChooseVehicle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        initView(view);

        rbUser.setChecked(true);
        rbLmv.setChecked(true);


        btChooseVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int drivePref = radioGroupPreference.getCheckedRadioButtonId();
                int vehiclePref = radioGroupVehicleType.getCheckedRadioButtonId();
                String pref = "";
                String vehicle = "";

                switch (drivePref) {
                    case R.id.rb_oneway:
                        pref = "false";
                        break;
                    case R.id.rb_twoway:
                        pref = "true";
                }

                switch (vehiclePref) {
                    case R.id.rb_lmv:
                        vehicle = "false";
                        break;
                    case R.id.rb_hgmv:
                        vehicle = "true";
                }
                if (pref.equals("") || vehicle.equals("")) {
                    Toast.makeText(getContext(), "Select Preferences", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("vehicleType",vehicle);
                    editor.putString("drivePref",pref);
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MapActivityChooseDestination.class);
                   // Toast.makeText(getContext(), vehicle, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

//                PopupMenu popupMenu = new PopupMenu(getContext(), btChooseVehicle);
//                popupMenu.getMenuInflater().inflate(R.menu.vehicle_type_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        switch (item.getItemId()) {
//                            case R.id.lmv:
//                                Intent intentOne = new Intent(getActivity(), MapActivityChooseDestination.class);
//                                intentOne.putExtra("vehicleType", "LMV");
//                                startActivity(intentOne);
//                                break;
//                            case R.id.hgmv:
//                                Intent intentTwo = new Intent(getActivity(), MapActivityChooseDestination.class);
//                                intentTwo.putExtra("vehicleType", "HGMV");
//                                startActivity(intentTwo);
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popupMenu.show();
            }
        });


        return view;
    }

    private void initView(View view) {
        tvHomeUser = view.findViewById(R.id.tv_home_user);
        radioGroupPreference = view.findViewById(R.id.radio_group_preference);
        rbUser = view.findViewById(R.id.rb_oneway);
        rbDriver = view.findViewById(R.id.rb_twoway);
        radioGroupVehicleType = view.findViewById(R.id.radio_group_vehicle_type);
        rbLmv = view.findViewById(R.id.rb_lmv);
        rbHgmv = view.findViewById(R.id.rb_hgmv);
        btChooseVehicle = view.findViewById(R.id.bt_choose_vehicle);
    }
}