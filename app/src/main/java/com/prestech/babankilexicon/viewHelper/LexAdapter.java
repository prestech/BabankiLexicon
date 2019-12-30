package com.prestech.babankilexicon.viewHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.Utility.AudioManager;
import com.prestech.babankilexicon.Utility.DetailAnimation;
import com.prestech.babankilexicon.Utility.FavLexManager;
import com.prestech.babankilexicon.Utility.LabeledTextView;
import com.prestech.babankilexicon.Utility.LexDataSource;
import com.prestech.babankilexicon.actvity.AlphabetFragment;
import com.prestech.babankilexicon.model.Lexicon;

import java.util.ArrayList;

public class LexAdapter extends RecyclerView.Adapter<LexAdapter.LexViewHolder> implements Filterable {
    private static String TAG = "PRESDEBUG";
    private ArrayList<Lexicon> listOfLexicon;
    private LexDataSource lexDataSource;
    private VIEW_CONTEXT view_context;
    private AudioManager audioManager;
    private AlphabetFragment.OnCharIndexSelectListener onCharIndexSelectListener;
    private LexiconFilter lexiconFilter;
    private LinearLayout lastToggled = null;
    private CardView lastRoot = null;

    private final String[] alphabets = {"A", "B", "Bv", "Ch", "D", "Dz", "E", "Ə", "F", "G", "Gh", "I", "Ɨ", "J", "ʼ", "K", "L", "M", "N", "Ny", "Ŋ", "O", "Pf", "S", "Sh", "T", "Ts", "U", "Ʉ", "V", "W", "Y", "Z", "Zh"};


    public enum VIEW_CONTEXT {FAVORITE_LIST, LEXICON_LIST, ALPHABET_LIST, SEARCH_LIST}


    public class LexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView engTextView, kjmTextView, alphabetTv;
        ImageView pictureIv;
        LabeledTextView pronLtv, examplesLtv, partOfSpeechLtv, pluralLtv, variationLtv;

        ImageButton favBtn, audioBtn;
        LinearLayout lexiconDetails;
        CardView root;

        LexViewHolder(View view, VIEW_CONTEXT view_context) {
            super(view);
            root = (CardView) view;
            lexiconDetails = view.findViewById(R.id.lexicon_details);

            switch (view_context) {
                case LEXICON_LIST:
                case SEARCH_LIST:
                case FAVORITE_LIST:
                    view.setOnClickListener(this);

                    engTextView = view.findViewById(R.id.eng_textview);
                    kjmTextView = view.findViewById(R.id.kjm_textview);

                    partOfSpeechLtv = view.findViewById(R.id.ltv_part_of_speech);
                    pronLtv = view.findViewById(R.id.ltv_pronunciation);
                    examplesLtv = view.findViewById(R.id.ltv_examples);
                    pluralLtv = view.findViewById(R.id.ltv_plural);
                    variationLtv = view.findViewById(R.id.ltv_variants);

                    favBtn = view.findViewById(R.id.favBtn);
                    audioBtn = view.findViewById(R.id.adioBtn);
                    pictureIv = view.findViewById(R.id.word_picture);
                    break;
                case ALPHABET_LIST:
                    alphabetTv = view.findViewById(R.id.alpabet_textview);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            boolean previouslyExpanded = lexiconDetails.getVisibility() != View.GONE;

            if (lastToggled != null) {
                toggleLayoutExpand(true, lastToggled, lastRoot);
                if (lastToggled == lexiconDetails) {
                    lastToggled = null;
                    lastRoot = null;
                } else {
                    toggleLayoutExpand(false, lexiconDetails, root);
                    lastToggled = lexiconDetails;
                    lastRoot = root;
                }
            } else {
                toggleLayoutExpand(previouslyExpanded, lexiconDetails, root);
                lastToggled = lexiconDetails;
                lastRoot = root;
                if (previouslyExpanded) {
                    lastToggled = null;
                    lastRoot = null;
                }
            }
        }
    }//LexViewHolder Ends

    public LexAdapter(Context context, VIEW_CONTEXT view_context) {
        this.lexDataSource = new LexDataSource(context);
        this.view_context = view_context;

        if (audioManager == null) {
            audioManager = new AudioManager(context);
        }

        if (listOfLexicon == null) {
            listOfLexicon = new ArrayList<>();
        }

        //lexDataSet =  lexDataSource.loadInitialData();
    }

