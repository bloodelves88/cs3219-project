import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
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
import java.util.Scanner;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JTextField;


public class GUI {
	private static final String FILENAME_SAVED_CVS = "Saved CVs.txt";
	private static final String FILENAME_JOB_LIST = "Job List.txt";
	private File[] originalFiles;
	private JFrame frmCvia;
	private String[] jobList;
	private String jobListString;
	private String keywords = "";
	private String selectedJob = "";
	private JTextField txtEnterNewJob;
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
		loadJobList();
		
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
		
		txtEnterNewJob = new JTextField();
		GridBagConstraints gbc_txtEnterNewJob = new GridBagConstraints();
		gbc_txtEnterNewJob.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterNewJob.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterNewJob.gridx = 4;
		gbc_txtEnterNewJob.gridy = 4;
		frmCvia.getContentPane().add(txtEnterNewJob, gbc_txtEnterNewJob);
		txtEnterNewJob.setColumns(10);
		
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
		

		final JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		frmCvia.getContentPane().add(comboBox, gbc_comboBox);
		for (int i = 0; i < jobList.length; i++) {
			comboBox.addItem(jobList[i]);
		}
		selectedJob = (String) comboBox.getSelectedItem();
		openJobKeywords(textAreaKeyWords, selectedJob);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//int index = comboBox.getSelectedIndex();
				selectedJob = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
				openJobKeywords(textAreaKeyWords, selectedJob);
			}
		});
		
		JButton btnAddJob = new JButton("Add New Job");
		btnAddJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtEnterNewJob.getText().equals("")) {
					comboBox.addItem(txtEnterNewJob.getText());
					txtEnterNewJob.setText("");
				}
			}
		});
		
		GridBagConstraints gbc_btnAddJob = new GridBagConstraints();
		gbc_btnAddJob.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddJob.gridx = 5;
		gbc_btnAddJob.gridy = 4;
		frmCvia.getContentPane().add(btnAddJob, gbc_btnAddJob);

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
					originalFiles = c.getSelectedFiles();
					for (int i = 0; i < files.length; i++) {
						DefaultTableModel model = (DefaultTableModel) tableFilesOpen.getModel();
						model.addRow(new Object[]{files[i].getPath(), "?", false});
						
						System.out.println(files[i].toString());
						files[i] = GUIModel.parsePDFFiles(files[i], i);
						GUIModel.startProcessing(files[i].toString());
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
		
		JButton btnSaveMarkedFiles = new JButton("Save Marked Files");
		GridBagConstraints gbc_btnSaveMarkedFiles = new GridBagConstraints();
		gbc_btnSaveMarkedFiles.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveMarkedFiles.gridx = 4;
		gbc_btnSaveMarkedFiles.gridy = 7;
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
				}
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
				String[][] results = GUIModel.search(textAreaKeyWords.getText());
				
				DefaultTableModel dtm = (DefaultTableModel) tableFilesOpen.getModel();
				dtm.setRowCount(0);
				for (int i = 0; i < results.length; i++) {
					System.out.println(results[i][1]);
					dtm.addRow(new Object[]{originalFiles[Integer.parseInt(results[i][0])], results[i][1], false});
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
	}

	private void openJobKeywords(final JTextArea textAreaKeyWords, String itemName) {
		for (int i = 0; i < jobList.length; i++) {
			if (itemName.equals(jobList[i])) {
				try {
					keywords = readFile(System.getProperty("user.dir")+"\\CViA\\" + jobList[i] + ".txt");
					textAreaKeyWords.setText(keywords);
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
