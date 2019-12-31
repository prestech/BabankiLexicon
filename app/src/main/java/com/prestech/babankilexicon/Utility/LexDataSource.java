package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com;
import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.model.Lexicon;
import com.prestech.babankilexicon.viewHelper.LexAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LexDataSource {

    private Context context = null;
    private static JsonNode rootNode = null;
    private static ObjectMapper objectMapper = null;
    private List<Lexicon> dataBufferList = null;
    public static final String[] alphabets = {"A" , "B" , "Bv" , "Ch" , "D" , "Dz" , "E" , "Ə" ,
            "Ff" , "G" , "Gh" , "I" , "Ɨ" , "J" , "ʼ" , "K" , "L" , "M" , "N" , "Ny" , "Ŋ" , "O" , "Pf" , "S" , "Sh" , "T" , "Ts" , "U" , "Ʉ" , "V" , "W" , "Y" , "Z" , "Zh"};
    public final Map<String, Integer> alphabetIndex = new HashMap<>();

    //read the data from the JSON and Parse them to object
    //control the number of Lexicon objects created
    public LexDataSource(Context context){
        this.context = context;

        if( objectMapper == null){

            objectMapper = new ObjectMapper();
        }

        if(dataBufferList == null){
           dataBufferList = Arrays.asList(new Lexicon[110]);
        }

        if(rootNode == null) {

            try(InputStream inputStream = context.getResources().openRawResource(R.raw.kejomlexicon)) {
               rootNode = objectMapper.readTree(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }//if Ends

        for (String a: alphabets ) {
            alphabetIndex.put(a, 0);
        }
    }

    public Lexicon getLexicon(String lexiconId){
        Log.i("getLexicon", "getLexicon: lexiconId "+lexiconId);
        try {
            Lexicon lexicon = objectMapper.readValue(rootNode.path(lexiconId).toString(), Lexicon.class);
            return lexicon;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int findAlphaIndex(String key){

        String tribalWord = "";
        Lexicon lexicon = null;
        for(int aIndex=0; aIndex < 1993; aIndex++){

            lexicon = getLexicon(""+aIndex);
            if (lexicon != null){
                tribalWord = lexicon.getKejomWord();
            }else{
                return -1;
            }

            if(tribalWord.toLowerCase().startsWith(key.toLowerCase())){

                return aIndex;
            }

        }
        return -1;
    }


    public List<Lexicon> loadInitialData(){

        Lexicon lexicon;

        for(int lexIndex = 0; lexIndex < 30; lexIndex++) {

            lexicon = getLexicon("" + lexIndex);
            dataBufferList.set(lexIndex, lexicon);

        }//for Ends

        return dataBufferList;
    }

    public List<Lexicon> provideData(int startIndex, int endIndex){

        Lexicon lexicon;
        int listIndex;
        Log.d("PRESDEBUG", "START LOAD" + startIndex);

        for(int lexIndex = startIndex; lexIndex < endIndex; lexIndex++) {

            listIndex = lexIndex%110;

            lexicon = getLexicon("" + lexIndex);

            if(lexicon != null){
                dataBufferList.set(listIndex, lexicon);
            }


        }//for Ends

        Log.d("PRESDEBUG", dataBufferList.toString());

        return dataBufferList;
    }


    public Lexicon getFavLexicon(int index) {
            String lexiconId = FavLexManager.getFavLexiconId(index);
            return  getLexicon(lexiconId);
    }
}
