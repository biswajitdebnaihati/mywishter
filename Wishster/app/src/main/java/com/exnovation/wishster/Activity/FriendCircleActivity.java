package com.exnovation.wishster.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.exnovation.wishster.Fragment.AllUsers;
import com.exnovation.wishster.Fragment.BlockList;
import com.exnovation.wishster.Fragment.Chat;
import com.exnovation.wishster.Fragment.FriendRequest;
import com.exnovation.wishster.Fragment.FriendSearchResult;
import com.exnovation.wishster.Fragment.MyFriends;
import com.exnovation.wishster.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

public class FriendCircleActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabs;
    ViewPagerAdapter viewPagerAdapter;
    ImageView ivBack;
    private BottomSheetBehavior mBottomSheetBehavior;
    View close_search,search,search_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        viewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);
        ivBack = findViewById(R.id.iv_back);
        search_container = findViewById(R.id.search_container);
        search=findViewById(R.id.search);
        close_search=findViewById(R.id.close_search);
        mBottomSheetBehavior = BottomSheetBehavior.from(search_container);

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

        FragmentManager manager=getSupportFragmentManager();
        viewPagerAdapter=new ViewPagerAdapter(manager);
        viewPager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        search_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}});

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        searchFragmentCall();
    }

    private void searchFragmentCall()
    {
        Fragment fsr=new FriendSearchResult();
        FragmentManager fm = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fsr);
        fragmentTransaction.commit();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.drawable.ic_all);
        tabs.getTabAt(1).setIcon(R.drawable.ic_friend_req);
        tabs.getTabAt(2).setIcon(R.drawable.ic_my_friends);
        tabs.getTabAt(3).setIcon(R.drawable.ic_chat);
        tabs.getTabAt(4).setIcon(R.drawable.ic_blocked);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Fragment fragment = null;

            switch (position){
                case 0:
                    return new AllUsers();
                case 1:
                    return new FriendRequest();
                case 2:
                    return new MyFriends();
                case 3:
                    return new Chat();
                case 4:
                    return new BlockList();
            }
            return null;

        }

        @Override
        public int getCount() {
            return 5;
        }

        /*@Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0:
                    return "All Users";
                case 1:
                    return "Friend Requests";
                case 2:
                    return "My Friends";
                case 3:
                    return "Chat";
                case 4:
                    return "Block List";
            }
            return super.getPageTitle(position);
        }*/

    }

}

