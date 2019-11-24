package com.prestech.babankilexicon.actvity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.view.LexAdapter;

public class LexiconFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String [] lexData =new String[100];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lex_recycler_view, container, false);


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


    public void receiveItemCharIndex(String charIndex){
        //Navigate to the index
        Log.i("LEXICON LOG", "Char index recieved in LexiconFragment: "+charIndex);
    }


}
