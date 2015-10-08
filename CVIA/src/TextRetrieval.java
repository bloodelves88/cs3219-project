import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class TextRetrieval {

	private ST<String,Set<String>> directIndex = new ST<String,Set<String>>() ; 
	private ST<String,Set<String>> invertedIndex = new ST<String,Set<String>>();
	private Set<String> queryTagSet,queryImageSet;
	private Map<String,Integer> occuranceMap=new HashMap<String,Integer>();
	private Map<String,Integer> resultMap=new HashMap<String,Integer>();
	private int count=0;
	
	//Possible loading of stored terms
	public void LoadTerms()
	{
		String textPath= System.getProperty("user.dir") + "\\terms\\terms.txt";
		try{
			LoadTerms(textPath,invertedIndex);
		}catch(IOException e1)
    	{
    		e1.printStackTrace();
    	}
	}
	
	//Adds the file to the directIndex
	public void AddFile(String fileName, String[] fileTerms)
	{		
		if(!directIndex.contains(fileName))
		{
			Set<String> fileSet=new HashSet<String>(Arrays.asList(fileTerms));
			directIndex.put(fileName, fileSet);
		}else
		{
			Set<String> previousFileSet=directIndex.get(fileName);
			for(int i=0;i<fileTerms.length;i++)
			{
				if(!previousFileSet.contains(fileTerms[i]))
				{
					previousFileSet.add(fileTerms[i]);
				}
			}
			directIndex.delete(fileName);
			directIndex.put(fileName, previousFileSet);
			UpdateInvertedIndex(fileName,fileTerms);
		}
	}
	
	//Loads terms into given index
	//Precondition: Text file given has terms separated by newline
	//Postcondition: Index is loaded
	private void LoadTerms(String tagspath,ST<String,Set<String>> index) throws IOException
	{
		File fr = new File(tagspath);
		String tags="";
		try{
			Scanner txtFile = new Scanner(fr);
		
			while(txtFile.hasNextLine())
			{
				tags=txtFile.nextLine();
				Set<String> mySet= new HashSet<String>(null);
				index.put(tags, mySet);
			}
			txtFile.close();
		}catch(FileNotFoundException e)
		{
			 e.printStackTrace();
		}
	}
		
	private void UpdateInvertedIndex(String fileName,String[] fileSet)
	{
		LinkedList<String> listofTerms= new LinkedList<String>();
		for(int i=0;i<fileSet.length;i++)
			{
				if(!invertedIndex.contains(fileSet[i]))
				{
					listofTerms.add(fileName);
					Set<String> imageSet = new HashSet<String>(listofTerms);
					invertedIndex.put(fileSet[i], imageSet);
				}
				else
				{
					Set<String> retrievedImageSet=invertedIndex.get(fileSet[i]);
					if(!retrievedImageSet.contains(fileName))
					{
						retrievedImageSet.add(fileName);
						invertedIndex.delete(fileSet[i]);
						invertedIndex.put(fileSet[i], retrievedImageSet);
					}
				}
				listofTerms.clear();
			}
	}

	/* private double CosineSimilarity(String[] database,String[]query)
	{
		int commonTerms=0;
		for(int i=0;i<query.length;i++)
		{
			if(Arrays.asList(database).contains(query[i]))
			{
				commonTerms++;
			}
		}
		return (commonTerms)/(Math.sqrt(database.length^2+query.length^2));
	}
	 */
			
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public String[] getDocumentsByTerm(String term)
	{
		return invertedIndex.get(term).toArray(new String[invertedIndex.get(term).size()]);
	}
	
	public String[] getTermsByDocument(String term)
	{
		return directIndex.get(term).toArray(new String[directIndex.get(term).size()]);
	}
}
