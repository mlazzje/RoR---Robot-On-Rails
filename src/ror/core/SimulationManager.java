package ror.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.PauseAction;
import ror.core.actions.StoreAction;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.IAlgDestocking;
import ror.core.algo.IAlgMove;
import ror.core.algo.IAlgStore;

public class SimulationManager extends Observable implements Observer, Runnable {

    // relationship
    /**
     * Interface store algorithm
     */
    private IAlgStore iAlgStore;
    /**
     * Interface move algorithm
     */
    private IAlgMove iAlgMove;
    /**
     * Interface destocking algorithm
     */
    private IAlgDestocking iAlgDestocking;

    /**
     * ArrayList of robots
     */
    private ArrayList<Robot> robots;
    /**
     * ArrayList of orders
     */
    private ArrayList<Order> orders;
    /**
     * Order source
     */
    private OrderSource orderSource;
    /**
     * Map
     */
    private Map map;

    // own attributes
    /**
     * Speed 
     */
    private Float speed = (float) 1;
    /**
     * Number of robots
     */
    private Integer nbRobot = 3;
    /**
     * Status
     */
    private Integer status = 0;
    /**
     * Boolean was in pause (by default = false)
     */
    private boolean wasInPause = false;
    /**
     * Boolean source
     */
    private boolean source;
    /**
     * Coeff for speed and/or timer
     */
    private Integer coeff = 3000; // <==> 1 second
    /**
     * Uptime of simulation manager
     */
    private long uptime;
    /**
     * ArrayList of stockProducts
     */
    private ArrayList<Product> stockProducts;
    /**
     * ArrayList of new logs
     */
    private ArrayList<String> newLogs;

    /**
     * Constructor of simulation manager
     */
    public SimulationManager() {
	this.map = new Map();
	this.setNewLogs(new ArrayList<String>());
	this.robots = new ArrayList<Robot>();
	this.orderSource = new OrderSource();
	this.source = false;
	this.iAlgStore = new AlgStoreFifo();
	this.iAlgDestocking = new AlgDestockingFifo();
	this.iAlgMove = new AlgMoveEco();
	this.orders = new ArrayList<Order>();
	this.stockProducts = new ArrayList<Product>();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
	return this.status;
    }

    /**
     * @return source
     */
    public boolean getSource() {
	return this.source;
    }

    /**
     * @return speed
     */
    public Float getSpeed() {
	return this.speed;
    }

    /**
     * @return store algorithm
     */
    public IAlgStore getiAlgStore() {
	return this.iAlgStore;
    }

    /**
     * @return move algorithm
     */
    public IAlgMove getiAlgMove() {
	return this.iAlgMove;
    }

    /**
     * @return destocking algorithm
     */
    public IAlgDestocking getiAlgDestocking() {
	return this.iAlgDestocking;
    }

    /**
     * @return number of robots
     */
    public Integer getNbRobot() {
	return this.nbRobot;
    }

