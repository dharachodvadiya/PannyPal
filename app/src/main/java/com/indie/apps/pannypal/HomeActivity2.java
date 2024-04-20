package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.indie.apps.pannypal.Adapter.MyPagerAdapter;
import com.indie.apps.pannypal.Fragment.ContactFragment;
import com.indie.apps.pannypal.Fragment.HomeFragment;

public class HomeActivity2 extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        int currFragment = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null) currFragment= extras.getInt(Globle.CURR_FRAG);


        init();

        pagerAdapter = new MyPagerAdapter(this);
        pagerAdapter.addFragment(new HomeFragment());
        pagerAdapter.addFragment(new ContactFragment());
        viewPager.setAdapter(pagerAdapter);

        if(currFragment != 0)
            viewPager.setCurrentItem(currFragment);


            new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Set tab icon here
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.btn_home);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.btn_contact);
                            break;
                        // Add more cases for additional tabs if needed
                    }
                }).attach();
    }
    void  init()
    {
        getOnBackPressedDispatcher().addCallback(this,callback);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Get the current fragment
            Fragment currentFragment = pagerAdapter.getFragement(viewPager.getCurrentItem());

            // Check if the current fragment is handling onBackPressed
            if (currentFragment instanceof OnBackPressedListener ) {
                if(((OnBackPressedListener) currentFragment).onBackPressed())
                {
                    viewPager.setCurrentItem(0);
                }
            }
        }
    };

    // Interface for Fragments to handle onBackPressed
    public interface OnBackPressedListener {
        boolean onBackPressed();
    }
}