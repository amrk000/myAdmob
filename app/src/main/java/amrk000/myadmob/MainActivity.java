package amrk000.myadmob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //ProgressBar progressBar;
    TabLayout tabs;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    ImageButton reload,theme;
    BrowserFragment browserFragment;

    boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        darkMode=getSharedPreferences("darkMode",MODE_PRIVATE).getBoolean("value",false);

        if(darkMode) setTheme(R.style.AppThemeDark);
        else setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        tabs = findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        reload=findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()>6) viewPager.setCurrentItem(0);

                browserFragment = (BrowserFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                browserFragment.browser.reload();
                reload.setRotation(0f);
                reload.animate().rotationBy(360f).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
            }
        });

        theme=findViewById(R.id.theme);

        if(darkMode){
            theme.setImageResource(R.drawable.dark_icon);
            theme.setRotation(-45f);
        }else {
            theme.setImageResource(R.drawable.light_icon);
            theme.setRotation(0f);
        }

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(darkMode){
                    theme.setRotation(-405f);
                    theme.setImageResource(R.drawable.light_icon);
                    theme.animate().rotationBy(225f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();

                    darkMode=false;

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    getSharedPreferences("darkMode",MODE_PRIVATE).edit()
                            .putBoolean("value",false)
                            .apply();

                    new Handler().postDelayed(()->{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                    },450);
                }
                else{
                    theme.setRotation(0f);
                    theme.setImageResource(R.drawable.dark_icon);
                    theme.animate().rotationBy(-405f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();

                    darkMode=true;

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    getSharedPreferences("darkMode",MODE_PRIVATE).edit()
                            .putBoolean("value",true)
                            .apply();

                    new Handler().postDelayed(()->{
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    },450);
                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()<=6){
        browserFragment = (BrowserFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if(browserFragment.browser.canGoBack())browserFragment.browser.goBack();
        else super.onBackPressed();
        }
        else super.onBackPressed();
    }

}