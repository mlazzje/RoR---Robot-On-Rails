package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * RandomCheckBox class
 * Represent the Random CheckBox
 */
public class RandomCheckBox extends JCheckBox implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the RandomCheckBox class
	 */
	public RandomCheckBox() {
		super();
		this.setText("Mode aléatoire");
	}
	
	/**
	 * Function called when item checked/unchecked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setRandomMode();
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}
}
