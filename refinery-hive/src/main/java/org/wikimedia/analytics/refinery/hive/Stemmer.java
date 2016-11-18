package org.wikimedia.analytics.refinery.hive;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.google.common.base.Joiner;

public class Stemmer extends UDF {
    public String evaluate(String text) {
        List<String> words = new ArrayList<>();
        try(    EnglishAnalyzer analyzer = new EnglishAnalyzer();
                TokenStream ts = analyzer.tokenStream("", new StringReader(text));
        ) {
            CharTermAttribute cattr = ts.getAttribute(CharTermAttribute.class);
            while(ts.incrementToken()) {
                words.add(cattr.toString());
            }
        } catch (IOException e) {
            return text;
        }
        return Joiner.on(" ").join(words);
    }
}
