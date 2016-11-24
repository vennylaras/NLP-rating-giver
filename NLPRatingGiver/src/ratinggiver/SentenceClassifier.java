/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Resample;
import weka.gui.beans.Classifier;

/**
 *
 * @author X200MA
 */
public class SentenceClassifier {
    
    public static void main(String args[]) throws Exception {
        Scanner scan = new Scanner(System.in);
        String fileName = "E:\\NLP-rating-giver\\NLPRatingGiver\\harry.potter.test.arff";//scan.nextLine();
        
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileName);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        
        
        FilteredClassifier cls = (FilteredClassifier) weka.core.SerializationHelper.read("E:\\NLP-rating-giver\\NLPRatingGiver\\test.model");
        
        for(int i = 0; i < data.numInstances(); i++){
            double value = cls.classifyInstance(data.instance(i));
            
            data.instance(i).setClassValue(value);
        }
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("E:/NLP-rating-giver/NLPRatingGiver/harry.potter.test.classified.arff"));
        //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
        saver.writeBatch();
    }
}
