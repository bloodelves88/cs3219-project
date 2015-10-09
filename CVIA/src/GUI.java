import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.GridBagConstraints;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;


public class GUI {

	private JFrame frmCvia;
	private String[] jobs = { "Java Developer", "Android Developer", "iOS Developer", "Web Developer" };
	private String keywords = "";
	private String selectedJob = "";
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
		gridBagLayout.columnWidths = new int[] {0, 0, 10, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, Double.MIN_VALUE};
		frmCvia.getContentPane().setLayout(gridBagLayout);
		
		Component verticalStrut = Box.createVerticalStrut(1);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 4;
		gbc_verticalStrut.gridy = 0;
		frmCvia.getContentPane().add(verticalStrut, gbc_verticalStrut);

		JLabel lblFilesOpened = new JLabel("Files Open:");
		GridBagConstraints gbc_lblFilesOpened = new GridBagConstraints();
		gbc_lblFilesOpened.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilesOpened.gridx = 1;
		gbc_lblFilesOpened.gridy = 1;
		frmCvia.getContentPane().add(lblFilesOpened, gbc_lblFilesOpened);
		
		final JTextArea textAreaFilesOpen = new JTextArea();
		textAreaFilesOpen.setEditable(false);
		textAreaFilesOpen.setLineWrap(true);
		textAreaFilesOpen.setRows(2);
		textAreaFilesOpen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaFilesOpenScrollPane = new JScrollPane(textAreaFilesOpen);
		GridBagConstraints gbc_textAreaFilesOpen = new GridBagConstraints();
		gbc_textAreaFilesOpen.gridwidth = 5;
		gbc_textAreaFilesOpen.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaFilesOpen.fill = GridBagConstraints.BOTH;
		gbc_textAreaFilesOpen.gridx = 1;
		gbc_textAreaFilesOpen.gridy = 2;
		frmCvia.getContentPane().add(textAreaFilesOpenScrollPane, gbc_textAreaFilesOpen);

		JLabel lblJob = new JLabel("Job:");
		lblJob.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 1;
		gbc_lblJob.gridy = 3;
		frmCvia.getContentPane().add(lblJob, gbc_lblJob);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 0;
		gbc_horizontalStrut_1.gridy = 6;
		frmCvia.getContentPane().add(horizontalStrut_1, gbc_horizontalStrut_1);

		final JTextArea textAreaKeyWords = new JTextArea();
		GridBagConstraints gbc_textAreaKeyWords = new GridBagConstraints();
		gbc_textAreaKeyWords.gridwidth = 5;
		gbc_textAreaKeyWords.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaKeyWords.fill = GridBagConstraints.BOTH;
		gbc_textAreaKeyWords.gridx = 1;
		gbc_textAreaKeyWords.gridy = 6;
		textAreaKeyWords.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaKeyWordsScrollPane = new JScrollPane(textAreaKeyWords);
		frmCvia.getContentPane().add(textAreaKeyWordsScrollPane, gbc_textAreaKeyWords);
		

		JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 5;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		frmCvia.getContentPane().add(comboBox, gbc_comboBox);
		for (int i = 0; i < jobs.length; i++) {
			comboBox.addItem(jobs[i]);
		}

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//int index = comboBox.getSelectedIndex();
				selectedJob = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
				
