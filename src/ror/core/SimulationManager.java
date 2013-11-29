package ror.core;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import ror.core.actions.Action;
import ror.core.actions.MoveAction;
import ror.core.actions.PauseAction;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.IAlgDestocking;
import ror.core.algo.IAlgMove;
import ror.core.algo.IAlgStore;

public class SimulationManager extends Observable implements Observer, Runnable {

    // relationship
    private IAlgStore iAlgStore;
    private IAlgMove iAlgMove;
    private IAlgDestocking iAlgDestocking;

    private ArrayList<Robot> robots;
    private ArrayList<Order> orders;
    private OrderSource orderSource;
    private Map map;

    // own attributes
    private Float speed = (float) 1;
    private Integer nbRobot = 0;
    private Integer status = 0;
    private boolean source;
    private Integer coeff = 2500; // <==> 1 second
    private long startTime;
    private ArrayList<Product> stockProducts;

    public SimulationManager() {
	this.map = new Map();

	this.robots = new ArrayList<Robot>();
	this.orderSource = new OrderSource();
	this.source = false;
	this.iAlgStore = new AlgStoreFifo();
	this.iAlgDestocking = new AlgDestockingFifo();
	this.iAlgMove = new AlgMoveEco();
	this.orders = new ArrayList<Order>();
	this.stockProducts = new ArrayList<Product>();
    }

    public Integer getStatus() {
	return this.status;
    }

    public boolean getSource() {
	return this.source;
    }

    public Float getSpeed() {
	return this.speed;
    }

    public IAlgStore getiAlgStore() {
	return this.iAlgStore;
    }

    public IAlgMove getiAlgMove() {
	return this.iAlgMove;
    }

    public IAlgDestocking getiAlgDestocking() {
	return this.iAlgDestocking;
    }

    public Integer getNbRobot() {
	return this.nbRobot;
    }

    public Integer getUptime() {
	return ((int) ((System.currentTimeMillis() - startTime) / 1000));
    }

    @Override
    public void run() {
	// add robots
	for (int i = 0; i < nbRobot; i++)
	    robots.add(new Robot(null)); // TODO ajouter le rail par defaut pour le robot

	startTime = System.currentTimeMillis();
	status = 1;
	while (status != 0) {
	    if (status == 1) // running
	    {
		ArrayList<Order> newOrders;
		ArrayList<Product> newProducts;
		if (!source) // random mode
		{
		    newOrders = SimulationManager.this.orderSource.getRandomOrders();
		    SimulationManager.this.orders.addAll(newOrders);
		    newProducts = SimulationManager.this.orderSource.getRandomProducts(SimulationManager.this.orders, SimulationManager.this.stockProducts);
		} else // scenario mode
		{
		    newOrders = SimulationManager.this.orderSource.getScenarioOrders(SimulationManager.this.getUptime());
		    SimulationManager.this.orders.addAll(newOrders);
		    newProducts = SimulationManager.this.orderSource.getScenartioProducts(SimulationManager.this.getUptime());
		}
		for (Product product : newProducts) {
		    this.map.getInput().addProduct(product);
		    this.stockProducts.add(product);
		}

		// add new products to stockProducts list
		SimulationManager.this.stockProducts.addAll(newProducts);

		// TODO Implémenter méthodes algo pour pouvoir tester
		/*
		 * // get store and input actions for newProducts ArrayList<Action> newActions = SimulationManager.this.iAlgStore .getActions(newProducts, newOrders, SimulationManager.this.map);
		 * 
		 * // get destocking and output actions for stockProducts newActions.addAll(SimulationManager.this.iAlgDestocking .getActions(newOrders, stockProducts, SimulationManager.this.output));
		 * 
		 * // update robot actions lists SimulationManager.this.iAlgMove. updateRobotsActions(newActions, SimulationManager.this.robots);
		 */
		// update statistic indicators
		SimulationManager.this.updateIndicators();

		// notify observers (UIController)
		SimulationManager.this.setChanged();
		SimulationManager.this.notifyObservers();

		// sleep
		try {
		    Thread.sleep((long) (3000 - (SimulationManager.this.coeff * SimulationManager.this.speed)));
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

    public Map getMap() {
	return map;
    }

    public void updateIndicators() {
	// TODO to implement and create indicators attributes
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

	    // save the last action of the robot
	    Action lastRobotAction = robot.getCurrentAction();

	    // going to the next action
	    robot.removeCurrentAction();

	    // search if the next action is blocked by another robot
	    Robot blockingRobot = checkNextAction(robot, robot.getCurrentAction());

	    // if no robot on the next rail
	    if (blockingRobot == null) {

		// execute next action
		robot.executeAction(robot.getCurrentAction());

		// get all robots waiting for the current robot to move
		ArrayList<Robot> waitingRobots = getWaitingRobots(blockingRobot);

		// then unblocks all robots blocked by this robot
		for (Robot r : waitingRobots) {

		    // cancel the scheduling task of blocked robot
		    r.stopSchedule();

		    // remove the PauseAction of the blocked robot
		    r.removeCurrentAction();

		    // then launch the next action
		    r.executeAction(r.getCurrentAction());
		}
	    }
	    // else add long waiting action to robot (to be sure that the task
	    // will
	    // be cancel by the robot his waiting)
	    else {
		robot.executeAction(new PauseAction(100000, robot, blockingRobot));
	    }

	    // notify observers (UIController)
	    this.setChanged();
	    this.notifyObservers();
	}
    }

    // retourne la liste des robots dont le mouvement est bloque par le robot
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
	orderSource.setFile(file);
    }

    public void setSpeed(Float speed) {
	this.speed = (speed >= 0) ? speed : 0.0f;
    }

    public void setNbRobot(Integer nbRobot) {
	this.nbRobot = (nbRobot >= 0) ? nbRobot : 0;
    }

    public void setEndSimulation() {
	coeff = 0;
	this.setPlay();
    }

    public ArrayList<Robot> getRobots() {
	return this.robots;
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

    public ArrayList<Order> getOrders() {
	return this.orders;
    }
}
