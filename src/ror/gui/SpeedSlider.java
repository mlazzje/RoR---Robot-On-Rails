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
		this.setMaximum(99);
		this.setMinimum(1);
		this.setPaintTicks(true);
		this.setMajorTickSpacing(10);
		this.addChangeListener(this);
	}

	/**
	 * Function called when state changed
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setSpeed((100F - this.getValue()) / 100F);
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}
}
