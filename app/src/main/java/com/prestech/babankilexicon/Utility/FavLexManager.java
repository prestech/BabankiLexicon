package com.prestech.babankilexicon.Utility;

import android.content.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FavLexManager {

   private static ArrayList<String> favList;
   private static JsonNode jsonNode;
   private static  File file;
   private static Context context;
   private static boolean favLexIsAltered;

    public static void initializeManager(Context ctx){
        context = ctx;
        file = new File(context.getFilesDir()+"/favLexicon.json");
        favLexIsAltered = false;
        readFavListFromFile();
    }

    public static boolean lexIsFavorite(String lexiconId){
        if(favList == null)return  false;

        return favList.contains(lexiconId);
    }

    public static void  addToFavList(String lexiconId){
        favList.add(lexiconId);
        favLexIsAltered = true;

    }

    public static  void  removeFromFavList(String lexiconId){
        favList.remove(lexiconId);
        favLexIsAltered = true;
    }

    private  static void readFavListFromFile(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            favList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class) );
            //Log.d("PRESDEBUG", "READ JSON VALUES: "+favList.toString());
        } catch (IOException e) {
            favList = new ArrayList<>();
            e.printStackTrace();
        }//try-catch
    }

    /***
     * Writes the favorite lexicon IDs to file
     */
    public  static void writeFavListToFile(){

        //only write to file if there was a change in the list of fav lixcon
        if (favLexIsAltered == false) return;

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            new ObjectMapper().writeValue(fileOutputStream, favList);

        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    
    public static String getFavLexiconId(int index){
        if(index < favList.size()){
            return favList.get(index);
        }//if Ends

        return null;
    }

    public static int getFavListSize(){

        if(favList != null)
            return favList.size();
        else return 0;
    }
}
