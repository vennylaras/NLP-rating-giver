/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.io.File;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ClassAssigner;

/**
 *
 * @author X200MA
 */
public class SentenceClassifier {
    private FilteredClassifier fClassifier;
    private Instances data, result;
    private ClassAssigner classAssigner;
    
    public SentenceClassifier() {
        classAssigner = new ClassAssigner();
    }
    
    public void setTrainData(Instances _data){
        data = new Instances(_data);
        System.out.println("Setting training data success.");
    }
    
    public void setTrainData(String fileName) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileName);
        data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("Loading file "+fileName+" success.");
    }
    
    public void filterData() throws Exception {
        String[] options = new String[2];
        options[0] = "-C";                                    
        options[1] = "last";                                      
        classAssigner.setOptions(options);                           
        classAssigner.setInputFormat(data);                          
        Instances newData = Filter.useFilter(data, classAssigner); 
        data = new Instances(newData);
        System.out.println("Filtering data success.");
    }
    
    public void buildClassifier() throws Exception {
        String[] options = new String[9];
        options[0] = "-F";
        options[1] = "weka.filters.unsupervised.attribute.StringToWordVector -R first-last -W 1000 -prune-rate -1.0 -C -T -I -N 0 -stemmer weka.core.stemmers.IteratedLovinsStemmer -stopwords-handler weka.core.stopwords.MultiStopwords -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\\'\\\\\\\"()?!\\\"\"";
        options[2] = "-W";
        options[3] = "weka.classifiers.trees.J48";
        options[4] = "--";
        options[5] = "-C";
        options[6] = "0.25";
        options[7] = "-M";
        options[8] = "2";
        fClassifier = new FilteredClassifier();         
        fClassifier.setOptions(options);     
        fClassifier.buildClassifier(data);
        
        Instances train = new Instances(data);
//        Instances test = new Instances(data);
        
        
        fClassifier.buildClassifier(train);
        
//        Evaluation eval = new Evaluation(train);
//        eval.evaluateModel(fClassifier, test);
//        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
        System.out.println("Building classifier success.");
    }
    
    public Instances classify(String fileName) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileName);
        result = source.getDataSet();
        result.setClassIndex(result.numAttributes() - 1);
        
        for(int i = 0; i < result.numInstances(); i++){
            double value = fClassifier.classifyInstance(result.instance(i));
            result.instance(i).setClassValue(value);
        }
        
        return result;
    }
    
    public Instances classify(Instances _data) throws Exception {
        result = new Instances(_data);
        
        for(int i = 0; i < result.numInstances(); i++){
            double value = fClassifier.classifyInstance(result.instance(i));
            result.instance(i).setClassValue(value);
        }
        
        return result;
    }
    
    public void classify(String fileName, String fileDestination) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileName);
        result = source.getDataSet();
        result.setClassIndex(result.numAttributes() - 1);
        
        System.out.println("Classifying file "+fileName+".");
        
        for(int i = 0; i < result.numInstances(); i++){
            double value = fClassifier.classifyInstance(result.instance(i));
            result.instance(i).setClassValue(value);
        }
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(result);
        saver.setFile(new File(fileDestination));
        //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
        saver.writeBatch();
        
        System.out.println("Result saved in "+fileDestination+".");
    }
    
    public void classify(Instances _data, String fileDestination) throws Exception {
        result = new Instances(_data);
        
        System.out.println("Classifying instances.");
        
        for(int i = 0; i < result.numInstances(); i++){
            double value = fClassifier.classifyInstance(result.instance(i));
            result.instance(i).setClassValue(value);
        }
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(result);
        saver.setFile(new File(fileDestination));
        //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
        saver.writeBatch();
        
        System.out.println("Result saved in "+fileDestination+".");
    }
    
    public static void main(String[] args) throws Exception{
        //Contoh Penggunaan
        SentenceClassifier sc = new SentenceClassifier();
        sc.setTrainData("E:\\NLP-rating-giver\\NLPRatingGiver\\harry.potter.1.combine.arff");
        sc.filterData();
        sc.buildClassifier();
        sc.classify("E:\\NLP-rating-giver\\NLPRatingGiver\\harry.potter.test.arff","E:\\NLP-rating-giver\\NLPRatingGiver\\harry.potter.test.classified.arff");
    }
}