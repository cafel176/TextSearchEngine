package com.ttds.cw3.Transaction;

import com.ttds.cw3.Factory.AnalysisFactory;
import com.ttds.cw3.Factory.ReaderFactory;
import com.ttds.cw3.Interface.PreProcessingInterface;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import com.ttds.cw3.Tools.Stemmer;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

public final class PreProcessing implements PreProcessingInterface
{
    private List<String> stopWords;
    private Stemmer stemmer;

    private DocReader reader;
    private DocAnalysis converter;
    private HashMap<String,ArrayList<Integer>> terms; // term, pos

    private boolean BremoveStopWords = true;
    private boolean Bstem = true;
    private String pattern = "[\\w]+";

    public PreProcessing(String filePath)
    {
        stemmer = new Stemmer();
        reader = new DocReader(filePath);
        converter = new DocAnalysis();
        terms = new HashMap<>();
    }

    public void setFilePath(String file)
    {
        reader.setFilePath(file);
    }

    public void setStopWordFile(String fileName, StrategyType type)
    {
        stopWords = converter.txtsfrom(AnalysisFactory.get(type),reader.get(fileName, ReaderFactory.get(type)));
    }

    public void setBremoveStopWords(boolean bremoveStopWords) {
        BremoveStopWords = bremoveStopWords;
    }

    public void setBstem(boolean bstem) {
        Bstem = bstem;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public HashMap<String, ArrayList<Integer>> getTerms() {
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
