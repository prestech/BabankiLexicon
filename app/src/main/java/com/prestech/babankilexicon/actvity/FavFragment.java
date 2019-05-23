package com.prestech.babankilexicon.actvity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.FavLexManager;
import com.prestech.babankilexicon.view.LexAdapter;

public class FavFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

  /*  @Override

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //write to file
        FavLexManager.writeFavListToFile();
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_fav, container, false);

        recyclerView = view.findViewById(R.id.fav_recycler_View);

        recyclerView.setHasFixedSize(false);

        //set layout; linear layout
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //setup adapter
        mAdapter = new LexAdapter(view.getContext(), true);
        recyclerView.setAdapter(mAdapter);

        return  view;
    }


}
