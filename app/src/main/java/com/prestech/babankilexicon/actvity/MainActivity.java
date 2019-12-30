package com.prestech.babankilexicon.actvity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.Constants;
import com.prestech.babankilexicon.Utility.FavLexManager;

public class MainActivity extends AppCompatActivity implements AlphabetFragment.OnCharIndexSelectListener {
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private FavFragment favFragment;
    private MainLexiconListFragment mainLexiconFragment;
    private FragmentTransaction fragTransaction;
    private LexiconFragment lexiconFragment;
    private static String logTag = Constants.Logs.logTag+":"+MainActivity.class.getName();

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
                case R.id.navigation_lexicon:
                    mainLexiconFragment = new MainLexiconListFragment();
                    fragTransaction = fragmentManager.beginTransaction();
                    fragTransaction.replace(R.id.fragment_container, mainLexiconFragment, Constants.FragmentTags.mainFragment);
                    fragTransaction.commit();
                    return true;
                case R.id.navigation_favorite:
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

        AlphabetFragment.setOnCharIndexSelectListener(this);


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

        String mlogTag = logTag+":onAttachFragment";

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

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        String mlogTag = logTag+":onAttachFragment";

        if(fragment instanceof MainLexiconListFragment){
            MainLexiconListFragment mainLexiconListFragment = (MainLexiconListFragment)fragment;
            lexiconFragment = (LexiconFragment) mainLexiconListFragment.getFragmentManager().findFragmentById(R.id.lexicon_frag);
        }
    }

    @Override
    public void retrieveSelectedIndex(int index) {

        String mlogTag = logTag+":retrieveSelectedIndex";

        Log.i(mlogTag,"Message Received by main activity"+index);
        //call
        MainLexiconListFragment mainLexiconListFragment = (MainLexiconListFragment) getSupportFragmentManager().findFragmentByTag(Constants.FragmentTags.mainFragment);

        if(mainLexiconListFragment != null) {
            mainLexiconListFragment.receiveItemCharIndex(index);
        }else{
            Log.i(mlogTag, "lexiconFragment is null");
        }

    }


}
