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
import java.util.List;
import java.util.Scanner;
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

public class GUI {
	private static final String FILENAME_SAVED_CVS = "Saved CVs.txt";
	private static final String FILENAME_JOB_LIST = "Job List.txt";

	private File[] originalFiles;
	private int[] resultIndex;
	private JFrame frmCvia;
	private String[] jobList;
	private String jobListString;
	private String keywords = "";
	private String selectedJob = "";
	private JTextField txtEnterNewJob;
	private JTextArea textAreaKeyWords;
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
	 * 		Job Dropdown List
	 * 		Buttons 
	 */
	private void initialize() {
		// Root frame and layout
		frmCvia = new JFrame();
		frmCvia.setTitle("CViA");
		frmCvia.setBounds(100, 25, 650, 700);
		frmCvia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, Double.MIN_VALUE};
		frmCvia.getContentPane().setLayout(gridBagLayout);

		// Struts for padding
		Component verticalStrut_top = Box.createVerticalStrut(1);
		GridBagConstraints gbc_verticalStrut_top = new GridBagConstraints();
		gbc_verticalStrut_top.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_top.gridx = 3;
		gbc_verticalStrut_top.gridy = 0;
		frmCvia.getContentPane().add(verticalStrut_top, gbc_verticalStrut_top);

		Component horizontalStrut_left = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_left = new GridBagConstraints();
		gbc_horizontalStrut_left.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_left.gridx = 0;
		gbc_horizontalStrut_left.gridy = 8;
		frmCvia.getContentPane().add(horizontalStrut_left, gbc_horizontalStrut_left);

		Component horizontalStrut_right = Box.createHorizontalStrut(3);
		GridBagConstraints gbc_horizontalStrut_right = new GridBagConstraints();
		gbc_horizontalStrut_right.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_right.gridx = 5;
		gbc_horizontalStrut_right.gridy = 8;
		frmCvia.getContentPane().add(horizontalStrut_right, gbc_horizontalStrut_right);

		Component verticalStrut_bottom = Box.createVerticalStrut(3);
		GridBagConstraints gbc_verticalStrut_bottom = new GridBagConstraints();
		gbc_verticalStrut_bottom.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_bottom.gridx = 3;
		gbc_verticalStrut_bottom.gridy = 10;
		frmCvia.getContentPane().add(verticalStrut_bottom, gbc_verticalStrut_bottom);

		// Labels
		JLabel lblFilesOpened = new JLabel("Files Open:");
		GridBagConstraints gbc_lblFilesOpened = new GridBagConstraints();
		gbc_lblFilesOpened.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilesOpened.gridx = 1;
		gbc_lblFilesOpened.gridy = 1;
		frmCvia.getContentPane().add(lblFilesOpened, gbc_lblFilesOpened);

		JLabel lblCvDetails = new JLabel("CV Details:");
		GridBagConstraints gbc_lblCvDetails = new GridBagConstraints();
		gbc_lblCvDetails.insets = new Insets(0, 0, 5, 5);
		gbc_lblCvDetails.gridx = 1;
		gbc_lblCvDetails.gridy = 3;
		frmCvia.getContentPane().add(lblCvDetails, gbc_lblCvDetails);

		JLabel lblJob = new JLabel("Job:");
		lblJob.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblJob = new GridBagConstraints();
		gbc_lblJob.insets = new Insets(0, 0, 5, 5);
		gbc_lblJob.gridx = 1;
		gbc_lblJob.gridy = 5;
		frmCvia.getContentPane().add(lblJob, gbc_lblJob);

		JLabel lblKeyWords = new JLabel("Key words:");
		GridBagConstraints gbc_lblKeyWords = new GridBagConstraints();
		gbc_lblKeyWords.insets = new Insets(0, 0, 5, 5);
		gbc_lblKeyWords.gridx = 1;
		gbc_lblKeyWords.gridy = 7;
		frmCvia.getContentPane().add(lblKeyWords, gbc_lblKeyWords);

