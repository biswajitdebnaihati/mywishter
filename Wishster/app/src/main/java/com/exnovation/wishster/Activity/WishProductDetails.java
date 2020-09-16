package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exnovation.wishster.Models.DeleteWishlistModel;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.Loader;
import com.squareup.picasso.NetworkPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishProductDetails extends AppCompatActivity {

    ImageView ivProduct;
    TextView tvDes, tvPrice;
    Button btnBuy, btnRemove;
    String ivP, des, price, url, uid, itemId;
    ImageView backIv;
    RequestBody bodyRequest;
    ApiManager manager = new ApiManager();
    JSONObject params=new JSONObject();
    Loader loader = new Loader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_product_details);

        ivProduct = findViewById(R.id.iv_product);
        tvDes = findViewById(R.id.tv_des);
        tvPrice = findViewById(R.id.tv_price);
        btnBuy = findViewById(R.id.btn_buy);
        btnRemove = findViewById(R.id.btn_remove);
        backIv = findViewById(R.id.back_iv);

        ivP = getIntent().getStringExtra("img");
        des = getIntent().getStringExtra("des");
        price = getIntent().getStringExtra("price");
        url = getIntent().getStringExtra("url");
        uid = getIntent().getStringExtra("u_id");
        itemId = getIntent().getStringExtra("item");

        Glide.with(this).load(ivP).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(ivProduct);
        tvDes.setText(des);
        tvPrice.setText("Price : "+price);

        (findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WishProductDetails.this,ActivityWebView.class).putExtra("url", url));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromWishlist();
            }
        });





    }

    private void deleteFromWishlist() {
        loader.showDialog(this);
        try {
            params.put("item_id", itemId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), params.toString());
        manager.service.deleteWishlist(bodyRequest).enqueue(new Callback<DeleteWishlistModel>() {
            @Override
            public void onResponse(Call<DeleteWishlistModel> call, Response<DeleteWishlistModel> response) {
                if (response.isSuccessful()){
                    loader.hideDialog();
                    if (response.body().status){

                        Intent i = new Intent(WishProductDetails.this, WishListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(WishProductDetails.this, response.body().message, Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(WishProductDetails.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    loader.hideDialog();
                    Toast.makeText(WishProductDetails.this,"No response from the server! Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteWishlistModel> call, Throwable t) {
                loader.hideDialog();
                Toast.makeText(WishProductDetails.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
