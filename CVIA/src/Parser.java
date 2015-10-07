import java.io.IOException;

public class Parser {
	public void ParseCV(String path)
	{
		try {
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			String directory=System.getProperty("user.dir");
			System.out.println("python "+directory+"\\pdfminer\\tools\\pdf2txt.py "+ path + ">pdfoutput.txt");
			Process p=Runtime.getRuntime().exec("python "+ directory +"\\pdfminer\\tools\\pdf2txt.py "+ path + ">pdfoutput.txt");
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
