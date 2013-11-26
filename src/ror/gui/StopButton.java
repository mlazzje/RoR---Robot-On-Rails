package ror.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class StopButton extends JButton {

    public StopButton()
    {
	super();
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/stop.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.setIcon(icon);
    }
}
