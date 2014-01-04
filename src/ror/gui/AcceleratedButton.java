package ror.gui;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * AcceleratedButton class Represent the Accelerated Button
 */
public class AcceleratedButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the AcceleratedButton class
	 */
	public AcceleratedButton() {
		super();
		ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/accelerated.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.addMouseListener(this);
	}

	/**
	 * Function called when click
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setEndSimulation();
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
