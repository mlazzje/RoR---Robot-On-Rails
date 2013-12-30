package ror.gui;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * @author RoR
 *
 */
public class ImportButton extends JButton implements MouseListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor ImportButton
	 */
	public ImportButton() {
		super();
		ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/import.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.addMouseListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			int ret = frame.getImportFileChooser().showOpenDialog(this);

			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = frame.getImportFileChooser().getSelectedFile();
				frame.getUiController().setFile(file);
				System.out.println(file);
			}
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
