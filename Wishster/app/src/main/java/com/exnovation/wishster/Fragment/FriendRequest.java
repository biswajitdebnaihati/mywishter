package com.exnovation.wishster.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exnovation.wishster.Models.AllFriendsModel;
import com.exnovation.wishster.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequest extends Fragment {
    RecyclerView requestRv;
    RequestAdapter reqAdapter;
    List<AllFriendsModel> requestList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_request_lay, container, false);

        requestRv = v.findViewById(R.id.request_rv);

        AllFriendsModel afM = new AllFriendsModel();
        afM.user_img = R.drawable.profile_image;
        afM.event_stat = 1;
        requestList.add(afM);

        AllFriendsModel afM1 = new AllFriendsModel();
        afM1.user_img = R.drawable.profile_image;
        afM1.event_stat = 0;
        requestList.add(afM1);

        AllFriendsModel afM2 = new AllFriendsModel();
        afM2.user_img = R.drawable.profile_image;
        afM2.event_stat = 0;
        requestList.add(afM2);

        AllFriendsModel afM3 = new AllFriendsModel();
        afM3.user_img = R.drawable.profile_image;
        afM3.event_stat = 1;
        requestList.add(afM3);

        AllFriendsModel afM4 = new AllFriendsModel();
        afM4.user_img = R.drawable.profile_image;
        afM4.event_stat = 1;
        requestList.add(afM4);

        reqAdapter = new RequestAdapter(getActivity(), requestList);
        requestRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        requestRv.setAdapter(reqAdapter);
        return v;
    }
    public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Hold>{

        Context context;
        List<AllFriendsModel> list;

        public RequestAdapter(FragmentActivity activity, List<AllFriendsModel> requestList) {

            this.context = activity;
            this.list = requestList;

        }

        @NonNull
        @Override
        public RequestAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_row, parent, false);
            return new Hold(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestAdapter.Hold holder, int position) {

            AllFriendsModel afModel = list.get(position);

            Glide.with(getContext()).load(afModel.user_img).into(holder.userImg);

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
