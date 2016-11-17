/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jessica
 */
public class JSONLoader {
    JSONParser parser;
    JSONArray array;
    
    public JSONLoader(){
        parser = new JSONParser();
    }
    
    public JSONLoader(String address) throws IOException, ParseException{
        parser = new JSONParser();
        array = (JSONArray) parser.parse(new FileReader(address));
    }
    
    public void load(String address) throws IOException, ParseException{
        array = (JSONArray) parser.parse(new FileReader(address));
    }
    
    public void getReview(){
        for (Object obj : array){
            JSONObject review = (JSONObject) obj;
            
            long rating = (long) review.get("rating");
            System.out.println (rating);
            
            JSONArray text = (JSONArray) review.get("text");
            Iterator<String> iterator = text.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
            
            
        }
    }
    
    public static void main(String[] args) {
        try {
            JSONLoader loader = new JSONLoader("test.json");
            loader.getReview();
        } catch (IOException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
