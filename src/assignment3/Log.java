package assignment3;

import java.awt.BorderLayout;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Log extends JPanel implements ActionListener {
	private final String TEXT_SHOW = "Show History";
	private final String TEXT_CLEAR = "Clear All";

	private JButton showBt = new JButton(TEXT_SHOW);
	private JButton clearBt = new JButton(TEXT_CLEAR);

	private JLabel historyLabel = new JLabel("VAMIX History");
	private JTextArea txtOutput = new JTextArea();

	private final  static String logDir = System.getProperty("user.home")+"/.vamix/";
	private final static String logFile = System.getProperty("user.home")+"/.vamix/log";
	
	private Font btFont = new Font("TimesRoman", Font.BOLD, 12);
	
	JLabel background = new JLabel(new ImageIcon("images.jpg"));
	Image bgimage;

	/**
	 * Set the GUI
	 */
	public Log() {

		//Set the background image
		MediaTracker mt = new MediaTracker(this);
		bgimage = Toolkit.getDefaultToolkit().getImage("img.jpg");
		mt.addImage(bgimage, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Set the VAMIX history title
		historyLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		add(historyLabel, BorderLayout.CENTER);
		
		//Set the txtOutput area
		txtOutput.setEditable(false);
		JScrollPane scroll = new JScrollPane(txtOutput);
		add(scroll, BorderLayout.CENTER);
		scroll.setPreferredSize(new Dimension(490,205));
		
		//Set the buttons
		showBt.addActionListener(this);
		showBt.setFont(btFont);
		showBt.setPreferredSize(new Dimension(150, 25));
		add(showBt, BorderLayout.SOUTH);
		
		clearBt.addActionListener(this);
		clearBt.setFont(btFont);
		clearBt.setPreferredSize(new Dimension(150, 25));
		clearBt.setEnabled(false);
		add(clearBt, BorderLayout.SOUTH);


	}
	
	/**
	 * Paint the background image
	 */
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	  }

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == showBt ) {

			//Display an error message if the log hasn't been created yet
			File f = new File(logFile);
			if (!f.exists()) { 
				JOptionPane.showMessageDialog(null, "No operations have been performed for this user so far");
				return;
			}

			try {
				//Read the log file and display it in the JText Area
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(f));
				String line; 
				txtOutput.setText("");
				while ((line = reader.readLine()) != null) {
					txtOutput.append(line + System.getProperty("line.separator"));
				}
				reader.close();
				
				//Store the print operation in the log
				Date date = new Date();
				Log.appendToLog ("PRINT", date);
				
				//Allow the user to delete the history
				clearBt.setEnabled(true);

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else { //When clear button is pressed
			
			//Clear the text area and delete the log file
			txtOutput.setText("");
			File f = new File(logFile);
			f.delete();
			clearBt.setEnabled(false);
		}

	}

	/**
	 * This method creates a log file in the required directory (~/.vamix)
	 */
	private static void createLog() {
		File f = new File(logFile);
		
		//Create the log file if it doesn't exist already
		if (!f.exists()) { 
			new File(logDir).mkdir();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method appends a successful operation done to the the vamix history
	 * @param functionality 
	 * @param time
	 */
	protected static void appendToLog (String functionality, Date date) {

		//Create the log file
		createLog();

		try {

			//Open the file writer
			File f = new File(logFile);
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);

			//Get the number of lines in the log file
			BufferedReader reader = new BufferedReader(new FileReader(f));
			int lines = 0;
			while (reader.readLine() != null) lines++;
			reader.close();

			//Set the format of the date and time
			DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy      HH:mm");

			//Append the history line to the log file
			bw.write((lines+1) +"\t"+ functionality +"\t"+ dateFormat.format(date));
			bw.newLine();

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}