package com.prestech.babankilexicon.viewHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.prestech.babankilexicon.model.Lexicon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexAdapter extends RecyclerView.Adapter<LexAdapter.LexViewHolder> implements Filterable {
    private static final int ALPHABET_INDEX_OFFSET = 0;
    private static final int EXPANDED_CARD_ELEVATION = 10;
    //    private ArrayList<Lexicon> listOfLexicon;
    private LexDataSource lexDataSource;
    private VIEW_CONTEXT view_context;
    private AudioManager audioManager;
    private OnAlphabetSelectListener onCharIndexSelectListener;
    private LexiconFilter lexiconFilter;
    private LinearLayout lastToggled = null;
    private CardView lastRoot = null;
    private List<Lexicon> mLexicons;
    private List<Lexicon> originalLexicons;
    private List<String> mAlphaList;

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
                    alphabetTv = view.findViewById(R.id.alphabet_tv);
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

//        if (listOfLexicon == null) {
//            listOfLexicon = new ArrayList<>();
//        }

        mLexicons = new ArrayList<>();
        originalLexicons = new ArrayList<>();
        mAlphaList = new ArrayList<>();
        //lexDataSet =  lexDataSource.loadInitialData();
    }

    public void setData(List<Lexicon> data) {
        this.originalLexicons.addAll(data);
        this.mLexicons = data;

        if (view_context == VIEW_CONTEXT.ALPHABET_LIST)
            mAlphaList.addAll(Arrays.asList(LexDataSource.alphabets));
        notifyDataSetChanged();
    }

    public LexAdapter(Context context, VIEW_CONTEXT view_context,
                      OnAlphabetSelectListener listener) {
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
                            TextView textView = view.findViewById(R.id.alphabet_tv);
                            String value = textView.getText().toString();

                            int alphaIndex = findAlphaIndex(value);

                            if (alphaIndex == -1) {
                                return;
                            }
                            alphaIndex = alphaIndex + ALPHABET_INDEX_OFFSET;

                            onCharIndexSelectListener.retrieveSelectedIndex(alphaIndex);

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

    private int findAlphaIndex(String key) {
        String tribalWord;

        for (int i = 0; i < mLexicons.size(); i++) {
            tribalWord = mLexicons.get(i).getKejomWord();
            if (tribalWord.toLowerCase().startsWith(key.toLowerCase()))
                return i;
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull LexViewHolder lexViewHolder, int dataIndex) {
        Lexicon lexicon = GetLexiconItem(dataIndex);
        String lexiconId;

        if (view_context == VIEW_CONTEXT.ALPHABET_LIST) {
            lexViewHolder.alphabetTv.setText(mAlphaList.get(dataIndex));
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
            root.setCardElevation(EXPANDED_CARD_ELEVATION);
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
            case SEARCH_LIST:
//                return listOfLexicon.size();
            case LEXICON_LIST:
                return mLexicons.size();

            case ALPHABET_LIST:
                return mAlphaList.size();
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
//                return lexDataSource.getLexicon(String.valueOf(dataIndex));
            case SEARCH_LIST:
//                return listOfLexicon.get(dataIndex);
                return mLexicons.get(dataIndex);
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

    public void clearSearch() {
//        this.view_context = context;
        mLexicons = originalLexicons;
        notifyDataSetChanged();
    }

    /*********************************************************************************************
     * This class filters the data and provide values that contains the search key
     */
    public class LexiconFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filteredResults = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                return null;
            }

            List<Lexicon> filteredData;
            if (charSequence.length() == 0)
                filteredData = originalLexicons;
            else {
                filteredData = getFilteredData(charSequence.toString().toLowerCase());
            }
            filteredResults.values = filteredData;

            return filteredResults;
        }

        private List<Lexicon> getFilteredData(String constraint) {
            List<Lexicon> results = new ArrayList<>();
            for (Lexicon lexicon : originalLexicons) {
                if (lexicon.getEnglishWord().toLowerCase().contains(constraint))
                    results.add(lexicon);
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            listOfLexicon.clear();
//            listOfLexicon.addAll(((ArrayList<Lexicon>) filterResults.values));
            mLexicons = (List<Lexicon>) filterResults.values;
//            view_context = VIEW_CONTEXT.SEARCH_LIST;
            notifyDataSetChanged();
        }
    }


}//LexAdapter Ends
