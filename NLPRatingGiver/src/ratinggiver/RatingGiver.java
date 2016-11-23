/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static ratinggiver.Preprocessor.posTagger;
import weka.classifiers.trees.SimpleCart;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author jessica
 */
public class RatingGiver {
    public static void main(String[] args) throws IOException, Exception{
        BufferedReader br = new BufferedReader(
                         new FileReader("harry.potter.test.arff"));

        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br);
        Instances testset = arff.getData();
        testset.setClassIndex(testset.numAttributes() - 1);
        
        br = new BufferedReader(
                         new FileReader("harry.potter.1.combine.filtered.arff"));
        arff = new ArffLoader.ArffReader(br);
        Instances trainset = arff.getData();
        trainset.setClassIndex(0);
        ArrayList<Attribute> attr = new ArrayList<>();
        for (int i = 1; i < trainset.numAttributes(); i++){
            attr.add(trainset.attribute(i));
            System.out.println(trainset.attribute(i));
        }
        
        // Cocokin arff test set sama attr
        
        SimpleCart tree = new SimpleCart();
        
        // JSONLoader loader = new JSONLoader("dataset/harry_potter_1.json");
        // loader.getReview();
        
        // ### CONTOH CARA PAKE calculateSentiment ###
        // String text = "I want to see Harry become a better person and be able to prove Draco and his stupid friends wrong about what they think";
        // double score = calculateSentiment(text);
        // System.out.println("score = "+score);
    }
    
    public static double calculateSentiment(String text) throws IOException {
        double sentimentScore = 0;
        
        // POST Tag
        ArrayList<String> tag = posTagger(text);
        
        // calculate Score based on SentiWordNet
        SentiWordNet sentiWordNet = new SentiWordNet();
        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            double s = sentiWordNet.getScore(words[i], tag.get(i));
            System.out.println(s);
            sentimentScore += s;
        }
        return sentimentScore;
    }
}
