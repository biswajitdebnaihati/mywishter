package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.exnovation.wishster.R;

public class Friends_user_profile extends AppCompatActivity {
    RecyclerView recyclerView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_user_profile);
        init();
        action();
    }
    private void action() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        MasonryAdapter adapter = new MasonryAdapter(this);
        recyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }
    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.masonry_grid);
        ivBack = (ImageView) findViewById(R.id.iv_back);
    }

    public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {

        private Context context;

        int[] imgList = { R.drawable.da, R.drawable.db, R.drawable.dc,
                R.drawable.dd, R.drawable.de, R.drawable.dc, R.drawable.da,
                R.drawable.da, R.drawable.da, R.drawable.da,
                R.drawable.dc, R.drawable.de, R.drawable.df, R.drawable.da,
                R.drawable.da, R.drawable.db, R.drawable.dc,
                R.drawable.dd, R.drawable.de, R.drawable.df, R.drawable.dc,
        };

   /* String[] nameList = {"One", "Two", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine", "Ten"};*/

        public MasonryAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MasonryView onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_items, parent, false);
            MasonryView masonryView = new MasonryView(layoutView);
            return masonryView;
        }

        @Override
        public void onBindViewHolder(MasonryView holder, int position) {
            holder.imageView.setImageResource(imgList[position]);
            //holder.textView.setText(nameList[position]);
        }

        @Override
        public int getItemCount() {
            return imgList.length;
        }

        class MasonryView extends RecyclerView.ViewHolder {
            ImageView imageView;
            // TextView textView;

            public MasonryView(View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.img);
                // textView = (TextView) itemView.findViewById(R.id.img_name);

            }
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space)
        {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }
}