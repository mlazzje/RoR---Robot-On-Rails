package ror.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class AlgMoveComboBox extends JComboBox {

    public AlgMoveComboBox() {
	super();
	String[] algStore = { "Automatique", "Economique", "Rapidité" };
	DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
	this.setModel(algModel);
	//TODO Faire en reflexif
    }
}
