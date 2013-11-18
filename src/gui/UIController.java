package gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import core.*;

public class UIController implements Observer {

	private RoRFrame rorFrame;
	private SimulationManager simulationManager;
	
	public UIController()
	{
		rorFrame=new RoRFrame(this,"Robot On Rails - Simulateur automatisé des stocks");		
		rorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rorFrame.setPreferredSize(new Dimension(1280, 800));
		rorFrame.setLocation(0, 0);
		rorFrame.setLocationRelativeTo(null);
		rorFrame.pack();
		rorFrame.setVisible(true);
		rorFrame.setResizable(false);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO mettre a jour la positions des robots et toutes les infos
	}
	
	public void setAlgMove(Integer algId)
	{
		switch (algId) {
		case 0:
			break;
		case 1:
			break;
			
		default:
			break;
		}
	}
	
	public void stopSimulation()
	{
		simulationManager.setStop();
	}
	
	public void pauseSimulation()
	{
		simulationManager.setPause();
	}
	
	public void startSimulation()
	{
		simulationManager.runSimulation();
	}

}
