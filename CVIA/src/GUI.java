import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;

public class GUI {
	private static final String FILENAME_SAVED_CVS = "Saved CVs.txt";
	private static final String FILENAME_JOB_LIST = "Job List.txt";
	private static final String DEFAULT_KEYWORD_WEIGHT = "1";

	private File[] originalFiles;
	private int[] resultIndex;
	private JFrame frmCvia;
	private String[] jobList;
	private String jobListString;
	private String selectedJob = "";
	private String theOutString;
	private JTextField txtEnterNewJob;
	private JTable keywordsTable;
	private boolean hasClickedAnalyze;
	private int chosenJob;
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
		loadJobList();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * Order of initialization (also the code structure): 
	 * 		Root frame and layout
	 * 		Struts
	 * 		Labels
	 * 		Text Area & Fields
	 * 		Table
	 * 		Dropdown List
	 * 		Checkbox
	 * 		Buttons 
	 */
	private void initialize() {
		// Root frame and layout
		frmCvia = new JFrame();
		frmCvia.setTitle("CViA");
		frmCvia.setBounds(100, 25, 650, 700);
		frmCvia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, Double.MIN_VALUE};
		frmCvia.getContentPane().setLayout(gridBagLayout);

		Component horizontalStrut_left = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_left = new GridBagConstraints();
		gbc_horizontalStrut_left.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_left.gridx = 0;
		gbc_horizontalStrut_left.gridy = 7;
		frmCvia.getContentPane().add(horizontalStrut_left, gbc_horizontalStrut_left);

		Component horizontalStrut_right = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_right = new GridBagConstraints();
		gbc_horizontalStrut_right.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_right.gridx = 6;
		gbc_horizontalStrut_right.gridy = 7;
		frmCvia.getContentPane().add(horizontalStrut_right, gbc_horizontalStrut_right);

		Component verticalStrut_bottom = Box.createVerticalStrut(3);
		GridBagConstraints gbc_verticalStrut_bottom = new GridBagConstraints();
		gbc_verticalStrut_bottom.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_bottom.gridx = 4;
		gbc_verticalStrut_bottom.gridy = 9;
		frmCvia.getContentPane().add(verticalStrut_bottom, gbc_verticalStrut_bottom);

		// Labels
		JLabel lblFilesOpened = new JLabel("Files Open:");
		GridBagConstraints gbc_lblFilesOpened = new GridBagConstraints();
		gbc_lblFilesOpened.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilesOpened.gridx = 1;
		gbc_lblFilesOpened.gridy = 0;
		frmCvia.getContentPane().add(lblFilesOpened, gbc_lblFilesOpened);

		JLabel lblCvDetails = new JLabel("CV Details:");
		GridBagConstraints gbc_lblCvDetails = new GridBagConstraints();
		gbc_lblCvDetails.insets = new Insets(0, 0, 5, 5);
		gbc_lblCvDetails.gridx = 1;
		gbc_lblCvDetails.gridy = 2;
		frmCvia.getContentPane().add(lblCvDetails, gbc_lblCvDetails);

		JLabel lblJob = new JLabel("Job:");
		lblJob.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 1;
		gbc_lblJob.gridy = 4;
		frmCvia.getContentPane().add(lblJob, gbc_lblJob);

		JLabel lblKeyWords = new JLabel("Key words:");
		GridBagConstraints gbc_lblKeyWords = new GridBagConstraints();
		gbc_lblKeyWords.insets = new Insets(0, 0, 5, 5);
		gbc_lblKeyWords.gridx = 1;
		gbc_lblKeyWords.gridy = 6;
		frmCvia.getContentPane().add(lblKeyWords, gbc_lblKeyWords);

		JLabel lblWeightingMethod = new JLabel("Weighting Method:");
		GridBagConstraints gbc_lblWeightingMethod = new GridBagConstraints();
		gbc_lblWeightingMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeightingMethod.anchor = GridBagConstraints.EAST;
		gbc_lblWeightingMethod.gridx = 4;
		gbc_lblWeightingMethod.gridy = 6;
		frmCvia.getContentPane().add(lblWeightingMethod, gbc_lblWeightingMethod);

