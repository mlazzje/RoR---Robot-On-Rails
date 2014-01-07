package ror.gui;

import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * ImportButton class Represent the import Button
 */
public class ImportButton extends JButton implements MouseListener {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the ImportButton class
     */
    public ImportButton() {
	super();
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/import.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.setIcon(icon);
	this.addMouseListener(this);
    }

    /**
     * Function called when click
     */
    public void mouseClicked(MouseEvent e) {
	if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();

	    FileDialog fd = new FileDialog(frame, "Import d'un scénario", FileDialog.LOAD);

	    fd.setVisible(true);
	    String filename = fd.getFile();

	    if (filename != null) {
		File f = fd.getFiles()[0];

		try {
		    frame.getUiController().setFile(f);
		    frame.getRandomCheckBox().setSelected(false);
		    frame.getAcceleratedButton().setEnabled(true);
		} catch (Exception e1) {
		    JOptionPane.showMessageDialog(frame, "Impossible d'ouvrir le fichier");
		}
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
