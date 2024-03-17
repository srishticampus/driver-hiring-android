package com.project.driverhiring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignin extends AppCompatActivity implements Validator.ValidationListener {


    private CircleImageView imgProfilePic;
    private TextView tvUserName;
    private CircleImageView editProImg;
    @NotEmpty
    private EditText etChooseId;
    private ImageView chooseIdOne;
    private ImageView chooseIdTwo;
    @NotEmpty
    private EditText etAddressSingIn;
    private LinearLayout layoutBtn;
    private TextView btSignIn;
    private LinearLayout login;
    String userName, phoneNumber, email, password, licenceImagePicturePath;
    private Validator validator;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;
    private static final int RESULT_LOAD_LICENSE_IMAGE = 105;
    private File proImageFile, licenseImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signin);
        initView();

        validator = new Validator(this);
        validator.setValidationListener(this);


        userName = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        tvUserName.setText(userName);

        editProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });

        etChooseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UserSignin.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_LICENSE_IMAGE);
                }
            }
        });


        layoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });


    }

    private void initView() {
        imgProfilePic = findViewById(R.id.img_profile_pic);
        tvUserName = findViewById(R.id.tv_user_name);
        editProImg = findViewById(R.id.edit_pro_img);
        etChooseId = findViewById(R.id.et_choose_id);
        chooseIdOne = findViewById(R.id.choose_id_one);
        chooseIdTwo = findViewById(R.id.choose_id_two);
        etAddressSingIn = findViewById(R.id.et_address_singIn);
        layoutBtn = findViewById(R.id.layout_btn);
        btSignIn = findViewById(R.id.bt_signIn);
        login = findViewById(R.id.login);
    }

    @Override
    public void onValidationSucceeded() {

        apiCall();

    }

    private void apiCall() {

        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody rPhone = RequestBody.create(MediaType.parse("text/plain"), phoneNumber);
        RequestBody rEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody rPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody rAddress = RequestBody.create(MediaType.parse("text/plain"), etAddressSingIn.getText().toString());
        MultipartBody.Part proImageFilePart = null;
        MultipartBody.Part licensePicFilePart = MultipartBody.Part.createFormData("icon",
                licenseImageFile.getName(),
                RequestBody.create(MediaType.parse("image/*"),
                        licenseImageFile));
        try {
            proImageFilePart = MultipartBody.Part.createFormData("avatar",
                    proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"),
                            proImageFile));

        } catch (NullPointerException e) {

        }
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.USERREGROOTCALL(rName, rEmail, rPhone, rPassword, rAddress, licensePicFilePart, proImageFilePart).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserSignin.this, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserSignin.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserSignin.this, root.message, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(UserSignin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(UserSignin.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getApplicationContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
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
                Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);

                final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                imgProfilePic.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
        if (requestCode == RESULT_LOAD_LICENSE_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            licenceImagePicturePath = cursor.getString(columnIndex);
            cursor.close();
            licenseImageFile = new File(licenceImagePicturePath);
            etChooseId.setText(licenceImagePicturePath);
        }
    }
}