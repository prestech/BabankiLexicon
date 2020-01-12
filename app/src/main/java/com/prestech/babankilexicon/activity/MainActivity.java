package com.prestech.babankilexicon.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.FavLexManager;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FavFragment favFragment;
    private HomeFragment homeFragment;
    private LexiconFragment lexiconFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (homeFragment == null)
                        homeFragment = new HomeFragment();
                    return openFragment(homeFragment);
                case R.id.navigation_lexicon:
                    if (lexiconFragment == null)
                        lexiconFragment = new LexiconFragment();
                    return openFragment(lexiconFragment);
                case R.id.navigation_favorite:
                    if (favFragment == null)
                        favFragment = new FavFragment();
                    return openFragment(favFragment);
            }
            return false;
        }
    };

    private boolean openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        return true;
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        String mlogTag = TAG+":onAttachFragment";

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default



        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.lexicon_frag);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
                return false;
            }
        });*/

        return true;
    }
}
