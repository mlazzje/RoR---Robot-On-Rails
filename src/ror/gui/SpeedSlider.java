package ror.gui;

import javax.swing.JSlider;

public class SpeedSlider extends JSlider {

    public SpeedSlider()
    {
	super();
	this.setSnapToTicks(true);
	this.setMaximum(100);
	this.setMinimum(0);
    }
}
