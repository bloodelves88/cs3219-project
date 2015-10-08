1. Import the .jar file to the project
2. Insert import org.tartarus.snowball.ext.englishStemmer;
3. To use the stemmer, follow this example:

englishStemmer stemmer = new englishStemmer();      
//set word to be stemmed
stemmer.setCurrent("caress");
if (stemmer.stem()){
	//get stemming result
	System.out.println(stemmer.getCurrent());
}