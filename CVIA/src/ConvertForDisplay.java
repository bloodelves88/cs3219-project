import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;


public class ConvertForDisplay {
	public String getPersonalParticulars(File file,TextRetrieval textretrieval,String path)
	{
		String[] temp;
		String workexperience="",experiences="";
		String name=getName(file);
		int totalWorkingExperience=0;
		ArrayList<String> list=textretrieval.getCVDetails(path);
		String retrievedData="",results="";
		
		retrievedData=list.get(1);
		retrievedData=retrievedData.replaceAll(" +", " ");
		retrievedData=retrievedData.replaceAll(";;", System.lineSeparator());
		retrievedData=retrievedData.replaceAll("^\\s+", "");

		if(retrievedData.contains("page1"))
		{
			//results="LinkedIn Profile : " + results.substring(results.lastIndexOf("page1")+5, results.length())+ System.getProperty("line.separator") +"Please contact through LinkedIn";
			results="LinkedIn Profile : " + getName(file)+ System.getProperty("line.separator") +"Please contact through LinkedIn" + System.lineSeparator() + System.lineSeparator();
		}
		else
		{
			results=getName(file)+ System.getProperty("line.separator")+ getEmail(retrievedData) + System.getProperty("line.separator") + extractPhoneNumber(retrievedData) + System.lineSeparator() + System.lineSeparator();
		}
		for(int i=0;i<list.size();i+=2)
		{
			if(list.get(i).contains("experience")||list.get(i).contains("employment")||list.get(i).contains("work-related"))
			{
				temp=getWorkExperience(list.get(i+1));
				totalWorkingExperience+=Integer.parseInt(temp[0]);
				experiences+=temp[1];
			}
		}
		workexperience="Worked for a total number of "+ (totalWorkingExperience/12) +" years and " + (totalWorkingExperience%12) + " months" + System.lineSeparator() + experiences;
	return results+ workexperience;
	}
	
	private String getName(File file)
	{
		List<String> list;
		String parsed = "";
	   	String filename=file.toString().substring(file.toString().lastIndexOf("\\")+1, file.toString().lastIndexOf("."));
	   	if(filename.contains("_"))
	   	{
	   		list=Arrays.asList(filename.split("_"));
	   	}else{
	   		list=Arrays.asList(filename.split(" "));	   		
	   	}
	   	for(int i=0;i<list.size();i++)
	   	{
	   		if(!(list.get(i).toLowerCase().contains("cv")||list.get(i).toLowerCase().contains("resume")))
	   		{
	   			parsed+=list.get(i).replaceAll(" ", "");
	   		}
	   	}
	   	parsed=parsed.replaceAll("\\d", "");
	   	parsed=parsed.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		return parsed;
	}
	
	private String getEmail(String str)
	{
		String email="";
	    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(str);
	    while (m.find()) {
	        email=m.group();
	    }
	    if(email.contains(".com"))
	    {
	    	email=email.substring(0, email.indexOf(".com")+4);
	    }
	    return email;
	}
	
