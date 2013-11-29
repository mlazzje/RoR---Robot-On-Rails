package ror.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RobotLabel extends JLabel {

    public RobotLabel() {
	super();
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/robot.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.setIcon(icon);
    }

}
