/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    private JSONParser parser;
    private JSONArray array;
    private ArrayList<Review> review;  
    
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
        review = new ArrayList<>(); 
        
        for (Object obj : array){
            JSONObject json = (JSONObject) obj;
            String content = "";
            
            long rating = (long) json.get("rating");
            System.out.println (rating);
            
            JSONArray text = (JSONArray) json.get("text");
            Iterator<String> iterator = text.iterator();
            while (iterator.hasNext()) {
                content += iterator.next();
                //System.out.println("s: " + iterator.next());
            }
            
            review.add(new Review(rating, content));
            System.out.println("content: "+ review.get(review.size()-1).getContent());
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
