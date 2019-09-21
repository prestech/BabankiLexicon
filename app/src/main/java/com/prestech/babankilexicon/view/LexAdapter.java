package com.prestech.babankilexicon.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.AudioManager;
import com.prestech.babankilexicon.Utility.FavLexManager;
import com.prestech.babankilexicon.Utility.LexDataSource;
import com.prestech.babankilexicon.model.Lexicon;

public class LexAdapter extends RecyclerView.Adapter<LexAdapter.LexViewHolder> {

    //private List<Lexicon> lexDataSet;
    private LexDataSource lexDataSource;
    private Context context;
    private VIEW_CONTEXT view_context;
    private static AudioManager audioManager;
    private String[] alphabets = {"A" , "B" , "Bv" , "Ch" , "D" , "Dz" , "E" , "Ə" , "Ff" , "G" , "Gh" , "I" , "Ɨ" , "J" , "ʼ" , "K" , "L" , "M" , "N" , "Ny" , "Ŋ" , "O" , "Pf" , "S" , "Sh" , "T" , "Ts" , "U" , "Ʉ" , "V" , "W" , "Y" , "Z" , "Zh"};

    public static enum VIEW_CONTEXT  {FAVORITE_LIST, LEXICON_LIST, ALPHABET_LIST};

    public static class LexViewHolder extends RecyclerView.ViewHolder{

        public TextView engTextView, kjmTextView, alphabetTv;
        public ImageButton favBtn, audioBtn;

        public  LexViewHolder(View view, VIEW_CONTEXT view_context ){
            super(view);

            switch (view_context){

                case LEXICON_LIST:
                case FAVORITE_LIST:
                    engTextView = view.findViewById(R.id.eng_textview);
                    kjmTextView = view.findViewById(R.id.kjm_textview);
                    favBtn = view.findViewById(R.id.favBtn);
                    audioBtn = view.findViewById(R.id.adioBtn);
                    break;
                case ALPHABET_LIST:
                    alphabetTv = view.findViewById(R.id.alpabet_textview);
                    break;
            }


        }

    }//LexViewHolder Ends

    public LexAdapter(Context context, VIEW_CONTEXT view_context){
        this.context = context;
        this.lexDataSource = new LexDataSource(context);
        this.view_context = view_context;

        if(audioManager == null){
            audioManager = new AudioManager(context);
        }
        //lexDataSet =  lexDataSource.loadInitialData();
    }


    /**
     * Interface implementation
     */
    @NonNull
    @Override
    public LexViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        LexViewHolder lexViewHolder = null;

        switch (view_context){
            case LEXICON_LIST:
            case FAVORITE_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lexicon_list_view, viewGroup, false);
                break;
            case ALPHABET_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alphabet_list_view, viewGroup, false);
                break;
        }

        if (view != null) {
             lexViewHolder = new LexViewHolder(view, view_context);
        }else {
            //throw exception
            new Exception("View is null");
        }

        return lexViewHolder;

    }//onCreateViewHolder() Ends

    @Override
    public void onBindViewHolder(@NonNull LexViewHolder lexViewHolder, int dataIndex) {
        Lexicon lexicon;
        String lexiconId;
        switch(view_context){
            case LEXICON_LIST:
            case FAVORITE_LIST:
            lexicon = lexDataSource.getLexicon("" + dataIndex);//lexDataSet.get(((dataIndex)%30));

            if(lexicon != null) {
                lexiconId = lexicon.getLexiconId()+"";

                lexViewHolder.kjmTextView.setText(lexicon.getKejomWord());
                lexViewHolder.engTextView.setText(lexicon.getEnglishWord());

                if(FavLexManager.lexIsFavorite(lexiconId) == false){
                    lexViewHolder.favBtn.setImageResource(android.R.drawable.btn_star_big_off);
                }else{
                    lexViewHolder.favBtn.setImageResource(android.R.drawable.btn_star_big_on);
                }

                lexViewHolder.favBtn.setOnClickListener(new ClickListener(lexicon, dataIndex));
                lexViewHolder.audioBtn.setOnClickListener(new ClickListener(lexicon, dataIndex));
            }
            break;
            case ALPHABET_LIST:
                lexViewHolder.alphabetTv.setText(alphabets[dataIndex]);
                break;
        }


    }//onBindViewHolder() Ends

    @Override
    public void onViewRecycled(@NonNull LexViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    //TODO:Make the list an Endless list in the future
    public int getItemCount() {
        switch (view_context){
            case FAVORITE_LIST:
                return FavLexManager.getFavListSize();
            case ALPHABET_LIST:
                return 34;
            case LEXICON_LIST:
                return 1993;
                default: return  0;
        }
    }//getItemCount() Ends


    private class ClickListener implements View.OnClickListener{
        private String lexiconId;
        private int indexOfData;
        private Lexicon lexicon;


        public ClickListener(Lexicon lexicon, int indexOfData){
            this.lexiconId = lexicon.getLexiconId()+"";
            this.lexicon = lexicon;
            this.indexOfData = indexOfData;

        }

        private void replaceFavBtnImage(ImageButton favBtn, int indexOfData){

            if(FavLexManager.lexIsFavorite(lexiconId)){
                FavLexManager.removeFromFavList(lexiconId);
                favBtn.setImageResource(android.R.drawable.btn_star_big_off);
            }else {
                FavLexManager.addToFavList(lexiconId);
                favBtn.setImageResource(android.R.drawable.btn_star_big_on);
            }

            if(view_context == VIEW_CONTEXT.FAVORITE_LIST){
                notifyItemRemoved(indexOfData);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.favBtn:
                    replaceFavBtnImage( (ImageButton) v, indexOfData);
                    break;
                case R.id.adioBtn:
                    if(lexicon != null) {
                        Log.d("PRESDEBUG", lexicon.getKejomWord() + " Audio Btn clicked" + v.getId());
                        audioManager.playAudio(lexicon.getKejomWord());
                    }
                    break;
            }
        }//onClick Ends
    }//ClickListener Ends


}//LexAdapter Ends
