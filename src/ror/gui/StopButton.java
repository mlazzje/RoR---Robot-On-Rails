package ror.gui;

import java.awt.Image;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 * StopButton class
 * Represent the Stop Button
 */
public class StopButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the StopButton class
	 */
	public StopButton() {
		super();
		ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/stop.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().stopSimulation();
			
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
