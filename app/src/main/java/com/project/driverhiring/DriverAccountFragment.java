package com.project.driverhiring;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverAccountFragment extends Fragment {


    private TextView tvAccount;
    private RelativeLayout submitBt;
    private LinearLayout layoutTwo;
    private TextView tvImg;
    private ShapeableImageView ivUserProPic;
    private TextView imgUploadBt;
    private LinearLayout layoutThree;
    private EditText etName;
    private LinearLayout layoutFour;
    private EditText etEmail;
    private LinearLayout layoutFive;
    private EditText etAddress;
    private LinearLayout layoutSix;
    private EditText etLanguagesKnown;
    private LinearLayout layoutSeven;
    private EditText etAccountNumber;
    private LinearLayout layoutEight;
    private EditText etIfscCode;
    private TextView logoutBt;

    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;
    private File proImageFile;
    String picturePathPath;
    private TextView driverPreferenceEdit;
    private LinearLayout driverPrefProfile;
    private RadioGroup radioGroupOnewayTwowayViewProfile;
    private RadioButton rbOneWayPreferenceViewProfile;
    private RadioButton rbTwoWayPreferenceViewProfile;
    String prefe = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_account, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("driverPref", MODE_PRIVATE);
        String driverId = sharedPreferences.getString("driverId", "");

        // Toast.makeText(getContext(), driverId, Toast.LENGTH_SHORT).show();

        viewProfileApi(driverId);


        imgUploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileApi(driverId);
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverLogout(driverId);
            }
        });



        return view;
    }

    private void driverLogout(String driverId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.DRIVER_LOGOUT(driverId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {

                        SharedPreferences sP = getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
                        SharedPreferences.Editor speditor = sP.edit();
                        speditor.putBoolean("session", false);
                        speditor.putString("role", "");
                        speditor.commit();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);

                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editProfileApi(String driverId) {

        int drivePreference = radioGroupOnewayTwowayViewProfile.getCheckedRadioButtonId();
        switch (drivePreference) {
            case R.id.rb_oneWay_preference_view_profile:
                prefe = "false";
                break;
            case R.id.rb_twoWay_preference_view_profile:
                prefe = "true";
                break;
        }

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        RequestBody rDriverId = RequestBody.create(MediaType.parse("text/plain"), driverId);
        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
        RequestBody rEmail = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody rAddress = RequestBody.create(MediaType.parse("text/plain"), etAddress.getText().toString());
        RequestBody rLanguagesKnown = RequestBody.create(MediaType.parse("text/plain"), etLanguagesKnown.getText().toString());
        RequestBody rAccountNumber = RequestBody.create(MediaType.parse("text/plain"), etAccountNumber.getText().toString());
        RequestBody rIfscCode = RequestBody.create(MediaType.parse("text/plain"), etIfscCode.getText().toString());
        RequestBody rDrivePref = RequestBody.create(MediaType.parse("text/plain"), prefe);


        MultipartBody.Part proImageFilePart = null;

        try {
            proImageFilePart = MultipartBody.Part.createFormData("icon",
                    proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"),
                            proImageFile));

        } catch (NullPointerException e) {

        }

        api.EDIT_DRIVER_PROFILE(rDriverId, rName, rAddress, rEmail, rLanguagesKnown,
                rAccountNumber, rIfscCode,rDrivePref, proImageFilePart).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewProfileApi(String driverId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_DRIVER_PROFILE(driverId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        etName.setText(root.driverProfileView.get(0).driver_name);
                        etEmail.setText(root.driverProfileView.get(0).email);
                        etAddress.setText(root.driverProfileView.get(0).address);
                        etLanguagesKnown.setText(root.driverProfileView.get(0).language);
                        etAccountNumber.setText(root.driverProfileView.get(0).account_no);
                        etIfscCode.setText(root.driverProfileView.get(0).ifsc);
                        Glide.with(getContext()).load(root.driverProfileView.get(0).photo).into(ivUserProPic);
                      //  Toast.makeText(getContext(), prefe, Toast.LENGTH_SHORT).show();

                        if (root.driverProfileView.get(0).route_preference.equals("true")) {
                            rbTwoWayPreferenceViewProfile.setChecked(true);
                            prefe = "true";
                        }else {
                            rbOneWayPreferenceViewProfile.setChecked(true);
                            prefe = "false";
                        }
                    } else {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initView(View view) {
        tvAccount = view.findViewById(R.id.tv_account);
        submitBt = view.findViewById(R.id.submit_bt);
        layoutTwo = view.findViewById(R.id.layout_two);
        tvImg = view.findViewById(R.id.tv_img);
        ivUserProPic = view.findViewById(R.id.iv_driver_pro_pic);
        imgUploadBt = view.findViewById(R.id.img_upload_bt);
        layoutThree = view.findViewById(R.id.layout_three);
        etName = view.findViewById(R.id.et_name);
        layoutFour = view.findViewById(R.id.layout_four);
        etEmail = view.findViewById(R.id.et_email);
        layoutFive = view.findViewById(R.id.layout_five);
        etAddress = view.findViewById(R.id.et_address);
        layoutSix = view.findViewById(R.id.layout_six);
        etLanguagesKnown = view.findViewById(R.id.et_languages_known);
        layoutSeven = view.findViewById(R.id.layout_seven);
        etAccountNumber = view.findViewById(R.id.et_account_number);
        layoutEight = view.findViewById(R.id.layout_eight);
        etIfscCode = view.findViewById(R.id.et_ifsc_code);
        logoutBt = view.findViewById(R.id.logout_bt);
        driverPreferenceEdit = view.findViewById(R.id.driver_preference_edit);
        driverPrefProfile = view.findViewById(R.id.driver_pref_profile);
        radioGroupOnewayTwowayViewProfile = view.findViewById(R.id.radio_group_oneway_twoway_view_profile);
        rbOneWayPreferenceViewProfile = view.findViewById(R.id.rb_oneWay_preference_view_profile);
        rbTwoWayPreferenceViewProfile = view.findViewById(R.id.rb_twoWay_preference_view_profile);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);
                // picturePathPath = cursor.getString(columnIndex);

                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                ivUserProPic.setImageBitmap(selectedImageBit);
                // Toast.makeText(getContext(), picturePathPath, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

            }
        }
    }


}