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
	private ST<String,Set<String>> fileIndex = new ST<String,Set<String>>();
	
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
	
	public void AddPortions(ArrayList<String> portions,String fileName)
	{
		ArrayList<String> parts=new ArrayList<String>();
		String categoryName="",categoryDescription="",tempDescription="";
		int i=0;
		while(i<portions.size())
		{
			categoryName=portions.get(i).trim();
			i++;
			if(i < portions.size())
			{
				categoryDescription=portions.get(i).trim();
			}
			else
			{
				categoryDescription="";
			}
			i++;
			StringTokenizer st=new StringTokenizer(categoryDescription);
			while(st.countTokens()<5&&(i+1)<portions.size())
			{
				if(i < portions.size())
				{
					tempDescription=portions.get(i).trim();
				}
				else
				{
					tempDescription="";
				}
				st=new StringTokenizer(tempDescription);
				categoryDescription+=tempDescription;
				i++;
			}
			parts.add(categoryName);
			parts.add(categoryDescription);
		}
		Set<String> set=new HashSet<String>(removeDuplicate(parts));
		fileIndex.put(fileName, set);
	}
	private ArrayList<String> removeDuplicate(ArrayList<String> parts)
	{
		ArrayList<String>results=new ArrayList<String>();
		int pointer=0;
		String currNumber,combined;
		while(pointer<parts.size())
		{   
		  currNumber=parts.get(pointer);
		  combined=parts.get(pointer+1);
		  for(int i=0;i<parts.size();i+=2){          
		    if(currNumber.equals(parts.get(i)) && i>pointer){
		    	combined+=" "+ parts.get(i)+ " " +parts.get(i+1);
		    	parts.remove(i);
		    	parts.remove(i+1);
		        break;
		    }
		  }   
		  pointer+=2;
		  results.add(currNumber);
		  results.add(combined);
		}
		return results;
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
		double commonTerms=0.0;
		for(int i=0;i<terms.length;i++)
		{
			if(Arrays.asList(fileTerms).contains(terms[i].toLowerCase()))
			{
				commonTerms++;
			}
		}
		return (commonTerms)/(Math.sqrt(Math.pow(fileTerms.length,2)+Math.pow(terms.length,2)));
	}
	
	public String[] getDocumentsByTerm(String term)
	{
		return invertedIndex.get(term).toArray(new String[invertedIndex.get(term).size()]);
	}
	
	public String[] getTermsByDocument(String term)
	{
		return directIndex.get(term).toArray(new String[directIndex.get(term).size()]);
	}
	public String[][] getWeightedResults(String[] terms)
	{
		String[] results=new String[directIndex.size()];
		double[] indexes=new double[directIndex.size()];
		String[][] combinedResult=new String[directIndex.size()][2];
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
		for(int i=0;i<combinedResult.length;i++)
		{
			String temp=results[i];
			temp=temp.substring(temp.lastIndexOf("\\")+1, temp.length());
			temp=temp.replaceAll("[^0-9]", "");
			combinedResult[i][0]=String.valueOf(temp);
			combinedResult[i][1]=String.valueOf(indexes[i]);
		}
		return combinedResult;
	}
	
	public String getCVDetails(String path)
	{
		Set<String> test=fileIndex.get(path);
		return null;
	}
}
