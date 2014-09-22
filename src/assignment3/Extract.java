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
import java.io.File;
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


public class Extract extends JPanel implements ActionListener {

	private final String TEXT_EXTRACT = "Extract";
	private final String TEXT_CHOOSE = "Choose ...";

	private JButton extractBt = new JButton(TEXT_EXTRACT);
	private JButton chooserBt = new JButton(TEXT_CHOOSE);

	private FileNameExtensionFilter filter = new FileNameExtensionFilter( "mp3 & mp4 Clips", "mp3", "mp4");

	private JLabel extractLabel = new JLabel("Extract");

	private JLabel inputlabel = new JLabel("Choose the file you want to extract:");
	private JLabel startTimelabel = new JLabel("Start time: ");
	private JLabel lengthlabel = new JLabel("Length to keep: ");
	private JLabel outputlabel = new JLabel("Output file name: ");
	private JLabel mp3 = new JLabel(".mp3");

	private Font defaultFont = new Font("TimesRoman", Font.PLAIN, 12);
	private Font btFont = new Font("TimesRoman", Font.BOLD, 12);
	
	JLabel background = new JLabel(new ImageIcon("img.jpg"));
	Image bgimage;

	private JLabel separator = new JLabel("");
	private JLabel separator2 = new JLabel("");
	private JLabel separator3 = new JLabel("");

	private JTextField startTime = new JTextField();
	private JTextField length = new JTextField();
	private JTextField outputName = new JTextField();

	private JFileChooser chooser = new JFileChooser();
	private File inputFile;

	/**
	 * Set the GUI
	 */
	public Extract() {

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
		extractLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		add(extractLabel);

		//Add blank space
		separator.setPreferredSize(new Dimension(500, 60));
		add(separator);

		//Set the input label
		inputlabel.setFont(defaultFont);
		add(inputlabel);

		//Set the file choosing button
		chooserBt.addActionListener(this);
		chooserBt.setFont(btFont);
		chooserBt.setPreferredSize(new Dimension(100, 20));
		add(chooserBt);

		//Add blank space
		separator2.setPreferredSize(new Dimension(500, 1));
		add(separator2);

		//Set the start time label and text field
		startTimelabel.setFont(defaultFont);
		add(startTimelabel);

		startTime.setPreferredSize(new Dimension(100, 25));
		startTime.setText("hh:mm:ss");
		add(startTime);

		//Set the length label and text field
		lengthlabel.setFont(defaultFont);
		add(lengthlabel);

		length.setPreferredSize(new Dimension(100, 25));
		length.setText("hh:mm:ss");
		add(length);

		//Set the output name label and text field
		outputName.setPreferredSize(new Dimension(200, 25));
		outputlabel.setFont(defaultFont);
		add(outputlabel);

		add(outputName);
		outputName.setPreferredSize(new Dimension(230, 25));
		mp3.setFont(defaultFont);
		add(mp3);

		//Add blank space
		separator3.setPreferredSize(new Dimension(500, 10));
		add(separator3);

		//Set t he extract button
		extractBt.addActionListener(this);
		extractBt.setFont(btFont);
		extractBt.setPreferredSize(new Dimension(150, 25));
		add(extractBt);

	}
	
	/**
	 * Paint the background image
	 */
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	  }

	/**
	 * ExtractTask do the extracting in a background thread
	 */
	class ExtractTask extends SwingWorker<Void, String> {

		private String _inputFileName;
		private String _startTime;
		private String _length;
		private String _outputFileName;

		private Process process;

		//Get the arguments necessary for extracting
		public ExtractTask (String inputFileName, String startTime, String length, String outputFileName ) {
			_inputFileName = inputFileName;
			_startTime = startTime;
			_length = length;
			_outputFileName = outputFileName;
		}

		//Do the extraction in background
		@Override
		protected Void doInBackground() throws Exception {	

			try {

				ProcessBuilder builder = new ProcessBuilder("avconv","-i", _inputFileName, "-acodec",
						"copy", "-ss", _startTime, "-t", _length, _outputFileName );

				process = builder.start();
				process.waitFor();

			} catch(Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}


		@Override
		protected void done() {
			//Check if the download process completed successfully or not
			if (process.exitValue() == 0) {

				JOptionPane.showMessageDialog(null, "Extraction Successful!");
				//Store the extract operation in the log
				Date date = new Date();
				Log.appendToLog ("Extract", date);

			} else {
				JOptionPane.showMessageDialog(null, "Sorry, error encountered!");
			}

			inputFile = null;
			startTime.setText("hh:mm:ss");
			length.setText("hh:mm:ss");
			outputName.setText("");
			extractBt.setEnabled(true);

		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == chooserBt ) {

			chooser.setFileFilter(filter); //Show only correct extension type file by default
			int returnVal = chooser.showOpenDialog(null);

			//Store the chosen file
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = chooser.getSelectedFile();	
			}

		} else {

			//Create number formatting pattern
			Pattern timePattern = Pattern.compile("(\\d{2})\\:(\\d{2})\\:(\\d{2})");	

			if (inputFile == null) { //Check the user had chosen an input file
				JOptionPane.showMessageDialog(null, "Please choose a file to extract!");
				return;

			} else if ( (startTime.getText().equals("hh:mm:ss")) //Check text fields are not empty
					|| (length.getText().equals("hh:mm:ss"))
					|| (outputName.getText().length() == 0) ) {
				JOptionPane.showMessageDialog(null, "Please fill all the text fields.");
				return;

			} else { //Check numerical inputs are in the correct format
				Matcher m1 = timePattern.matcher(startTime.getText());
				Matcher m2 = timePattern.matcher(length.getText());
				if (! m1.matches()) {
					JOptionPane.showMessageDialog(null, "Please enter the start time in the correct format (hh:mm:ss:)");
					return;
				} else if (! m2.matches()) {
					JOptionPane.showMessageDialog(null, "Please enter the required length in the correct format (hh:mm:ss)");
					return;
				}
			}

			//Check input file type
			String inputFileName = inputFile.getName();
			ProcessBuilder builder = new ProcessBuilder("file", "-b", "--mime-type", inputFileName);
			builder.redirectErrorStream(true);
			try {
				Process process = builder.start();
				//read the output of the process
				InputStream outStr = process.getInputStream();
				BufferedReader stdout = new BufferedReader(new InputStreamReader(outStr));

				//If the file is not a valid type, show error message
				if (! stdout.readLine().equals("audio/mpeg")) { 
					JOptionPane.showMessageDialog(null, "The file has invalid type, please try again");
					return;
				}

			} catch (IOException e1) {
				e1.printStackTrace();
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
			String chosenDir = dirChooser.getSelectedFile().getPath();

			//Check that the output file doesn't exists in the specified directory
			String outputFileName = chosenDir+"/"+outputName.getText()+".mp3";
			File f = new File(outputFileName);

			if ( f.exists()) {
				//Allow user to choose either overwriting the current file or change the output file name
				Object[] existOptions = {"Cancel", "Overwrite"};
				int optionChosen = JOptionPane.showOptionDialog(null, "File already exists. " +
						"Do you want to overwrite the existing file?",
						"File Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null,existOptions, existOptions[0]);

				if (optionChosen == 1) {
					f.delete(); //Delete the existing file
				} else {
					outputName.setText("");
					return;
				}
			}

			//Do the extraction in background thread
			ExtractTask extract = new ExtractTask(inputFileName, startTime.getText(), length.getText(), outputFileName );
			extract.execute();
			extractBt.setEnabled(false);
		}

	}

}