package assignment3;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class TextEditor extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Initializing the text for the buttons
	private final String TEXT_TEXTEDIT = "Save";

	// Initializing the three buttons
	private JButton saveButton = new JButton(TEXT_TEXTEDIT);
	private JButton prevBtn = new JButton("preview");

	// Initializing the labels
	private JLabel textEditorLabel = new JLabel("Text Editor");
	private JLabel screenLabel = new JLabel("Add text on: ");
	private JLabel durationLabel = new JLabel("Duration (in seconds): ");
	private JLabel backgroundImageLabel = new JLabel("Use for background: ");
	private JLabel addTextLabel = new JLabel("Add Text:");
	private JLabel wordLimitLabel = new JLabel("(30 words)");
	private JLabel chooseFontLabel = new JLabel("Font type: ");
	private JLabel chooseFontStyleLabel = new JLabel("Font style: ");
	private JLabel chooseFontSizeLabel = new JLabel("Font size: ");
	private JLabel chooseColorLabel = new JLabel("Font colour: ");

	// Initializing the textFields and textAreas
	private JTextField addDuration = new JTextField();
	private JTextArea addTextArea = new JTextArea(10, 40);
	private JTextField addTimeFrame = new JTextField();

	// Inializing the JRadioButton
	final private JRadioButton overlayCheck = new JRadioButton(
			"Overlay on the video");
	final private JRadioButton defaultCheck = new JRadioButton(
			"Default black image");
	final private JRadioButton frameCheck = new JRadioButton(
			"A frame from the video at:");

	String[] dropDownScreen = { "Title Screen", "Credit Screen" };
	private JComboBox screenList = new JComboBox(dropDownScreen);

	String[] dropDownFonts = { "Arial", "Courier", "Georgia",
			"TimesNewRoman", "Verdana" };
	private JComboBox fontsList = new JComboBox(dropDownFonts);

	String[] dropDownStyles = { "PLAIN", "BOLD", "ITALIC", "BOLD&ITALIC" };
	private JComboBox stylesList = new JComboBox(dropDownStyles);

	String[] dropDownSizes = { "8", "10", "14", "18", "22", "26", "30", "34",
			"38", "42", "48", "52", "56", "72" };
	private JComboBox sizesList = new JComboBox(dropDownSizes);

	String[] dropDownColors = { "black", "green", "blue", "yellow", "red",
			"white", "pink" };
	private JComboBox coloursList = new JComboBox(dropDownColors);

	private File textFile;
	private String screenType = "Title Screen";
	private String titleDuration = "";
	private String creditDuration = "";
	private int backgroundImageOption = 0;
	//private int 
	private int titleFontType = 0;
	private int titleFontStyle = 0;
	private int titleFontSize = 12;
	private String titleFontColour = "black";

	private JLabel separator = new JLabel("");
	private JLabel separator2 = new JLabel("");
	private JLabel separator3 = new JLabel("");
	private JLabel separator4 = new JLabel("");
	private JLabel separator5 = new JLabel("");
	private JLabel separator6 = new JLabel("");

	private String directory = "/afs/ec.auckland.ac.nz/users/c/c/ccha504/unixhome/Documents/206/assignment3/";
	private String fontDir = "/usr/share/fonts/truetype/msttcorefonts/";
	private String fontName = "";

	private BackgroundTask longTask;
	EmbeddedMediaPlayer mediaVideo;

	// Download constructor - sets the GUI for download
	public TextEditor(EmbeddedMediaPlayer video) {
		
		
	
		mediaVideo = video;


		// change the font of the title
		textEditorLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));

		// add the addTextArea textArea to the ScrollPane scroll
		JScrollPane scroll = new JScrollPane(addTextArea);

		// adding action listeners to all the buttons so when a user clicks a
		// button, the corresponding actions are done.
		prevBtn.addActionListener(this);
		saveButton.addActionListener(this);
		screenList.addActionListener(this);
		fontsList.addActionListener(this);
		stylesList.addActionListener(this);
		sizesList.addActionListener(this);
		coloursList.addActionListener(this);

		// add all the buttons, labels, progressBar and textFields to the panel
		add(textEditorLabel);
		add(separator);

		add(screenLabel);
		add(screenList);
		add(durationLabel);
		add(addDuration);
		add(separator2);

		add(backgroundImageLabel);
		add(separator3);
		add(overlayCheck);
		add(defaultCheck);
		add(frameCheck);
		add(addTimeFrame);

		add(separator4);

		add(addTextLabel);
		add(wordLimitLabel);
		add(separator5);

		add(chooseFontLabel);
		add(fontsList);
		add(chooseFontStyleLabel);
		add(stylesList);
		add(separator6);
		add(chooseFontSizeLabel);
		add(sizesList);
		add(chooseColorLabel);
		add(coloursList);

		add(scroll);

		add(prevBtn);
		add(saveButton);

		// set the preferred size for the buttons
		separator.setPreferredSize(new Dimension(525, 10));
		separator2.setPreferredSize(new Dimension(525, 0));
		separator3.setPreferredSize(new Dimension(525, 0));
		separator4.setPreferredSize(new Dimension(525, 0));
		separator5.setPreferredSize(new Dimension(525, 0));
		separator6.setPreferredSize(new Dimension(525, 0));
		saveButton.setPreferredSize(new Dimension(150, 25));

		addTimeFrame.setColumns(10);
		addDuration.setColumns(3);

		saveButton.setEnabled(true);

		// add ItemListnew to the radio buttons, to check what happens when the
		// radioButtons are checked or unchecked
		frameCheck.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {

					defaultCheck.setEnabled(false);
					overlayCheck.setEnabled(false);
					backgroundImageOption = 2;

				} else {

					defaultCheck.setEnabled(true);
					overlayCheck.setEnabled(true);

				}

			}
		});

		defaultCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					frameCheck.setEnabled(false);
					overlayCheck.setEnabled(false);
					backgroundImageOption = 1;

				} else {
					frameCheck.setEnabled(true);
					overlayCheck.setEnabled(true);

				}

			}
		});

		overlayCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					frameCheck.setEnabled(false);
					defaultCheck.setEnabled(false);
					backgroundImageOption = 0;

				} else {
					frameCheck.setEnabled(true);
					defaultCheck.setEnabled(true);

				}

			}
		});
	}

	/**
	 * Background Task class extends SwingWorker and handles all the long tasks.
	 */
	class BackgroundTask extends SwingWorker<Void, String> {

		String percentageDone;
		String line;
		Process process;
		ProcessBuilder builder;

		// Override doInBackgrount() to execute longTask in the background
		@Override
		protected Void doInBackground() throws Exception {

			try {

				File fileTitle = new File("TitleText.txt");
				File fileCredit = new File("CreditText.txt");

				if (backgroundImageOption == 0){


					if (fileTitle.exists() && !fileCredit.exists()){
						

						///needs to pick up media
						//delay
						String cmd2 = "avconv -ss 0 -i cc.mp4 -strict experimental -vf \"drawtext=fontfile='" + fontDir + fontName +"':textfile='" + directory + fileTitle + "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize=" + titleFontSize + ":fontcolor=" + titleFontColour + "\" -t " + titleDuration + " -y text.mp4";
						String cmd3 = "avconv -ss 0 -i text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file1.ts ; avconv -ss " + titleDuration + " -i cc.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts; avconv -i concat:\"file1.ts|file2.ts\" -c copy -bsf:a aac_adtstoasc -y title.mp4";
						builder = new ProcessBuilder("/bin/bash", "-c",
								cmd2 + ";" + cmd3);
					}
					
					else if (!fileTitle.exists() && fileCredit.exists()){
						int time = (int) (mediaVideo.getLength()/1000 - Integer.parseInt(creditDuration));
					
						String startTime = "" + time;
						
						String cmd2 = "avconv -ss " + startTime + " -i cc.mp4 -strict experimental -vf \"drawtext=fontfile='" + fontDir + fontName +"':textfile='" + directory + fileCredit + "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize=" + titleFontSize + ":fontcolor=" + titleFontColour + "\" -t " + creditDuration + " -y text1.mp4";
						String cmd3 = "avconv -ss 0 -i cc.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -t " + startTime + " -y file1.ts ; avconv -ss 0 -i text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts; avconv -i concat:\"file1.ts|file2.ts\" -c copy -bsf:a aac_adtstoasc -y credit.mp4";
						builder = new ProcessBuilder("/bin/bash", "-c",
								cmd2 + ";" + cmd3);
					}
					
					
					else {
						int time = (int) (mediaVideo.getLength()/1000 - Integer.parseInt(creditDuration));
						int time1 = time - Integer.parseInt(titleDuration);
			
						String startTime = ""+ time;
						String stopTime = ""+ time1;
						String cmd2 = "avconv -ss 0 -i cc.mp4 -strict experimental -vf \"drawtext=fontfile='" + fontDir + fontName +"':textfile='" + directory + fileTitle + "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize=" + titleFontSize + ":fontcolor=" + titleFontColour + "\" -t " + titleDuration + " -y text.mp4";
						String cmd3 = "avconv -ss " + startTime + " -i cc.mp4 -strict experimental -vf \"drawtext=fontfile='" + fontDir + fontName +"':textfile='" + directory + fileCredit + "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize=" + titleFontSize + ":fontcolor=" + titleFontColour + "\" -t " + creditDuration + " -y text1.mp4";
						String cmd4 = "avconv -ss 0 -i text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file3.ts ; avconv -ss " + titleDuration + " -i cc.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y -t " + stopTime + " file5.ts; avconv -ss 0 -i text1.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file6.ts ; avconv -i concat:\"file3.ts|file5.ts|file6.ts\" -c copy -bsf:a aac_adtstoasc -y both.mp4";

						builder = new ProcessBuilder("/bin/bash", "-c",
								cmd2 + ";" + cmd3 + ";" + cmd4);
						
						
					}
					
					
					
					
				}

				else {		


					//create video from image
					String cmd = "avconv -loop 1 -shortest -y -i " + directory
							+ "/out.png -t 10 -y " + directory + "/result.mp4";

					//add text
					String cmd2 = "avconv -i result.mp4 -vf \"drawtext=fontfile='" + fontDir + fontName +"':textfile='" + directory + textFile + "':x=(main_w-text_w)/3:y=(main_h-text_h)/2:fontsize=" + titleFontSize + ":fontcolor=" + titleFontColour + "\" -an -y text.mp4";

					//append
					String cmd3 = "avconv -ss 0 -i text.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file1.ts ; avconv -ss 0 -i wild.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts; avconv -i concat:\"file2.ts|file1.ts\" -c copy -bsf:a aac_adtstoasc -y full.mp4";

					builder = new ProcessBuilder("/bin/bash", "-c",
							cmd + ";" + cmd2 + ";" + cmd3);

				}

				process = builder.start();

				// using bufferedReader to read the ErrorStream
				InputStream err = process.getErrorStream();
				BufferedReader stderr = new BufferedReader(
						new InputStreamReader(err));

				String line = null;

				// read the error stream and publish the progress of the
				// download unless it was canceled.
				while ((line = stderr.readLine()) != null) {
					if (!this.isCancelled()) {
						publish(line);
					} else {
						process.destroy();
						return null;
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;

		}

		// Override done() to perform specific functionalities when
		// doInBackground has finished
		@Override
		protected void done() {

			if (this.isCancelled()) {

				// if it was cancelled, destroy the process and tell the user
				// that their download was canceled
				process.destroy();
				JOptionPane.showMessageDialog(null, "Download was Cancelled");

				// refresh the download tab
				refreshDownload();

			} else {

				// if it was not cancelled, read the exit status of the process
				// and inform accordingly to the user
				int exitStatus = process.exitValue();

				// download was successful
				if (exitStatus == 0) {
					JOptionPane.showMessageDialog(null,
							"TextEdit was Successful!");

				}

				else {
					JOptionPane.showMessageDialog(null, "ERROR");
				}

				// refresh the download tab
				refreshDownload();

			}
		}

		// Override process() to update the progressBar using the intermediate
		// values

	}

	/**
	 * actionPerformed method responds to all the actions done by the user on
	 * the GUI
	 */
	public void actionPerformed(ActionEvent e) {
		int fileExistsResponse = -1;

		if (e.getSource() == saveButton) {
			
			
			
			//check the screen type to select the corresponding text file
			if (screenType == "Title Screen") {
				textFile = new File("TitleText.txt");

				//get the duration for title screen
				titleDuration = addDuration.getText();

			} else {
				
				textFile = new File("CreditText.txt");

				//get the duration for title screen
				creditDuration = addDuration.getText();
			}

			//check whether the file exists
			if (textFile.exists()) {

				//if the file does exist, inform the user that they have previously created a screen
				while (fileExistsResponse == JOptionPane.CLOSED_OPTION) {
					Object[] options = { "Override it",
					"Keep original settings" };
					fileExistsResponse = JOptionPane
							.showOptionDialog(
									null,
									"Chosen screen already created. Are you sure you want to make changes?",
									"Screen Exists",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[1]);

					//override option
					if (fileExistsResponse == 0) {
						FileWriter fw;
						try {
							fw = new FileWriter(textFile, false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter x = new PrintWriter(bw);
							x.println(addTextArea.getText());
							bw.close();

						} catch (IOException e1) {

							e1.printStackTrace();
						}


					}

					//keep existing option
					else {
						//refresh
					}
				}


			}

			// If the file does not exist create a new file, and append the
			// addTextArea text into the file
			else {
				
				
				FileWriter fw;
				try {
					
					System.out.println("im here ");
					System.out.println(textFile);
					
					
					fw = new FileWriter(textFile, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter x = new PrintWriter(bw);
					x.println(addTextArea.getText());
					bw.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}

			setSettings();

			//saveButton.setEnabled(false);
		
		}else if (e.getSource() == prevBtn){
			commenceTextEdit();
			
		} else if (e.getSource() == screenList) {
			screenType = screenList.getSelectedItem().toString();

		} else if (e.getSource() == fontsList) {
			titleFontType = fontsList.getSelectedIndex();

		} else if (e.getSource() == stylesList) {
			titleFontStyle = stylesList.getSelectedIndex();

		} else if (e.getSource() == sizesList) {
			titleFontSize = Integer.parseInt(sizesList.getSelectedItem().toString());

		} else if (e.getSource() == coloursList) {
			titleFontColour = coloursList.getSelectedItem().toString();

		}

	}

	// CommenceTextEdit method executes longTask
	public void commenceTextEdit() {
		addTextArea.setFont(new Font("TimesRoman", 0, titleFontSize)); // ASK CHAHAT
		// WHERE TO ADD
		// THIS
		longTask = new BackgroundTask();
		longTask.execute();
	}

	// RefreshDownload method sets the GUI of the download tab to the default
	// (starting point)
	public void refreshDownload() {

		saveButton.setEnabled(false);

	}

	public void setSettings() {

		StringBuilder font = new StringBuilder();
		if (titleFontType == 0){
			font.append("Arial");
		}
		else if (titleFontType == 1){
			font.append("Courier_New");
		}
		else if (titleFontType == 2){
			font.append("Georgia");
		}
		else if (titleFontType == 2){
			font.append("Times_New_Roman");
		}
		else{
			font.append("Verdana");
		}

		if (titleFontStyle == 1){
			font.append("_Bold");
		}
		else if (titleFontStyle == 2){
			font.append("_Italic");
		}
		else if (titleFontStyle == 3){
			font.append("_Bold_Italic");
		}

		font.append(".ttf");
		fontName = font.toString();



	}

}
