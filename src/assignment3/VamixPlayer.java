package assignment3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import assignment3.Download;

public class VamixPlayer implements ActionListener, ChangeListener {

	/**
	 * Main method that runs the Vamix player
	 * @param args
	 */
	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VamixPlayer();
			}
		});

	}
	
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private JButton fastFwdBtn = new JButton(">>");
	private JButton backFwdBtn = new JButton("<<");
	private JButton playBtn = new JButton("play");
	private JButton stopBtn = new JButton("stop");
	private JButton muteBtn = new JButton("mute");
	//private JButton fullScreenBtn = new JButton("full screen");
	private JLabel timeDisplay = new JLabel("0 Seconds");
	private JSlider volume = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
	private JSlider videoTimeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
	private JFrame frame;
	private String inputVideo;

	protected EmbeddedMediaPlayer video;
	private SkipTask longTask;
	private Download download;
	private JFileChooser chooser = new JFileChooser();
	private FileNameExtensionFilter filter = new FileNameExtensionFilter( "mp3 & mp4 Clips", "mp3", "mp4");
	private File inputFile;
	
	private JMenuBar menuBar;
	private JMenu fileMenu, submenu, helpMenu;
	private JMenuItem newProj, openProj, fromURL, fromFolder, export, exit, report, about;
	
	private VamixPlayer() {
		
		frame = new JFrame("VAMIX player");
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the file menu.
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		//Create a group of JMenuItems
		newProj = new JMenuItem("New project");
		fileMenu.add(newProj);

		openProj = new JMenuItem("Open project", new ImageIcon("images/middle.gif"));
		fileMenu.add(openProj);

		//Create a submenu in the file menu
		fileMenu.addSeparator();
		submenu = new JMenu("Import video");

		fromURL = new JMenuItem("Download from URL");
		submenu.add(fromURL);

		fromFolder = new JMenuItem("Import from computer");
		submenu.add(fromFolder);
		fileMenu.add(submenu);
	//	submenu.setEnabled(false);
		
		export = new JMenuItem("Export video");
		fileMenu.add(export);
		//export.setEnabled(false);


		//Create Exit JMenuItem
		fileMenu.addSeparator();
		exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		
		menuBar.add(fileMenu);
		
		//Build second menu in the menu bar.
		helpMenu = new JMenu("Help");
		
		report = new JMenuItem("Report issue");
		helpMenu.add(report);

		about = new JMenuItem("About Vamix");
		helpMenu.add(about);
		
		menuBar.add(helpMenu);
		
		//--------------------------------------------
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setPreferredSize(new Dimension(525, 400));
		video = mediaPlayerComponent.getMediaPlayer();

		//Turn on the mark lines of the volume bar
		volume.setMajorTickSpacing(20);
		volume.setMinorTickSpacing(1);
		volume.setPaintTicks(true);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel timeBar = new JPanel();
		timeBar.add(videoTimeSlider);
		timeBar.add(timeDisplay);
		
		JPanel toolBar = new JPanel();
		//toolBar.add(videoTimeSlider);
		toolBar.add(backFwdBtn);
		toolBar.add(stopBtn);
		toolBar.add(playBtn);
		toolBar.add(fastFwdBtn);
		toolBar.add(muteBtn);
		//toolBar.add(fullScreenBtn);
		toolBar.add(volume);
		
		
		// adding action listeners to all the buttons so when a user clicks a
		// button, the corresponding actions are done.
		backFwdBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		playBtn.addActionListener(this);
		fastFwdBtn.addActionListener(this);
		muteBtn.addActionListener(this);
		//fullScreenBtn.addActionListener(this);
		volume.addChangeListener(this);
		videoTimeSlider.addChangeListener(this);
		
		newProj.addActionListener(this);
		openProj.addActionListener(this);
		fromURL.addActionListener(this);
		fromFolder.addActionListener(this);
		export.addActionListener(this);
		exit.addActionListener(this);
		report.addActionListener(this);
		about.addActionListener(this);
		
		timeDisplay.setPreferredSize(new Dimension(100,20));
		playBtn.setPreferredSize(new Dimension(80,25));
		volume.setPreferredSize(new Dimension(120,40));
		videoTimeSlider.setPreferredSize(new Dimension(425,20));
		
		videoTimeSlider.setEnabled(false);

		JPanel videoPanel = new JPanel(new BorderLayout());
		videoPanel.add(mediaPlayerComponent, BorderLayout.NORTH);
		videoPanel.add(timeBar, BorderLayout.CENTER);
		videoPanel.add(toolBar, BorderLayout.SOUTH);
				
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(525, 440));
		tabbedPane.add("Audio Manipulater", new AudioManipulater(video));
		tabbedPane.add("Text Editor", new TextEditor(video));

