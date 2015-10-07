import java.util.List;


public class GUIModel {
	 /**
     * 
     * @param contents    ArrayList of strings containing the contents of all the open files.
     * 					  Each index contains the contents of 1 file.
     * 
     */
	public static void processOpenFiles(List<String> fileContentsList) {
		// Do stemming/lemmatization, weighting, etc.
		// E.g. 
		// for(int i = 0; i < fileContentsList.size(); i++) {
		// 	  result = doStemming(fileContentsList.get(i));
		// }
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
}
