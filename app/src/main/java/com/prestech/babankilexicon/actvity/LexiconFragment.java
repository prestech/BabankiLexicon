package com.prestech.babankilexicon.actvity;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.Constants;
import com.prestech.babankilexicon.viewHelper.LexAdapter;

public class LexiconFragment extends Fragment  {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String [] lexData =new String[100];
    private static String logTag = Constants.Logs.logTag+":"+AlphabetFragment.class.getName();
    private int scrollIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lex_recycler_view, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.lex_recycler_view);
        recyclerView.setHasFixedSize(false);

        //set layout; linear layout
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //setup adapter
        mAdapter = new LexAdapter(view.getContext(), LexAdapter.VIEW_CONTEXT.LEXICON_LIST);
        recyclerView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        String mlogTag = logTag+":onAttachFragment";

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default


        //implement setOnQueryTextListener(): This call back is called when the user press the search button
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(logTag, "Search query: "+query);
                //Call a DataSource Function to get a list of the result.
                ((LexAdapter)mAdapter).getFilter().filter(query);

                return true;

            }//onQueryTextSubmit() Ends

            @Override
            public boolean onQueryTextChange(String newText) {

                if( newText == null ||newText.trim().equals("") ){
                    ((LexAdapter)mAdapter).changeContext( LexAdapter.VIEW_CONTEXT.LEXICON_LIST);
                    return true;
                }
                return false;
            }

        });//setOnQueryTextListener() Ends

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                ((LexAdapter)mAdapter).changeContext( LexAdapter.VIEW_CONTEXT.LEXICON_LIST);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((LexAdapter)mAdapter).changeContext( LexAdapter.VIEW_CONTEXT.LEXICON_LIST);
            }
        });


    }



    public void receiveItemCharIndex(int index){
        String mLogTag = logTag+":receiveItemCharIndex";
        Log.i(mLogTag, "Char index recieved in LexiconFragment: "+index);

        //TODO Scroll to the position of the CharIndex: Require JSON restructure

        scrollView(index);
    }



   private  void scrollView(final int index){
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                //int i = 8;

                    View view = recyclerView.getChildAt(1);
                    if(view != null) {
                        Log.i(logTag, "Position: " + index);
                        TextView textView = (TextView) view.findViewById(R.id.kjm_textview);
                        String value = textView.getText().toString();
                        Log.i(logTag, "Lexicon at position: " + value);
                    }

                    recyclerView.scrollToPosition(index);

            }
        });
    }



}
