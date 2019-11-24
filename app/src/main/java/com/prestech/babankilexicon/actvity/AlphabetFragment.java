package com.prestech.babankilexicon.actvity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.Constants;
import com.prestech.babankilexicon.view.LexAdapter;

public class AlphabetFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static OnCharIndexSelectListener charIndexSelectListener;
    private View view;
    private static String logTag = Constants.Logs.logTag+":"+AlphabetFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         view = inflater.inflate(R.layout.alphabet_recycler_view, container, false);


        recyclerView = view.findViewById(R.id.alphabet_recycler);
        recyclerView.setHasFixedSize(true);

        //set layout; linear layout
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup adapter
        mAdapter = new LexAdapter(view.getContext(), LexAdapter.VIEW_CONTEXT.ALPHABET_LIST, charIndexSelectListener);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    public static void setOnCharIndexSelectListener(OnCharIndexSelectListener listener){
        charIndexSelectListener = listener;
    }

    /**
     * This interface is used as a communication channel
     */
    public interface OnCharIndexSelectListener{
         void retrieveSelectedIndex(String message);
    }

}
