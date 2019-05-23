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
    private boolean isFavoriteView;
    private static AudioManager audioManager;


    public static class LexViewHolder extends RecyclerView.ViewHolder{

        public TextView engTextView, kjmTextView;
        public ImageButton favBtn, audioBtn;

        public  LexViewHolder(View view){
            super(view);
            engTextView = view.findViewById(R.id.eng_textview);
            kjmTextView = view.findViewById(R.id.kjm_textview);
            favBtn = view.findViewById(R.id.favBtn);
            audioBtn = view.findViewById(R.id.adioBtn);

        }

    }//LexViewHolder Ends

    public LexAdapter(Context context, boolean isFavoriteView){
        this.context = context;
        this.lexDataSource = new LexDataSource(context);
        this.isFavoriteView = isFavoriteView;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_lex_list_view, viewGroup, false);

        LexViewHolder lexViewHolder = new LexViewHolder(view);

        return lexViewHolder;

    }//onCreateViewHolder() Ends

    @Override
    public void onBindViewHolder(@NonNull LexViewHolder lexViewHolder, int dataIndex) {
        Lexicon lexicon;
        String lexiconId;
        if(isFavoriteView == false) {
            lexicon = lexDataSource.getLexicon("" + dataIndex);//lexDataSet.get(((dataIndex)%30));
        }
        else{
            lexicon = lexDataSource.getFavLexicon(dataIndex);
        }
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

    }//onBindViewHolder() Ends

    @Override
    public void onViewRecycled(@NonNull LexViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    //TODO:Make the list an Endless list in the future
    public int getItemCount() {
        if(isFavoriteView == false) return 1993;
        return FavLexManager.getFavListSize();
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

            if(isFavoriteView == true){
                notifyItemRemoved(indexOfData);
            }
        }

        private void playAudio(){
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.favBtn:
                    replaceFavBtnImage( (ImageButton) v, indexOfData);
                    break;
                case R.id.adioBtn:
                    Log.d("PRESDEBUG", "Audio Btn clicked"+v.getId());
                    audioManager.playAudio(lexicon.getKejomWord());
                    break;
            }
        }//onClick Ends
    }//ClickListener Ends


}//LexAdapter Ends
