/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.Scanner;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.SpreadSubsample;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author jessica
 */
public class RatingGiver {
    public static void main(String[] args) throws IOException, Exception{
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Input review: ");
        String jsonfile = scanner.next();
        String arfffile = jsonfile.replace(".json", ".arff").replaceAll("_", ".");
        JSONLoader loader = new JSONLoader();
        loader.convertJson(jsonfile, arfffile);
        
        SentenceClassifier sc = new SentenceClassifier();
        sc.loadModel("AspectClassifier.model");
        String classifiedfile = arfffile.replace(".arff",".classified.arff");
        Instances data = sc.classify(arfffile,classifiedfile);
        
        SentiWordNet sentiment = new SentiWordNet();
        double[] score = sentiment.overallSentiment(data);
        // System.out.println("\ncharacter score = " + score[0]);
        // System.out.println("plot score = " + score[1]);
        
        double[] rating = sentiment.overallRating(score);
        System.out.print("\ncharacter rating = ");
        System.out.printf("%.2f", rating[0]);
        System.out.print("\nplot rating = ");
        System.out.printf("%.2f", rating[1]);
        System.out.println();
    }
    

    public static void filterData(String training, String test) throws FileNotFoundException{
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(training));
            Instances trainingData = new Instances(br);
            br = new BufferedReader(
                    new FileReader(test));
            Instances testData = new Instances(br);
            StringToWordVector stwv = new StringToWordVector();
            stwv.setInputFormat(trainingData);
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            testData.setClassIndex(testData.numAttributes() - 1);
            Instances filteredTraining = Filter.useFilter(trainingData, stwv);
            Instances filteredTest = Filter.useFilter(testData, stwv);
            filteredTraining.setClassIndex(trainingData.numAttributes() - 1);
            filteredTest.setClassIndex(testData.numAttributes() - 1);
            
            BufferedWriter wr = new BufferedWriter(new FileWriter ("harry.potter.1.combine.filtered.arff"));
            wr.write(filteredTraining.toString());
            wr.newLine();
            wr.flush();
            wr.close();
            
            BufferedWriter wr1 = new BufferedWriter(new FileWriter ("harry.potter.test.filtered.arff"));
            wr1.write(filteredTest.toString());
            wr1.newLine();
            wr1.flush();
            wr1.close();
                        
        } catch (Exception ex) {
            Logger.getLogger(RatingGiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
