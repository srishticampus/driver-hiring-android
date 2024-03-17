package com.project.driverhiring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.driverhiring.model.Root;
import com.project.driverhiring.retrofit.APIInterface;
import com.project.driverhiring.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPaymentActivity extends AppCompatActivity {

    private TextView tvHead;
    private ImageView ivImgOne;
    private RelativeLayout layoutOne;
    private TextView tvOne;
    private TextInputLayout outlinedTextFieldOne;
    private TextInputLayout outlinedTextFieldTwo;
    private TextInputLayout outlinedTextFieldThree;
    private TextInputLayout outlinedTextFieldFour;
    private TextView payBtn;
    private TextInputEditText cardNumberTv;
    private TextInputEditText expiryDateTv;
    private TextInputEditText cvvTv;
    private TextInputEditText cardHolderTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        initView();

        String rideId = getIntent().getStringExtra("rideId");
        String fare = getIntent().getStringExtra("fare");

        payBtn.setText(fare);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardNumberTv.getText().toString().isEmpty() || expiryDateTv.getText().toString().isEmpty() ||
                        cvvTv.getText().toString().isEmpty() || cardHolderTv.getText().toString().isEmpty()) {
                    Toast.makeText(UserPaymentActivity.this, "Fill The Details", Toast.LENGTH_SHORT).show();
                } else {
                    payApi(rideId, fare);
                }

            }
        });

    }

    private void payApi(String rideId, String fare) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.PAYMENT_API_USER(rideId, fare).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserPaymentActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserPaymentActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserPaymentActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserPaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(UserPaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Toast.makeText(this, "Please complete Payment", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        tvHead = findViewById(R.id.tv_head);
        ivImgOne = findViewById(R.id.iv_img_one);
        layoutOne = findViewById(R.id.layout_one);
        tvOne = findViewById(R.id.tv_one);
        outlinedTextFieldOne = findViewById(R.id.outlinedTextField_one);
        outlinedTextFieldTwo = findViewById(R.id.outlinedTextField_two);
        outlinedTextFieldThree = findViewById(R.id.outlinedTextField_three);
        outlinedTextFieldFour = findViewById(R.id.outlinedTextField_four);
        payBtn = findViewById(R.id.pay_btn);
        cardNumberTv = findViewById(R.id.card_number_tv);
        expiryDateTv = findViewById(R.id.expiry_date_tv);
        cvvTv = findViewById(R.id.cvv_tv);
        cardHolderTv = findViewById(R.id.card_holder_tv);
    }
}