package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * @author RoR
 * 
 */
public class RandomCheckBox extends JCheckBox implements ActionListener {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor RandomCheckBox
     */
    public RandomCheckBox() {
	super();
	this.setText("Mode aléatoire");
	this.addActionListener(this);
	this.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
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
