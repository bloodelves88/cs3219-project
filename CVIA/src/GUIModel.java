import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GUIModel {
	private static List<String> fileContentsList;
	private static Parser parser= new Parser();

	private static TextRetrieval textRetrieval=new TextRetrieval();
	private static Stemmer stemmer=new Stemmer();
	
	/**
     * 
     * @param file          PDF file to parse into a text file
     * @param fileNumber    Index number of file (depends on number of files opened)
     * 
     */
	public static File parsePDFFiles(File file, int fileNumber) {
		parser.writeTexttoFile(parser.pdftoText(file.toString()),"pdfoutput" + fileNumber +".txt");
		
		return new File(System.getProperty("user.dir")+"\\pdfoutput" + fileNumber + ".txt");
	}
	
	/**
     * 
     * This function returns a string that will be saved 
     * into a .txt file when the user clicks "save".
     * 
     */
	public static String saveDataToTextFile() {
		String dataToSave = "Null data. You didn't open a PDF file.";
		
		// Get your string.
		if (!fileContentsList.isEmpty()) {
			dataToSave = fileContentsList.get(0);
		}
		
		dataToSave = dataToSave.replaceAll("(?!\\r)\\n", "\r\n");
		return dataToSave;
	}
	
	/**
     * 
     * Function is called when the user presses "start processing"
	 * 
	 * @param keywords 	String of keywords (need to split them)
     * 
     */
	public static void startProcessing(String fileName) {
		String[] fileTerms=stemmer.processFile(fileName);
		textRetrieval.AddFile(fileName, fileTerms);
	}
	
	public static String[][] search(String keywords)
	{
		String[] lines=keywords.split("\\r?\\n");
		
		return textRetrieval.getWeightedResults(lines);
		
				
	}
}
