package ror.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AcceleratedButton extends JButton {

    public AcceleratedButton()
    {
	super();
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/accelerated.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.setIcon(icon);
    }
}
