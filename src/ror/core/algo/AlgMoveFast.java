package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.StoreAction;

public class AlgMoveFast implements IAlgMove {

    public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map) {
	// si des actions input ou store sont disponibles
	if (newDestockActions != null) {
	    while (newDestockActions.size() > 0) {
		Robot robot;

		robot = getBestRobot(robots);
		robot.setSpeed(Robot.SPEED_3);

		ArrayList<Action> destockingActionToAffect = new ArrayList<Action>();
		int freeSpace = robot.getLastActionSpaceAvailability();

		while (freeSpace > 0 && newDestockActions.size() > 0) {
		    destockingActionToAffect.add(newDestockActions.get(0));
		    newDestockActions.remove(newDestockActions.get(0));
		    freeSpace--;
		}
		// ajout des actions de mouvements et de destockage au robot
		ArrayList<Action> actionMoveAndDestock = sortActionsAndMoves(destockingActionToAffect, map, robot.getLastActionRail());
		for (Action action : actionMoveAndDestock) {
		    robot.addAction(action);
		}

		Rail start = robot.getLastActionRail();
		Rail end = map.getOutput().getAccess();

		// ajout des actions des mouvements jusqu'a l'output au robot
		ArrayList<MoveAction> movesToOutput = railsToMoveActions(map.getPath(start, end));
		for (MoveAction move : movesToOutput) {
		    robot.addAction(move);
		}

		// ajout des actions d'output
		for (Action action : destockingActionToAffect) {
		    DestockingAction destockingAction = (DestockingAction) action;
		    OutputAction outputAction = new OutputAction(1000, robot, map.getOutput());
		    outputAction.setProduct(destockingAction.getProduct());
		    robot.addAction(outputAction);
		}

	    }
	}
	// si des actions input ou store sont disponibles
	if (newStoreActions != null) {
	    while (newStoreActions.size() > 0) {

		Robot robot = getBestRobot(robots);
		robot.setSpeed(Robot.SPEED_3);

		ArrayList<InputAction> inputActions = new ArrayList<InputAction>();
		for (StoreAction storeAction : newStoreActions) {
		    InputAction inputaction = new InputAction(1000, null, map.getInput());
		    inputaction.setProduct(storeAction.getProduct());
		    inputActions.add(inputaction);
		}

		Rail start = robot.getLastActionRail();
		Rail end = map.getInput().getAccess();

		ArrayList<MoveAction> movesToInput = railsToMoveActions(map.getPath(start, end));
		for (MoveAction move : movesToInput) {
		    robot.addAction(move);
		}

		ArrayList<InputAction> affectedInputAction = new ArrayList<InputAction>();

		// ajout des inputs actions au robot
		while (robot.getLastActionSpaceAvailability() > 0 && inputActions.size() > 0) {
		    affectedInputAction.add(inputActions.get(0));
		    robot.addAction(inputActions.get(0));
		    inputActions.remove(inputActions.get(0));
		}

		ArrayList<Action> storeActionsToAffect = new ArrayList<Action>();

		// on parcours les actions affectees pour recuperer les storeactions associées
		for (InputAction inputAction : affectedInputAction) {
		    for (StoreAction storeAction : newStoreActions) {
			if (inputAction.getProduct() == storeAction.getProduct()) {
			    storeActionsToAffect.add(storeAction);
			}
		    }
		}
		newStoreActions.removeAll(storeActionsToAffect);

		// ajout des actions de mouvements et de stockage au robot
		ArrayList<Action> actionMoveAndStore = sortActionsAndMoves(storeActionsToAffect, map, map.getInput().getAccess());
		for (Action action : actionMoveAndStore) {
		    robot.addAction(action);
		}
	    }
	}

    }

    public ArrayList<Action> sortActionsAndMoves(ArrayList<Action> storeOrDestockActions, Map map, Rail startRail) {
	ArrayList<Action> actions = new ArrayList<Action>();
	Rail firstRail = startRail;
	Action nextBestAction = null;
	ArrayList<Rail> rails = null;
	ArrayList<Action> copyActions = new ArrayList<Action>(storeOrDestockActions);
	// tant qu'il reste des actions a traiter
	while (copyActions.size() > 0) {

	    if (copyActions.get(0) instanceof StoreAction) {
		nextBestAction = (StoreAction) copyActions.get(0);
		rails = map.getPath(firstRail, ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess());
	    } else if (copyActions.get(0) instanceof DestockingAction) {
		nextBestAction = (DestockingAction) copyActions.get(0);
		rails = map.getPath(firstRail, ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess());
	    }

	    for (Action action : copyActions) {
		ArrayList<Rail> tmpRails = null;
		if (copyActions.get(0) instanceof StoreAction)
		    tmpRails = map.getPath(firstRail, ((StoreAction) action).getDrawer().getColumn().getAccess());
		else if (copyActions.get(0) instanceof DestockingAction)
		    tmpRails = map.getPath(firstRail, ((DestockingAction) action).getDrawer().getColumn().getAccess());

		if (tmpRails != null && tmpRails.size() < rails.size()) {
		    nextBestAction = action;
		    rails = tmpRails;
		}
	    }

	    // ajout des move actions au total
	    actions.addAll(railsToMoveActions(rails));

	    // ajout de l'action au total
	    actions.add(nextBestAction);

	    if (copyActions.get(0) instanceof StoreAction)
		firstRail = ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess();
	    else if (copyActions.get(0) instanceof DestockingAction)
		firstRail = ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess();

	    copyActions.remove(nextBestAction);
	}
	return actions;
    }

    public ArrayList<MoveAction> railsToMoveActions(ArrayList<Rail> rails) {
	if (rails.isEmpty())
	    return new ArrayList<MoveAction>();

	ArrayList<MoveAction> moves = new ArrayList<MoveAction>();
	Rail previous = rails.get(0);
	for (Rail rail : rails) {
	    if (rails.get(0) == rail)
		continue;
	    MoveAction move = new MoveAction(1000, null, previous, rail);
	    moves.add(move);
	    previous = rail;
	}
	return moves;
    }

    public Robot getBestRobot(ArrayList<Robot> robots) {
	Robot bestRobot = robots.get(0);
	for (Robot r : robots) {
	    if (r.getActions().size() < bestRobot.getActions().size())
		bestRobot = r;
	}

	return bestRobot;
    }

    public Rail getRail(ArrayList<Rail> rails, int x, int y) {
	for (Rail r : rails) {
	    if (r.getX() == x && r.getY() == y) {
		return r;
	    }
	}
	return null;
    }

    public ArrayList<Rail> getCloneRail(ArrayList<Rail> rails) {
	// dupliquer la liste des rails (attention bien dupliquer la liste pour ne pas garder les references)
	ArrayList<Rail> copyRails = new ArrayList<Rail>();
	for (Rail trueRail : rails) {
	    Rail railCopy = new Rail(trueRail.getX(), trueRail.getY(), null, null, null, null);
	    copyRails.add(railCopy);
	}

	for (Rail trueRail : rails) {
	    Rail copyRail = copyRails.get(rails.indexOf(trueRail));
	    if (trueRail.getRightRail() != null)
		copyRail.setRightRail(copyRails.get(rails.indexOf(trueRail.getRightRail())));
	    else if (trueRail.getLeftRail() != null)
		copyRail.setLeftRail(copyRails.get(rails.indexOf(trueRail.getLeftRail())));
	}

	return copyRails;
    }

}
