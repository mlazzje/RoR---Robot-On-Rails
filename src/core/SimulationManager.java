package core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class SimulationManager extends Observable implements Observer, Runnable {

	// relationship
	private IAlgStore iAlgStore;
	private IAlgMove iAlgMove;
	private IAlgDestocking iAlgDestocking;
	private ArrayList<Cabinet> cabinets;
	private RoRElement[][] map;
	private ArrayList<Robot> robots;
	private ArrayList<Order> orders;
	private ArrayList<Product> stockProducts; // TODO ajouter la relation au
												// D.Classes
	private OrderSource orderSrouce;

	// own attributes
	private Float speed = (float) 0;
	private Integer nbRobot = 0;
	private Integer status = 0;
	private boolean source;
	private Integer coeff = 1000; // <==> 1 second
	private long startTime; // TODO modifier le type de l'attribut dans le
							// D.Classes

	public Integer getUptime() {
		return ((int) ((System.currentTimeMillis() - startTime) / 1000));
	}

	public void runSimulation() {
		startTime = System.currentTimeMillis();
		status = 1;
		this.run();
	}

	@Override
	public void run() {
		while (status != 0) {
			if (status == 1) // running
			{
				ArrayList<Order> newOrders = null;
				ArrayList<Product> newProducts = null;
				if (source) // random mode
				{
					// TODO ajouter les liaisons vers l'orderSource
				} else // scenario mode
				{
					// TODO ajouter les liaisons vers l'orderSource
				}

				SimulationManager.this.stockProducts.addAll(newProducts);
				SimulationManager.this.orders.addAll(newOrders);

				// TODO appeler les algos

				SimulationManager.this.updateIndicators();

				// notify observers (UIController)
				this.setChanged();
				this.notifyObservers();

				// sleep
				try {
					Thread.sleep((int) (SimulationManager.this.coeff * SimulationManager.this.speed));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else if (status == 2) // pause simulation
			{
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateIndicators() {
		// TODO to implement
	}

	public void setStop() {
		status = 0;
	}
	
	public void setPause() {
		status = 2;
	}

	public void setPlay() {
		status = 1;
		synchronized (this) {
			this.notify();
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		// notify observers (UIController)
		this.setChanged();
		this.notifyObservers();
	}

	public void setiAlgStore(IAlgStore iAlgStore) {
		this.iAlgStore = iAlgStore;
	}

	public void setiAlgMove(IAlgMove iAlgMove) {
		this.iAlgMove = iAlgMove;
	}

	public void setiAlgDestocking(IAlgDestocking iAlgDestocking) {
		this.iAlgDestocking = iAlgDestocking;
	}

	public void setRandomMode() {
		source = false;
	}

	public void setFile(File file) {
		source = true;
		// TODO set file to orderSource
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public void setNbRobot(Integer nbRobot) {
		this.nbRobot = nbRobot;
	}

	public void setEndSimulation() {
		coeff = 0;
		this.setPlay();
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}

}
