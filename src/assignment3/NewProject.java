package assignment3;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;


public class NewProject extends JPanel implements ActionListener {

	private final String TEXT_OK = "Ok";
	private JButton okButton = new JButton(TEXT_OK);

	private JLabel newProjectLabel = new JLabel("Create new Project");
	private JLabel projectLabel = new JLabel("Project name: ");

	private Font defaultFont = new Font("TimesRoman", Font.PLAIN, 12);
	private Font btFont = new Font("TimesRoman", Font.BOLD, 12);

	private JLabel separator = new JLabel("");
	private JLabel separator2 = new JLabel("");

	private JTextField outputName = new JTextField();
	
	JLabel background = new JLabel(new ImageIcon("img.jpg"));
	Image bgimage;


	/**
	 * Set the GUI
	 */
	public NewProject() {

		//Set the background image
		MediaTracker mt = new MediaTracker(this);
		bgimage = Toolkit.getDefaultToolkit().getImage("img.jpg");
		mt.addImage(bgimage, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Set the Extract title
		newProjectLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		add(newProjectLabel);

		//Add blank space
		separator.setPreferredSize(new Dimension(500, 20));
		add(separator);
		
		//Set the output name label and text field
		projectLabel.setFont(defaultFont);
		add(projectLabel);
		
		add(outputName);
		outputName.setPreferredSize(new Dimension(230, 25));

		//Add blank space
		separator2.setPreferredSize(new Dimension(500, 10));
		add(separator2);
		
		//Set t he extract button
		okButton.addActionListener(this);
		okButton.setFont(btFont);
		okButton.setPreferredSize(new Dimension(100, 25));
		add(okButton);

	}
	
	/**
	 * Paint the background image
	 */
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	  }
	

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == okButton ) {


			if (outputName.getText().isEmpty()) { //Check the user had chosen an input file
				
				JOptionPane.showMessageDialog(null, "Please choose a project name");
				return;

			} 
			
			//Allow the user to choose the output file destination
			JFileChooser dirChooser = new JFileChooser();  
			dirChooser.setDialogTitle("Save to ...");
			dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//dirChooser.setAcceptAllFileFilterUsed(false);

			int returnVal = dirChooser.showSaveDialog(null);

			//If no directory was selected, don't extract!
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			//Get the name of the chosen directory 
			String workingDir = dirChooser.getSelectedFile().getPath();

			//Check that the output file doesn't exists in the specified directory
			String outputFileName = workingDir+"/"+outputName.getText();
			File f = new File(outputFileName);
			File hiddenDir = new File(workingDir+"/h"+outputName.getText()); //TODO change it to hidden!

			if ( f.exists()) {
				//Allow user to choose either overwriting the current file or change the output file name
				Object[] existOptions = {"Cancel", "Overwrite"};
				int optionChosen = JOptionPane.showOptionDialog(null, "Project already exists. " +
						"Do you want to overwrite the existing project?",
						"Project Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null,existOptions, existOptions[0]);

				if (optionChosen == 1) {
					f.delete(); //Delete the existing file
					hiddenDir.delete();
				} else {
					outputName.setText("");
					return;
				}
			}
			
			/*
			 * Create all the necessary text files
			 */
			try {
				//Create the main project file
				f.createNewFile();
				
				//Create the hidden directory 
				hiddenDir.mkdir();
				
				//Create the info and commands files
				File videoInfo = new File (hiddenDir.toString()+"/"+"videoInfo");
				File audioEdit = new File (hiddenDir.toString()+"/"+"audioEdit");
				File titleEdit = new File (hiddenDir.toString()+"/"+"titleEdit");
				File creditEdit = new File (hiddenDir.toString()+"/"+"creditEdit");
				File titleCredit = new File (hiddenDir.toString()+"/"+"titleCredit");
				
				videoInfo.createNewFile();
				audioEdit.createNewFile();
				titleEdit.createNewFile();
				creditEdit.createNewFile();
				titleCredit.createNewFile();
				
				//Write the hidden directory and the working directory to the main project file
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(hiddenDir.toString());
				bw.newLine();
				bw.write(workingDir);
				
				bw.close();
				
				//Make the project file read only
				f.setReadOnly();
				
				} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			

		}

	}

}