		// Text Area & Text Fields
		final JTextArea textAreaCVDetails = new JTextArea();
		textAreaCVDetails.setEditable(false);
		GridBagConstraints gbc_textAreaCVDetails = new GridBagConstraints();
		gbc_textAreaCVDetails.gridwidth = 4;
		gbc_textAreaCVDetails.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaCVDetails.fill = GridBagConstraints.BOTH;
		gbc_textAreaCVDetails.gridx = 1;
		gbc_textAreaCVDetails.gridy = 4;
		textAreaCVDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaCVDetailsScrollPane = new JScrollPane(textAreaCVDetails);
		frmCvia.getContentPane().add(textAreaCVDetailsScrollPane, gbc_textAreaCVDetails);

		txtEnterNewJob = new JTextField();
		GridBagConstraints gbc_txtEnterNewJob = new GridBagConstraints();
		gbc_txtEnterNewJob.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterNewJob.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterNewJob.gridx = 3;
		gbc_txtEnterNewJob.gridy = 6;
		frmCvia.getContentPane().add(txtEnterNewJob, gbc_txtEnterNewJob);
		txtEnterNewJob.setColumns(10);

		textAreaKeyWords = new JTextArea();
		GridBagConstraints gbc_textAreaKeyWords = new GridBagConstraints();
		gbc_textAreaKeyWords.gridwidth = 4;
		gbc_textAreaKeyWords.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaKeyWords.fill = GridBagConstraints.BOTH;
		gbc_textAreaKeyWords.gridx = 1;
		gbc_textAreaKeyWords.gridy = 8;
		textAreaKeyWords.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaKeyWordsScrollPane = new JScrollPane(textAreaKeyWords);
		frmCvia.getContentPane().add(textAreaKeyWordsScrollPane, gbc_textAreaKeyWords);

		// Table
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
		DefaultTableModel tableModel = new DefaultTableModel();
		tableFilesOpen.setAutoCreateRowSorter(true);

		tableModel.setColumnIdentifiers(new Object[] {"Files", "Score", "Keep?"});
		tableFilesOpen.setModel(tableModel);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
		tableFilesOpen.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		tableFilesOpen.getColumnModel().getColumn(0).setMinWidth(400);

		tableFilesOpen.setCellSelectionEnabled(true);
		tableFilesOpen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JScrollPane textAreaFilesOpenScrollPane = new JScrollPane(tableFilesOpen);
		GridBagConstraints gbc_textAreaFilesOpen = new GridBagConstraints();
		gbc_textAreaFilesOpen.gridwidth = 4;
		gbc_textAreaFilesOpen.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaFilesOpen.fill = GridBagConstraints.BOTH;
		gbc_textAreaFilesOpen.gridx = 1;
		gbc_textAreaFilesOpen.gridy = 2;
		tableFilesOpen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frmCvia.getContentPane().add(textAreaFilesOpenScrollPane, gbc_textAreaFilesOpen);

