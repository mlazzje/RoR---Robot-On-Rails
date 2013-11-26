package ror.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpeedSlider extends JSlider implements ChangeListener {

    public SpeedSlider()
    {
	super();
	this.setSnapToTicks(true);
	this.setMaximum(100);
	this.setMinimum(0);
	this.setPaintTicks(true);
	this.setMajorTickSpacing(10);
	this.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	if(this.getParent().getParent().getParent().getParent() instanceof RoRFrame)
	{
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
	    frame.getUiController().setSpeed(this.getValue()/100F);
	}
	else
	{
	    System.err.println("Can't get parent RoRFrame");
	}
    }
}
