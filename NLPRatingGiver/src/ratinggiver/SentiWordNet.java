/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Venny
 */
public class SentiWordNet {
    public HashMap<String, Double> dictionary_a = new HashMap<>();
    public HashMap<String, Double> dictionary_n = new HashMap<>();
    public HashMap<String, Double> dictionary_v = new HashMap<>();
    public HashMap<String, Double> dictionary_r = new HashMap<>();
    
    public SentiWordNet() throws FileNotFoundException, IOException {
        BufferedReader csv =  new BufferedReader(new FileReader("SentiWordNet_3.0.0_20130122.txt"));
        String line = "";           
        
        while((line = csv.readLine()) != null) {
            String[] data = line.split("\t");
            
            Double score = 0.0;
            if (data[2].contains(".") && data[3].contains(".")) {
                score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
            } else if (data[2].contains(".") && !data[3].contains(".")) {
                score = Double.parseDouble(data[2]);
            } else if (!data[2].contains(".") && data[3].contains(".")) {
                score = Double.parseDouble(data[3]);
            }
            
            String[] words = data[4].split(" ");
            for(String word: words) {
                String[] w = word.split("#");
                switch(data[0]) {
                    case "a" : dictionary_a.put(w[0], score); break;
                    case "n" : dictionary_n.put(w[0], score); break;
                    case "v" : dictionary_v.put(w[0], score); break;
                    case "r" : dictionary_r.put(w[0], score); break;
                }
            }
        }
    }
    
    public Double getScore(String word, String pos) {
        Double score = 0.0;
        switch(pos) {
            case "A" : score = dictionary_a.get(word); break;
            case "N" : score = dictionary_n.get(word); break;
            case "V" : score = dictionary_v.get(word); break;
            case "R" : score = dictionary_r.get(word); break;
        }
        if (score == null) score = 0.0;
        return score;
    }
}
