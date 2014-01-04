package ror.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * SpeedSlider class
 * Represent the Speed Slider
 */
public class SpeedSlider extends JSlider implements ChangeListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the SpeedSlider class
	 */
	public SpeedSlider() {
		super();
		this.setSnapToTicks(true);
		this.setMaximum(1500);
		this.setMinimum(0);
		this.setValue(0);
		this.setPaintTicks(true);
		this.setMajorTickSpacing(150);
		this.addChangeListener(this);
	}

	/**
	 * Function called when state changed
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setSpeed((float) this.getValue());
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}
}
