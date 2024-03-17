package com.project.driverhiring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener {

    private ImageView icon;
    @NotEmpty
    private EditText etNameSingIn;
    @NotEmpty
    @Length(max = 10, min = 10, message = "Enter a valid mobile number")
    private EditText etPhnSingIn;
    @NotEmpty
    @Email
    private EditText etEmailSingIn;
    @NotEmpty
    private EditText etPassSingIn;
    private RadioGroup radioGroup;
    private RadioButton rbUser;
    private RadioButton rbDriver;
    private LinearLayout layoutBtn;
    private TextView btSignIn;
    private LinearLayout login;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

        validator = new Validator(this);
        validator.setValidationListener(this);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    private void initView() {
        icon = findViewById(R.id.icon);
        etNameSingIn = findViewById(R.id.et_name_singIn);
        etPhnSingIn = findViewById(R.id.et_phn_singIn);
        etEmailSingIn = findViewById(R.id.et_email_singIn);
        etPassSingIn = findViewById(R.id.et_pass_singIn);
        radioGroup = findViewById(R.id.radio_group);
        rbUser = findViewById(R.id.rb_user);
        rbDriver = findViewById(R.id.rb_driver);
        layoutBtn = findViewById(R.id.layout_btn);
        btSignIn = findViewById(R.id.bt_signIn);
        login = findViewById(R.id.login);
    }

    @Override
    public void onValidationSucceeded() {

        int checkedId = radioGroup.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rb_user:
                Intent userIntent = new Intent(SignInActivity.this, UserSignin.class);
                userIntent.putExtra("name", etNameSingIn.getText().toString());
                userIntent.putExtra("phone", etPhnSingIn.getText().toString());
                userIntent.putExtra("email", etEmailSingIn.getText().toString());
                userIntent.putExtra("password", etPassSingIn.getText().toString());
                startActivity(userIntent);
                break;
            case R.id.rb_driver:
                Intent driverIntent = new Intent(SignInActivity.this, DriverSignin.class);
                driverIntent.putExtra("name", etNameSingIn.getText().toString());
                driverIntent.putExtra("phone", etPhnSingIn.getText().toString());
                driverIntent.putExtra("email", etEmailSingIn.getText().toString());
                driverIntent.putExtra("password", etPassSingIn.getText().toString());
                startActivity(driverIntent);
                break;
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }

    }
}