/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratinggiver;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author jessica
 */
public class Review {
    private long rating;
    private String content;
    private ArrayList<String> sentences;
    
    public Review (){
        rating = 0;
        content = "";
        sentences = new ArrayList<>();
    }
    
    public Review (long rating, String content){
        this.rating = rating;
        this.content = content;
        sentences = new ArrayList<>();
    
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(content);
        int start = iterator.first();
        for (int end = iterator.next();
            end != BreakIterator.DONE;
            start = end, end = iterator.next()) {
                System.out.println(content.substring(start,end));
                sentences.add(content.substring(start,end));
        }
    }

    /**
     * @return the rating
     */
    public long getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(long rating) {
        this.rating = rating;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sentences
     */
    public ArrayList<String> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(ArrayList<String> sentences) {
        this.sentences = sentences;
    }
    
    
}
