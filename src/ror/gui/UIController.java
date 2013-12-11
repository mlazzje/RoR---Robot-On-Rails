package ror.gui;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import ror.core.SimulationManager;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgDestockingOrder;
import ror.core.algo.AlgMoveAuto;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgMoveFast;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.AlgStoreOrder;

public class UIController implements Observer {

    private RoRFrame rorFrame;
    private SimulationManager simulationManager;
    private Thread thread;

    public UIController() {
	this.simulationManager = new SimulationManager();
	this.rorFrame = new RoRFrame(this, "Robot On Rails - Simulateur automatisé des stocks");
	this.simulationManager.addObserver(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {
	// Mise à jours de la liste des commandes
	((OrderListModel) this.rorFrame.getOrderList().getModel()).fireTableDataChanged();

	// Mise à jours de la liste des logs
	synchronized (simulationManager.getNewLogs()) {

	    ArrayList<String> newLogs = simulationManager.getNewLogs();
	    DefaultListModel<String> model = (DefaultListModel<String>) this.rorFrame.getLogList().getModel();
	    for (String log : newLogs) {
		// model.addElement(log);
		model.add(0, log);
	    }
	    this.rorFrame.getLogList().setModel(model);
	    simulationManager.setNewLogs(new ArrayList<String>());
	}

	// TODO Mise à jours des indicateurs
	this.rorFrame.reColor();
    }

    public void setAlgMove(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgMove(new AlgMoveEco());
	    break;
	case 1:
	    simulationManager.setiAlgMove(new AlgMoveFast());
	    break;
	case 2:
	    simulationManager.setiAlgMove(new AlgMoveAuto());
	    break;
	default:
	    break;
	}
    }

    public void setAlgStore(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgStore(new AlgStoreFifo());
	    break;
	case 1:
	    simulationManager.setiAlgStore(new AlgStoreOrder());
	    break;
	default:
	    break;
	}
    }

    public void setAlgDestocking(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgDestocking(new AlgDestockingFifo());
	    break;
	case 1:
	    simulationManager.setiAlgDestocking(new AlgDestockingOrder());
	    break;
	default:
	    break;
	}
    }

    public void setEndSimulation() {
	simulationManager.setEndSimulation();
    }

    public void setNbRobot(Integer nbRobot) {
	simulationManager.setNbRobot(nbRobot);
    }

    public void setFile(File file) {
	simulationManager.setFile(file);
    }

    public void setRandomMode() {
	simulationManager.setRandomMode();
    }

    public void stopSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	simulationManager.setStop();
    }

    public void pauseSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	simulationManager.setPause();
    }

    public void startSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/pause.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	this.thread = new Thread(this.simulationManager);
	this.thread.start();
    }

    public void setSpeed(Float speed) {
	simulationManager.setSpeed(speed);
    }

    public SimulationManager getSimulationManager() {
	return this.simulationManager;
    }

}
