package ror.gui;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class ImportButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	public ImportButton() {
		super();
		ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/import.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.addMouseListener(this);
	}

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
