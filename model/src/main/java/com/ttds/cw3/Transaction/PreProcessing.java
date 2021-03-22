package com.ttds.cw3.Transaction;

import com.ttds.cw3.Interface.PreProcessingInterface;
import com.ttds.cw3.Tools.Stemmer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

public final class PreProcessing implements PreProcessingInterface
{
    private Stemmer stemmer;
    private ConcurrentHashMap<String,ArrayList<Integer>> terms; // term, pos
    private List<String> stopWords;

    private boolean BremoveStopWords = true;
    private boolean Bstem = true;
    private String pattern = "[\\w]+";

    public PreProcessing(List<String> stopWords,boolean bremoveStopWords, boolean bstem,String pattern)
    {
        stemmer = new Stemmer();
        terms = new ConcurrentHashMap<>();
        BremoveStopWords = bremoveStopWords;
        Bstem = bstem;
        this.stopWords = stopWords;
        this.pattern = pattern;
    }

    public ConcurrentHashMap<String, ArrayList<Integer>> getTerms() {
        return terms;
    }

    public void doProcessing(String text)
    {
        terms.clear();

        // Tokenisation
        ArrayList<String> tokens = getSymbols(text, pattern);

        for(int i=0;i<tokens.size();i++)
        {
            // Case folding: make all text into lower case
            String t = tokens.get(i).trim().toLowerCase();
            if(t==null||t.isEmpty())
                continue;

            // Stopping: remove English stop words
            if(!checkStopWords(t))
            {
                // Normalisation: Porter stemmer at least.
                t = stem(t);
                if(terms.containsKey(t))
                    terms.get(t).add(i);
                else
                {
                    ArrayList<Integer> a = new ArrayList<>();
                    a.add(i);
                    terms.put(t,a);
                }
            }
        }
    }

    private ArrayList<String> getSymbols(String text, String pattern)
    {
        if(pattern.isEmpty())
            pattern = "[\\w]+";

        ArrayList<String> re = new ArrayList<>();

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        while (m.find())
        {
            re.add(m.group());
        }

        return re;
    }

    private boolean checkStopWords(String word)
    {
        if(!BremoveStopWords)
            return false;

        return stopWords.contains(word);
    }

    private String stem(String word)
    {
        if(!Bstem)
            return word;

        char[] chars = word.toCharArray();
        stemmer.add(chars,chars.length);
        stemmer.stem();
        return stemmer.toString();
    }


}
