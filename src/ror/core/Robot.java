package ror.core;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.StoreAction;
import ror.core.Order;

/**
 * Robot class : Core class that represents a robot
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Robot extends Observable implements Runnable {

    public static final int STATUS_RUNNING = 1;

    public static final int STATUS_SLEEPING = 2;

    public static final int STATUS_PAUSE = 3;

    /**
     * First speed
     */
    public static final float SPEED_1 = 1;
    /**
     * Second speed
     */
    public static final float SPEED_2 = 0.5f;
    /**
     * Third speed
     */
    public static final float SPEED_3 = 0.25f;

    /**
     * use to lock when make changes to list of actions
     */
    public ReentrantLock lock;
    /**
     * Traveled distance
     */
    private Integer traveledDistance = 0;
    /**
     * Consumption in watts
     */
    private Integer consumption = 0;
    /**
     * Actions list
     */
    private ArrayList<Action> actions = null;
    /**
     * Robot on Rail !!!
     */
    private Rail rail = null;
    /**
     * Speed default
     */
    private Float speed = SPEED_1;

    /**
     * Order in progress
     */
    private Order orderInProgress = null;
    /**
     * Robot status
     */
    private Integer status = 0;
    /**
     * Products list
     */
    private ArrayList<Product> products = null;

    /**
     * Last robot move
     */
    private MoveAction lastMove;
    /**
     * Robot number
     */
    private Integer number;
    /**
     * Simulation manager
     */
    private SimulationManager simulationManager;

    /**
     * Timer
     */
    private Timer timer = null;
    /**
     * Timer task
     */
    private TimerTask timerTask = null;

    /**
     * Constructor
     * 
     * @param initRail
     * @param num
     * @param simulationManager
     */
    public Robot(Rail initRail, Integer num, SimulationManager simulationManager) {
	number = num;
	actions = new ArrayList<Action>();
	if (initRail != null) {
	    rail = initRail;
	    rail.setRobot(this);
	}
	products = new ArrayList<Product>();
	this.consumption = 0;
	this.simulationManager = simulationManager;
	timer = null;
	lock = new ReentrantLock();
    }

    @Override
    public String toString() {
	return "Robot #" + getNumber();
    }

    /**
     * @return robot number
     */
    public Integer getNumber() {
	return number;
    }

    /**
     * Set robot number
     * 
     * @param number
     */
    public void setNumber(Integer number) {
	this.number = number;
    }

    /**
     * schedule a timer to notify the robot after a wait of the duration (miliseconds)
     * 
     * @param duration
     */
    private void waitForTimer(int duration) {

	timer = new Timer("#TIMER#" + this);
	timerTask = new TimerTask() {
	    public void run() {
		synchronized (Robot.this) {
		    Robot.this.notify();
		}
	    }
	};

	timer.schedule(timerTask, duration);
	synchronized (Robot.this) {
	    try {
		Robot.this.wait();
		this.stopTimerTask();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Execute action passed in parameter
     * 
     * @param action
     */
    private void executeAction(final Action action) {

	// création d'un timer par action
	if (action instanceof MoveAction) {
	    // aguillage du prochain rail

	    MoveAction moveAction = (MoveAction) action;
	    // aiguillage du prochain rail
	    moveAction.getPrevious().setNextRail(moveAction.getNext());

	    if (lastMove != null) {
		// detection d'erreurs dans les deplacements
		if (!moveAction.getPrevious().getPreviousRail().contains(lastMove.getPrevious())) {
		    Integer countActions = 0;

		    for (Action tmpCountAction : Robot.this.getActions()) {
			if (!(tmpCountAction instanceof MoveAction))
			    countActions++;
		    }
		    System.out.println("Erreur :  " + Robot.this + " " + moveAction.getPrevious() + " <- " + lastMove.getPrevious() + " -- Nombre d'actions : " + countActions + " en cours " + Robot.this.actions.get(0).getClass().getSimpleName());
		}
	    }

	    this.waitForTimer((int) (action.getDuration() * Robot.this.speed));

	    lastMove = moveAction;

	    this.setOrderInProgress(null);
	    this.rail.setRobot(null);
	    this.rail.lock.unlock();

	    if (((MoveAction) action).getNext() != null)
		Robot.this.rail = ((MoveAction) action).getNext();
	    Robot.this.rail.setRobot(Robot.this);
	    Robot.this.consumption += (int) (20 / speed);
	    Robot.this.traveledDistance++;

	} else if (action instanceof StoreAction) {
	    final StoreAction storeAction = ((StoreAction) action);
	    this.setOrderInProgress(storeAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    Drawer drawer = storeAction.getDrawer();
	    Product product = storeAction.getProduct();

	    if (drawer.getProduct() != null)
		System.out.println("\nErreur tiroir plein\n " + drawer);

	    Robot.this.removeProduct(product); // on supprime le produit du plateau
	    drawer.setProduct(product); // on associe le tiroir au produit
	    product.setDrawer(drawer); // on associe le produit au tiroir

	    // dans le cas du stockage par commande
	    if (product.getOrder() != null) {
		product.getOrder().addProduct(storeAction.getProduct());
	    }

	} else if (action instanceof DestockingAction) {
	    final DestockingAction destockingAction = ((DestockingAction) action);

	    this.waitForTimer(action.getDuration());

	    this.setOrderInProgress(destockingAction.getProduct().getOrder());

	    Drawer drawer = destockingAction.getDrawer();
	    Product product = destockingAction.getProduct();

	    product.getOrder().setStatus(Order.BEING_DESTOCKED);
	    drawer.getStatus();
	    if (drawer.getProduct() == null) {
		System.out.println("\n" + Robot.this + " Erreur destocking " + drawer + " : product null \n");
	    }

	    Robot.this.addProduct(product);
	    drawer.setProduct(null); // status is set to free in setProduct
	    drawer.setStatus(Drawer.FREE);
	    product.setDrawer(null); // on supprime le tiroir au produit

	} else if (action instanceof InputAction) {
	    final InputAction inputAction = ((InputAction) action);
	    this.setOrderInProgress(inputAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    if (inputAction.getProduct() == null) {
		System.out.println("\n Erreur input action product null " + Robot.this + "\n");
	    }
	    Robot.this.addProduct(inputAction.getProduct());
	    inputAction.getInput().removeProduct(inputAction.getProduct());

	} else if (action instanceof OutputAction) {
	    final OutputAction outputAction = ((OutputAction) action);
	    this.setOrderInProgress(outputAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    Robot.this.removeProduct(outputAction.getProduct());
	    outputAction.getOutput().addProduct(outputAction.getProduct());
	    Order o = outputAction.getProduct().getOrder();

	    synchronized (o.getProducts()) {

		Boolean orderDone = true;
		for (Product p : o.getProducts()) {
		    if (p.getStatus() != Product.DONE) {
			orderDone = false;
		    }
		}
		if (orderDone) {
		    synchronized (outputAction.getOutput().getProductList()) {
			outputAction.getOutput().getProductList().removeAll(o.getProducts());
		    }

		    o.setStatus(Order.DONE);
		    o.setProcessingTime(simulationManager.getUptime() - o.getProcessingTime());
		}
	    }

	}
    }

    /**
     * @return current action
     */
    private Action getCurrentAction() {

	if (actions != null && actions.size() > 0)
	    return actions.get(0);
	else
	    return null;

    }

    /**
     * Add product on robot platform
     * 
     * @param product
     */
    private void addProduct(Product product) {

	products.add(product);
    }

    /**
     * Remove product on robot platform
     * 
     * @param product
     */
    private void removeProduct(Product product) {
	synchronized (this.products) {
	    products.remove(product);
	}
    }

    /**
     * @return ArrayList of products on the platform
     */
    public ArrayList<Product> getProducts() {
	return products;
    }

    /**
     * @return Traveled distance
     */
    public Integer getTraveledDistance() {
	return traveledDistance;
    }

    /**
     * @return consumption in watts
     */
    public Integer getConsumption() {
	return consumption;
    }

    /**
     * @return ArrayList of actions
     */
    public ArrayList<Action> getActions() {
	return actions;
    }

    /**
     * @return Rail under robot
     */
    public Rail getRail() {
	return rail;
    }

    /**
     * @return robot status
     */
    public Integer getStatus() {
	return status;
    }

    /**
     * @return Order in progress
     */
    public Order getOrderInProgress() {
	return orderInProgress;
    }

    /**
     * Set an order in progress
     * 
     * @param orderInProgress
     */
    private void setOrderInProgress(Order orderInProgress) {
	this.orderInProgress = orderInProgress;
    }

    /**
     * @return Last action rail
     */
    public Rail getLastActionRailUnsynchronized() {
	Action lastAction;

	if (this.actions.size() > 0) {
	    lastAction = this.actions.get(this.actions.size() - 1);

	    if (lastAction instanceof MoveAction) {
		return ((MoveAction) lastAction).getNext();
	    } else if (lastAction instanceof StoreAction) {
		return ((StoreAction) lastAction).getDrawer().getColumn().getAccess();
	    } else if (lastAction instanceof DestockingAction) {
		return ((DestockingAction) lastAction).getDrawer().getColumn().getAccess();
	    } else if (lastAction instanceof OutputAction) {
		return ((OutputAction) lastAction).getOutput().getAccess();
	    } else if (lastAction instanceof InputAction) {
		return ((InputAction) lastAction).getInput().getAccess();
	    } else
		return this.rail;
	} else
	    return this.rail;

    }

    /**
     * @return Number of products that robot can transport
     */
    public Integer getLastActionSpaceAvailabilityUnsynchronized() {
	int count = this.products.size();

	for (Action action : actions) {

	    if (action instanceof StoreAction) {
		count--;
	    } else if (action instanceof DestockingAction) {
		count++;
	    } else if (action instanceof OutputAction) {
		count--;
	    } else if (action instanceof InputAction) {
		count++;
	    }
	}

	return 10 - count;

    }

    /**
     * For a robot which block
     * 
     * @return the next available rail
     */
    public Rail getOpositeRailAtNextIntersection() {
	Rail intersectionRail = null;
	MoveAction lastAction = null;

	// Parcours des actions

	for (Action action : this.actions) {
	    if (action instanceof MoveAction) {
		intersectionRail = ((MoveAction) action).getNext();

		// Le rail est une intersection
		if (intersectionRail.getLeftRail() != null && rail.getRightRail() != null) {
		    if (intersectionRail.getLeftRail() == ((MoveAction) action).getNext())
			return intersectionRail.getRightRail();
		    else
			return intersectionRail.getLeftRail();
		}
		lastAction = (MoveAction) action;
	    }
	}

	return lastAction.getNext().getRightRail();
    }

    /**
     * @return robot speed
     */
    public Float getSpeed() {
	return speed;
    }

    /**
     * Set robot speed
     * 
     * @param speed
     */
    public void setSpeed(Float speed) {
	this.speed = speed;
    }

    /**
     * force the blocking robot to move to allow the robot sending the action to go on the blocking robot rail.
     * 
     * @param blockingRobot
     */
    private void moveBlockingRobot(Robot blockingRobot) {

	System.out.println(this + " bloqué par " + blockingRobot + " qui a le status : " + blockingRobot.getStatus() + " et " + blockingRobot.getActions().size() + " actions");
	
	// si le robot qui bloque n'a pas prévu d'avancer
	blockingRobot.lock.lock();

	if (blockingRobot.willMove() == false) {
	    // on fait avancer le robot jusqu'à la prochaine intersection ou jusqu'au rail suivant
	    ArrayList<MoveAction> movesBlockingRobot = this.simulationManager.getiAlgMove().railsToMoveActions(simulationManager.getMap().getPath(blockingRobot.getLastActionRailUnsynchronized(), this.getOpositeRailAtNextIntersection()));
	    if (movesBlockingRobot.size() == 0)
		System.out.println("erreur 1000");
	    blockingRobot.getActions().addAll(movesBlockingRobot);
	}
	blockingRobot.lock.unlock();
	// si le robot dort on le reveille
	synchronized (blockingRobot.status) {

	    if (blockingRobot.getStatus() == Robot.STATUS_SLEEPING) {
		synchronized (blockingRobot) {
		    blockingRobot.notify();
		}
	    }
	}

    }

    /**
     * 
     * @return true if the robot has a MoveAction in his actions's list else return false
     */
    public boolean willMove() {
	for (Action a : this.actions) {
	    if (a instanceof MoveAction) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void run() {
	do {
	    int simulationStatus = this.simulationManager.getStatus();

	    // on lock le rail courant la premiere fois a l'initialisation de la simulation
	    if (lastMove == null && !this.rail.lock.isLocked()) {
		this.rail.lock.lock();
	    }

	    // mise en veille si plus d'actions ou si simulation en pause ou arrêtée
	    this.lock.lock();
	    if (this.getCurrentAction() == null || simulationStatus == SimulationManager.PAUSED) {
		this.lock.unlock();
		// maj du status si pas d'actions
		synchronized (this.status) {
		    this.status = Robot.STATUS_SLEEPING;
		}

		// mise en pause du robot
		synchronized (this) {
		    try {
			this.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}

		synchronized (this.status) {
		    this.status = Robot.STATUS_RUNNING;
		}
	    } else {

		if (this.actions.get(0) instanceof MoveAction) {

		    MoveAction move = (MoveAction) this.actions.get(0);
		    Rail r = move.getNext();
		    if (this.getRail() == move.getNext()) // erreur
		    {
			this.actions.remove(0);
			continue;
		    }
		    this.lock.unlock();
		    while (r.lock.tryLock() == false) {
			// on a pas le lock donc c'est un autre robot qui l'a
			Robot blockingRobot = this.simulationManager.checkNextAction(this, move);

			if (blockingRobot != null) {
			    this.moveBlockingRobot(blockingRobot);
			    r.lock.lock();
			    break;
			}
		    }
		    this.lock.lock();
		}
		// execution de la prochaine action
		Action a = this.actions.get(0);
		this.lock.unlock();
		a.setDuration((int) (1000 / this.simulationManager.getSpeed()));

		this.executeAction(a);

		this.setChanged();
		this.notifyObservers();

		this.lock.lock();
		this.actions.remove(0);
		this.lock.unlock();

	    }

	} while (simulationManager.getStatus() != SimulationManager.STOPPED);

	// on delock le rail puisque la simulation est arrêttée
	if (this.rail.lock.isLocked()) {
	    this.rail.lock.unlock();
	    this.rail.setRobot(null);
	    System.out.println("robot unlock son rail et termine son thread");
	}

    }

    /**
     * Cancel the timer and timertask of current action
     */
    public void stopTimerTask() {
	if (this.timer != null) {
	    timer.cancel();
	    timer.purge();
	    timer = null;
	}
	if (this.timerTask != null) {
	    timerTask.cancel();
	    timerTask = null;
	}
    }

}
