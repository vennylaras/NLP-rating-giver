/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.ArrayList;

/**
 *
 * @author jessica
 */
public class Preprocessor {
    
    public static ArrayList<String> posTagger (String text) {
        // Initialize the tagger
        MaxentTagger tagger = new MaxentTagger(
                "models/english-left3words-distsim.tagger");
        
        String tagged = tagger.tagString(text);
        System.out.println(tagged);
        
        // Tagger simplified into: "V" (verb), "N" (noun), "R"(adverb), "A"(adjective), "-"(none)
        // Biar sesuai sama POS di SentiWordNet
        ArrayList<String> tags= new ArrayList<>();
        String[] taggedArr = tagged.split(" ");
        for (String s: taggedArr) {
            String[] word = s.split("_");
            switch (word[1]) {
                case "CC": tags.add("-"); break;
                case "CD": tags.add("-"); break;
                case "DT": tags.add("-"); break;
                case "EX": tags.add("-"); break;
                case "FW": tags.add("-"); break;
                case "IN": tags.add("-"); break;
                case "JJ": tags.add("A"); break;
                case "JJR": tags.add("A"); break;
                case "JJS": tags.add("A"); break;
                case "LS": tags.add("-"); break;
                case "MD": tags.add("-"); break;
                case "NN": tags.add("N"); break;
                case "NNS": tags.add("N"); break;
                case "NNP": tags.add("N"); break;
                case "NNPS": tags.add("N"); break;
                case "PDT": tags.add("-"); break;
                case "POS": tags.add("-"); break;
                case "PRP": tags.add("N"); break;
                case "PRP$": tags.add("N"); break;
                case "RB": tags.add("R"); break;
                case "RBR": tags.add("R"); break;
                case "RBS": tags.add("R"); break;
                case "RP": tags.add("-"); break;
                case "SYM": tags.add("-"); break;
                case "TO": tags.add("-"); break;
                case "UH": tags.add("-"); break;
                case "VB": tags.add("V"); break;
                case "VBD": tags.add("V"); break;
                case "VBG": tags.add("V"); break;
                case "VBN": tags.add("V"); break;
                case "VBP": tags.add("V"); break;
                case "VBZ": tags.add("V"); break;
                case "WDT": tags.add("-"); break;
                case "WP": tags.add("N"); break;
                case "WP$": tags.add("N"); break;
                case "WRB": tags.add("R"); break;
                default: tags.add("-");
            }
        }
        return tags;
    }
}
