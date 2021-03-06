import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tartarus.snowball.ext.englishStemmer;

public class WordGenerator {
	private static List<String> phraseList = new ArrayList<String>();

	public WordGenerator() {
	}

	public String[] processFile(String fileName) {
		List<String> wordList;
		Set<String> stemmedSet;


		//Initialize phraseList
		phraseList = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("phrases.txt"));
			String words = in.readLine();

			while (words != null) {
				phraseList.add(words.toLowerCase().trim());
				words = in.readLine();
			}
			in.close();
		} catch (Exception e) {

		}
		//System.out.println(phraseList);


		wordList = readFileToList(fileName);
		stemmedSet = stemWordList(wordList);

		return stemmedSet.toArray(new String[stemmedSet.size()]);
	}

	private static List<String> readFileToList(String fileName) {
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
			String firstLine = null, secondLine = "", jointLines = "";
			String[] arr = null;

			while (words != null) {
				words = words.trim();
				if (!(words.equals(""))) {
					words = words.toLowerCase();
					words = words.replaceAll(",", "");
					words = words.replaceAll("(\\w)\\.(\\W*)", "$1$2");
					secondLine = words;

					//Check whether phrase exists in 2 consecutive lines
					if (firstLine != null) {
						if (firstLine.equals("")) {

						} else {
							jointLines = firstLine + " " + secondLine;
						}

						for (String phrase : phraseList) {
							if (jointLines.contains(phrase)) {
								if (!wordList.contains(phrase)) {
									wordList.add(phrase);
								}
							}
						}
					}

					//Add words in the current line
					arr = words.split("\\s");
					for (String temp : arr) {
						if (!stopWordList.contains(temp)) {
							wordList.add(temp);
						}
					}
					//System.out.println(wordList);
					firstLine = secondLine;
				} else {
					firstLine = "";
				}

				words = in.readLine();
			}
			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return wordList;
	}

	private static Set<String> stemWordList(List<String> wordList) {
		List<String> stemmedLineList = new ArrayList<String>();

		for (String line : wordList) {
			if (line.contains(" ")) {
				String[] wordArr = line.split(" ");
				String stemResult = "";
				for (String word : wordArr) {
					stemResult += stem(word) + " ";
				}
				stemResult = stemResult.trim();

				stemmedLineList.add(stemResult);
			} else {
				stemmedLineList.add(stem(line));
			}
		}

		Set<String> stemmedSet = new HashSet<String>(stemmedLineList);

		return stemmedSet;
	}

	private static String stem(String word) {
		englishStemmer stemmer = new englishStemmer();
		stemmer.setCurrent(word);
		if (stemmer.stem()) {
			return stemmer.getCurrent();
		} else {
			return null;
		}
	}
}
