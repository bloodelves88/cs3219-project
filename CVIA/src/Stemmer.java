package snowballstemmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {
	
	public Stemmer() {
	}
    
    public String[] processFile(String fileName) {
        List<String> wordList;
        Set<String> stemmedSet;
        
        wordList = readFileToList(fileName);
        stemmedSet = stemWordList(wordList);
        
        return (String[]) stemmedSet.toArray();
    }
    
    public static List<String> readFileToList(String fileName) {
        List<String> wordList = new ArrayList<String>();
        List<String> stopWordList = new ArrayList<String> (Arrays.asList("a", "about", "above", 
        		"after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", 
        		"as", "at",	"be", "because", "been", "before", "being", "below", "between", 
        		"both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", 
        		"do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", 
        		"for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", 
        		"having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", 
        		"herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", 
        		"i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", 
        		"let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", 
        		"of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", 
        		"ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", 
        		"she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", 
        		"the", "their", "theirs", "them", "themselves", "then", "there", "there's", 
        		"these", "they", "they'd", "they'll", "they're", "they've", "this", "those", 
        		"through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", 
        		"we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", 
        		"when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", 
        		"why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", 
        		"you're", "you've", "your", "yours", "yourself", "yourselves"));
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String words = in.readLine();
            String[] arr = null;
            
            while (words != null) {
                if (!(words.trim().equals(""))) {
                    words = words.toLowerCase();
                    words = words.replaceAll("(\\w)\\.$", "$1");
                    arr = words.split("\\s");
                    for (String temp : arr) {
                    	if (!stopWordList.contains(temp)) {
                    		wordList.add(temp);
                    	}
                    }
//                    wordList.addAll(Arrays.asList(words.split("\\s")));
                } 
                words = in.readLine();
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return wordList;
    }
    
    public static Set<String> stemWordList(List<String> wordList) {
        List<String> stemmedLineList = new ArrayList<String>();
        englishStemmer stemmer = new englishStemmer();
        
        for (String word : wordList) {
                stemmedLineList.add(stem(stemmer, word));
        }
        
        Set<String> stemmedSet = new HashSet<String>(stemmedLineList);
        return stemmedSet;
    }
    
    public static String stem(englishStemmer stemmer, String word) {
        stemmer.setCurrent(word);
        if (stemmer.stem()) {
            return stemmer.getCurrent();
        } else {
            return null;
        }
    }
}
