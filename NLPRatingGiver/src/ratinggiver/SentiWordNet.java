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
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Venny
 */
public class SentiWordNet {
    private HashMap<String, Double> dictionary_a = new HashMap<>();
    private HashMap<String, Double> dictionary_n = new HashMap<>();
    private HashMap<String, Double> dictionary_v = new HashMap<>();
    private HashMap<String, Double> dictionary_r = new HashMap<>();
    private static Preprocessor preprocessor = new Preprocessor();
    
    public static void main (String args[]) throws IOException {
        BufferedReader br = new BufferedReader(
                         new FileReader("harry.potter.1.combine.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br);
        Instances data = arff.getData();
        data.setClassIndex(1);
        
        double[] score = overallSentiment(data);
        System.out.println("character score = " + score[0]);
        System.out.println("plot score = " + score[1]);
    }
    
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
    
    public static double calculateSentiment(String text) throws IOException {
        double sentimentScore = 0.0;
        
        // POST Tag
        ArrayList<String> tag = preprocessor.posTagger(text);
        
        // calculate Score based on SentiWordNet
        SentiWordNet sentiWordNet = new SentiWordNet();
        String[] words = text.trim().split(" ");
        for (int i = 0; i < words.length; i++) {
            // System.out.println(words[i]);
            double s = sentiWordNet.getScore(words[i], tag.get(i));
            // System.out.println(s);
            sentimentScore += s;
        }
        return sentimentScore;
    }
    
    public static double[] overallSentiment(Instances data) throws IOException {
        double[] score = new double[2];
        double characterSum = 0.0;
        double plotSum = 0.0;
        int characterCount = 0;
        int plotCount = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            if (data.instance(i).stringValue(data.instance(i).classAttribute()).equals("character")) {
                double s = calculateSentiment(data.instance(i).stringValue(0));
                characterSum += s;
                characterCount++;
            } else if (data.instance(i).stringValue(data.instance(i).classAttribute()).equals("plot")){
                double s = calculateSentiment(data.instance(i).stringValue(0));
                plotSum += s;
                plotCount++;
            }
        }
        score[0] = characterSum/characterCount;
        score[1] = plotSum/plotCount;
        
        return score;
    }
}
