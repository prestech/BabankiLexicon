package com.prestech.babankilexicon.actvity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.FavLexManager;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private FavFragment favFragment;
    private MainLexiconListFragment lexiconFragment;
    private FragmentTransaction fragTransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    fragTransaction = fragmentManager.beginTransaction();
                    fragTransaction.replace(R.id.fragment_container, homeFragment);
                    fragTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    lexiconFragment = new MainLexiconListFragment();
                    fragTransaction = fragmentManager.beginTransaction();
                    fragTransaction.replace(R.id.fragment_container, lexiconFragment);
                    fragTransaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    favFragment = new FavFragment();
                    fragTransaction = fragmentManager.beginTransaction();
                    fragTransaction.replace(R.id.fragment_container, favFragment);
                    fragTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FavLexManager.initializeManager(getApplication());

    }


    @Override
    public void onPause() {
        super.onPause();
        //write to file
        FavLexManager.writeFavListToFile();
    }

}
