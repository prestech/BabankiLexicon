package com.prestech.babankilexicon.viewHelper;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.AudioManager;
import com.prestech.babankilexicon.Utility.FavLexManager;
import com.prestech.babankilexicon.Utility.LexDataSource;
import com.prestech.babankilexicon.actvity.AlphabetFragment;
import com.prestech.babankilexicon.model.Lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LexAdapter extends RecyclerView.Adapter<LexAdapter.LexViewHolder>  implements Filterable {

    private ArrayList<Lexicon> listOfLexicon;
    private LexDataSource lexDataSource;
    private Context context;
    private VIEW_CONTEXT view_context;
    private static AudioManager audioManager;
    private AlphabetFragment.OnCharIndexSelectListener onCharIndexSelectListener;
    private LexiconFilter lexiconFilter;
    private boolean searchPublish = false;

    public final String[] alphabets = {"A" , "B" , "Bv" , "Ch" , "D" , "Dz" , "E" , "Ə" , "F" , "G" , "Gh" , "I" , "Ɨ" , "J" , "ʼ" , "K" , "L" , "M" , "N" , "Ny" , "Ŋ" , "O" , "Pf" , "S" , "Sh" , "T" , "Ts" , "U" , "Ʉ" , "V" , "W" , "Y" , "Z" , "Zh"};

    private Dialog dialog;


    public static enum VIEW_CONTEXT  {FAVORITE_LIST, LEXICON_LIST, ALPHABET_LIST,SEARCH_LIST};

    public static class LexViewHolder extends RecyclerView.ViewHolder{

        public TextView engTextView, kjmTextView, alphabetTv;
        public ImageButton favBtn, audioBtn;

        public  LexViewHolder(View view, VIEW_CONTEXT view_context ){
            super(view);

            switch (view_context){

                case LEXICON_LIST:
                case SEARCH_LIST:
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

        if(listOfLexicon == null){
            listOfLexicon = new ArrayList<>();
        }

        //lexDataSet =  lexDataSource.loadInitialData();
    }

    public  LexAdapter(Context context, VIEW_CONTEXT view_context, AlphabetFragment.OnCharIndexSelectListener listener){
        this(context, view_context);
        this.onCharIndexSelectListener = listener;
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
            case SEARCH_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lexicon_list_view, viewGroup, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(context);
                    }
                });
                break;
            case ALPHABET_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alphabet_list_view, viewGroup, false);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onCharIndexSelectListener != null) {
                            TextView textView = (TextView)view.findViewById(R.id.alpabet_textview);
                            String value = textView.getText().toString();

                            int alpaIndex = lexDataSource.findAlphaIndex(value);
                            if(alpaIndex == -1){
                                return;
                            }
                            alpaIndex = alpaIndex+7;

                            System.out.println("Alphabet index: "+alpaIndex);

                            onCharIndexSelectListener.retrieveSelectedIndex(alpaIndex);

                        }else{
                            Log.i("LEXICON_LOG", "onCharIndexSelectListener is null");
                        }
                    }
                });

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
        Lexicon lexicon = null;
        String lexiconId;
        String tribalWord;
        String favLexicon;
        int listIndex = 0;


        switch(view_context){

            case LEXICON_LIST:

                lexicon = lexDataSource.getLexicon("" + dataIndex);

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

            case FAVORITE_LIST:

                lexicon = lexDataSource.getLexicon(FavLexManager.getFavLexiconId(dataIndex));

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
            case SEARCH_LIST:

                lexicon = listOfLexicon.get(dataIndex);

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
                Log.i("LEXICON_LOG", "Setting alphebet char index value "+alphabets[dataIndex]);
                lexViewHolder.alphabetTv.setText(alphabets[dataIndex]);
                //int alpaIndex = lexDataSource.findAlphaIndex(alphabets[dataIndex]);

                //Log.i("ALPHABET LEXICON_LOG", " alphebet char index value "+alpaIndex);
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
            case SEARCH_LIST:
                return listOfLexicon.size();
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

    private void showDialog(Context context){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.lexicon_popup_view);
        dialog.show();
    }


    @Override
    public Filter getFilter() {
        if(lexiconFilter == null){
            lexiconFilter = new LexiconFilter();
        }
        return lexiconFilter;
    }

    public void changeContext(VIEW_CONTEXT context){
        this.view_context = context;
        notifyDataSetChanged();
    }

    /*********************************************************************************************
     * This class filters the data and provide values that contains the search key
     */
    public class LexiconFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults filteredResults = new FilterResults();


            if(charSequence == null && charSequence.length() == 0){
                return null;
            }

            ArrayList<Lexicon> filteredData = new ArrayList<>();

            //
            int pageIncrement = 100;

            int startIndex = 1;
            int endIndex = pageIncrement;

            int MAX_INDEX = 1993; //Max number of lexicons for now

            while(endIndex < MAX_INDEX){


                for(Lexicon lexicon: lexDataSource.provideData(startIndex,endIndex)){

                    if (lexicon == null) continue;

                    if(lexicon.getEnglishWord().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())){

                        System.out.println("Found searched word "+lexicon.getEnglishWord());

                        filteredData.add(lexicon);

                    }
                }

                if(endIndex < MAX_INDEX){
                    startIndex = endIndex;
                    endIndex = endIndex+pageIncrement;
                }
                if (endIndex == MAX_INDEX){
                    endIndex++;
                }

                if (endIndex > MAX_INDEX){
                    endIndex = MAX_INDEX;
                    startIndex = endIndex -1;

                    for(Lexicon lexicon: lexDataSource.provideData(startIndex,endIndex)){
                        if(lexicon.getEnglishWord().toLowerCase()
                                .contains(charSequence.toString().toLowerCase())){

                            System.out.println("Found searched word "+lexicon.getEnglishWord());

                            filteredData.add(lexicon);

                        }
                    }
                    break;
                }

            }

            endIndex = 0;
            startIndex =0;

            filteredResults.values = filteredData;
            filteredResults.count = filteredData.size();


            return filteredResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listOfLexicon.clear();
            listOfLexicon.addAll ( ( (ArrayList<Lexicon>) filterResults.values) );
            Log.i("publishResults", "publishResults: Number of search results: "+listOfLexicon.size());
            view_context = VIEW_CONTEXT.SEARCH_LIST;
            notifyDataSetChanged();
        }
    }


}//LexAdapter Ends
