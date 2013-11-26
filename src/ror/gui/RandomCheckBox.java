package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class RandomCheckBox extends JCheckBox implements ActionListener {

    public RandomCheckBox()
    {
	super();
	this.setText("Mode aléatoire");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if(this.getParent().getParent().getParent().getParent() instanceof RoRFrame)
	{
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
	    frame.getUiController().setRandomMode();
	}
	else
	{
	    System.err.println("Can't get parent RoRFrame");
	}
    }
}
