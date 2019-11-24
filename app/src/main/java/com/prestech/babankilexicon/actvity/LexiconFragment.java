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
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.Constants;
import com.prestech.babankilexicon.view.LexAdapter;

public class LexiconFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String [] lexData =new String[100];
    private static String logTag = Constants.Logs.logTag+":"+AlphabetFragment.class.getName();
    int scrollIndex = 0;

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
        String mLogTag = logTag+":receiveItemCharIndex";
        Log.i(mLogTag, "Char index recieved in LexiconFragment: "+charIndex);

        //TODO Scroll to the position of the CharIndex: Require JSON restructure

        int index = 0; //TODO: get the index of charIndex

        //i++;
        scrollView(index);
    }


    void scrollView(final int index){
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

                   // i++;
               // }


            }
        });
    }

}