	public String extractPhoneNumber(String input){
		String temp="",numbers="",result="";
		List<String> list;
		list=Arrays.asList(input.replaceAll("[^-+()?0-9]+", " ").split(" ")); 
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).contains("+"))
			{
				numbers+=list.get(i)+" ";
			}
			else if(list.get(i).contains("(")&&list.get(i).contains(")"))
			{
				numbers+="+" + list.get(i).substring(list.get(i).indexOf("(")+1, list.get(i).indexOf(")"))+" ";
			}
			else if(list.get(i).length()%2==0 &&(list.get(i).length()==4 || list.get(i).length()>6))
			{
				numbers+=list.get(i)+" ";
			}
		}
		if (!numbers.contains("+"))
		{
			temp=numbers;
			numbers="+65 " + temp;
		}
		if(numbers.contains("+65")&&numbers.length()>13)
		{
			numbers=numbers.substring(0, 13);
		}
	    Iterator<PhoneNumberMatch> existsPhone=PhoneNumberUtil.getInstance().findNumbers(numbers, "IN").iterator();
	    while (existsPhone.hasNext()){
	        //System.out.println("Phone == " + existsPhone.next().number());
	        result="Phone == " + existsPhone.next().number();
	    }
	    return result;
	}
	
	private String[] getWorkExperience(String actual )
	{
		String[] checker;
		//String workexperience="";
		//String text=checker("   senior software engineer (2006 - 2010), brewed concepts inc. and".split(";;"));
		//checker1=checker(before.split(";;"));
		checker=checker(actual.split(";;"));
		//int totalWorkingExperience=Integer.parseInt(checker1[0])+Integer.parseInt(checker2[0]);
		//System.out.println(checker1);
		//System.out.println(checker2);
		//workexperience="Worked for a total number of "+ (totalWorkingExperience/12) +" years and " + (totalWorkingExperience%12) + " months" + System.lineSeparator() + checker1[1] + checker2[1];
		return checker;
	}
	private String[] checker(String[] str)
	{
		//Pattern pattern1 = Pattern.compile("\\d{4}");
		//Pattern pattern2 = Pattern.compile("(.)*(?:jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|(nov|dec)(?:ember)?) (?:19[7-9]\\d|2\\d{3})(?=\\D|$)(.)*");
		String[] parsed=new String[]{"",""};
		String copied="";
		int difference=0,totalWorkingExperience=0;
		ArrayList<Date> datesretrieved=new ArrayList<Date>();
		String[] yearsretrieved;
		Parser parser = new Parser();
		for (int i=0;i<str.length;i++)
		{
			difference=0;
			datesretrieved.clear();
			//Matcher matcher1 = pattern1.matcher(str[i]);
			//Matcher matcher2 = pattern2.matcher(str[i]);
			//boolean isMatched1 = matcher1.matches();
			//boolean isMatched2 = matcher2.matches();
			System.out.println(str[i]);
			//Somehow the word "summer" causes the parser used by the library in checker function to fail. Hence need to remove
			copied=str[i].replace("summer", "");
			//if(/*isMatched1&&*/isMatched2)
			//{
				List<DateGroup> groups = parser.parse(copied);
				for(DateGroup group:groups) {
					List<Date> dates = group.getDates();
					//System.out.println(dates.getClass().getName());
					for(int j=0;j<dates.size();j++)
					{
						if(SingleDateDifference(dates.get(j))!=0)
						{	
							//System.out.println("Storing");							
							//System.out.println(dates.get(j));
							datesretrieved.add(dates.get(j));
						}
					}
				}
				if(datesretrieved.size()==1&&(str[i].contains("present")||str[i].contains("current")))
				{
					//System.out.println("Inside case 1");
					difference=SingleDateDifference(datesretrieved.get(0));					
				}else if(datesretrieved.size()>1)
				{
					//System.out.println("Inside case 2");
					//System.out.println(datesretrieved.get(1));
					//System.out.println(datesretrieved.get(0));	
					if(str[i].contains("present")||str[i].contains("current"))
					{
						difference=SingleDateDifference(datesretrieved.get(datesretrieved.size()-1));							
					}else{
						difference=DoubleDateDifference(datesretrieved.get(0),datesretrieved.get(1));
					}
				}else{
					String number=str[i].replaceAll("[\\D]", " ");
					number=number.trim();
					number=number.replaceAll("\\s+", " ");
					yearsretrieved=yearChecker(number.split(" "));
					if((yearsretrieved.length==2)&&(Integer.parseInt(yearsretrieved[1])>Integer.parseInt(yearsretrieved[0])))
					{
						difference=(Integer.parseInt(yearsretrieved[1])-Integer.parseInt(yearsretrieved[0]))*12;
					}else if((yearsretrieved.length==1)&&(str[i].contains("present")||str[i].contains("current"))){
						difference=(2015-Integer.parseInt(yearsretrieved[0]))*12;						
					}
				}
				System.out.print("Difference is ");
				System.out.println(difference);
				if(difference>0)
				{
					parsed[1]+="Worked for "+ (difference/12) +" years and " + (difference%12) + " months : " + str[i].trim() + System.lineSeparator();
					totalWorkingExperience+=difference;
				}
			}
		//}
		parsed[0]=Integer.toString(totalWorkingExperience);
		return parsed;
	}
	
	private int SingleDateDifference(Date date)
	{
	    Calendar current = Calendar.getInstance();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
		return ((current.get(current.YEAR)*12+current.get(current.MONTH)+1)-(cal.get(Calendar.YEAR)*12+cal.get(Calendar.MONTH)+1));
	}
	
	private int DoubleDateDifference(Date date1,Date date2)
	{
	    Calendar older = Calendar.getInstance();
	    older.setTime(date1);
	    Calendar newer = Calendar.getInstance();
	    newer.setTime(date2);
	    if(date2.compareTo(date1)>0)
	    {
	    	return ((newer.get(newer.YEAR)*12+newer.get(newer.MONTH)+1)-(older.get(Calendar.YEAR)*12+older.get(Calendar.MONTH)+1));
	    }else if(date1.compareTo(date2)>0){
	    	return ((newer.get(newer.YEAR)*12+newer.get(newer.MONTH)+1)-(newer.get(Calendar.YEAR)*12+older.get(Calendar.MONTH)+1));	    
	    }else{
	    	return 0;
	    }
	    	
	}
	private String[] yearChecker(String[] data)
	{
		ArrayList<String> result=new ArrayList<String>();
		for(int i=0;i<data.length;i++)
		{
			if(data[i].trim().length()==4)
			{
				result.add(data[i].trim());
			}
		}
		return result.toArray(new String[result.size()]);
	}
}
