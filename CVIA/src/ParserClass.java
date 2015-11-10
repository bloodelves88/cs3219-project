import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import org.apache.tika.*;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import java.io.PrintWriter;
import java.nio.file.Files;

public class ParserClass {

	NonSequentialPDFParser parser;
	String parsedText;
	PDFTextStripper pdfStripper;
	PDDocument pdDoc;
	COSDocument cosDoc;
	PDDocumentInformation pdDocInfo;

	// PDFTextParser Constructor 
	public ParserClass() {
	}

	// Extract text from PDF Document
	private String pdftoText(String fileName) {

		//System.out.println("Parsing text from PDF file " + fileName + "....");
		File f = new File(fileName);

		try {
			String fileType=Files.probeContentType(f.toPath());

			PDDocument pdDocument= PDDocument.loadNonSeq(f,null);  
			if (!f.isFile()) {
				System.out.println("File " + fileName + " does not exist.");
				return null;
			}

			try {
				parser = new NonSequentialPDFParser(new FileInputStream(f));
			} catch (Exception e) {
				System.out.println("Unable to open PDF Parser.");
				return null;
			}

			try {
				//parser.parse();
				cosDoc = pdDocument.getDocument();
				pdfStripper = new PDFTextStripper();
				pdDoc = new PDDocument(cosDoc);
				parsedText = pdfStripper.getText(pdDoc);
				pdDocument.close();
			} catch (Exception e) {
				System.out.println("An exception occured in parsing the PDF Document.");
				e.printStackTrace();
				try {
					if (cosDoc != null) cosDoc.close();
					if (pdDoc != null) pdDoc.close();
				} catch (Exception e1) {
					e.printStackTrace();
				}
				return null;
			}
			pdDocument.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		//System.out.println("Done.");
		return parsedText;
	}

	private String wordtoText(File fileName)
	{
		Tika tika=new Tika();
		try {
			String extractedText=tika.parseToString(fileName);
			String lineSeparator=System.lineSeparator();
			extractedText=extractedText.replace("\n", lineSeparator);
			return extractedText;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String texttoText(File fileName)
	{
		String everything="";
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return everything;
	}

	public void parseFile(File fileName,int fileNumber)
	{
		String fileType=fileName.toString().substring(fileName.toString().lastIndexOf(".")+1,fileName.toString().length());
		if(fileType.equals("docx")||(fileType.equals("doc")))
		{
			writeTexttoFile(wordtoText(fileName),"pdfoutput" + fileNumber +".txt");
		}else if(fileType.equals("pdf"))
		{
			writeTexttoFile(pdftoText(fileName.toString()),"pdfoutput" + fileNumber +".txt");				
		}else if(fileType.equals("txt")){
			writeTexttoFile(texttoText(fileName),"pdfoutput" + fileNumber +".txt");
		}
	}
	// Write the parsed text from PDF to a file
	private void writeTexttoFile(String pdfText, String fileName) {

		//System.out.println("\nWriting PDF text to output text file " + fileName + "....");
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.print(pdfText);
			pw.close();  
		} catch (Exception e) {
			System.out.println("An exception occured in writing the pdf text to file.");
			e.printStackTrace();
		}
	}
}