//		JPanel framesPanel = new JPanel(new BorderLayout());
//		framesPanel.setBounds(0, 400, 1050, 300);

		
		GridBagConstraints lBGBC = new GridBagConstraints();
		lBGBC.gridx = 0;
		lBGBC.gridy = 0;
		lBGBC.weightx = 0.5;
		lBGBC.weighty = 1;
		// lBGBC.fill = GridBagConstraints.BOTH;
		// lBGBC.insets = new Insets(10, 10, 10, 10);
		// lBGBC.anchor = GridBagConstraints.EAST;

		GridBagConstraints rBGBC = new GridBagConstraints();
		rBGBC.gridx = 1;
		rBGBC.gridy = 0;
		rBGBC.weightx = 0;
		//rBGBC.weighty = 0;

		// rBGBC.weighty = 1;
		rBGBC.gridheight = 2;
		// rBGBC.fill = GridBagConstraints.BOTH;
		// rBGBC.anchor = GridBagConstraints.SOUTH;

		GridBagConstraints bBGBC = new GridBagConstraints();
		bBGBC.gridx = 0;
		bBGBC.gridy = 1;
		bBGBC.weighty = 0;
		bBGBC.weightx = 1;
		// bBGBC.fill = GridBagConstraints.BOTH;
		// bBGBC.anchor = GridBagConstraints.NORTHEAST;
		// bBGBC.gridwidth = 2;
		
	
		panel.add(videoPanel, BorderLayout.WEST);
		panel.add(tabbedPane, BorderLayout.EAST);

		frame.setJMenuBar(menuBar);
		frame.setContentPane(panel);

		frame.setLocation(100, 100);
		frame.setSize(1066, 508);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		//Set the volume values back to default when the user closes the Vamix player
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				video.mute(false);
				video.setVolume(100);
			}
		});
		
		//Set the video playing and the video imported by user to null
		//to start the window with fresh inputs next time.
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent windowEvent) {
				inputVideo = null;
				inputFile = null;
			}
		});


	}
	
	
	/**
	 * SkipTask class rewinds or forwards the video in the 
	 * worker thread.
	 */
	class SkipTask extends SwingWorker<Void, Void> {

		private int skipSpeed;

		public SkipTask(int time) {
			this.skipSpeed = time;
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				while (!isCancelled()) {
					video.skip(skipSpeed);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
	

	@Override
	public void stateChanged(ChangeEvent e) {
		if (video.isPlayable()) {
			
			if (e.getSource() == volume) {
				video.setVolume(volume.getValue());
				
			} else {
				mediaPlayerComponent.positionChanged(video, (video.getLength()*(videoTimeSlider.getValue()/100)));
				mediaPlayerComponent.seekableChanged(video, (int) (video.getLength()*(videoTimeSlider.getValue()/100)));
				//seekableChanged(video, (int)(video.getLength()*(videoTimeSlider.getValue()/100)));
				//video.skipPosition(video.getLength()*(videoTimeSlider.getValue()/100));
				System.out.println(videoTimeSlider.getValue());
				System.out.println(video.getLength());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == muteBtn) {

			if (video.isMute()) { // Un-mute the video
				video.mute(false);
				muteBtn.setText("mute");

			} else { // mute the video
				video.mute(true);
				muteBtn.setText("unmute");
			}
			
		} else if (e.getSource() == stopBtn) {
			video.stop();
			timeDisplay.setText(" 0 Seconds");
			playBtn.setText("play");

		} else if (e.getSource() == playBtn) {
			
			if (longTask != null && !longTask.isDone()) {
				longTask.cancel(false);
				video.play();
				playBtn.setText("pause");

			} else if (video.isPlaying()) {	
				video.pause();
				playBtn.setText("play");

			} else {
				if (!video.isPlayable()) {
					//------ TEMPERORY--------------------------
//					video.playMedia("/afs/ec.auckland.ac.nz/users/c/c/ccha504/unixhome/Documents/206/assignment3/cc.mp4");
//					playBtn.setText("pause");
//				// Setting the timer
//				Timer tempticker = new Timer(200, new ActionListener() {
//
//						@Override
//						public void actionPerformed(ActionEvent e) {
//
//							int time = (int) (video.getTime() / 1000);
//							if (time < video.getLength() / 1000) {
//								timeDisplay.setText(" " + time + " Seconds");
//							}
//
//						}
//
//					});
//
//					tempticker.start();
					//---------------------------------------------
					
					if (download != null || inputFile != null) {
						if (download != null) { //Store the inputVideo that was downloaded
							String chosenFile = download.getChosenFile();
							if (!chosenFile.equals("No file yet")) {
								inputVideo = chosenFile;
								videoTimeSlider.setEnabled(true);
								video.playMedia(inputVideo);
								//Create a txt file with all the info about the imported video
								playBtn.setText("pause");
							}
						}
						if (inputFile != null) {
							videoTimeSlider.setEnabled(true);
							video.playMedia(inputVideo);
							//Create a txt file with all the info about the imported video

							File videoFile = new File(inputVideo);
							System.out.println(videoFile.getName());
							System.out.println(videoFile.getPath());
							System.out.println(videoFile.getParent());
							playBtn.setText("pause");
							
						}
						

						//Setting the timer
						Timer ticker = new Timer(200, new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								
								int time = (int) (video.getTime() / 1000);
								if (time < video.getLength() / 1000) {
									timeDisplay.setText(time + " Seconds");
								} else {
									playBtn.setText("play");
								}

							}

						});
						
						ticker.start();
						
					}
					

				} else {
					video.play();
					System.out.println(video.getLength());
					playBtn.setText("pause");
				}
			}
			
		} else if (e.getSource() == fastFwdBtn) {

			if (longTask != null && !longTask.isDone()) {
				longTask.cancel(false);
			}
			longTask = new SkipTask(10);
			longTask.execute();
			System.out.println(video.getLength());
			playBtn.setText("play");

		} else if (e.getSource() == backFwdBtn) {

			if (longTask != null && !longTask.isDone()) {
				longTask.cancel(false);
			}

			longTask = new SkipTask(-10);
			longTask.execute();
			playBtn.setText("play");
			
		}
	

		//Handle Menu Bar events
		if (e.getSource() == newProj) {

			//Allow the user to choose the output file destination
			JFileChooser dirChooser = new JFileChooser();  
			dirChooser.setDialogTitle("Save to ...");
			dirChooser.setFileSelectionMode(JFileChooser.CANCEL_OPTION);
			//dirChooser.setAcceptAllFileFilterUsed(false);

			int returnVal = dirChooser.showSaveDialog(null);

			//If no directory was selected, don't extract!
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			//Get the name of the chosen directory 
			String chosenDir = dirChooser.getSelectedFile().getPath();
			
		} else if (e.getSource() == openProj) {

		//If user chooses to import video by downloading it from web 
		} else if (e.getSource() == fromURL) {
			
			//Create a new download frame
			JFrame downloadFrame = new JFrame("Download");
			
			download = new Download();
			downloadFrame.setContentPane(download);
			downloadFrame.setLocation(300, 300);
			downloadFrame.setSize(500, 200);
			downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			downloadFrame.setVisible(true);
		
		//If user chooses to import video from local folder 
		} else if (e.getSource() == fromFolder) {
			
			chooser.setFileFilter(filter); //Show only correct extension type file by default
			int returnVal = chooser.showOpenDialog(null);

			//Store the chosen file as an input video
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = chooser.getSelectedFile();
				inputVideo = inputFile.toString();
			}

		} else if (e.getSource() == export) {

		} else if (e.getSource() == exit) {
			frame.dispose();
		} else if (e.getSource() == report) {

		} else if (e.getSource() == about) {

		}

	}

}