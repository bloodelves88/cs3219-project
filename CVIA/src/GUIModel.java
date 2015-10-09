import java.io.File;
import java.util.List;


public class GUIModel {
	private static List<String> fileContentsList;
	private static Parser parser= new Parser();
	
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
     * @param contents    ArrayList of strings containing the contents of all the open files.
     * 					  Each index contains the contents of 1 file.
     * 
     */
	public static void storeContentsOfOpenFiles(List<String> fileContents) {
		fileContentsList = fileContents;
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
	public static void startProcessing(String keywords) {
		// Do stemming/lemmatization, weighting, etc.
		// E.g. 
		// for(int i = 0; i < fileContentsList.size(); i++) {
		// 	  result = doStemming(fileContentsList.get(i));
		// }
	}
}
