package ror.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class RobotComboBox extends JComboBox {

    public RobotComboBox() {
	super();
	String[] algStore = { "1", "2", "3" };
	DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
	this.setModel(algModel);
	//TODO Faire en reflexif
    }
}
