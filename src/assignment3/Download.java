package assignment3;

import java.awt.Color;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Download extends JPanel implements ActionListener {

	private final String TEXT_DOWNLOAD = "Download";
	private final String TEXT_CANCEL = "Cancel";

	private JButton downloadBt = new JButton(TEXT_DOWNLOAD);
	private JButton cancelBt = new JButton(TEXT_CANCEL);

	private JLabel downloadLabel = new JLabel("Download");

	private JLabel label = new JLabel("Please enter a url: ");
	private JTextField url = new JTextField();
	
	private final JProgressBar progressBar = new JProgressBar(0, 100);
	private String percentageCompleted;
	
	private JLabel separator = new JLabel("");
	private JLabel separator2 = new JLabel("");
	
	private Font defaultFont = new Font("TimesRoman", Font.PLAIN, 12);
	private Font btFont = new Font("TimesRoman", Font.BOLD, 12);
	
	JLabel background = new JLabel(new ImageIcon("img.jpg"));
	Image bgimage;

	private DownloadTask download;
	private File chosenFile;

	/**
	 * Set the GUI
	 */
	public Download() {

		//Set the background image
		MediaTracker mt = new MediaTracker(this);
	    bgimage = Toolkit.getDefaultToolkit().getImage("img.jpg");
	    mt.addImage(bgimage, 0);
	    try {
	      mt.waitForAll();
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	    
		//Set the download label
		downloadLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		add(downloadLabel);
		
		//Add blank space
		separator.setPreferredSize(new Dimension(500, 20));
		add(separator);
		
		//Create the user input area
		label.setFont(defaultFont);
		add(label);
		
		url.setPreferredSize(new Dimension(300, 25));
		add(url);
		
		//Create the progress bar
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(420, 20));
		progressBar.setValue(0);
		add(progressBar);
		
		//Add blank space
		separator2.setPreferredSize(new Dimension(500, 1));
		add(separator2);

		//Create the buttons
		downloadBt.addActionListener(this);
		downloadBt.setFont(btFont);
		downloadBt.setPreferredSize(new Dimension(150, 25));
		add(downloadBt);

		cancelBt.addActionListener(this);
		cancelBt.setFont(btFont);
		cancelBt.setPreferredSize(new Dimension(150, 25));
		cancelBt.setEnabled(false);
		add(cancelBt);
		
	}
	
	/**
	 * Paint the background image
	 */
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	  }

	/**
	 * DownloadTask do the downloading in a background thread
	 */
	class DownloadTask extends SwingWorker<String, String> {
		
		private String url;
		private String chosenDir;
		
		//Get the url and the destination directory from ED thread
		public DownloadTask (String url, String chosenDir) {
			this.url = url;
			this.chosenDir = chosenDir;
		}

		private Process process;

		@Override
		protected String doInBackground() throws Exception {	

			try {

				//Download from the given url to the specified directory
				ProcessBuilder builder = new ProcessBuilder("wget","--progress=bar:force", "-c", url, "-P", chosenDir);
				process = builder.start();
				
				//send the intermediate results (to update progress bar)
				InputStream error = process.getErrorStream();
				BufferedReader stderr = new BufferedReader(new InputStreamReader(error));

				String line = null;
				while ((line = stderr.readLine()) != null ) {
					if (!isCancelled()) {
						publish(line);
					} else {
						process.destroy();
						return null;
					}
				}

			} catch(Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		/**
		 * process updates the progress bar
		 */
		@Override
		protected void process(List<String> chunks) {
			if (!isCancelled()) {
				for (String s : chunks) {
					Pattern p = Pattern.compile("(\\d*)\\%");
					Matcher m = p.matcher(s);

					while (m.find()) {
						percentageCompleted = m.group(1);
						progressBar.setValue(Integer.parseInt(percentageCompleted));
					}

				}
			}
		}

		@Override
		protected void done() {

			//Check if the download process completed successfully or not
			if (!isCancelled()) {
				if (process.exitValue() == 0) {
					JOptionPane.showMessageDialog(null, "Download Successful!");
					
				//Generate error messages
				} else if (process.exitValue() == 1) {
					JOptionPane.showMessageDialog(null, "Generic error code!");
					
				} else if (process.exitValue() == 2) { 
					JOptionPane.showMessageDialog(null, "Parse error occured!");
					
				} else if (process.exitValue() == 3) { 
					JOptionPane.showMessageDialog(null, "File input/output error occured!");
					
				} else if (process.exitValue() == 4) { 
					JOptionPane.showMessageDialog(null, "Error: Network failure!");
					
				} else if (process.exitValue() == 5) {
					JOptionPane.showMessageDialog(null, "Error: Secure Sockets Layer (SSL) verification failure!");
					
				} else if (process.exitValue() == 6) { 
					JOptionPane.showMessageDialog(null, "Error: Username/password authentication failure!");
					
				} else if (process.exitValue() ==7) { 
					JOptionPane.showMessageDialog(null, "Error: Protocolerrors occured!");
					
				} else if (process.exitValue() == 8) {
					JOptionPane.showMessageDialog(null, "Sorry, server issued an error response!");
				} else {
					JOptionPane.showMessageDialog(null, "Sorry, error encountered!");
				}
			} else {
				process.destroy();
				JOptionPane.showMessageDialog(null, "Downloading was cancelled");
			}

			//Go back to initial state
			progressBar.setValue(0);
			downloadBt.setEnabled(true);
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == downloadBt ) {

			downloadBt.setEnabled(false);
			String inputURL = url.getText();

			//Check a url was inserted
			if (inputURL.length() == 0) {
				JOptionPane.showMessageDialog(null, "Please enter a url to download from");
				downloadBt.setEnabled(true);
				return;
			}
			
			//Allow the user to choose the output file destination
			JFileChooser dirChooser = new JFileChooser();  
			dirChooser.setDialogTitle("Save to ...");
			dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	
			int returnVal = dirChooser.showSaveDialog(null);
			
			//If no directory was selected, don't download!
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				downloadBt.setEnabled(true);
				return;
			}
			//Get the name of the chosen directory 
			String chosenDir = dirChooser.getSelectedFile().getPath();

			//Get the file name from the url
			File f = new File(inputURL);
			String fileName = f.getName();

			//Check if the file exists locally
			chosenFile = new File(chosenDir+"/"+fileName);
			if ( chosenFile.exists()) {

				//Allow user to choose either resuming the download or overwriting the current file
				Object[] existOptions = {"Proceed", "Overwrite"};
				int optionChosen = JOptionPane.showOptionDialog(null, "File already exists. " +
						"Do you want to proceed downloading or overwrite the existing file?",
						"File Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null,existOptions, existOptions[0]);

				if (optionChosen == 1) {
					chosenFile.delete(); //Delete the existing file
				} else if (optionChosen == -1) {
					downloadBt.setEnabled(true);
					return;
				}
			}

			//Check a url is open source
			Object[] OSoptions = {"Yes, it is.", "No, it is not"};
			int optionChosen = JOptionPane.showOptionDialog(null, "Please confirm that the file is open source.",
					"Confirm open source!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null,OSoptions, OSoptions[0]);
			if (optionChosen == 1 || optionChosen == -1) {
				JOptionPane.showMessageDialog(null, "Sorry, file must be an open source");
				downloadBt.setEnabled(true);
				url.setText("");
				return;
			}

			//Start downloading in background thread
			download = new DownloadTask(inputURL, chosenDir);
			download.execute();
			cancelBt.setEnabled(true);
			url.setText("");

		} else { //Cancel button is pressed
			//Cancel the download process
			if (download != null) {
				download.cancel(false);
			}
		}

	}
	
	public String getChosenFile() {
		if (chosenFile != null)  {
			return chosenFile.toString();
		} else { 
			return "No file yet";
		}
	}
}