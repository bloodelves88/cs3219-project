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
	public void LoadTerms(String path)
	{
		try{
			LoadTerms(path,invertedIndex);
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
				if(!index.contains(tags))
				{
					Set<String> mySet= new HashSet<String>(null);
					index.put(tags, mySet);
				}
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

	private double CosineSimilarity(String[] fileTerms,String[]terms)
	{
		int commonTerms=0;
		for(int i=0;i<terms.length;i++)
		{
			if(Arrays.asList(fileTerms).contains(terms[i]))
			{
				commonTerms++;
			}
		}
		return (commonTerms)/(Math.sqrt(fileTerms.length^2+terms.length^2));
	}
	
	public String[] getDocumentsByTerm(String term)
	{
		return invertedIndex.get(term).toArray(new String[invertedIndex.get(term).size()]);
	}
	
	public String[] getTermsByDocument(String term)
	{
		return directIndex.get(term).toArray(new String[directIndex.get(term).size()]);
	}
	public String[] getWeightedResults(String[] terms)
	{
		String[] results=new String[directIndex.size()];
		double[] indexes=new double[directIndex.size()];
		int counter=0;
		double similarity=0;
		for(String str:directIndex.keys())
		{
			similarity=CosineSimilarity(directIndex.get(str).toArray(new String[directIndex.get(str).size()]),terms);
			if (counter == 0){
				results[counter] = str;
				indexes [counter] = similarity;
			}
			else {
				int index;
				for (index =0; index < counter; index ++){
					if (similarity > indexes[counter])
						break;
				}
				for (int j = counter - 1; j > index - 1; j--){
					results [j+1] = results [j];
					indexes [j+1] = indexes[j];
				}
				results[index] = str;
				indexes[index] = similarity;
			}
			counter++;
		}
		return results;
	}
}
