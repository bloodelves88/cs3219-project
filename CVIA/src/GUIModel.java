import java.util.List;


public class GUIModel {
	private static List<String> fileContentsList;
	
	 /**
     * 
     * @param contents    ArrayList of strings containing the contents of all the open files.
     * 					  Each index contains the contents of 1 file.
     * 
     */
	public static void saveContentsOfOpenFiles(List<String> fileContents) {
		fileContentsList = fileContents;
	}
	
	/**
     * 
     * This function returns a string that will be saved 
     * into a .txt file when the user clicks "save".
     * 
     */
	public static String saveData() {
		String dataToSave = "Test data";
		
		// Get your string.
		
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
