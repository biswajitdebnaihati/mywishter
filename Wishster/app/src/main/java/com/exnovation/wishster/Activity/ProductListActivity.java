package com.exnovation.wishster.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.ProductListModel;
import com.exnovation.wishster.Models.ProductListSubModel;
import com.exnovation.wishster.Models.SaveToWishlistModel;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView productRv;
    ProductAdapter pAdapter;
    List<ProductListSubModel> pList = new ArrayList<>();
    String scanCode = "";
    ApiManager manager = new ApiManager();
    RequestBody bodyRequest,bodyRequest2;
    JSONObject params=new JSONObject();
    Loader loader = new Loader();
    Call<ProductListModel> coll2=null;
    ApiManager apiManager=new ApiManager();
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        iv_back=findViewById(R.id.iv_back);
        Intent i=getIntent();
       /* if (i!=null)
            scanCode = i.getStringExtra("barcode");
        else {
            Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_SHORT).show();
            //finish();
        }*/
        scanCode ="8906104550408";
                productRv = findViewById(R.id.product_rv);

        try {
            showProductList(scanCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        (findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

    }

    private void callLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setMessage("Please signup / login to continue ");
        builder.setTitle("Note");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               startActivity(new Intent(ProductListActivity.this,Login.class));
               finish();
            }
        });
       // builder.setNeutralButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProductList(String scanCode) throws JSONException {
        loader.showDialog(ProductListActivity.this);
        JSONObject scancode_obj=new JSONObject();
        scancode_obj.put("scancode",scanCode);
        //Log.d("errMsgAAA",""+scancode_obj.toString());
        bodyRequest2 = RequestBody.create(MediaType.parse("application/json"), scancode_obj.toString());
        coll2=apiManager.service.getProductList(bodyRequest2);
        coll2.enqueue(new Callback<ProductListModel>()
        {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response)
            {
                if (response.isSuccessful()){
                    loader.hideDialog();
                    if (response.body().status.equals("true")){
                        pList = response.body().data;

                        if (pList.size() > 0){
                            pAdapter = new ProductAdapter(ProductListActivity.this, pList);
                            productRv.setLayoutManager(new LinearLayoutManager(ProductListActivity.this, LinearLayoutManager.VERTICAL, false));
                            productRv.setAdapter(pAdapter);
                        }

                    }else {

                        Toast.makeText(ProductListActivity.this, "A\n"+response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    loader.hideDialog();
                    Toast.makeText(ProductListActivity.this, "B\n"+"No Response from the sever! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t)
            {
                Log.d("errMsg","AAA : "+t.getMessage());
                loader.hideDialog();
                Toast.makeText(ProductListActivity.this, "C\n"+"Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Hold> {

        List<ProductListSubModel> list;
        Context context;

        public ProductAdapter(ProductListActivity productListActivity, List<ProductListSubModel> pList) {
            this.context = productListActivity;
            this.list = pList;
        }

        @NonNull
        @Override
        public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
            return new Hold(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Hold holder, int position) {

            final ProductListSubModel plsM = list.get(position);

            Glide.with(context).load(plsM.pic_url).into(holder.ivProduct);
            holder.tvPname.setText(plsM.title);
            holder.tvPprice.setText(plsM.price);

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (!(App.loginStatus))
                 startActivity(new Intent(ProductListActivity.this,ActivityWebView.class).putExtra("url", plsM.url));
                else
                    callLogin();
                }
            });

            holder.whishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (!(App.loginStatus))
                            saveWish(plsM.item_id, plsM.pic_url, plsM.url, plsM.title, plsM.price);
                        else
                            callLogin();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Hold extends RecyclerView.ViewHolder {

            ImageView ivProduct;
            TextView tvPname, tvPprice;
            Button buy, whishlist;

            public Hold(@NonNull View itemView) {
                super(itemView);

                ivProduct = itemView.findViewById(R.id.iv_product);
                tvPname = itemView.findViewById(R.id.tv_pname);
                tvPprice = itemView.findViewById(R.id.tv_price);
                buy = itemView.findViewById(R.id.btn_buy);
                whishlist = itemView.findViewById(R.id.btn_wish);

            }
        }
    }

    private void saveWish(String item_id, String pic_url, String url, String title, String price) throws JSONException {
        loader.showDialog(ProductListActivity.this);
        params.put("item_id", item_id);
        params.put("pic_url", pic_url);
        params.put("url", url);
        params.put("title", title);
        params.put("price", price);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), params.toString());
        manager.service.saveToWishlist(bodyRequest).enqueue(new Callback<SaveToWishlistModel>() {
            @Override
            public void onResponse(Call<SaveToWishlistModel> call, Response<SaveToWishlistModel> response) {
                if (response.isSuccessful()) {
                    loader.hideDialog();
                    if (response.body().status){
                        Toast.makeText(ProductListActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProductListActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loader.hideDialog();
                    Toast.makeText(ProductListActivity.this,"No response from the server! Please try again...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SaveToWishlistModel> call, Throwable t) {
                loader.hideDialog();
                Toast.makeText(ProductListActivity.this, "Network Error! Please check your internet connection and try again.\n"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}