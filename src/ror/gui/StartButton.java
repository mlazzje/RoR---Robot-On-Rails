package ror.gui;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class StartButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;
	private Boolean isPlaying;

	public StartButton() {
		super();
		this.isPlaying = false;
		ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			if (this.isPlaying) {
				frame.getUiController().pauseSimulation();
				ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				this.setIcon(icon);
				this.isPlaying = false;
			} else {
				frame.getUiController().startSimulation();
				ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/pause.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				this.setIcon(icon);
				frame.getAlgDestockingComboBox().setEnabled(false);
				frame.getAlgStoreComboBox().setEnabled(false);
				frame.getAlgMoveComboBox().setEnabled(false);
				frame.getRobotComboBox().setEnabled(false);
				frame.getRandomCheckBox().setEnabled(false);
				frame.getImportButton().setEnabled(false);
				this.isPlaying = true;
			}
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
