import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;



public class TextRetrieval {

	private ST<String,Set<String>> directIndex = new ST<String,Set<String>>() ; 
	private ST<String,Set<String>> invertedIndex = new ST<String,Set<String>>();
	private ST<String,ArrayList<String>> fileIndex = new ST<String,ArrayList<String>>();
	private ST<String,ArrayList<String[]>> fileAndTermsIndex=new ST<String,ArrayList<String[]>>();
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
				categoryDescription+=" "+ tempDescription;
				i++;
			}
			parts.add(categoryName);
			parts.add(categoryDescription);
		}
		fileIndex.put(fileName, removeDuplicate(parts));
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
	
	
	public void DeleteData()
	{
		ArrayList<String> keys=new ArrayList<String>();
		if(!directIndex.isEmpty())
		{
			for(String str:directIndex.keys())
			{
				keys.add(str);
			}
			for(int i=0;i<keys.size();i++)
			{
				directIndex.delete(keys.get(i));
			}
		}
	}
	
	public void clearUnmatchedTerms()
	{
		ArrayList<String> keys=new ArrayList<String>();
		if(!fileAndTermsIndex.isEmpty())
		{
			for(String str:fileAndTermsIndex.keys())
			{
				keys.add(str);
			}
			for(int i=0;i<keys.size();i++)
			{
				fileAndTermsIndex.delete(keys.get(i));
			}
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

	private double JaccardCoefficient(String[] fileTerms,String[][]terms,String filename)
	{
		double commonTerms=0.0;
		ArrayList<String[]> arraylist=new ArrayList<String[]>();
		for(int i=0;i<terms.length;i++)
		{
			String[] termAndappearance=new String[2];
			termAndappearance[0]=terms[i][0];
			if(Arrays.asList(fileTerms).contains(terms[i][0].toLowerCase()))
			{
				commonTerms++;
				termAndappearance[1]="Yes";
			}else{
				termAndappearance[1]="No";
			}
			//System.out.println(termAndappearance[0]);
			//System.out.println(termAndappearance[1]);
			arraylist.add(termAndappearance);
		}
		fileAndTermsIndex.put(filename, arraylist);
		return (commonTerms)/(fileTerms.length+terms.length-commonTerms);
	}
	
	private double BasicCalculation(String[] fileTerms,String[][]terms,String filename)
	{
		double commonTerms=0.0;
		ArrayList<String[]> arraylist=new ArrayList<String[]>();
		String[] termAndappearance=new String[2];
		for(int i=0;i<terms.length;i++)
		{
			termAndappearance[0]=terms[i][0];
			if(Arrays.asList(fileTerms).contains(terms[i][0].toLowerCase()))
			{
				commonTerms++;
				termAndappearance[1]="Yes";
			}else{
				termAndappearance[1]="No";
			}
			arraylist.add(termAndappearance);
		}
		fileAndTermsIndex.put(filename, arraylist);
		return commonTerms/terms.length;
	}
	
	private double WeightedJaccardCoefficient(String[] fileTerms,String[][]terms,String filename)
	{
		double commonTerms=0.0,weightedSum=0.0;
		ArrayList<String[]> arraylist=new ArrayList<String[]>();
		String[] termAndappearance=new String[2];
		for(int i=0;i<terms.length;i++)
		{
			termAndappearance[0]=terms[i][0];
			if(Arrays.asList(fileTerms).contains(terms[i][0].toLowerCase()))
			{
				commonTerms+=Integer.parseInt(terms[i][1]);
				termAndappearance[1]="Yes";
			}else{
				termAndappearance[1]="No";
			}
			System.out.println(termAndappearance[0]);
			System.out.println(termAndappearance[1]);
			arraylist.add(termAndappearance);
		}
		for(int i=0;i<terms.length;i++)
		{
			weightedSum+=Integer.parseInt(terms[i][1]);
		}
		fileAndTermsIndex.put(filename, arraylist);
		return (commonTerms)/(weightedSum-commonTerms+fileTerms.length);
	}
		
	public String[][] getMatchedAndUnmatchedTerms(String filename)
	{
		ArrayList<String[]> termSet=fileAndTermsIndex.get(filename);
		if(termSet!=null){
		String[][] resultSet=new String[termSet.size()][2];
		for(int i=0;i<termSet.size();i++)
		{
			resultSet[i][0]=termSet.get(i)[0];
			resultSet[i][1]=termSet.get(i)[1];
		}
		return resultSet;
		}else{
			return null;
		}
		
	}
	
	public String[] getDocumentsByTerm(String term)
	{
		return invertedIndex.get(term).toArray(new String[invertedIndex.get(term).size()]);
	}
	
	public String[] getTermsByDocument(String term)
	{
		return directIndex.get(term).toArray(new String[directIndex.get(term).size()]);
	}
	public String[][] getWeightedResults(String[][] terms,int customWeights)
	{
		String[] results=new String[directIndex.size()];
		double[] indexes=new double[directIndex.size()];
		String[][] combinedResult=new String[directIndex.size()][2];
		int counter=0;
		double similarity=0;
		clearUnmatchedTerms();
		for(String str:directIndex.keys())
		{
			if(customWeights==2)
			{
				similarity=WeightedJaccardCoefficient(directIndex.get(str).toArray(new String[directIndex.get(str).size()]),terms,str);				
			}else if(customWeights==1)//basic
			{
				similarity=BasicCalculation(directIndex.get(str).toArray(new String[directIndex.get(str).size()]),terms,str);
			}
			else if(customWeights==0){
				similarity=JaccardCoefficient(directIndex.get(str).toArray(new String[directIndex.get(str).size()]),terms,str);
			}
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
	
	public ArrayList<String> getCVDetails(String path)
	{
		ArrayList<String> list=fileIndex.get(path);
		return list;
	}
}
