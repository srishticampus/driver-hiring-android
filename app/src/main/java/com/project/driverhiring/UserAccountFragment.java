package com.project.driverhiring;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
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


public class UserAccountFragment extends Fragment {


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

    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;
    private File proImageFile;
    private TextView logoutBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        viewProfileApi(userId);

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
                editProfileApi(userId);
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogout(userId);
            }
        });

        return view;
    }

    private void userLogout(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.USER_LOGOUT(userId).enqueue(new Callback<Root>() {
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

    private void editProfileApi(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        RequestBody rUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
        RequestBody rEmail = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody rAddress = RequestBody.create(MediaType.parse("text/plain"), etAddress.getText().toString());
        MultipartBody.Part proImageFilePart = null;
        try {
            proImageFilePart = MultipartBody.Part.createFormData("icon",
                    proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"),
                            proImageFile));

        } catch (NullPointerException e) {

        }


        api.EDIT_USER_PROFILE(rUserId, rName, rEmail, rAddress, proImageFilePart).enqueue(new Callback<Root>() {
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

    private void viewProfileApi(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_USER_PROFILE(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        etName.setText(root.userProfileView.get(0).user_name);
                        etEmail.setText(root.userProfileView.get(0).email);
                        etAddress.setText(root.userProfileView.get(0).address);
                        Glide.with(getContext()).load(root.userProfileView.get(0).photo).into(ivUserProPic);
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
        ivUserProPic = view.findViewById(R.id.iv_user_pro_pic);
        imgUploadBt = view.findViewById(R.id.img_upload_bt);
        layoutThree = view.findViewById(R.id.layout_three);
        etName = view.findViewById(R.id.et_name);
        layoutFour = view.findViewById(R.id.layout_four);
        etEmail = view.findViewById(R.id.et_email);
        layoutFive = view.findViewById(R.id.layout_five);
        etAddress = view.findViewById(R.id.et_address);
        logoutBt = view.findViewById(R.id.logout_bt);
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

                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                ivUserProPic.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
    }

}