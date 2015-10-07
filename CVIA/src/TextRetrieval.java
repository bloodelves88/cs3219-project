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
	//Assumption that all images have tags. Need to clarify

	private String imageAndtags="";
	private LinkedList<String> listOfTags= new LinkedList<String>();
	private LinkedList<String> listOfImages= new LinkedList<String>();
	private ST<String,Set<String>> directIndex = new ST<String,Set<String>>() ; 
	private ST<String,Set<String>> invertedIndex = new ST<String,Set<String>>();
	private ST<String,Set<String>> queryIndex = new ST<String,Set<String>>();
	private Set<String> queryTagSet,queryImageSet;
	private Map<String,Integer> occuranceMap=new HashMap<String,Integer>();
	private Map<String,Integer> resultMap=new HashMap<String,Integer>();
	private int count=0;
	public void LoadIndexes(String tagspath)
	{
		String imagesPath= tagspath + "train_tags.txt";
		String queryPath=tagspath.substring(0,tagspath.length()-6)+"test\\test_tags.txt";
		try{
			LoadImagesTags(imagesPath,directIndex);
			CreateInvertedIndex();
			LoadImagesTags(queryPath,queryIndex);
		}catch(IOException e1)
    	{
    		e1.printStackTrace();
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
	//Retrival without inverted index
	/* 	public BufferedImage[] matching(String imagename,int resultSize,String path)
	{
		double result=0;
		String[] tempResults=new String[directIndex.size()];
		System.out.println(tempResults.length);
		String[] results=new String[resultSize];
		int counter=0;
		if(queryIndex.contains(imagename))
		{
			queryTagSet=queryIndex.get(imagename);
			String[] queryTags=queryTagSet.toArray(new String[queryTagSet.size()]);
			double[] indexes=new double[directIndex.size()];
			for(String str:directIndex.keys())
			{
				result=CosineSimilarity(directIndex.get(str).toArray(new String[directIndex.get(str).size()]),queryTags);
				if (counter == 0){
					tempResults[counter] = str;
					indexes [counter] = result;
				}
				else {
					int index;
					for (index =0; index < counter; index ++){
						if (result > indexes[counter])
							break;
					}
					for (int j = counter - 1; j > index - 1; j--){
						tempResults [j+1] = tempResults [j];
						indexes [j+1] = indexes[j];
					}
					tempResults[index] = str;
					indexes[index] = result;
				}
				counter++;
			}
			for(int i=0;i<resultSize;i++)
			{
				results[i]=tempResults[i];
			}
			return findFiles(results,path);
		}
		else
		{
			return null;
		}
		
	} */
	
	public BufferedImage[] matching(String imagename,int resultSize,String path)
	{
		if(queryIndex.contains(imagename))
		{
			queryTagSet=queryIndex.get(imagename);
			for(String str:queryTagSet)
			{
				if(invertedIndex.contains(str))
				{
					queryImageSet=invertedIndex.get(str);
					for (String s:queryImageSet)
					{
						if(occuranceMap.containsKey(s))
						{
							occuranceMap.put(s, occuranceMap.get(s)+1);
						}
						else
						{
							occuranceMap.put(s,1);
						}
					}
				}
			}
			resultMap=sortByComparator(occuranceMap);
			count=0;
			String[] results=new String[resultSize];
			for(String str: resultMap.keySet())
			{
				if(count<resultSize)
				{
					results[count]=str;
					count++;
				}
				else
				{
					break;
				}
			}

			return findFiles(results,path);
		}
		else
		{
			return null;
		}
		
	}
	
	private BufferedImage[] findFiles(String[] results, String path)
	{
		BufferedImage[] imgs = new BufferedImage[results.length];
		String inputDatasetpath="",categoryNames = "";
		File category_names = new File(path.substring(0,path.lastIndexOf("train"))+ "category_names.txt");
		int counter=0;
		System.out.println(category_names);
    	try {
	        Scanner txtFile = new Scanner(category_names);

	        while (txtFile.hasNextLine() && counter<results.length) {
	        	categoryNames = txtFile.next();
	        	inputDatasetpath = path + categoryNames;
	        	
	        	File dir = new File(inputDatasetpath);  //path of the dataset
	    		File [] files = dir.listFiles();
				for(int i=0;i<files.length;i++)
				{
					String str=files[i].toString().substring(files[i].toString().lastIndexOf("\\")+1,files[i].toString().length());
					if(str.equals(results[counter]))
					{
						imgs[counter] = ImageIO.read(files[i]);
						counter++;
						txtFile = new Scanner(category_names);
						break;
					}
				}
	        }
	        txtFile.close();
    	}catch(IOException e)
	    {
    		System.out.println(e);
	    }
    	return imgs;
	}
	
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
	
	public void LoadImagesTags(String tagspath,ST<String,Set<String>> index) throws IOException
	{
		File fr = new File(tagspath);

		try{
			Scanner txtFile = new Scanner(fr);
		
			while(txtFile.hasNextLine())
			{
				imageAndtags=txtFile.nextLine();
				String[] result= imageAndtags.split("\\s+");
				for(int j=1;j<result.length;j++)
				{
					listOfTags.add(result[j]);
				}
				Set<String> mySet= new HashSet<String>(listOfTags);
				index.put(result[0], mySet);
				listOfTags.clear();
			}
			txtFile.close();
		}catch(FileNotFoundException e)
		{
			 e.printStackTrace();
		}
	}
	
	private void CreateInvertedIndex()
	{
		Iterable<String> keySet=directIndex.keys();
		Set<String> valueSet;
		for(String str: keySet)
		{
			valueSet=directIndex.get(str);	
			String[] tags=valueSet.toArray(new String [valueSet.size()]);
			for(int i=0;i<tags.length;i++)
			{
				if(!invertedIndex.contains(tags[i]))
				{
					listOfImages.add(str);
					Set<String> imageSet = new HashSet<String>(listOfImages);
					invertedIndex.put(tags[i], imageSet);
				}
				else
				{
					Set<String> retrievedImageSet=invertedIndex.get(tags[i]);
					retrievedImageSet.add(str);
					invertedIndex.delete(tags[i]);
					invertedIndex.put(tags[i], retrievedImageSet);
				}
				listOfImages.clear();
			}
		}
	}
}