		// Job Dropdown List
		final JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 6;
		frmCvia.getContentPane().add(comboBox, gbc_comboBox);
		for (int i = 0; i < jobList.length; i++) {
			comboBox.addItem(jobList[i]);
		}
		selectedJob = (String) comboBox.getSelectedItem();
		openJobKeywords(textAreaKeyWords, selectedJob);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedJob = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
				openJobKeywords(textAreaKeyWords, selectedJob);
			}
		});

		// Buttons
		JButton buttonBrowse = new JButton("Open PDF Files");
		GridBagConstraints gbc_buttonBrowse = new GridBagConstraints();
		gbc_buttonBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_buttonBrowse.gridx = 3;
		gbc_buttonBrowse.gridy = 1;
		frmCvia.getContentPane().add(buttonBrowse, gbc_buttonBrowse);
		buttonBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setAcceptAllFileFilterUsed(false);
				String[] extensions = {"doc", "docx", "pdf", "txt"};
				String[] descriptions = {".doc, .docx, .pdf, .txt"};
				c.addChoosableFileFilter(new OpenFileFilter(extensions, descriptions));
				c.setMultiSelectionEnabled(true); // returns a array of File objects
				int rVal = c.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					File[] files = c.getSelectedFiles();
					originalFiles = c.getSelectedFiles();
					resultIndex=new int[files.length];
					for (int i = 0; i < files.length; i++) {
						tableModel.addRow(new Object[]{files[i].getPath(), "?", false});

						System.out.println(files[i].toString());
						files[i] = GUIModel.parsePDFFiles(files[i], i);
						GUIModel.startProcessing(files[i].toString());
						resultIndex[i]=i;
					}
				}
			}
		});

		JButton buttonAnalyze = new JButton("Start Analyzing");
		GridBagConstraints gbc_buttonAnalyze = new GridBagConstraints();
		gbc_buttonAnalyze.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonAnalyze.insets = new Insets(0, 0, 5, 5);
		gbc_buttonAnalyze.gridx = 4;
		gbc_buttonAnalyze.gridy = 1;
		frmCvia.getContentPane().add(buttonAnalyze, gbc_buttonAnalyze);
		buttonAnalyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[][] results = GUIModel.search(textAreaKeyWords.getText());

				tableModel.setRowCount(0);
				for (int i = 0; i < results.length; i++) {
					System.out.println(results[i][1]);
					tableModel.addRow(new Object[]{originalFiles[Integer.parseInt(results[i][0])], results[i][1], false});
					resultIndex[i]=Integer.parseInt(results[i][0]);
				}
			}
		});

		JButton btnSeeDetailsOf = new JButton("See details of selected file");
		GridBagConstraints gbc_btnSeeDetailsOf = new GridBagConstraints();
		gbc_btnSeeDetailsOf.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSeeDetailsOf.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeeDetailsOf.gridx = 3;
		gbc_btnSeeDetailsOf.gridy = 3;
		frmCvia.getContentPane().add(btnSeeDetailsOf, gbc_btnSeeDetailsOf);
		btnSeeDetailsOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableFilesOpen.getSelectedRow();
				int colIndex = tableFilesOpen.getSelectedColumn();

				if (colIndex == 0) {
					String CVDetails = GUIModel.getCVDetails(resultIndex[rowIndex]);
					textAreaCVDetails.setText(CVDetails);
					textAreaCVDetails.setCaretPosition(0);
				} else {
					JOptionPane.showMessageDialog(frmCvia, "You have not selected a CV. Please select a CV from the table above by clicking on its entry");
				}
			}
		});

		JButton btnSaveMarkedFiles = new JButton("Save Marked Files");
		GridBagConstraints gbc_btnSaveMarkedFiles = new GridBagConstraints();
		gbc_btnSaveMarkedFiles.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveMarkedFiles.gridx = 4;
		gbc_btnSaveMarkedFiles.gridy = 3;
		frmCvia.getContentPane().add(btnSaveMarkedFiles, gbc_btnSaveMarkedFiles);
		btnSaveMarkedFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String saveFileList = "";
				for (int i = 0; i < tableFilesOpen.getRowCount(); i++) {
					if ((Boolean) tableFilesOpen.getModel().getValueAt(i, 2) == Boolean.TRUE) {
						saveFileList = saveFileList.concat((String) tableFilesOpen.getModel().getValueAt(i, 0) + "\n");
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
		gbc_btnAddJob.gridx = 4;
		gbc_btnAddJob.gridy = 6;
		frmCvia.getContentPane().add(btnAddJob, gbc_btnAddJob);
		btnAddJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtEnterNewJob.getText().equals("")) {
					comboBox.addItem(txtEnterNewJob.getText());
					txtEnterNewJob.setText("");
					comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
				}
			}
		});

		JButton buttonSaveResults = new JButton("Save Keywords");
		GridBagConstraints gbc_buttonSaveResults = new GridBagConstraints();
		gbc_buttonSaveResults.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSaveResults.gridx = 4;
		gbc_buttonSaveResults.gridy = 9;
		frmCvia.getContentPane().add(buttonSaveResults, gbc_buttonSaveResults);
		buttonSaveResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//int index = comboBox.getSelectedIndex();
				String keywords = textAreaKeyWords.getText();
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

	private void saveJobKeywords(String selectedJob, String keywords) {
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

	private void openJobKeywords(final JTextArea textAreaKeyWords, String itemName) {
		for (int i = 0; i < jobList.length; i++) {
			if (itemName.equals(jobList[i])) {
				try {
					keywords = readFile(System.getProperty("user.dir")+"\\CViA\\" + jobList[i] + ".txt");
					textAreaKeyWords.setText(keywords);
					textAreaKeyWords.setCaretPosition(0);
				} catch (IOException e1) {
					System.out.println("An exception occured in opening the keywords. ");
					e1.printStackTrace();
				}
				return;
			}
		}
		textAreaKeyWords.setText("");
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