				openJobKeywords(textAreaKeyWords, selectedJob);
			}
		});

		JLabel lblKeyWords = new JLabel("Key words:");
		GridBagConstraints gbc_lblKeyWords = new GridBagConstraints();
		gbc_lblKeyWords.insets = new Insets(0, 0, 5, 5);
		gbc_lblKeyWords.gridx = 1;
		gbc_lblKeyWords.gridy = 5;
		frmCvia.getContentPane().add(lblKeyWords, gbc_lblKeyWords);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
		gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_2.gridx = 6;
		gbc_horizontalStrut_2.gridy = 6;
		frmCvia.getContentPane().add(horizontalStrut_2, gbc_horizontalStrut_2);

		JButton buttonBrowse = new JButton("Open PDF Files");
		GridBagConstraints gbc_buttonBrowse = new GridBagConstraints();
		gbc_buttonBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_buttonBrowse.gridx = 1;
		gbc_buttonBrowse.gridy = 7;
		frmCvia.getContentPane().add(buttonBrowse, gbc_buttonBrowse);
		buttonBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setMultiSelectionEnabled(true); // returns a array of File objects
				int rVal = c.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					File[] files = c.getSelectedFiles();
					String openFileList= ""; // list of open files (shown in the GUI)
					for (int i = 0; i < files.length; i++) {
						System.out.println(files[i].toString());
						files[i] = GUIModel.parsePDFFiles(files[i], i);
						openFileList = openFileList.concat(System.getProperty("user.dir")+"\\pdfoutput" + i + ".txt" + "\n");
					}
					textAreaFilesOpen.setText(openFileList);

					try {
						List<String> contents = new ArrayList<String>();
						for (int i = 0; i < files.length; i++) {
							contents.add(readFile(files[i].toString())); // contents of files here
						}
						GUIModel.storeContentsOfOpenFiles(contents);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 2;
		gbc_horizontalStrut.gridy = 7;
		frmCvia.getContentPane().add(horizontalStrut, gbc_horizontalStrut);


		JButton buttonSaveResults = new JButton("Save Keywords");
		GridBagConstraints gbc_buttonSaveResults = new GridBagConstraints();
		gbc_buttonSaveResults.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSaveResults.gridx = 3;
		gbc_buttonSaveResults.gridy = 7;
		frmCvia.getContentPane().add(buttonSaveResults, gbc_buttonSaveResults);
		buttonSaveResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int index = comboBox.getSelectedIndex();
				String keywords = textAreaKeyWords.getText();
				saveJobKeywords(selectedJob, keywords);
			}
		});

		JButton buttonAnalyze = new JButton("Start Analyzing");
		GridBagConstraints gbc_buttonAnalyze = new GridBagConstraints();
		gbc_buttonAnalyze.insets = new Insets(0, 0, 5, 5);
		gbc_buttonAnalyze.gridx = 5;
		gbc_buttonAnalyze.gridy = 7;
		frmCvia.getContentPane().add(buttonAnalyze, gbc_buttonAnalyze);
		
		Component verticalStrut_1 = Box.createVerticalStrut(3);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_1.gridx = 4;
		gbc_verticalStrut_1.gridy = 8;
		frmCvia.getContentPane().add(verticalStrut_1, gbc_verticalStrut_1);
		buttonAnalyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIModel.startProcessing(textAreaKeyWords.getText());
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

	private void writeTextToFile(String contents, String fileName) {
		//contents = contents.replaceAll("(?!\\r)\\n", "\r\n");
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.print(contents);
			pw.close();  
		} catch (Exception e) {
			System.out.println("An exception occured in writing the text to file.");
			e.printStackTrace();
		}
	}

	private void saveJobKeywords(String itemName, String keywords) {
		if (itemName.equals("Java Developer")) {
			writeTextToFile(keywords, System.getProperty("user.dir")+"\\Java Developer.txt");
		} else if (itemName.equals("Android Developer")) {
			writeTextToFile(keywords, System.getProperty("user.dir")+"\\Android Developer.txt");
		} else if (itemName.equals("iOS Developer")) {
			writeTextToFile(keywords, System.getProperty("user.dir")+"\\iOS Developer.txt");
		} else if (itemName.equals("Web Developer")) {
			writeTextToFile(keywords, System.getProperty("user.dir")+"\\Web Developer.txt");
		}
	}

	private void openJobKeywords(final JTextArea textAreaKeyWords, String itemName) {
		if (itemName.equals("Java Developer")) {
			try {
				keywords = readFile(System.getProperty("user.dir")+"\\Java Developer.txt");
				textAreaKeyWords.setText(keywords);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (itemName.equals("Android Developer")) {
			try {
				keywords = readFile(System.getProperty("user.dir")+"\\Android Developer.txt");
				textAreaKeyWords.setText(keywords);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (itemName.equals("iOS Developer")) {
			try {
				keywords = readFile(System.getProperty("user.dir")+"\\iOS Developer.txt");
				textAreaKeyWords.setText(keywords);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (itemName.equals("Web Developer")) {
			try {
				keywords = readFile(System.getProperty("user.dir")+"\\Web Developer.txt");
				textAreaKeyWords.setText(keywords);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