    public LexAdapter(Context context, VIEW_CONTEXT view_context, AlphabetFragment.OnCharIndexSelectListener listener) {
        this(context, view_context);
        this.onCharIndexSelectListener = listener;
    }


    /**
     * Interface implementation
     */
    @NonNull
    @Override
    public LexViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View view = null;
        LexViewHolder lexViewHolder = null;

        switch (view_context) {
            case LEXICON_LIST:
            case FAVORITE_LIST:
            case SEARCH_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lexicon_list_view, viewGroup, false);
                break;
            case ALPHABET_LIST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alphabet_list_view, viewGroup, false);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCharIndexSelectListener != null) {
                            TextView textView = (TextView) view.findViewById(R.id.alpabet_textview);
                            String value = textView.getText().toString();

                            int alpaIndex = lexDataSource.findAlphaIndex(value);
                            if (alpaIndex == -1) {
                                return;
                            }
                            alpaIndex = alpaIndex + 7;

                            System.out.println("Alphabet index: " + alpaIndex);

                            onCharIndexSelectListener.retrieveSelectedIndex(alpaIndex);

                        } else {
                            Log.i("LEXICON_LOG", "onCharIndexSelectListener is null");
                        }
                    }
                });

                break;
        }

        if (view != null) {
            lexViewHolder = new LexViewHolder(view, view_context);
        } else {
            //throw exception
            new Exception("View is null");
        }

        return lexViewHolder;

    }//onCreateViewHolder() Ends

    @Override
    public void onBindViewHolder(@NonNull LexViewHolder lexViewHolder, int dataIndex) {
        Lexicon lexicon = GetLexiconItem(dataIndex);
        String lexiconId;

        if (view_context == VIEW_CONTEXT.ALPHABET_LIST) {
            Log.i("LEXICON_LOG", "Setting alphabet char index value " + alphabets[dataIndex]);
            lexViewHolder.alphabetTv.setText(alphabets[dataIndex]);
            return;
        }

        lexiconId = String.valueOf(lexicon.getLexiconId());

        lexViewHolder.kjmTextView.setText(lexicon.getKejomWord());
        lexViewHolder.engTextView.setText(lexicon.getEnglishWord());

            lexViewHolder.partOfSpeechLtv.setValueText(lexicon.getPartOfSpeech());
            lexViewHolder.pronLtv.setValueText(lexicon.getPronunciation());
            lexViewHolder.examplesLtv.setValueText(lexicon.getExamplePhrase());
            lexViewHolder.variationLtv.setValueText(lexicon.getVariant());
            lexViewHolder.pluralLtv.setValueText(lexicon.getPluralForm());


        // TODO add image link to data model, fetch and display
        if (dataIndex % 3 == 0) lexViewHolder.pictureIv.setVisibility(View.GONE);
//        lexViewHolder.pictureIv.setImageDrawable();

        if (!FavLexManager.lexIsFavorite(lexiconId))
            lexViewHolder.favBtn.setImageResource(R.drawable.ic_star);
        else
            lexViewHolder.favBtn.setImageResource(R.drawable.ic_star_orange);

        lexViewHolder.favBtn.setOnClickListener(new ClickListener(lexicon, dataIndex));
        lexViewHolder.audioBtn.setOnClickListener(new ClickListener(lexicon, dataIndex));
    }//onBindViewHolder() Ends

    /**
     * Toggles a card view between displaying and not displaying the details
     *
     * @param isExpanded     is card view currently expanded?
     * @param layoutToToggle Layout to toggle
     * @param root           Root card to add or remove elevation
     */
    private void toggleLayoutExpand(boolean isExpanded, LinearLayout layoutToToggle, CardView root) {
        if (isExpanded) {
            root.setCardElevation(0);
            DetailAnimation.collapse(layoutToToggle);
        } else {
            root.setCardElevation(10);
            DetailAnimation.expand(layoutToToggle);
        }
    }

    @Override
    public void onViewRecycled(@NonNull LexViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    //TODO:Make the list an Endless list in the future
    public int getItemCount() {
        switch (view_context) {
            case FAVORITE_LIST:
                return FavLexManager.getFavListSize();
            case ALPHABET_LIST:
                return 34;
            case LEXICON_LIST:
                return 1993;
            case SEARCH_LIST:
                return listOfLexicon.size();
            default:
                return 0;
        }
    }//getItemCount() Ends

    /**
     * Get the lexicon item from the appropriate data source
     *
     * @param dataIndex Index of the data in the data source
     * @return the matching lexicon item.
     */
    private Lexicon GetLexiconItem(int dataIndex) {
        switch (view_context) {
            case FAVORITE_LIST:
                return lexDataSource.getLexicon(FavLexManager.getFavLexiconId((dataIndex)));
            case LEXICON_LIST:
                return lexDataSource.getLexicon(String.valueOf(dataIndex));
            case SEARCH_LIST:
                return listOfLexicon.get(dataIndex);
            case ALPHABET_LIST:
            default:
                return new Lexicon();
        }
    }

    private class ClickListener implements View.OnClickListener {
        private String lexiconId;
        private int indexOfData;
        private Lexicon lexicon;


        ClickListener(Lexicon lexicon, int indexOfData) {
            this.lexiconId = lexicon.getLexiconId() + "";
            this.lexicon = lexicon;
            this.indexOfData = indexOfData;
        }

        private void replaceFavBtnImage(ImageButton favBtn, int indexOfData) {

            if (FavLexManager.lexIsFavorite(lexiconId)) {
                FavLexManager.removeFromFavList(lexiconId);
                favBtn.setImageResource(R.drawable.ic_star);
            } else {
                FavLexManager.addToFavList(lexiconId);
                favBtn.setImageResource(R.drawable.ic_star_orange);
            }

            if (view_context == VIEW_CONTEXT.FAVORITE_LIST) {
                notifyItemRemoved(indexOfData);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.favBtn:
                    replaceFavBtnImage((ImageButton) v, indexOfData);
                    break;
                case R.id.adioBtn:
                    if (lexicon != null) {
                        Log.d(TAG, lexicon.getKejomWord() + " Audio Btn clicked" + v.getId());
                        audioManager.playAudio(lexicon.getKejomWord());
                    }
                    break;
            }
        }//onClick Ends
    }//ClickListener Ends

    @Override
    public Filter getFilter() {
        if (lexiconFilter == null) {
            lexiconFilter = new LexiconFilter();
        }
        return lexiconFilter;
    }

    public void changeContext(VIEW_CONTEXT context) {
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


            if (charSequence == null && charSequence.length() == 0) {
                return null;
            }

            ArrayList<Lexicon> filteredData = new ArrayList<>();

            //
            int pageIncrement = 100;

            int startIndex = 1;
            int endIndex = pageIncrement;

            int MAX_INDEX = 1993; //Max number of lexicons for now

            while (endIndex < MAX_INDEX) {


                for (Lexicon lexicon : lexDataSource.provideData(startIndex, endIndex)) {

                    if (lexicon == null) continue;

                    if (lexicon.getEnglishWord().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())) {

                        System.out.println("Found searched word " + lexicon.getEnglishWord());

                        filteredData.add(lexicon);

                    }
                }

                if (endIndex < MAX_INDEX) {
                    startIndex = endIndex;
                    endIndex = endIndex + pageIncrement;
                }
                if (endIndex == MAX_INDEX) {
                    endIndex++;
                }

                if (endIndex > MAX_INDEX) {
                    endIndex = MAX_INDEX;
                    startIndex = endIndex - 1;

                    for (Lexicon lexicon : lexDataSource.provideData(startIndex, endIndex)) {
                        if (lexicon.getEnglishWord().toLowerCase()
                                .contains(charSequence.toString().toLowerCase())) {

                            System.out.println("Found searched word " + lexicon.getEnglishWord());

                            filteredData.add(lexicon);

                        }
                    }
                    break;
                }

            }

            endIndex = 0;
            startIndex = 0;

            filteredResults.values = filteredData;
            filteredResults.count = filteredData.size();


            return filteredResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listOfLexicon.clear();
            listOfLexicon.addAll(((ArrayList<Lexicon>) filterResults.values));
            Log.i("publishResults", "publishResults: Number of search results: " + listOfLexicon.size());
            view_context = VIEW_CONTEXT.SEARCH_LIST;
            notifyDataSetChanged();
        }
    }


}//LexAdapter Ends
