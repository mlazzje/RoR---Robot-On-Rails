package ror.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImportButton extends JButton {

    public ImportButton()
    {
	super();
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/import.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.setIcon(icon);
    }
}
