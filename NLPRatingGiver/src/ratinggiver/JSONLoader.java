/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
            
            long rating = (long) json.get("rating");
            System.out.println (rating);
            
            String content = (String) json.get("text");
            
            review.add(new Review(rating, content));
            System.out.println("content: "+ review.get(review.size()-1).getContent());
        }
    }
    
    public static void main(String[] args) {
        try {
            JSONLoader loader = new JSONLoader("dataset/harry_potter_1.json");
            loader.getReview();
            loader.arffGenerator("harry.potter.1.arff", loader.review);
        } catch (IOException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void arffGenerator(String arffFile, ArrayList<Review> review){
        try {
            File file = new File(arffFile);
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@relation review\n\n");
            
            bw.write("@attribute sentence string\n");
            bw.write("@attribute aspect {character, plot, neither}\n\n");
            bw.write("@data\n");
            
            for (Review r: review){
                ArrayList<String> sentences = r.getSentences();
                for (String s: sentences){
                    bw.write(s + ",\n");
                }
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
