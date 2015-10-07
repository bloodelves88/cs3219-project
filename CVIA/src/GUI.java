import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;


public class GUI {

	private JFrame frmCvia;
	private Parser parser= new Parser();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmCvia.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCvia = new JFrame();
		frmCvia.setTitle("CViA");
		frmCvia.setBounds(100, 100, 650, 500);
		frmCvia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		frmCvia.getContentPane().setLayout(gridBagLayout);

		JLabel lblFilesOpened = new JLabel("Files Open:");
		GridBagConstraints gbc_lblFilesOpened = new GridBagConstraints();
		gbc_lblFilesOpened.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilesOpened.gridx = 0;
		gbc_lblFilesOpened.gridy = 0;
		frmCvia.getContentPane().add(lblFilesOpened, gbc_lblFilesOpened);

		final JTextArea textAreaFilesOpen = new JTextArea();
		textAreaFilesOpen.setLineWrap(true);
		textAreaFilesOpen.setRows(2);
		GridBagConstraints gbc_textAreaFilesOpen = new GridBagConstraints();
		gbc_textAreaFilesOpen.gridwidth = 3;
		gbc_textAreaFilesOpen.insets = new Insets(0, 0, 5, 0);
		gbc_textAreaFilesOpen.fill = GridBagConstraints.BOTH;
		gbc_textAreaFilesOpen.gridx = 1;
		gbc_textAreaFilesOpen.gridy = 0;
		frmCvia.getContentPane().add(textAreaFilesOpen, gbc_textAreaFilesOpen);

		JLabel lblContents = new JLabel("Contents:");
		GridBagConstraints gbc_lblContents = new GridBagConstraints();
		gbc_lblContents.insets = new Insets(0, 0, 5, 5);
		gbc_lblContents.gridx = 0;
		gbc_lblContents.gridy = 1;
		frmCvia.getContentPane().add(lblContents, gbc_lblContents);

		final JTextArea textAreaContents = new JTextArea();
		textAreaContents.setLineWrap(true);
		GridBagConstraints gbc_textAreaContents = new GridBagConstraints();
		gbc_textAreaContents.gridwidth = 3;
		gbc_textAreaContents.insets = new Insets(0, 0, 5, 0);
		gbc_textAreaContents.fill = GridBagConstraints.BOTH;
		gbc_textAreaContents.gridx = 1;
		gbc_textAreaContents.gridy = 1;
		frmCvia.getContentPane().add(textAreaContents, gbc_textAreaContents);

		JButton buttonBrowse = new JButton("Browse");
		GridBagConstraints gbc_buttonBrowse = new GridBagConstraints();
		gbc_buttonBrowse.insets = new Insets(0, 0, 0, 5);
		gbc_buttonBrowse.gridx = 0;
		gbc_buttonBrowse.gridy = 2;
		frmCvia.getContentPane().add(buttonBrowse, gbc_buttonBrowse);
		buttonBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser c = new JFileChooser();
				// Demonstrate "Open" dialog:
				c.setMultiSelectionEnabled(true); // returns a array of File objects
				int rVal = c.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					File[] files = c.getSelectedFiles();
					String openFileList= ""; 
					for (int i = 0; i < files.length; i++) {
						parser.ParseCV(files[i].toString());
						openFileList = openFileList.concat(files[i].toString() + "\n"); 
					}
					
					//textAreaFilesOpen.setText(c.getSelectedFile().getName());
					textAreaFilesOpen.setText(openFileList);
					textAreaContents.setText(c.getCurrentDirectory().toString());
					
					try {
						String[] contents = null;
						for (int i = 0; i < files.length; i++) {
							contents[i] = readFile(files[i].toString()); // contents of files here
						}
						//textAreaContents.setText(contents);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					textAreaFilesOpen.setText("You pressed cancel");
					textAreaContents.setText("");
				}
			}
		});

		JButton buttonAnalyze = new JButton("Start Analyzing");
		GridBagConstraints gbc_buttonAnalyze = new GridBagConstraints();
		gbc_buttonAnalyze.insets = new Insets(0, 0, 0, 5);
		gbc_buttonAnalyze.gridx = 2;
		gbc_buttonAnalyze.gridy = 2;
		frmCvia.getContentPane().add(buttonAnalyze, gbc_buttonAnalyze);

		JButton buttonSaveResults = new JButton("Save Results");
		GridBagConstraints gbc_buttonSaveResults = new GridBagConstraints();
		gbc_buttonSaveResults.gridx = 3;
		gbc_buttonSaveResults.gridy = 2;
		frmCvia.getContentPane().add(buttonSaveResults, gbc_buttonSaveResults);
		buttonSaveResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					//textAreaFileOpened.setText(c.getSelectedFile().getName());
					//textAreaContents.setText(c.getCurrentDirectory().toString());

					Writer writer = null;
					String filepath = c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName();

					try {
						writer = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(filepath), "utf-8"));
						String content = textAreaContents.getText().replaceAll("(?!\\r)\\n", "\r\n"); // saving contents here
						writer.write(content);
					} catch (IOException ex) {
						// report
					} finally {
						try {writer.close();} catch (Exception ex) {/*ignore*/}
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					textAreaFilesOpen.setText("You pressed cancel");
					textAreaContents.setText("");
				}
			}  
		});
	}

	private String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
}
