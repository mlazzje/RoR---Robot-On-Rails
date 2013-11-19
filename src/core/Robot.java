package core;

import java.awt.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Robot extends Observable {

	private Integer traveledDistance = 0;
	private Integer consumption = 0;
	private ArrayList<Action> actions = null;
	private Rail rail = null;
	private Integer speed = 0;
	private Boolean working = false;
	private Order orderInProgress = null; // a quoi il sert cet attribut ??
	private Integer status = 0;
	private ArrayList<Product> products = null;

	public Robot(Rail initRail) {
		actions = new ArrayList<Action>();
		rail = initRail;
	}

	public void executeAction(Action action) {
		Timer timer = new Timer();
		TimerTask tt;
		if (action instanceof MoveAction) {
			tt = new TimerTask() {
				public void run() {
					// Robot.rail=((MoveAction)action).destinationRail;
					Robot.this.consumption += 20 * speed; // TODO vérifier le
															// calcul
					Robot.this.traveledDistance++;
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
		} else if (action instanceof StoreAction) {
			tt = new TimerTask() {
				public void run() {
					/*
					 * Storeaction storeAction = ((StoreAction)action);Drawer
					 * drawer =storeAction.drawer;
					 * this.removeProduct(storeAction.getProduct());
					 * drawer.setProduct(storeAction.getProduct()); *
					 */
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
		} else if (action instanceof DestockingAction) {
			tt = new TimerTask() {
				public void run() {
					/*
					 * DestockingAction destockingAction =
					 * ((DestockingAction)action);
					 * drawer=destockingAction.drawer;
					 * this.addProduct(drawer.getProduct());
					 * drawer.setProduct(null);
					 */
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
		} else if (action instanceof InputAction) {
			tt = new TimerTask() {
				public void run() {
					/*
					 * InputAction inputAction = ((InputAction)action);
					 * this.addProduct(inputAction.getProduct());
					 * inputAction.getInput().removeProduct(null);
					 */
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
		} else if (action instanceof OutputAction) {
			tt = new TimerTask() {
				public void run() {
					/*
					 * OutputAction outputAction = ((OutputAction)action);
					 * this.removeProduct(OutputAction.getProduct());
					 * outputAction
					 * .getOutput().addProduct(OutputAction.getProduct());
					 */
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
		}

		// timer.schedule(tt, action.getDuration());

	}

	public Action getNextAction() {
		if (actions != null && actions.size() > 0)
			return actions.get(0);
		else
			return null;
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
		actions.add(action);
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
}
