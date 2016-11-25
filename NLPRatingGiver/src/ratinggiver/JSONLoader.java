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
            //System.out.println (rating);
            
            String content = (String) json.get("text");
            
            review.add(new Review(rating, content));
            //System.out.println("content: "+ review.get(review.size()-1).getContent());
        }
    }
    
    public void convertJson(String filename, String outfile) throws IOException, ParseException {
        JSONLoader loader = new JSONLoader(filename);
        loader.getReview();
        
        loader.arffGenerator(outfile, loader.review);
        System.out.println("Generating " + outfile + " succeed!");
    }
    
    public void arffGenerator(String arffFile, ArrayList<Review> review) throws IOException{
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
                s = s.replaceAll("[U+].....", "");
                s = s.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");
                if (s.matches(".*\\w.*")){
                    //System.out.println(s);
                    bw.write("\""+ s + "\",?\n");
                }
            }
        }
        bw.close();
        fw.close();

    }    
}