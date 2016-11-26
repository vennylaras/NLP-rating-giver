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
import java.util.Map;
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
    private boolean adjectiveOnly = true;
    
    public SentiWordNet() throws FileNotFoundException, IOException {
        System.out.print("Generating dictionary ... ");
        BufferedReader csv =  new BufferedReader(new FileReader("SentiWordNet_3.0.0_20130122.txt"));
        String line = "";           
        
        HashMap<String, HashMap<Integer, Double>> tempdict_a = new HashMap<>();
        HashMap<String, HashMap<Integer, Double>> tempdict_v = new HashMap<>();
        HashMap<String, HashMap<Integer, Double>> tempdict_n = new HashMap<>();
        HashMap<String, HashMap<Integer, Double>> tempdict_r = new HashMap<>();
        
        while((line = csv.readLine()) != null) {
            String[] data = line.split("\t");
            
            Double score = 0.0;
            if (data[2].contains(".") && data[3].contains(".")) {
                score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
            } else if (data[2].contains(".") && !data[3].contains(".")) {
                score = Double.parseDouble(data[2]);
            } else if (!data[2].contains(".") && data[3].contains(".")) {
                score = -Double.parseDouble(data[3]);
            }
            
            String[] words = data[4].split(" ");
            for(String word: words) {
                String[] w = word.split("#");
                switch(data[0]) {
                    case "a" :
                        if (!tempdict_a.containsKey(w[0])) tempdict_a.put(w[0], new HashMap<>());
                        tempdict_a.get(w[0]).put(Integer.parseInt(w[1]), score);
                        break;
                    case "n" :
                        if (!tempdict_n.containsKey(w[0])) tempdict_n.put(w[0], new HashMap<>());
                        tempdict_n.get(w[0]).put(Integer.parseInt(w[1]), score);
                        break;
                    case "v" :
                        if (!tempdict_v.containsKey(w[0])) tempdict_v.put(w[0], new HashMap<>());
                        tempdict_v.get(w[0]).put(Integer.parseInt(w[1]), score);
                        break;
                    case "r" :
                        if (!tempdict_r.containsKey(w[0])) tempdict_r.put(w[0], new HashMap<>());
                        tempdict_r.get(w[0]).put(Integer.parseInt(w[1]), score);
                        break;
                }
            }
        }
        
        generateDictionary(tempdict_a, "a");
        generateDictionary(tempdict_n, "n");
        generateDictionary(tempdict_v, "v");
        generateDictionary(tempdict_r, "r");
        
        System.out.println("done");
    }
    
    public SentiWordNet(boolean adj) throws IOException {
        this();
        this.adjectiveOnly = adj;
    }
    
    private void generateDictionary(HashMap<String, HashMap<Integer, Double>> tempdict, String pos) {
        for (Map.Entry<String, HashMap<Integer, Double>> entry : tempdict.entrySet()) {
            String word = entry.getKey();
            Map<Integer, Double> synSetScoreMap = entry.getValue();

            // Calculate weighted average. Weigh the synsets according to their rank.
            // Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
            // Sum = 1/1 + 1/2 + 1/3 ...
            double score = 0.0;
            double sum = 0.0;
            for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
                    score += setScore.getValue() / (double) setScore.getKey();
                    sum += 1.0 / (double) setScore.getKey();
            }
            score /= sum;

            switch(pos) {
                case "a" : dictionary_a.put(word, score); break;
                case "n" : dictionary_n.put(word, score); break;
                case "v" : dictionary_v.put(word, score); break;
                case "r" : dictionary_r.put(word, score); break;
            }
        }
    }
    
    public Double getScore(String word, String pos) {
        Double score = 0.0;
        if (adjectiveOnly) {
            switch(pos) {
                case "A" : score = dictionary_a.get(word); break;
                case "N" : score = 0.0; break;
                case "V" : score = 0.0; break;
                case "R" : score = 0.0; break;
            }
        } else {
            switch(pos) {
                case "A" : score = dictionary_a.get(word); break;
                case "N" : score = dictionary_n.get(word); break;
                case "V" : score = dictionary_v.get(word); break;
                case "R" : score = dictionary_r.get(word); break;
            }
        }
        if (score == null) score = 0.0;
        return score;
    }
    
    // Calculate sentence polarity
    // count=3 || 1=positive, 0=neutral, -1=negative
    // count=5 || 1=strongly positive, 0.5=weakly positive, 0=noutral, -0.5=weakly negative, -1=strongly negative
    public double calculatePolarity(String sentence, int count) throws IOException {
        double sentiment = calculateSentiment(sentence);
        double polarity = 0;
        if (count == 3) {
            if (sentiment < 0 ) {
                polarity = -1;
            } else if (sentiment > 0) {
                polarity = 1;
            } else {
                polarity = 0;
            }
        } else if (count == 5) {
            if (sentiment <= -0.5 ) {
                polarity = -1;
            } else if (sentiment > -0.5 && sentiment < 0) {
                polarity = -0.5;
            } else if (sentiment ==0){
                polarity = 0;
            } else if (sentiment > 0 && sentiment < 0.5) {
                polarity = 0.5;
            } else {
                polarity = 1;
            }
        }
        return polarity;
    }
    
    // Calculate sentence score
    public double calculateSentiment(String text) throws IOException {
        double sentimentScore = 0.0;
        int sentiWordCount = 0;
        // POST Tag
        ArrayList<String> tag = preprocessor.posTagger(text);
        
        // calculate Score based on SentiWordNet
        String[] words = text.trim().split(" ");
        for (int i = 0; i < words.length; i++) {
            double s = getScore(words[i], tag.get(i));
            sentimentScore += s;
            if (s != 0) sentiWordCount++;
        }
        if (sentiWordCount == 0) return 0.0;
        return sentimentScore/sentiWordCount;
    }
    
    public double[] overallSentiment(Instances data) throws IOException {
        System.out.print("Calculating sentiment score ... ");
        double[] score = new double[2];
        double characterSum = 0.0;
        double plotSum = 0.0;
        int characterCount = 0;
        int plotCount = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            if (data.instance(i).stringValue(data.instance(i).classAttribute()).equals("character")) {
                if (!data.instance(i).stringValue(0).equals("character")) {
                    double s = calculatePolarity(data.instance(i).stringValue(0), 3);
                    characterSum += s;
                    characterCount++;
                }
            } else if (data.instance(i).stringValue(data.instance(i).classAttribute()).equals("plot")){
                double s = calculatePolarity(data.instance(i).stringValue(0), 3);
                plotSum += s;
                plotCount++;
            }
        }
        score[0] = characterSum/characterCount;
        score[1] = plotSum/plotCount;
        System.out.println("done");
        return score;
    }
    
    public double[] overallRating(double[] score) {
        double[] rating = new double[score.length];
        for (int i = 0; i < score.length; i++) {
            rating[i] = (score[i]+1)*5/2;
        }
        return rating;
    }
    
    public double maxScore() {
        double max = 0;
        for (Map.Entry<String, Double> entry : dictionary_a.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_n.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_v.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_r.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
        }
        return max;
    }
    
    public double minScore() {
        double min = Integer.MAX_VALUE;
        for (Map.Entry<String, Double> entry : dictionary_a.entrySet()) {
            if (entry.getValue() < min) min = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_n.entrySet()) {
            if (entry.getValue() < min) min = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_v.entrySet()) {
            if (entry.getValue() < min) min = entry.getValue();
        }
        for (Map.Entry<String, Double> entry : dictionary_r.entrySet()) {
            if (entry.getValue() < min) min = entry.getValue();
        }
        return min;
    }
    
    // MAIN FOR TESTING
    public static void main (String args[]) throws IOException {
        BufferedReader br = new BufferedReader(
                         new FileReader("harry.potter.1.combine.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br);
        Instances data = arff.getData();
        data.setClassIndex(1);
        
        SentiWordNet sentiWordNet = new SentiWordNet();
        System.out.println("max value = " + sentiWordNet.maxScore());
        System.out.println("min value = " + sentiWordNet.minScore());
    }
}
