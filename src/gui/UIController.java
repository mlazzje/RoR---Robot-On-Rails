package gui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import algo.AlgDestockingFifo;
import core.*;

public class UIController implements Observer {

	private RoRFrame rorFrame;
	private SimulationManager simulationManager;

	public UIController() {
		rorFrame = new RoRFrame(this,
				"Robot On Rails - Simulateur automatisé des stocks");
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO mettre à jour tous les composants graphiques en fonction de la simulation
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
			simulationManager.setiAlgStore(new AlgStoreFIFO());
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
		simulationManager.setStop();
	}

	public void pauseSimulation() {
		simulationManager.setPause();
	}

	public void startSimulation() {
		simulationManager.run();
	}

	public void setSpeed(Float speed) {
		simulationManager.setSpeed(speed);
	}

}
