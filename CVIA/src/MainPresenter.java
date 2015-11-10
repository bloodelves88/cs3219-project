import java.io.File;
import java.util.ArrayList;

public class MainPresenter {
	private static ParserClass parser= new ParserClass();
	private static TextRetrieval textRetrieval=new TextRetrieval();
	private static WordGenerator stemmer=new WordGenerator();
	private static ConvertForDisplay convertForDisplay = new ConvertForDisplay();

	/**
	 * 
	 * @param file          PDF file to parse into a text file
	 * @param fileNumber    Index number of file (depends on number of files opened)
	 * 
	 */
	public static File parsePDFFiles(File file, int fileNumber) {

		parser.parseFile(file, fileNumber);

		return new File(System.getProperty("user.dir")+"\\pdfoutput" + fileNumber + ".txt");
	}

	/**
	 * 
	 * Function is called when the user presses "start processing"
	 * 
	 * @param keywords 	String of keywords (need to split them)
	 * 
	 */
	public static void startProcessing(String fileName) {
		Category category=new Category();
		ArrayList<String> fileportions=category.SplitFile(fileName);
		String[] fileTerms=stemmer.processFile(fileName);
		textRetrieval.AddFile(fileName, fileTerms);
		textRetrieval.AddPortions(fileportions, fileName);
	}

	public static String[][] search(String[][] keywords, int customWeights)
	{
		return textRetrieval.getWeightedResults(keywords,customWeights);

	}

	public static String getCVDetails(File file,int index)
	{
		return convertForDisplay.getPersonalParticulars(file,textRetrieval,System.getProperty("user.dir")+"\\pdfoutput" + index + ".txt");
	}

	public static void ClearData()
	{
		textRetrieval.DeleteData();
	}

	public static String[][] getMatchedAndUnmatchedTerms(int fileIndex)
	{
		return textRetrieval.getMatchedAndUnmatchedTerms(System.getProperty("user.dir")+"\\pdfoutput" + fileIndex + ".txt");
	}
}