    /**
     * @return uptime
     */
    public Long getUptime() {
	return this.uptime;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	// add robots
	if (!this.wasInPause) {
	    for (int i = 0; i < nbRobot; i++) {
		Robot r = new Robot((Rail) getMap().getMap()[1 + i][1], i, this);
		r.addObserver(SimulationManager.this);
		robots.add(r);
	    }
	}

	// demo
	/*
	 * List<Rail> path = getMap().getPath((Rail) getMap().getMap()[1][1], (Rail) getMap().getMap()[2][23]); for (Rail rail : path) { MoveAction m = new MoveAction((int) (2500 * SimulationManager.this.speed), robots.get(0), null, rail); robots.get(0).addAction(m);
	 * 
	 * }
	 */

	for (Robot robot : this.robots) {

	    PauseAction p = new PauseAction((int) (1000 * SimulationManager.this.speed), robot, null);
	    robot.addAction(p);
	    robot.executeAction(robot.getCurrentAction());
	}
	uptime = 0;

	status = 1;
	while (status != 0) {
	    Long startTime = System.currentTimeMillis();
	    if (status == 1) // running
	    {
		if (wasInPause) {
		    for (Robot r : this.robots) {
			r.executeAction(r.getCurrentAction());
		    }
		    wasInPause = false;
		}
		ArrayList<Order> newOrders;
		ArrayList<Product> newProducts = null;
		if (!source) // random mode
		{
		    newOrders = SimulationManager.this.orderSource.getRandomOrders();
		    for (Order order : newOrders)
			order.setProcessingTime(this.getUptime());
		    SimulationManager.this.orders.addAll(newOrders);
		    if (this.map.getInput().getProductList().size() < 2)
			newProducts = SimulationManager.this.orderSource.getRandomProducts(SimulationManager.this.orders, SimulationManager.this.stockProducts);
		} else // scenario mode
		{
		    newOrders = SimulationManager.this.orderSource.getScenarioOrders(SimulationManager.this.getUptime());
		    for (Order order : newOrders)
			order.setProcessingTime(this.getUptime());
		    SimulationManager.this.orders.addAll(newOrders);
		    if (this.map.getInput().getProductList().size() < 2)
			newProducts = SimulationManager.this.orderSource.getScenarioProducts(SimulationManager.this.getUptime());
		}
		if (newProducts != null) {
		    for (Product product : newProducts) {
			this.map.getInput().addProduct(product);
			this.stockProducts.add(product);
		    }
		}

		// TODO Implémenter méthodes algo pour pouvoir tester
		// System.out.println("orders for algo "+newOrders); // I need all orders for algo ! //TODO passer en paramètre que les Orders en cours, non celles terminées
		ArrayList<StoreAction> newStoreActions = SimulationManager.this.iAlgStore.getActions(newProducts, SimulationManager.this.getOrders(), SimulationManager.this.map);

		ArrayList<DestockingAction> newDestockActions = SimulationManager.this.iAlgDestocking.getActions(this.orders, stockProducts);
		synchronized (map) {
		    SimulationManager.this.iAlgMove.updateRobotsActions(newDestockActions, newStoreActions, SimulationManager.this.robots, this.map);
		}
		// update statistic indicators
		SimulationManager.this.updateIndicators();

		// notify observers (UIController)
		SimulationManager.this.setChanged();
		SimulationManager.this.notifyObservers();

		// sleep
		try {
		    Thread.sleep((long) (3500 - (SimulationManager.this.coeff * SimulationManager.this.speed)));

		    uptime += (System.currentTimeMillis() - startTime);

		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    } else if (status == 2) // pause simulation
	    {
		for (Robot r : this.robots) {
		    r.stopSchedule();
		}
		try {
		    synchronized (this) {
			this.wait();
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
	for (Robot r : this.robots) {
	    r.stopSchedule();
	}
	// Suppression des robots des rails
	for(Rail rail : this.map.getRails()) {
	    rail.setRobot(null);
	}
	for(Column col : this.map.getColumns()) {
	    for(Drawer dra : col.getDrawerList()) {
		dra.setProduct(null);
	    }
	}
	this.robots = new ArrayList<Robot>();
    }

    /**
     * @return map
     */
    public Map getMap() {
	return map;
    }

    /**
     * Update indicators // TODO TO Complete
     */
    public void updateIndicators() {
	// TODO to implement and create indicators attributes
    }

    /**
     * Set stop
     */
    public void setStop() {
	status = 0;
	this.map.getInput().clearProducts();
	this.map.getOutput().clearProducts();
	this.setNewLogs(new ArrayList<String>());
	this.orderSource = new OrderSource();
	this.orders = new ArrayList<Order>();
	this.stockProducts = new ArrayList<Product>();
	Order.resetLastId();
    }

    /**
     * Set pause
     */
    public void setPause() {
	status = 2;
    }

    /**
     * Set play 
     */
    public void setPlay() {
	if (status == 2) {
	    this.wasInPause = true;
	}
	status = 1;
	synchronized (this) {
	    this.notify();
	}
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) { // called by a robot
	if (o instanceof Robot) {
	    Robot robot = (Robot) o;

	    synchronized (this.newLogs) {

		// Traçage des actions des robots
		if (robot.getCurrentAction() instanceof InputAction) {
		    InputAction action = (InputAction) robot.getCurrentAction();
		    this.newLogs.add(robot + " prend " + action.getProduct().getName() + " au point d'entrée");
		} else if (robot.getCurrentAction() instanceof OutputAction) {
		    OutputAction action = (OutputAction) robot.getCurrentAction();
		    this.newLogs.add(robot + " dépose " + action.getProduct().getName() + " au point de sortie");
		} else if (robot.getCurrentAction() instanceof StoreAction) {
		    StoreAction action = (StoreAction) robot.getCurrentAction();
		    this.newLogs.add(robot + " dépose " + action.getProduct().getName() + " dans la colonne (" + action.getDrawer().getColumn().getX() + ", " + action.getDrawer().getColumn().getY() + ") dans le tiroir " + action.getDrawer().getPositionInColumn());
		} else if (robot.getCurrentAction() instanceof DestockingAction) {
		    DestockingAction action = (DestockingAction) robot.getCurrentAction();
		    this.newLogs.add(robot + " prend " + action.getProduct().getName() + " dans la colonne (" + action.getDrawer().getColumn().getX() + ", " + action.getDrawer().getColumn().getY() + ") dans le tiroir " + action.getDrawer().getPositionInColumn());
		}
	    }
	    // save the last action of the robot
	    // Action lastRobotAction = robot.getCurrentAction();

	    // going to the next action
	    robot.removeCurrentAction();
	    if (robot.getCurrentAction() != null) {
		robot.getCurrentAction().setDuration((int) (1000 * SimulationManager.this.speed));

		// aguillage du prochain rail
		if (robot.getCurrentAction() instanceof MoveAction) {
		    MoveAction moveAction = (MoveAction) robot.getCurrentAction();
		    moveAction.getPrevious().setNextRail(moveAction.getNext());
		}

		// search if the next action is blocked by another robot
		Robot blockingRobot = checkNextAction(robot, robot.getCurrentAction());
		// if no robot on the next rail
		if (blockingRobot == null) {

		    // execute next action
		    robot.executeAction(robot.getCurrentAction());

		    /*
		     * // get all robots waiting for the current robot to move ArrayList<Robot> waitingRobots = getWaitingRobots(blockingRobot);
		     * 
		     * // then unblocks all robots blocked by this robot for (Robot r : waitingRobots) {
		     * 
		     * // cancel the scheduling task of blocked robot r.stopSchedule();
		     * 
		     * // remove the PauseAction of the blocked robot r.removeCurrentAction();
		     * 
		     * // then launch the next action r.executeAction(r.getCurrentAction()); }
		     */
		}
		// else add long waiting action to robot (to be sure that the task
		// will
		// be cancel by the robot his waiting)
		else {
		    synchronized (blockingRobot.getActions()) {
			// Si le robot de devant n'a pas d'action affectée
			if (blockingRobot.getActions().size() == 1 && blockingRobot.getActions().get(0) instanceof PauseAction) {
			    blockingRobot.stopSchedule();
			    blockingRobot.removeCurrentAction();

			    // on fait avancer le robot jusqu'a une intersection

			    synchronized (map) {
				ArrayList<MoveAction> movesBlockingRobot = iAlgMove.railsToMoveActions(map.getPath(blockingRobot.getRail(), robot.getOpositeRailAtNextIntersection()));
				blockingRobot.getActions().addAll(movesBlockingRobot);
			    }
			    PauseAction pa = new PauseAction(0, robot, blockingRobot);
			    synchronized (blockingRobot.getActions()) {
				blockingRobot.getActions().add(0, pa);
				blockingRobot.executeAction(blockingRobot.getCurrentAction());
			    }
			}
		    }

		    // System.out.println(robot + " bloqué par " + blockingRobot);
		    PauseAction pa = new PauseAction((int) (1000 * SimulationManager.this.speed), robot, blockingRobot);
		    synchronized (robot.getActions()) {
			robot.getActions().add(0, pa);
			robot.executeAction(robot.getCurrentAction());
		    }

		}

		// notify observers (UIController)
		this.setChanged();
		this.notifyObservers();
	    } else {
		PauseAction pa = new PauseAction((int) (1000 * SimulationManager.this.speed), robot, null);
		synchronized (robot.getActions()) {
		    robot.getActions().add(0, pa);
		    robot.executeAction(robot.getCurrentAction());
		}
	    }
	}
    }

    /**
     * Return ArrayList of robots blocked by robot passed in parameter
     * 
     * @param blocking robot
     * @return robots blocked
     */
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

    /**
     * Set store algorithm
     * 
     * @param iAlgStore
     */
    public void setiAlgStore(IAlgStore iAlgStore) {
	this.iAlgStore = iAlgStore;
    }

    /**
     * Set move algorithm
     * 
     * @param iAlgMove
     */
    public void setiAlgMove(IAlgMove iAlgMove) {
	this.iAlgMove = iAlgMove;
    }

    /**
     * Set destocking algorithm
     * 
     * @param iAlgDestocking
     */
    public void setiAlgDestocking(IAlgDestocking iAlgDestocking) {
	this.iAlgDestocking = iAlgDestocking;
    }

    /**
     * Set random mode
     */
    public void setRandomMode() {
	source = false;
    }

    /**
     * Set scenario file
     * 
     * @param file
     */
    public void setFile(File file) {
	source = true;
	orderSource.setScenarioFile(file);
    }

    /**
     * Set speed
     * 
     * @param speed
     */
    public void setSpeed(Float speed) {
	this.speed = (speed >= 0) ? speed : 0.0f;
    }

    /**
     * Set number of robots
     * 
     * @param nbRobot
     */
    public void setNbRobot(Integer nbRobot) {
	this.nbRobot = (nbRobot >= 0) ? nbRobot : 0;
    }

    /**
     * Set end of simulation
     */
    public void setEndSimulation() {
	coeff = 0;
	this.setPlay();
    }

    /**
     * @return ArrayList of robots
     */
    public ArrayList<Robot> getRobots() {
	return this.robots;
    }

    /**
     * @param robot
     * @param action
     * @return nextAction of robot and action passed in parameter
     */
    private Robot checkNextAction(Robot robot, Action action) {
	if (action instanceof MoveAction) {
	    MoveAction moveAction = (MoveAction) action;
	    for (Robot r : robots) {
		if (r != robot) {
		    if (moveAction.getNext() == r.getRail()) {
			return r;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * @return ArrayList of orders
     */
    public ArrayList<Order> getOrders() {
	return this.orders;
    }

    /**
     * @return ArrayList of new logs
     */
    public ArrayList<String> getNewLogs() {
	return newLogs;
    }

    /**
     * Set new logs
     * 
     * @param newLogs
     */
    public void setNewLogs(ArrayList<String> newLogs) {
	this.newLogs = newLogs;
    }

    /**
     * @return average consumption by order
     */
    public int getAverageConsumption() {
	int count = 0;
	for (Order order : this.orders) {
	    if (order.getStatus() != Order.INIT)
		count++;
	}
	int totalConsumption = this.getTotalConsumption();
	if (totalConsumption != 0 && count != 0)
	    return (int) (totalConsumption / count);
	else
	    return 0;
    }

    /**
     * @return average processing time to prepare order
     */
    public long getAverageOrderProcessingTime() {
	if (orders.size() > 0) {
	    long count = 0;
	    int orderCount = 0;
	    for (Order order : this.orders) {
		if (order.getStatus() == order.DONE) {
		    orderCount++;
		    count += order.getProcessingTime();
		}
	    }
	    if (count != 0 && orderCount != 0)
		return (long) (count / orderCount);
	    else
		return 0;
	} else
	    return 0;
    }

    /**
     * @return total consumption of robots
     */
    public int getTotalConsumption() {
	int cons = 0;
	for (Robot robot : this.getRobots()) {
	    cons += robot.getConsumption();
	}
	return cons;
    }

    /**
     * @return numbers of orders prepared
     */
    public int getOrdersDoneCount() {
	int count = 0;
	for (Order order : this.orders) {
	    if (order.getStatus() == Order.DONE)
		count++;
	}
	return count;
    }
}
