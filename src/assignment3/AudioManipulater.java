package assignment3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class AudioManipulater extends JPanel implements ActionListener {

	final private JRadioButton stripCheck = new JRadioButton(
			"Strip audio");
	final private JRadioButton replaceCheck = new JRadioButton(
			"Replace audio");
	final private JRadioButton overlayCheck = new JRadioButton(
			"Overlay on the existing audio");
	
	private EmbeddedMediaPlayer video;
	
	public AudioManipulater(EmbeddedMediaPlayer video) {
		this.video = video;
		
		stripCheck.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {

					replaceCheck.setEnabled(false);
					overlayCheck.setEnabled(false);

				} else {

					replaceCheck.setEnabled(true);
					overlayCheck.setEnabled(true);

				}

			}
		});

		replaceCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					stripCheck.setEnabled(false);
					overlayCheck.setEnabled(false);

				} else {
					stripCheck.setEnabled(true);
					overlayCheck.setEnabled(true);

				}

			}
		});

		overlayCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					stripCheck.setEnabled(false);
					replaceCheck.setEnabled(false);

				} else {
					stripCheck.setEnabled(true);
					replaceCheck.setEnabled(true);

				}

			}
		});
		
		add(stripCheck);
		add(replaceCheck);
		add(overlayCheck);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}