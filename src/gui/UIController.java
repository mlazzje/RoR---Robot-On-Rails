package gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class UIController implements Observer {

	private RoRFrame rorFrame;

	public UIController() {
		rorFrame = new RoRFrame(this,
				"Robot On Rails - Simulateur automatisé des stocks");
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
