package ror.core;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.PauseAction;
import ror.core.actions.StoreAction;

public class Robot extends Observable{

    private Integer traveledDistance = 0;
    private Integer consumption = 0;
    private ArrayList<Action> actions = null;
    private Rail rail = null;
    private Integer speed = 1;
    private Boolean working = false;
    private Order orderInProgress = null;
    private Integer status = 0;
    private ArrayList<Product> products = null;
    private Timer timer = null;
    private TimerTask timerTask;
    private MoveAction lastMove;
    private Integer number;
    public Robot(Rail initRail, Integer num) {
	number = num;
	timer = new Timer();
	actions = new ArrayList<Action>();
	rail = initRail;
	products = new ArrayList<Product>();
	initRail.setRobot(this);
	this.consumption = 0;
    }

    
    
    @Override
    public String toString() {
	return "Robot "+getNumber();
    }



    public Integer getNumber() {
        return number;
    }



    public void setNumber(Integer number) {
        this.number = number;
    }



    public void stopSchedule() {
	if (timerTask != null) {
	    timerTask.cancel();
	}
	timer.cancel();
	timer.purge();
	timer = new Timer();
    }

    public void executeAction(final Action action) {
	// add action to the actions list if it no exist (only happens if we add
	// a pause action)
	synchronized (this.actions) {
	    if (this.actions.size()>0 && action != this.actions.get(0) && action !=null)
		this.actions.add(0, action);
	}
	// création d'un timer par action
	if (action instanceof MoveAction) {
	    MoveAction moveAction = (MoveAction) action;
	    if (lastMove != null) {
		if (!moveAction.getPrevious().getPreviousRail().contains(lastMove.getPrevious()))
		    System.out.println("Erreur : " + moveAction.getPrevious() + "<-" + lastMove.getPrevious());
	    }
	    lastMove = moveAction;
	    this.setOrderInProgress(null);
	    timerTask = new TimerTask() {
		public void run() {
		    Robot.this.rail.setRobot(null);
		    if (((MoveAction) action).getNext() != null)
			Robot.this.rail = ((MoveAction) action).getNext();
		    Robot.this.rail.setRobot(Robot.this);
		    Robot.this.consumption += 20 * speed; // TODO vérifier le calcul
		    Robot.this.traveledDistance++;
		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof StoreAction) {
	    final StoreAction storeAction = ((StoreAction) action);
	    this.setOrderInProgress(storeAction.getProduct().getOrder());

	    timerTask = new TimerTask() {
		public void run() {

		    Drawer drawer = storeAction.getDrawer();
		    storeAction.getProduct().setStatus(Product.STORED);
		    Robot.this.removeProduct(storeAction.getProduct());
		    drawer.setProduct(storeAction.getProduct());
		    drawer.getProduct().setDrawer(drawer);

		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof DestockingAction) {
	    final DestockingAction destockingAction = ((DestockingAction) action);
	    this.setOrderInProgress(destockingAction.getProduct().getOrder());

	    timerTask = new TimerTask() {
		public void run() {
		    Drawer drawer = destockingAction.getDrawer();
		    Robot.this.addProduct(drawer.getProduct());
		    drawer.setStatus(Drawer.FREE);
		    drawer.setProduct(null);

		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof InputAction) {
	    final InputAction inputAction = ((InputAction) action);
	    this.setOrderInProgress(inputAction.getProduct().getOrder());

	    timerTask = new TimerTask() {
		public void run() {
		    Robot.this.addProduct(inputAction.getProduct());
		    inputAction.getInput().removeProduct(inputAction.getProduct());

		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof OutputAction) {
	    final OutputAction outputAction = ((OutputAction) action);
	    this.setOrderInProgress(outputAction.getProduct().getOrder());
	    timerTask = new TimerTask() {
		public void run() {

		    Robot.this.removeProduct(outputAction.getProduct());
		    outputAction.getOutput().addProduct(outputAction.getProduct());

		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof PauseAction) {
	    this.setOrderInProgress(null);
	    timerTask = new TimerTask() {
		public void run() {
		    System.out.println(Robot.this +" effectue une pause");
		    Robot.this.setChanged();
		    Robot.this.notifyObservers();
		}
	    };
	    timer.schedule(timerTask, action.getDuration());
	}
    }

    public Action getCurrentAction() {
	synchronized (this.actions) {

	    if (actions != null && actions.size() > 0)
		return actions.get(0);
	    else
		return null;
	}
    }

    public void removeCurrentAction() {
	synchronized (this.actions) {

	    if (actions != null && actions.size() > 0)
		this.actions.remove(0);
	}
    }

    private void addProduct(Product product) {
	products.add(product);
    }

    private void removeProduct(Product product) {
	products.remove(product);
    }

    public ArrayList<Product> getProducts() {
	return products;
    }

    public void addAction(Action action) {

	synchronized (this.actions) {

	    actions.add(action);
	}
    }

    public Integer getTraveledDistance() {
	return traveledDistance;
    }

    public Integer getConsumption() {
	return consumption;
    }

    public ArrayList<Action> getActions() {
	return actions;
    }

    public Rail getRail() {
	return rail;
    }

    public Boolean getWorking() {
	return working;
    }

    public Integer getStatus() {
	return status;
    }

    public Order getOrderInProgress() {
	return orderInProgress;
    }

    public void setOrderInProgress(Order orderInProgress) {
	this.orderInProgress = orderInProgress;
    }

    public Rail getLastActionRail() {
	Action lastAction;
	synchronized (this.actions) {

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
    }

    public Integer getLastActionSpaceAvailability() {
	int count = this.products.size();

	synchronized (this.actions) {

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
    }
}
