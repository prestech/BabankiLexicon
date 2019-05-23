package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com;
import com.prestech.babankilexicon.R;
import com.prestech.babankilexicon.model.Lexicon;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LexDataSource {

    private Context context = null;
    private static JsonNode rootNode = null;
    private static ObjectMapper objectMapper = null;
    private List<Lexicon> dataBufferList = null;

    //read the data from the JSON and Parse them to object
    //control the number of Lexicon objects created
    public LexDataSource(Context context){
        this.context = context;

        if( objectMapper == null){

            objectMapper = new ObjectMapper();
        }

        if(dataBufferList == null){
            dataBufferList = Arrays.asList(new Lexicon[30]);
        }

        if(rootNode == null) {

            try(InputStream inputStream = context.getResources().openRawResource(R.raw.kejomlexicon)) {
               rootNode = objectMapper.readTree(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//if Ends
    }

    public Lexicon getLexicon(String lexiconId){

        try {
            Lexicon lexicon = objectMapper.readValue(rootNode.path(lexiconId).toString(), Lexicon.class);
            return lexicon;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Lexicon> loadInitialData(){

        Lexicon lexicon;

        for(int lexIndex = 0; lexIndex < 30; lexIndex++) {

            lexicon = getLexicon("" + lexIndex);
            dataBufferList.set(lexIndex, lexicon);

        }//for Ends

        return dataBufferList;
    }
    public List<Lexicon> provideData(int startIndex, int displace){

        Lexicon lexicon;
        int listIndex;
        Log.d("PRESDEBUG", "START LOAD" + startIndex);

        for(int lexIndex = startIndex+displace; lexIndex < (startIndex+displace)+30; lexIndex++) {

            listIndex = (lexIndex%30);

            lexicon = getLexicon("" + lexIndex);

            if(lexicon != null){
                dataBufferList.set(listIndex, lexicon);
            }else{
                dataBufferList.set(listIndex, null);
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
