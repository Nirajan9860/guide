package com.example.guide.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.guide.R;
import com.example.guide.adapters.SelectionPagerAdapter;
import com.example.guide.fragments.AboutFragment;
import com.example.guide.fragments.ContactFragment;
import com.example.guide.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private Button navbarButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navbarButton = findViewById(R.id.navbarButton);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                navbarButton,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(1000);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
        navbarButton.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        setupViewPager();
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 112);
            } else {

            }
        } else {

        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    private void openMapActivity(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }
    private void setupViewPager(){
        // adapter for viewPager
        FragmentManager fm = getSupportFragmentManager();
        SelectionPagerAdapter adapter = new SelectionPagerAdapter(getSupportFragmentManager());



        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ContactFragment());
        adapter.addFragment(new ContactFragment());
        adapter.addFragment(new AboutFragment());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(context, R.color.tabSelectedIconColor);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            tab.getIcon().setColorFilter(new BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP));
                        } else {
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_ATOP);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(context, R.color.tabUnselectedIconColor);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            tab.getIcon().setColorFilter(new BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP));
                        } else {
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_ATOP);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
        tabLayout.setBackgroundColor(getResources().getColor(R.color.white));
        // For color text
        //tabLayout.setTabTextColors(getResources().getColor(R.color.black),getResources().getColor(R.color.white));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_phone_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_info_outline_black_24dp);
        // Set initial color of icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tabLayout.getTabAt(0).getIcon().setColorFilter(new BlendModeColorFilter(R.color.tabSelectedIconColor, BlendMode.SRC_ATOP));
        } else {
            tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.tabSelectedIconColor), PorterDuff.Mode.SRC_ATOP);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tabLayout.getTabAt(1).getIcon().setColorFilter(new BlendModeColorFilter(R.color.tabUnselectedIconColor, BlendMode.SRC_ATOP));
        } else {
            tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.tabUnselectedIconColor), PorterDuff.Mode.SRC_ATOP);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tabLayout.getTabAt(2).getIcon().setColorFilter(new BlendModeColorFilter(R.color.tabUnselectedIconColor, BlendMode.SRC_ATOP));
        } else {
            tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.tabUnselectedIconColor), PorterDuff.Mode.SRC_ATOP);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tabLayout.getTabAt(3).getIcon().setColorFilter(new BlendModeColorFilter(R.color.tabUnselectedIconColor, BlendMode.SRC_ATOP));
        } else {
            tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.tabUnselectedIconColor), PorterDuff.Mode.SRC_ATOP);
        }

        // Change color of indicator bar
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.black));

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            openMapActivity();
        } else if (id == R.id.nav_slideshow) {
            openMapActivity();
        } else if (id == R.id.nav_tools) {
            openMapActivity();
        } else if (id == R.id.nav_share) {
            openMapActivity();
        } else if (id == R.id.nav_send) {
            openMapActivity();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
