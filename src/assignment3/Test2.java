package assignment3;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Test2 extends JPanel {
	
	 private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	 final EmbeddedMediaPlayer video;
	public static void main(String[] args) {


		JFrame myFrame = new JFrame("Test2");
		myFrame.setLocation(100,100);
		myFrame.setSize(500,500);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Test2 myTest2 = new Test2();
		
		myFrame.setContentPane(myTest2);
		
		myFrame.setVisible(true);
		myTest2.video.playMedia("/afs/ec.auckland.ac.nz/users/z/a/zall747/unixhome/Documents/softeng206/assignment3/cc.mp4");
		//myTest2.myEMPC.getMediaPlayer().playMedia(args[0]);
	}

	//EmbeddedMediaPlayerComponent myEMPC;
	
	public Test2() {
		this.setLayout(new GridBagLayout());
		JButton rB = new JButton("Right");
		JButton lB = new JButton("Left");
		JButton bB = new JButton("Bottom");
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();       
		video = mediaPlayerComponent.getMediaPlayer();
        //mediaPlayerComponent.setPreferredSize(new Dimension(500, 500));
      
       
		
		GridBagConstraints lBGBC = new GridBagConstraints();
		lBGBC.gridx = 0;
		lBGBC.gridy = 0;
		lBGBC.weightx = 1;
		lBGBC.weighty = 2;
		lBGBC.fill = GridBagConstraints.BOTH;
		lBGBC.insets = new Insets(10, 10, 10, 10);
		//lBGBC.anchor = GridBagConstraints.EAST;
		

		GridBagConstraints rBGBC = new GridBagConstraints();
		rBGBC.gridx = 1;
		rBGBC.gridy = 0;
		rBGBC.weightx = 1;
		rBGBC.gridheight = 2;
		rBGBC.fill = GridBagConstraints.BOTH;
		//rBGBC.anchor = GridBagConstraints.SOUTH;
		
		GridBagConstraints bBGBC = new GridBagConstraints();
		bBGBC.gridx = 0;
		bBGBC.gridy = 1;
		bBGBC.weighty = 0;
		bBGBC.fill = GridBagConstraints.BOTH;
		//bBGBC.anchor = GridBagConstraints.NORTHEAST;
		//bBGBC.gridwidth = 2;
		
		
		this.add(rB, rBGBC);
		this.add(mediaPlayerComponent, lBGBC);
		this.add(bB, bBGBC);
		
		
	}
}
