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
        
        //cocokin arff test set sama attr
        
        SimpleCart tree = new SimpleCart();
        
        //JSONLoader loader = new JSONLoader("dataset/harry_potter_1.json");
        //loader.getReview();
        
        
        
    }
    
    public double calculateSentiment(String text) {
        double score = 0;
        
        // POST Tag
        ArrayList<String> tag = posTagger(text);
        for (String t: tag) {
            System.out.print(t+ " ");
        }
        
        
        
        return score;
    }
}
