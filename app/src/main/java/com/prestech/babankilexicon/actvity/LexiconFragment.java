package com.prestech.babankilexicon.actvity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.model.Lexicon;
import com.prestech.babankilexicon.viewHelper.LexAdapter;
import com.prestech.babankilexicon.viewHelper.OnAlphabetSelectListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LexiconFragment extends Fragment implements LoaderCallbacks<List<Lexicon>>, OnAlphabetSelectListener {
    private RecyclerView mLexiconRecyclerView;
    RecyclerView.LayoutManager mLexiconLayoutManager;
    RecyclerView mAlphabetRecyclerView;
    private LexAdapter mLexiconAdapter;
    private LexAdapter mAlphabetAdapter;
    RecyclerView.SmoothScroller smoothScroller;
    private View mLoadingIndicator;
    private boolean dataLoaded = false;
    List<Lexicon> lexData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lex_recycler_view, container, false);

        setHasOptionsMenu(true);

        // Setup lexicon recycler view
        mLexiconRecyclerView = view.findViewById(R.id.lex_recycler_view);
        mLexiconRecyclerView.setHasFixedSize(false);
//        smoothScroller = new LinearSmoothScroller(getContext()) {
//            @Override
//            protected int getVerticalSnapPreference() {
//                return SNAP_TO_START;
//            }
//
//
//        };

        //set layout; linear layout
        mLexiconLayoutManager = new LinearLayoutManager(view.getContext());
        mLexiconRecyclerView.setLayoutManager(mLexiconLayoutManager);

        //setup adapter
        mLexiconAdapter = new LexAdapter(view.getContext(), LexAdapter.VIEW_CONTEXT.LEXICON_LIST);
        mLexiconRecyclerView.setAdapter(mLexiconAdapter);


        // Setup alphabet recycler view
        mAlphabetRecyclerView = view.findViewById(R.id.alphabet_recycler_view);
        mAlphabetRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager alphabetLayoutManager = new LinearLayoutManager(view.getContext());
        mAlphabetRecyclerView.setLayoutManager(alphabetLayoutManager);

        mAlphabetAdapter = new LexAdapter(view.getContext(),
                LexAdapter.VIEW_CONTEXT.ALPHABET_LIST, this);
        mAlphabetRecyclerView.setAdapter(mAlphabetAdapter);

        // Setup loader
        mLoadingIndicator = view.findViewById(R.id.loading_indicator);
        if (dataLoaded) {
            mLexiconRecyclerView.setVisibility(View.VISIBLE);
            mLexiconRecyclerView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.GONE);
        } else {
            mLexiconRecyclerView.setVisibility(View.GONE);
            mLexiconRecyclerView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        setAdapterData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!dataLoaded) {
            dataLoaded = true;
            loadData();
        }

        super.onActivityCreated(savedInstanceState);
    }


    private void loadData() {
        LoaderManager loaderManager = getLoaderManager();
        Loader<List<Lexicon>> loader = loaderManager.getLoader(0);
        if (loader == null)
            loaderManager.initLoader(0, null, this);
        else
            loaderManager.restartLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        // remove search icon from search input field and set query hint
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_hint));

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default


        //implement setOnQueryTextListener(): This call back is called when the user press the search button
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Call a DataSource Function to get a list of the result.
                mLexiconAdapter.getFilter().filter(query);

                return true;

            }//onQueryTextSubmit() Ends

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().equals("")) {
                    mLexiconAdapter.clearSearch();
                    return true;
                }
                return false;
            }

        });//setOnQueryTextListener() Ends

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mLexiconAdapter.clearSearch();
                return false;
            }
        });

//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                mLexiconAdapter.clearSearch();
//            }
//        });
    }


    public void receiveItemCharIndex(int index) {
        //TODO Scroll to the position of the CharIndex: Require JSON restructure

        scrollView(index);
//        smoothScroller.setTargetPosition(index);
//        mLexiconLayoutManager.startSmoothScroll(smoothScroller);
    }


    private void scrollView(final int index) {
        mLexiconRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mLexiconRecyclerView.scrollToPosition(index);
            }
        });
    }

    @Override
    public Loader<List<Lexicon>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Lexicon>>(this.getContext()) {
            @Override
            public List<Lexicon> loadInBackground() {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = null;
                List<Lexicon> data = null;

                try {
                    InputStream inputStream = this.getContext()
                            .getResources().openRawResource(R.raw.kejomlexicon_new);
                    rootNode = objectMapper.readTree(inputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (rootNode != null) {
                    try {
                        data = objectMapper.readValue(rootNode.toString(),
                                new TypeReference<List<Lexicon>>() {
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return data == null ? new ArrayList<Lexicon>() : data;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Lexicon>> loader, List<Lexicon> data) {
        lexData = data;
        mLexiconAdapter.setData(data);
        mAlphabetAdapter.setData(data);
        setAdapterData();
        mLoadingIndicator.setVisibility(View.GONE);
        mLexiconRecyclerView.setVisibility(View.VISIBLE);
        mAlphabetRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setAdapterData() {
        if (lexData == null)
            return;
        mLexiconAdapter.setData(lexData);
        mAlphabetAdapter.setData(lexData);
    }

    @Override
    public void onLoaderReset(Loader<List<Lexicon>> loader) {
    }


    @Override
    public void retrieveSelectedIndex(int index) {
        receiveItemCharIndex(index);
    }
}

