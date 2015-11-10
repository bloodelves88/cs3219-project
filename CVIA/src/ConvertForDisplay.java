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

import org.apache.commons.lang.WordUtils;

public class ConvertForDisplay {
	public String getPersonalParticulars(File file,TextRetrieval textretrieval,String path)
	{
		String[] temp;
		String workexperience="",experiences="";
		int totalWorkingExperience=0;
		ArrayList<String> list=textretrieval.getCVDetails(path);
		String retrievedData="",results="";
		boolean isLinkedIn =false;
		
		retrievedData=list.get(1);
		retrievedData=retrievedData.replaceAll(" +", " ");
		retrievedData=retrievedData.replaceAll(";;", System.lineSeparator());
		retrievedData=retrievedData.replaceAll("^\\s+", "");

		if(retrievedData.contains("page1"))
		{
			isLinkedIn=true;
			results="LinkedIn Profile : " + getName(file)+ System.getProperty("line.separator") +"Please contact through LinkedIn" + System.lineSeparator() + System.lineSeparator();
		}
		else
		{
			results=getName(file)+ System.getProperty("line.separator")+ getEmail(retrievedData) + System.getProperty("line.separator") + extractPhoneNumber(retrievedData) + System.lineSeparator() + System.lineSeparator();
		}
		for(int i=0;i<list.size();i+=2)
		{
			if((list.get(i).contains("experience")&&!(list.get(i).contains("volunteer experience")))||list.get(i).contains("employment")||list.get(i).contains("work-related"))
			{
				temp=getWorkExperience(list.get(i+1),isLinkedIn);
				totalWorkingExperience+=Integer.parseInt(temp[0])-1;
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
				numbers+="+" + list.get(i).substring(list.get(i).indexOf("(")+1, list.get(i).indexOf(")"))+" "+list.get(i).substring(list.get(i).indexOf(")")+1, list.get(i).length())+ " ";
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
	        result="Phone == " + existsPhone.next().number();
	    }
	    return result;
	}
	
	private String[] getWorkExperience(String actual,boolean isLinkedIn )
	{
		String[] checker;
		checker=checker(actual.split(";;"),isLinkedIn);
		return checker;
	}
	private String[] checker(String[] str,boolean isLinkedIn)
	{
		Pattern monthPattern = Pattern.compile("(.)*(?:jan(?:uary)? |feb(?:ruary)? |mar(?:ch)? |apr(?:il)? |may |jun(?:e)? |jul(?:y)? |aug(?:ust)? |sep(?:tember)? |oct(?:ober)? |(nov|dec)(?:ember)? )(.)*");
		String[] parsed=new String[]{"",""};
		String copied="";
		int difference=0,totalWorkingExperience=0;
		ArrayList<Date> datesretrieved=new ArrayList<Date>();
		String[] yearsretrieved;
		Parser parser = new Parser();
		boolean latestDate=true;
		Date date=new Date(),firstDate=new Date(),lastDate=new Date();
		for (int i=0;i<str.length;i++)
		{
			difference=0;
			datesretrieved.clear();
			//Somehow the word "summer" causes the parser used by the library in checker function to fail. Hence need to remove
			copied=str[i].replace("summer", "");
			List<DateGroup> groups = parser.parse(copied);
			for(DateGroup group:groups) {
				List<Date> dates = group.getDates();
				for(int j=0;j<dates.size();j++)
				{
					if(SingleDateDifference(dates.get(j))!=0)
					{	
						if(dates.get(j).compareTo(date)<0)
						{
							datesretrieved.add(dates.get(j));
						}
					}
				}
			}
			Matcher matcher = monthPattern.matcher(str[i]);
			boolean isMatched = matcher.matches();
			if(datesretrieved.size()==1&&(str[i].contains("present")||str[i].contains("current"))&&isMatched)
			{
				difference=SingleDateDifference(datesretrieved.get(0))+1;					
			}else if(datesretrieved.size()>1)
			{
				if(str[i].contains("present")||str[i].contains("current"))
				{
					difference=SingleDateDifference(datesretrieved.get(datesretrieved.size()-1))+1;							
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
					datesretrieved=monthAndYearCheck(str[i],yearsretrieved);
					//difference=(Integer.parseInt(yearsretrieved[1])-Integer.parseInt(yearsretrieved[0]))*12;
					difference=DoubleDateDifference(datesretrieved.get(0),datesretrieved.get(1));
				}else if((yearsretrieved.length==1)&&(str[i].contains("present")||str[i].contains("current"))){
					datesretrieved=monthAndYearCheck(str[i],yearsretrieved);
					Date date1=new Date();
				    datesretrieved.add(0, date1);
					//difference=(2015-Integer.parseInt(yearsretrieved[0]))*12;	
					difference=DoubleDateDifference(datesretrieved.get(0),datesretrieved.get(1));
				}
			}
			if(difference>0)
			{
				if(isLinkedIn)
				{
					parsed[1]+="Worked for "+ (difference/12) +" years and " + (difference%12) + " months : " + WordUtils.capitalize(str[i-1].trim()) +" " + WordUtils.capitalize(str[i].trim()) + System.lineSeparator();
				}
				else{
					parsed[1]+="Worked for "+ (difference/12) +" years and " + (difference%12) + " months : " + WordUtils.capitalize(str[i].trim()) + System.lineSeparator();						
				}
				if(latestDate&&(str[i].contains("present")||str[i].contains("current"))){
					firstDate=new Date();
				}else if(latestDate&&datesretrieved.size()!=0){
					firstDate=datesretrieved.get(1);				
				}
				latestDate=false;
				if(datesretrieved.size()==1)
				{
					lastDate=datesretrieved.get(0);
				}
				else if(datesretrieved.size()>1)
				{
					lastDate=datesretrieved.get(0);
				    Calendar earliestMonth = Calendar.getInstance();
				    earliestMonth.setTime(datesretrieved.get(0));
				    Calendar secondEarliestMonth = Calendar.getInstance();
				    secondEarliestMonth.setTime(datesretrieved.get(1));
				    if(earliestMonth.get(earliestMonth.YEAR)==2015)
				    {
				    	earliestMonth.set(secondEarliestMonth.get(secondEarliestMonth.YEAR), earliestMonth.get(earliestMonth.MONTH), earliestMonth.get(earliestMonth.DAY_OF_MONTH));
				    	lastDate=earliestMonth.getTime();
				    }
				}
				totalWorkingExperience=DoubleDateDifference(lastDate,firstDate);
			}
		}
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
	    	return ((newer.get(newer.YEAR)*12+newer.get(newer.MONTH)+1)-(older.get(Calendar.YEAR)*12+older.get(Calendar.MONTH)+1))+1;
	    }else if(date1.compareTo(date2)>0){
	    	return ((newer.get(newer.YEAR)*12+newer.get(newer.MONTH)+1)-(newer.get(Calendar.YEAR)*12+older.get(Calendar.MONTH)+1))+1;	    
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
	
	private ArrayList<Date> monthAndYearCheck(String str,String[] years)
	{
		ArrayList<Date> checker=new ArrayList<Date>();
		String temp="";
		int monthchecker=0;
		Calendar monthyear = Calendar.getInstance();
		for(int i=0;i<years.length;i++)
		{
			temp=str.substring(0, str.indexOf(years[i]));
			monthchecker=monthChecker(temp);
			if(monthchecker>0)
			{
				monthyear.set(Integer.parseInt(years[i]),monthchecker-1 , 1);
			}else{
				monthyear.set(Integer.parseInt(years[i]),0 , 1);
			}
			Date date=monthyear.getTime();
			checker.add(date);
		}
		return checker;
	}

	private int monthChecker(String str)
	{
		int index=0,month=0;
		if(str.contains("january")||str.contains("jan ")){
			if(str.contains("january")){
				if(indexChecker(index,str.lastIndexOf("january"))){
					index=str.lastIndexOf("january")+6;
					month=1;
				};
			}else{
				if(indexChecker(index,str.lastIndexOf("jan "))){
					index=str.lastIndexOf("jan ")+2;
					month=1;
				};			
			}
		}else if(str.contains("february")||str.contains("feb ")){
			if(str.contains("february")){
				if(indexChecker(index,str.lastIndexOf("february"))){
					index=str.lastIndexOf("february")+7;
					month=2;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("feb "))){
					index=str.lastIndexOf("feb ")+2;
					month=2;
				};						
			}
		}else if(str.contains("march")||str.contains("mar ")){
			if(str.contains("march")){
				if(indexChecker(index,str.lastIndexOf("march"))){
					index=str.lastIndexOf("march")+4;
					month=3;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("mar "))){
					index=str.lastIndexOf("mar ")+2;
					month=3;
				};						
			}
		}else if(str.contains("april")||str.contains("apr ")){
			if(str.contains("april")){
				if(indexChecker(index,str.lastIndexOf("april"))){
					index=str.lastIndexOf("april")+4;
					month=4;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("apr "))){
					index=str.lastIndexOf("apr ")+2;
					month=4;
				};						
			}
		}else if(str.contains("may ")){
			if(indexChecker(index,str.lastIndexOf("may "))){
				index=str.lastIndexOf("may ")+2;
				month=5;
			};						
		}else if(str.contains("june")||str.contains("jun ")){
			if(str.contains("june")){
				if(indexChecker(index,str.lastIndexOf("june"))){
					index=str.lastIndexOf("june")+3;
					month=6;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("jun "))){
					index=str.lastIndexOf("jun ")+2;
					month=6;
				};						
			}
		}else if(str.contains("july")||str.contains("jul ")){
			if(str.contains("july")){
				if(indexChecker(index,str.lastIndexOf("july"))){
					index=str.lastIndexOf("july")+3;
					month=7;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("jul "))){
					index=str.lastIndexOf("jul ")+2;
					month=7;
				};						
			}
		}else if(str.contains("august")||str.contains("aug ")){
			if(str.contains("august")){
				if(indexChecker(index,str.lastIndexOf("august"))){
					index=str.lastIndexOf("august")+5;
					month=8;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("aug "))){
					index=str.lastIndexOf("aug ")+2;
					month=8;
				};						
			}
		}else if(str.contains("september")||str.contains("sep ")){
			if(str.contains("september")){
				if(indexChecker(index,str.lastIndexOf("september"))){
					index=str.lastIndexOf("september")+8;
					month=9;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("sep "))){
					index=str.lastIndexOf("sep ")+2;
					month=9;
				};						
			}
		}else if(str.contains("october")||str.contains("oct ")){
			if(str.contains("october")){
				if(indexChecker(index,str.lastIndexOf("october"))){
					index=str.lastIndexOf("october")+6;
					month=10;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("oct "))){
					index=str.lastIndexOf("oct ")+2;
					month=10;
				};						
			}
		}else if(str.contains("november")||str.contains("nov ")){
			if(str.contains("november")){
				if(indexChecker(index,str.lastIndexOf("november"))){
					index=str.lastIndexOf("november")+7;
					month=11;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("nov "))){
					index=str.lastIndexOf("nov ")+2;
					month=11;
				};						
			}
		}else if(str.contains("december")||str.contains("dec ")){
			if(str.contains("december")){
				if(indexChecker(index,str.lastIndexOf("december"))){
					index=str.lastIndexOf("december")+7;
					month=12;
				};						
			}else{
				if(indexChecker(index,str.lastIndexOf("dec "))){
					index=str.lastIndexOf("dec ")+2;
					month=12;
				};						
			}
		}
		return month;
	}
	
	private boolean indexChecker(int index,int month){
		return month>index;
	}
}
