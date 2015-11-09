import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Category {
	List<String> subheadings=new ArrayList<String>(Arrays.asList("summary","qualification","skill","expertise","technical","education"
			,"work experience","experience","education","extracurricular","curricular","interests","employment","reference","publication","work-related"));
	public ArrayList<String> SplitFile(String path)
	{
		ArrayList<String> portions=new ArrayList<String>();
		File fr=new File(path);
		String tags="",store="";
		int index=0;
		try {
			if(portions.isEmpty())
			{
				portions.add("Personal Details");
			}
			Scanner txtFile=new Scanner(fr);
			while(txtFile.hasNextLine())
			{
				tags=txtFile.nextLine().toLowerCase().trim();
				tags=tags.replaceAll("\t", "   ");
				StringTokenizer st=new StringTokenizer(tags);
				for(int i=0;i<subheadings.size();i++)
				{
					if(tags.contains(subheadings.get(i))&&(st.countTokens()<5))
					{
						portions.add(store);
						portions.add(tags);
						store="";
						tags="";
						break;
					}else if(tags.contains(subheadings.get(i)))
					{
						index=(tags.lastIndexOf(subheadings.get(i)))+subheadings.get(i).length();
						if(index+2<tags.length())
						{							
							if(Character.isWhitespace(tags.charAt(index+1))&&Character.isWhitespace(tags.charAt(index+2)))
							{
								portions.add(store);
								portions.add(tags.substring(0,index));
								tags=tags.substring(index,tags.length());
								store="";
							}
						}
					}
				}
				if(st.countTokens()<2)
				{
					store+=tags;
				}else{
					store+=";;" + tags;
				}
			}
			portions.add(store);
			txtFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portions;
	}
}