		// Text Area & Text Fields
		final JTextArea textAreaCVDetails = new JTextArea();
		textAreaCVDetails.setEditable(false);
		GridBagConstraints gbc_textAreaCVDetails = new GridBagConstraints();
		gbc_textAreaCVDetails.gridwidth = 5;
		gbc_textAreaCVDetails.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaCVDetails.fill = GridBagConstraints.BOTH;
		gbc_textAreaCVDetails.gridx = 1;
		gbc_textAreaCVDetails.gridy = 3;
		textAreaCVDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaCVDetailsScrollPane = new JScrollPane(textAreaCVDetails);
		frmCvia.getContentPane().add(textAreaCVDetailsScrollPane, gbc_textAreaCVDetails);

		txtEnterNewJob = new JTextField();
		GridBagConstraints gbc_txtEnterNewJob = new GridBagConstraints();
		gbc_txtEnterNewJob.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterNewJob.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterNewJob.gridx = 4;
		gbc_txtEnterNewJob.gridy = 5;
		frmCvia.getContentPane().add(txtEnterNewJob, gbc_txtEnterNewJob);
		txtEnterNewJob.setColumns(10);

		// Files Open Table
		final JTable tableFilesOpen = new JTable() {
			// Sets the type of the column in the table
			// Needed to make checkboxes in the table 
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return Double.class;
				default:
					return Boolean.class;
				}
			}
		};
		final DefaultTableModel filesOpenTableModel = new DefaultTableModel();
		tableFilesOpen.setAutoCreateRowSorter(true);

		filesOpenTableModel.setColumnIdentifiers(new Object[] {"Files", "Score", "Keep?"});
		tableFilesOpen.setModel(filesOpenTableModel);

		final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
		tableFilesOpen.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		tableFilesOpen.getColumnModel().getColumn(0).setMinWidth(400);

		tableFilesOpen.setCellSelectionEnabled(true);
		tableFilesOpen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaFilesOpenScrollPane = new JScrollPane(tableFilesOpen);
		GridBagConstraints gbc_textAreaFilesOpen = new GridBagConstraints();
		gbc_textAreaFilesOpen.gridwidth = 5;
		gbc_textAreaFilesOpen.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaFilesOpen.fill = GridBagConstraints.BOTH;
		gbc_textAreaFilesOpen.gridx = 1;
		gbc_textAreaFilesOpen.gridy = 1;
		tableFilesOpen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frmCvia.getContentPane().add(textAreaFilesOpenScrollPane, gbc_textAreaFilesOpen);

		// Keywords Table
		keywordsTable = new JTable() {
			// Sets the input type of the column in the table
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				default:
					return Integer.class;
				}
			}
		};
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 5;
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 1;
		gbc_table.gridy = 7;
		JScrollPane keywordsTableScrollPane = new JScrollPane(keywordsTable);
		frmCvia.getContentPane().add(keywordsTableScrollPane, gbc_table);

		final DefaultTableModel keywordsTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				switch (col) {
				case 0:
					return true;
				case 1:
					return false;
				case 2:
					return true;
				default:
					return true;
				}
			}
		};
		keywordsTable.setAutoCreateRowSorter(true);

		keywordsTableModel.setColumnIdentifiers(new Object[] {"Keywords", "Selected CV Contains Keyword?", "Weight"});
		keywordsTable.setModel(keywordsTableModel);
		keywordsTable.getColumnModel().getColumn(0).setMinWidth(400);
		keywordsTable.getColumnModel().getColumn(1).setMaxWidth(200);
		keywordsTable.getColumnModel().getColumn(1).setMinWidth(200);
		keywordsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		keywordsTable.removeColumn(keywordsTable.getColumnModel().getColumn(2));

		// Dropdown List
		final JComboBox<String> comboBox_Job = new JComboBox<String>();
		GridBagConstraints gbc_comboBox_Job = new GridBagConstraints();
		gbc_comboBox_Job.gridwidth = 3;
		gbc_comboBox_Job.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_Job.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_Job.gridx = 1;
		gbc_comboBox_Job.gridy = 5;
		frmCvia.getContentPane().add(comboBox_Job, gbc_comboBox_Job);
		for (int i = 0; i < jobList.length; i++) {
			comboBox_Job.addItem(jobList[i]);
		}
		selectedJob = (String) comboBox_Job.getSelectedItem();
		openJobKeywords(keywordsTable, selectedJob);

		comboBox_Job.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedJob = (String) comboBox_Job.getSelectedItem();
				openJobKeywords(keywordsTable, selectedJob);
			}
		});

		final JComboBox<String> comboBox_weighting = new JComboBox<String>();
		comboBox_weighting.setModel(new DefaultComboBoxModel<String>(new String[] {"Default", "Simplified", "Custom Weights"}));
		GridBagConstraints gbc_comboBox_weighting = new GridBagConstraints();
		gbc_comboBox_weighting.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_weighting.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_weighting.gridx = 5;
		gbc_comboBox_weighting.gridy = 6;
		frmCvia.getContentPane().add(comboBox_weighting, gbc_comboBox_weighting);
		comboBox_weighting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openJobKeywords(keywordsTable, selectedJob);
				if (comboBox_weighting.getSelectedIndex() == 2) {
					((DefaultTableModel) keywordsTable.getModel()).addColumn(null);
					while(keywordsTable.getColumnCount() > 3) {
						keywordsTable.removeColumn(keywordsTable.getColumnModel().getColumn(3));
					}
					keywordsTable.getColumnModel().getColumn(2).setMaxWidth(100);
					keywordsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
					keywordsTable.getColumnModel().getColumn(1).setMaxWidth(200);
					keywordsTable.getColumnModel().getColumn(1).setMinWidth(200);
					keywordsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
				} else {
					if (keywordsTable.getColumnCount() > 2) {
						keywordsTable.removeColumn(keywordsTable.getColumnModel().getColumn(2));
					}
				}
			}
		});

		// Buttons
		JButton buttonBrowse = new JButton("Open PDF Files");
		GridBagConstraints gbc_buttonBrowse = new GridBagConstraints();
		gbc_buttonBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_buttonBrowse.gridx = 4;
		gbc_buttonBrowse.gridy = 0;
		frmCvia.getContentPane().add(buttonBrowse, gbc_buttonBrowse);
		buttonBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setAcceptAllFileFilterUsed(false);
				String[] extensions = {"doc", "docx", "pdf", "txt"};
				String[] descriptions = {".doc, .docx, .pdf, .txt"};
				c.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				c.addChoosableFileFilter(new OpenFileFilter(extensions, descriptions));
				c.setMultiSelectionEnabled(true); // returns a array of File objects
				if (theOutString != null) {
					c.setCurrentDirectory(new File(theOutString)); 
				}

				int rVal = c.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					File[] files = c.getSelectedFiles();
					theOutString = files[0].getPath();
					List<File> fileList = new ArrayList<File>();
					fileList = iterateFiles(files, fileList);
					files = fileList.toArray(new File[fileList.size()]);
					originalFiles = fileList.toArray(new File[fileList.size()]);

					MainPresenter.ClearData();
					resultIndex=new int[files.length];
					filesOpenTableModel.setRowCount(0);
					for (int i = 0; i < files.length; i++) {
						filesOpenTableModel.addRow(new Object[]{files[i].getPath(), "?", false});

						System.out.println(files[i].toString());
						files[i] = MainPresenter.parsePDFFiles(files[i], i);
						MainPresenter.startProcessing(files[i].toString());
						resultIndex[i]=i;
					}
					hasClickedAnalyze = false;
					openJobKeywords(keywordsTable, selectedJob);
				}
			}
		});

		JButton buttonAnalyze = new JButton("Start Analyzing");
		GridBagConstraints gbc_buttonAnalyze = new GridBagConstraints();
		gbc_buttonAnalyze.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonAnalyze.insets = new Insets(0, 0, 5, 5);
		gbc_buttonAnalyze.gridx = 5;
		gbc_buttonAnalyze.gridy = 0;
		frmCvia.getContentPane().add(buttonAnalyze, gbc_buttonAnalyze);
		buttonAnalyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[][] keywords = new String[keywordsTable.getRowCount()][2];
				//boolean isCustomWeights = false;
				int selectedAnalyzingMethod = comboBox_weighting.getSelectedIndex();

				for (int i = 0; i < keywordsTable.getRowCount(); i++) {					
					keywords[i][0] = (String) keywordsTable.getModel().getValueAt(i, 0);
					if (selectedAnalyzingMethod == 2) {
						keywords[i][1] = String.valueOf(keywordsTable.getModel().getValueAt(i, 2));
						//isCustomWeights = true;
					} else {
						keywords[i][1] = DEFAULT_KEYWORD_WEIGHT; // or whatever other default
						//isCustomWeights = false;
					}
				}
				String[][] results = MainPresenter.search(keywords, selectedAnalyzingMethod);

				filesOpenTableModel.setRowCount(0);
				for (int i = 0; i < results.length; i++) {
					System.out.println(results[i][1]);
					filesOpenTableModel.addRow(new Object[]{originalFiles[Integer.parseInt(results[i][0])], Double.parseDouble(results[i][1])*100, false});
					resultIndex[i]=Integer.parseInt(results[i][0]);
				}
				hasClickedAnalyze = true;
				chosenJob = comboBox_Job.getSelectedIndex();
			}
		});

		JButton btnSeeDetailsOf = new JButton("See details of selected file");
		GridBagConstraints gbc_btnSeeDetailsOf = new GridBagConstraints();
		gbc_btnSeeDetailsOf.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSeeDetailsOf.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeeDetailsOf.gridx = 4;
		gbc_btnSeeDetailsOf.gridy = 2;
		frmCvia.getContentPane().add(btnSeeDetailsOf, gbc_btnSeeDetailsOf);
		btnSeeDetailsOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableFilesOpen.getSelectedRow();
				int colIndex = tableFilesOpen.getSelectedColumn();
				String[][] CVTermDetails;
				if (colIndex == 0) {
					String CVDetails = MainPresenter.getCVDetails(originalFiles[resultIndex[rowIndex]],resultIndex[rowIndex]);
					textAreaCVDetails.setText(CVDetails);
					textAreaCVDetails.setCaretPosition(0);
					CVTermDetails= MainPresenter.getMatchedAndUnmatchedTerms(resultIndex[rowIndex]);

					if(CVTermDetails != null && hasClickedAnalyze == true && chosenJob == comboBox_Job.getSelectedIndex()) {
						keywordsTableModel.setRowCount(0);
						for (int i = 0; i < CVTermDetails.length; i++) {
							System.out.println(CVTermDetails[i][1]);
							keywordsTableModel.addRow(new Object[]{CVTermDetails[i][0], CVTermDetails[i][1], Integer.parseInt(DEFAULT_KEYWORD_WEIGHT)});
						}
					}

				} else {
					JOptionPane.showMessageDialog(frmCvia, "You have not selected a CV. Please select a CV from the table above by clicking on its entry");
				}


			}
		});

		JButton btnSaveMarkedFiles = new JButton("Save Marked Files");
		GridBagConstraints gbc_btnSaveMarkedFiles = new GridBagConstraints();
		gbc_btnSaveMarkedFiles.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveMarkedFiles.gridx = 5;
		gbc_btnSaveMarkedFiles.gridy = 2;
		frmCvia.getContentPane().add(btnSaveMarkedFiles, gbc_btnSaveMarkedFiles);
		btnSaveMarkedFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String saveFileList = "";
				for (int i = 0; i < tableFilesOpen.getRowCount(); i++) {
					if ((Boolean) tableFilesOpen.getModel().getValueAt(i, 2) == Boolean.TRUE) {
						saveFileList = saveFileList.concat(tableFilesOpen.getModel().getValueAt(i, 0) + "\n");
					}
				}
				if (!saveFileList.isEmpty()) {
					writeTextToFile(saveFileList, System.getProperty("user.dir")+ "\\"+ FILENAME_SAVED_CVS);
					JOptionPane.showMessageDialog(frmCvia, "The marked files have been saved in " + System.getProperty("user.dir")+ "\\"+ FILENAME_SAVED_CVS);
				} else {
					JOptionPane.showMessageDialog(frmCvia, "No files have been chosen. Please use the checkboxes above.");
				}
			}
		});

		JButton btnAddJob = new JButton("Add New Job");
		GridBagConstraints gbc_btnAddJob = new GridBagConstraints();
		gbc_btnAddJob.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddJob.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddJob.gridx = 5;
		gbc_btnAddJob.gridy = 5;
		frmCvia.getContentPane().add(btnAddJob, gbc_btnAddJob);
		btnAddJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtEnterNewJob.getText().equals("")) {
					comboBox_Job.addItem(txtEnterNewJob.getText());
					txtEnterNewJob.setText("");
					comboBox_Job.setSelectedIndex(comboBox_Job.getItemCount() - 1);
				}
			}
		});

		JButton btnAddKeyword = new JButton("Add Keyword");
		GridBagConstraints gbc_btnAddKeyword = new GridBagConstraints();
		gbc_btnAddKeyword.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddKeyword.gridx = 1;
		gbc_btnAddKeyword.gridy = 8;
		frmCvia.getContentPane().add(btnAddKeyword, gbc_btnAddKeyword);
		btnAddKeyword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keywordsTableModel.addRow(new Object[]{"", Integer.valueOf(DEFAULT_KEYWORD_WEIGHT)});	
				keywordsTable.scrollRectToVisible(keywordsTable.getCellRect(keywordsTable.getRowCount()-1, 0, true));
			}
		});

		JButton btnDeleteKeyword = new JButton("Delete Keyword");
		GridBagConstraints gbc_btnDeleteKeyword = new GridBagConstraints();
		gbc_btnDeleteKeyword.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteKeyword.gridx = 2;
		gbc_btnDeleteKeyword.gridy = 8;
		frmCvia.getContentPane().add(btnDeleteKeyword, gbc_btnDeleteKeyword);
		btnDeleteKeyword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (keywordsTable.getSelectedRow() != -1) {
					keywordsTableModel.removeRow(keywordsTable.getSelectedRow());
				} else {
					JOptionPane.showMessageDialog(frmCvia, "Please choose a keyword to delete!");
				}
			}
		});

		JButton buttonSaveResults = new JButton("Save Keywords");
		GridBagConstraints gbc_buttonSaveResults = new GridBagConstraints();
		gbc_buttonSaveResults.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSaveResults.gridx = 5;
		gbc_buttonSaveResults.gridy = 8;
		frmCvia.getContentPane().add(buttonSaveResults, gbc_buttonSaveResults);
		buttonSaveResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int index = comboBox.getSelectedIndex();
				String keywords = "";
				for (int i = 0; i < keywordsTable.getRowCount(); i++) {
					keywords = keywords.concat((String) keywordsTable.getModel().getValueAt(i, 0) + "~" + keywordsTable.getModel().getValueAt(i, 1) + "\n");
				}
				int reply = JOptionPane.showConfirmDialog(frmCvia, "Are you sure you want to save these keywords (the previous list will be overwritten)?", "Are you sure?",  JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					saveJobKeywords(selectedJob, keywords);
				}
			}
		});
	}

	private void loadJobList() {
		try {
			jobListString = readFile(System.getProperty("user.dir")+"\\CViA\\" + FILENAME_JOB_LIST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		jobList = jobListString.split("\n");
		for (int i = 0; i < jobList.length; i++) {
			jobList[i] = jobList[i].trim();
		}
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

	public List<File> iterateFiles(File[] files, List<File> fileList) {
		for (File file : files) {
			if (file.isFile()) {
				fileList.add(file);
			} else if (file.isDirectory()) {
				iterateFiles(file.listFiles(), fileList);
			}
		}
		return fileList;
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

	private void updatePhraseFile(String keywords) {
		Set<String> phraseSet = new HashSet<String>();

		// Add the phrases inside the file to a set
		try {
			String initialPhrases = readFile(System.getProperty("user.dir")+"\\phrases.txt");
			String phrases[] = initialPhrases.split("\\r?\\n");
			for (int k = 0; k < phrases.length; k++) {
				phraseSet.add(phrases[k]);
			}
		} catch (IOException e1) {
			System.out.println("An exception occured in opening the phrase list. ");
			e1.printStackTrace();
		}

		// Add the modified keywords to the set
		String keywordArr[] = keywords.split("\\r?\\n");
		for (int i = 0; i < keywordArr.length; i++) {
			String keyword = keywordArr[i];
			keyword = keyword.substring(0, keyword.indexOf("~")).trim();
			if (keyword.contains(" ")) {
				phraseSet.add(keyword);	
			}
		}

		// Write the phrases inside the set to the file
		String allPhrases = "";
		List<String> phraseList = new ArrayList<String>();
		phraseList.addAll(phraseSet);

		for (int i = 0; i < phraseList.size(); i++) {
			allPhrases = allPhrases.concat(phraseList.get(i) + "\n");
		}

		writeTextToFile(allPhrases, System.getProperty("user.dir")+"\\phrases.txt");
	}

	private void saveJobKeywords(String selectedJob, String keywords) {
		updatePhraseFile(keywords);
		for (int i = 0; i < jobList.length; i++) {
			if (selectedJob.equals(jobList[i])) {
				writeTextToFile(keywords, System.getProperty("user.dir")+"\\CViA\\" + jobList[i] + ".txt");
				return;
			}
		}
		saveNewJob(selectedJob, keywords);
	}

	private void saveNewJob(String jobName, String keywords) {
		writeTextToFile(keywords, System.getProperty("user.dir")+"\\CViA\\" + jobName + ".txt");
		jobListString = jobListString.concat(jobName + "\n");
		writeTextToFile(jobListString, System.getProperty("user.dir")+"\\CViA\\" + FILENAME_JOB_LIST);
		loadJobList();
	}

	private void openJobKeywords(final JTable keywordsTable, String itemName) {
		((DefaultTableModel) keywordsTable.getModel()).setRowCount(0);
		for (int i = 0; i < jobList.length; i++) {
			if (itemName.equals(jobList[i])) {
				try {
					String allKeywords = readFile(System.getProperty("user.dir")+"\\CViA\\" + jobList[i] + ".txt");
					String keywords[] = allKeywords.split("\\r?\\n");
					for (int k = 0; k < keywords.length; k++) {
						String keywordWeightPair[] = keywords[k].split("~");
						((DefaultTableModel) keywordsTable.getModel()).addRow(new Object[]{keywordWeightPair[0], "Unknown" ,keywordWeightPair[1]});
					}
				} catch (IOException e1) {
					System.out.println("An exception occured in opening the keywords. ");
					e1.printStackTrace();
				}
				return;
			}
		}
	}
}

class OpenFileFilter extends FileFilter {

	List<String> acceptedExtensions = new ArrayList<String>();
	List<String> typeDescriptions = new ArrayList<String>();

	public OpenFileFilter(String[] extensions, String[] descriptions) {
		for (int i = 0; i < extensions.length; i++) {
			acceptedExtensions.add(extensions[i]);
		}
		for (int i = 0; i < descriptions.length; i++) {
			typeDescriptions.add(descriptions[i]);
		}
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		} else {
			for (int i = 0; i < acceptedExtensions.size(); i++) {
				String extension = acceptedExtensions.get(i);
				if (f.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return typeDescriptions.get(0);
	}
}