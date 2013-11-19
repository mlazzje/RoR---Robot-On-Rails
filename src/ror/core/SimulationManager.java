package ror.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ror.core.actions.Action;
import ror.core.actions.MoveAction;
import ror.core.actions.PauseAction;
import ror.core.algo.IAlgDestocking;
import ror.core.algo.IAlgMove;
import ror.core.algo.IAlgStore;

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

	public SimulationManager() {
		this.map = new RoRElement[0][0];
		this.robots = new ArrayList<Robot>();
	}

	public Integer getUptime() {
		return ((int) ((System.currentTimeMillis() - startTime) / 1000));
	}

	@Override
	public void run() {
		// add robots
		for (int i = 0; i < nbRobot; i++)
			robots.add(new Robot(null)); // TODO ajouter le rail par defaut pour
											// le robot

		this.stockProducts = new ArrayList<Product>();
		this.orders = new ArrayList<Order>();
		startTime = System.currentTimeMillis();
		status = 1;
		while (status != 0) {
			System.out.print("simulation manager loop ");
			System.out.println(status);
			if (status == 1) // running
			{
				ArrayList<Order> newOrders = new ArrayList<Order>();
				ArrayList<Product> newProducts = new ArrayList<Product>();
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

	public RoRElement[][] getMap() {
		return map;
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
	public void update(Observable o, Object arg) { // called by a robot
		if (o instanceof Robot) {
			Robot robot = (Robot) o;
			Robot blockingRobot = checkNextAction(robot,
					robot.getCurrentAction());

			// if there is no robot on the next rail => allow robot to execute
			// action
			if (blockingRobot == null) {
				robot.executeAction(robot.getCurrentAction());
			}
			// else add waiting action to robot
			else {
				robot.executeAction(new PauseAction(1, robot, blockingRobot));
			}

			// then unblocks all robots blocked by this robot
			ArrayList<Robot> waitingRobots = getWaitingRobots(blockingRobot);
			for (Robot r : waitingRobots) {
				r.stopSchedule();
				r.removeCurrentAction();
				r.executeAction(r.getCurrentAction()); //relaunch the actions of the ex-waiting robot
			}
			
			// notify observers (UIController)
			this.setChanged();
			this.notifyObservers();
		}
	}

	// retourne la liste des robots dont le mouvement est bloque par le robot en
	// passé paramètre
	private ArrayList<Robot> getWaitingRobots(Robot blockingRobot) {
		ArrayList<Robot> waitingRobots = new ArrayList<Robot>();

		for (Robot robot : robots) {
			Action action = robot.getCurrentAction();
			if (action instanceof PauseAction) {
				PauseAction pauseAction = (PauseAction) action;
				if (pauseAction.getWaitingRobot() == blockingRobot)
					waitingRobots.add(robot);
			}
		}
		return waitingRobots;
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

	private Robot checkNextAction(Robot robot, Action action) {
		if (action instanceof MoveAction) {
			MoveAction moveAction = (MoveAction) action;
			for (Robot r : robots) {
				if (r != robot) {
					if (moveAction.getNext() == robot.getRail()) {
						return robot;
					}
				}
			}
		}
		return null;
	}
}
