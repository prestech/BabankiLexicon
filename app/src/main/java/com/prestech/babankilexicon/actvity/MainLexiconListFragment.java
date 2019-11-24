package com.prestech.babankilexicon.actvity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.Constants;

public class MainLexiconListFragment extends Fragment {

    private static AlphabetFragment.OnCharIndexSelectListener onCharIndexSelectListener;
    private static String logTag = Constants.Logs.logTag+":"+MainLexiconListFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.activity_lexicon, container, false);

         return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }

    public void receiveItemCharIndex(String charIndex){
        String mLogTag = logTag+":"+charIndex;

        //Navigate to the index
        Log.i(mLogTag, "Char index recieved in MainLexiconListFragment: "+charIndex);

        LexiconFragment lexiconFragment = (LexiconFragment)getChildFragmentManager().findFragmentById(R.id.lexicon_frag);

        if(lexiconFragment != null){
            lexiconFragment.receiveItemCharIndex(charIndex);
        }else{
            Log.w(mLogTag, "lexiconFragment is null");
        }

    }

}
