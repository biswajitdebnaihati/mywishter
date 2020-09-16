package com.exnovation.wishster.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exnovation.wishster.Models.AllFriendsModel;
import com.exnovation.wishster.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedUsers extends AppCompatActivity {

    RecyclerView blockRv;
    List<AllFriendsModel> blockList = new ArrayList<>();
    BlockAdapter bAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        blockRv = findViewById(R.id.blocked_rv);

        AllFriendsModel afM = new AllFriendsModel();
        afM.user_img = R.drawable.user1;
        afM.event_stat = 1;
        blockList.add(afM);

        AllFriendsModel afM1 = new AllFriendsModel();
        afM1.user_img = R.drawable.user2;
        afM1.event_stat = 0;
        blockList.add(afM1);

        AllFriendsModel afM2 = new AllFriendsModel();
        afM2.user_img = R.drawable.user1;
        afM2.event_stat = 0;
        blockList.add(afM2);

        AllFriendsModel afM3 = new AllFriendsModel();
        afM3.user_img = R.drawable.user3;
        afM3.event_stat = 1;
        blockList.add(afM3);

        AllFriendsModel afM4 = new AllFriendsModel();
        afM4.user_img = R.drawable.user2;
        afM4.event_stat = 1;
        blockList.add(afM4);

        bAdapter = new BlockAdapter(BlockedUsers.this, blockList);
        blockRv.setLayoutManager(new LinearLayoutManager(BlockedUsers.this, LinearLayoutManager.VERTICAL, false));
        blockRv.setAdapter(bAdapter);


    }

    public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.Hold>{

        Context context;
        List<AllFriendsModel> list;

        public BlockAdapter(BlockedUsers blockedUsers, List<AllFriendsModel> blockList) {
            this.context = blockedUsers;
            this.list = blockList;
        }


        @NonNull
        @Override
        public BlockAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_friends_row, parent, false);
            return new Hold(v);
        }

        @Override
        public void onBindViewHolder(@NonNull BlockAdapter.Hold holder, int position) {

            AllFriendsModel afModel = list.get(position);

            Glide.with(BlockedUsers.this).load(afModel.user_img).into(holder.userImg);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Hold extends RecyclerView.ViewHolder {

            CircleImageView userImg;
            TextView uName;
            public Hold(@NonNull View itemView) {
                super(itemView);
                userImg = itemView.findViewById(R.id.user_img);
                uName = itemView.findViewById(R.id.u_name);
            }
        }
    }

}
