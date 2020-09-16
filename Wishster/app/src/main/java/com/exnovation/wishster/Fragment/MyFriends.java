package com.exnovation.wishster.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exnovation.wishster.Models.AllFriendsModel;
import com.exnovation.wishster.Models.OnlineFriendsModel;
import com.exnovation.wishster.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriends extends Fragment {
    RecyclerView onlineRv, myFriendsRv;
    OnlineAdapter oAdapter;
    List<OnlineFriendsModel> onlineFriendList = new ArrayList<>();
    List<AllFriendsModel> allFriendList = new ArrayList<>();
    AllFriendsAdapter afAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_friends_layout, container, false);

        onlineRv = v.findViewById(R.id.rv_online);
        myFriendsRv = v.findViewById(R.id.my_friends_rv);

        OnlineFriendsModel ofM = new OnlineFriendsModel();
        ofM.userImg = R.drawable.profile_image;
        onlineFriendList.add(ofM);
        OnlineFriendsModel ofM1 = new OnlineFriendsModel();
        ofM1.userImg = R.drawable.profile_image;
        onlineFriendList.add(ofM1);
        OnlineFriendsModel ofM2 = new OnlineFriendsModel();
        ofM2.userImg = R.drawable.profile_image;
        onlineFriendList.add(ofM2);
        OnlineFriendsModel ofM3 = new OnlineFriendsModel();
        ofM3.userImg = R.drawable.profile_image;
        onlineFriendList.add(ofM3);

        AllFriendsModel afM = new AllFriendsModel();
        afM.user_img = R.drawable.profile_image;
        afM.event_stat = 1;
        allFriendList.add(afM);

        AllFriendsModel afM1 = new AllFriendsModel();
        afM1.user_img = R.drawable.profile_image;
        afM1.event_stat = 0;
        allFriendList.add(afM1);

        AllFriendsModel afM2 = new AllFriendsModel();
        afM2.user_img = R.drawable.profile_image;
        afM2.event_stat = 0;
        allFriendList.add(afM2);

        AllFriendsModel afM3 = new AllFriendsModel();
        afM3.user_img = R.drawable.profile_image;
        afM3.event_stat = 1;
        allFriendList.add(afM3);

        AllFriendsModel afM4 = new AllFriendsModel();
        afM4.user_img = R.drawable.profile_image;
        afM4.event_stat = 1;
        allFriendList.add(afM4);

        oAdapter = new OnlineAdapter(getActivity(), onlineFriendList);
        onlineRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        onlineRv.setAdapter(oAdapter);


        afAdapter = new AllFriendsAdapter(getActivity(), allFriendList);
        myFriendsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myFriendsRv.setAdapter(afAdapter);




        return v;
    }

    public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.Hold>{
        Context context;
        List<OnlineFriendsModel> list;

        public OnlineAdapter(FragmentActivity activity, List<OnlineFriendsModel> onlineFriendList) {
            this.context = activity;
            this.list = onlineFriendList;
        }

        @NonNull
        @Override
        public OnlineAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_row_lay, parent, false);
            return new Hold(v);
        }

        @Override
        public void onBindViewHolder(@NonNull OnlineAdapter.Hold holder, int position) {

            Glide.with(getContext()).load(list.get(position).userImg).into(holder.userImg);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Hold extends RecyclerView.ViewHolder {

            CircleImageView userImg;

            public Hold(@NonNull View itemView) {
                super(itemView);
                userImg = itemView.findViewById(R.id.iv_online);
            }
        }
    }


    public class AllFriendsAdapter extends RecyclerView.Adapter<AllFriendsAdapter.Hold>{
        Context context2;
        List<AllFriendsModel> list;

        public AllFriendsAdapter(FragmentActivity activity, List<AllFriendsModel> allFriendList) {
            this.context2 = activity;
            this.list = allFriendList;
        }

        @NonNull
        @Override
        public AllFriendsAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_all_friends_row_lay, parent, false);
            return new Hold(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AllFriendsAdapter.Hold holder, int position) {

            AllFriendsModel afModel = list.get(position);

            Glide.with(getContext()).load(afModel.user_img).into(holder.userImg);
            //holder.uName.setText();

            if (afModel.event_stat == 0){
                holder.icEvent.setVisibility(View.GONE);
            }else{
                holder.icEvent.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Hold extends RecyclerView.ViewHolder {
            CircleImageView userImg;
            TextView uName;
            ImageView icEvent;
            public Hold(@NonNull View itemView) {
                super(itemView);

                userImg = itemView.findViewById(R.id.user_img);
                uName = itemView.findViewById(R.id.u_name);
                icEvent = itemView.findViewById(R.id.iv_event);

            }
        }
    }
}
