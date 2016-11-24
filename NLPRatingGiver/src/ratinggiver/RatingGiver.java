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

import static ratinggiver.Preprocessor.posTagger;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.SimpleCart;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author jessica
 */
public class RatingGiver {
    public static void main(String[] args) throws IOException, Exception{

//        BufferedReader br = new BufferedReader(
//                         new FileReader("harry.potter.test.arff"));
//
//        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br);
//        Instances testset = arff.getData();
//        testset.setClassIndex(testset.numAttributes() - 1);
//        
//        br = new BufferedReader(
//                         new FileReader("harry.potter.1.combine.filtered.arff"));
//        arff = new ArffLoader.ArffReader(br);
//        Instances trainset = arff.getData();
//        trainset.setClassIndex(0);
//        ArrayList<Attribute> attr = new ArrayList<>();
//        for (int i = 1; i < trainset.numAttributes(); i++){
//            attr.add(trainset.attribute(i));
//            System.out.println(trainset.attribute(i));
//        }
        
        // Cocokin arff test set sama attr
        
        //SimpleCart tree = new SimpleCart();

        // JSONLoader loader = new JSONLoader("dataset/harry_potter_1.json");
        // loader.getReview();
        
        BufferedReader br = new BufferedReader(
                         new FileReader("harry.potter.test.filtered.arff"));

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
            // System.out.println(trainset.attribute(i));
        }
        
        // Cocokin arff test set sama attr
        
        SimpleCart tree = new SimpleCart();
        tree.buildClassifier(trainset);
        // System.out.println(tree.toString());

        
        //Evaluation eval = new Evaluation(testset);
       // eval.evaluateModel(tree, testset);
//        System.out.println(eval.toSummaryString("\n\n\n\nFull Training\n============\n", false));
//        eval.crossValidateModel(tree, testset, 10, new Random());
//        System.out.println(eval.toSummaryString("\n\n\n\n10-Fold Cross Validation\n============\n", false));
        
        // ### CONTOH CARA PAKE calculateSentiment ###
        // String text = "I want to see Harry become a better person and be able to prove Draco and his stupid friends wrong about what they think";
        // double score = calculateSentiment(text);
        // System.out.println("score = "+score);
        filterData("harry.potter.1.combine.arff","harry.potter.test.arff");
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
            
            BufferedWriter wr = new BufferedWriter(new FileWriter ("filteredTraining.arff"));
            wr.write(filteredTraining.toString());
            wr.newLine();
            wr.flush();
            wr.close();
            
            BufferedWriter wr1 = new BufferedWriter(new FileWriter ("filteredTest.arff"));
            wr1.write(filteredTest.toString());
            wr1.newLine();
            wr1.flush();
            wr1.close();
                        
        } catch (Exception ex) {
            Logger.getLogger(RatingGiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
