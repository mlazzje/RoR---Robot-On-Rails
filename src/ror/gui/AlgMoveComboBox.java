package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

@SuppressWarnings("rawtypes")
/**
 * AlgMoveComboBox class
 * Represent the AlgMove ComboBox
 */
public class AlgMoveComboBox extends JComboBox implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the AlgMoveComboBox class
	 */
	@SuppressWarnings("unchecked")
	public AlgMoveComboBox() {
		super();
		String[] algStore = { "Automatique", "Economique", "Rapidité" };
		DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
		this.setModel(algModel);
		this.setSelectedIndex(1);
		this.addActionListener(this);
	}

	/**
	 * Function called when item selected
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setAlgMove(this.getSelectedIndex());
		} else {
			System.err.println("Can't get parent RoRFrame");
		}
	}
}
