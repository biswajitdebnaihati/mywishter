package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exnovation.wishster.Models.ProductListModel;
import com.exnovation.wishster.Models.WishListModel;
import com.exnovation.wishster.Models.WishListSubModel;
import com.exnovation.wishster.Models.wishListModel2;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.SpacesItemDecoration;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity {
    RequestBody bodyRequest;
    RecyclerView wishRv;
    MasonryAdapter masonryAdapter;
    ApiManager manager = new ApiManager();
    List<WishListSubModel> imgList = new ArrayList<>();
    Loader loader = new Loader();
    Call<wishListModel2> coll2=null;
    RequestBody bodyRequest2;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        wishRv = findViewById(R.id.wish_rv);
        ivBack = findViewById(R.id.iv_back);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        (findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });



            //getUserWishlist();
        try {
            showProductList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
       /* Intent ref = new Intent(this, WishListActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(ref);
        overridePendingTransition(0,0);*/

    }

    private void showProductList() throws JSONException {
        loader.showDialog(this);
       /* JSONObject scancode_obj=new JSONObject();
        scancode_obj.put("scancode",scanCode);
        Log.d("errMsgAAA","TOKEN \t"+ App.token);
        bodyRequest2 = RequestBody.create(MediaType.parse("application/json"), scancode_obj.toString());*/
        coll2=manager.service.getwishList();
        coll2.enqueue(new Callback<wishListModel2>()
        {
            @Override
            public void onResponse(Call<wishListModel2> call, Response<wishListModel2> response)
            {
               // Toast.makeText(getApplicationContext(), ""+response.body().isStatus(), Toast.LENGTH_SHORT).show();
                Log.d("pppppdata","A \t"+response.body().isStatus());
                loader.hideDialog();
                if (response.body().isStatus()){

                    imgList = response.body().getData();
                    Log.d("pppppdata",""+imgList.get(0).pic_url);
                    wishRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

                    if (imgList.size()>0){
                        masonryAdapter = new MasonryAdapter(WishListActivity.this, imgList);

                        wishRv.setAdapter(masonryAdapter);
                        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
                        wishRv.addItemDecoration(decoration);
                    }else {
                        Toast.makeText(WishListActivity.this, "You don't have any item in your wish list", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }

            }

            @Override
            public void onFailure(Call<wishListModel2> call, Throwable t)
            {
                loader.hideDialog();
                finish();
                Toast.makeText(WishListActivity.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                Log.d("errMsg",""+t.getMessage());
            }
        });


    }


    public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {

        private Context context;
        List<WishListSubModel> list;

   /* String[] nameList = {"One", "Two", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine", "Ten"};*/

        public MasonryAdapter(Context context, List<WishListSubModel> imgList) {
            this.context = context;
            this.list = imgList;
        }

        @Override
        public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_row, parent, false);
            MasonryView masonryView = new MasonryView(layoutView);
            return masonryView;
        }

        @Override
        public void onBindViewHolder(MasonryView holder, final int position) {
           // holder.imageView.setImageResource(list.get(position));
           // Glide.with(context).load(list.get(position).pic_url).into(holder.imageView);
            //holder.textView.setText(nameList[position]);

            Picasso.with(WishListActivity.this).load(list.get(position).pic_url)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.imageView);

            holder.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(WishListActivity.this, WishProductDetails.class);
                    i.putExtra("img", list.get(position).pic_url);
                    i.putExtra("des", list.get(position).title);
                    i.putExtra("price", list.get(position).price);
                    i.putExtra("u_id", list.get(position).user_id);
                    i.putExtra("url", list.get(position).url);
                    i.putExtra("item", list.get(position).item_id);
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MasonryView extends RecyclerView.ViewHolder {
            ImageView imageView;
            LinearLayout mainLay;
            // TextView textView;

            public MasonryView(View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.img);
                mainLay = (LinearLayout) itemView.findViewById(R.id.main_lay);
                // textView = (TextView) itemView.findViewById(R.id.img_name);

            }
        }
    }

}
