package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.StoreAction;

public class AlgMoveEco implements IAlgMove {
    public void updateRobotsActions(ArrayList<Action> newActions, ArrayList<Robot> robots, Map map) {


	ArrayList<StoreAction> storeActions = new ArrayList<StoreAction>();
	for (Action action : newActions) {
	    if (action instanceof StoreAction) {
		storeActions.add((StoreAction) action);
	    }
	}

	// si des actions input ou store sont disponibles
	while (storeActions.size() > 0) {

	    Robot robot = getBestRobot(robots);

	    ArrayList<InputAction> inputActions = new ArrayList<InputAction>();
	    for (StoreAction storeAction : storeActions) {
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
		for (StoreAction storeAction : storeActions) {
		    if (inputAction.getProduct() == storeAction.getProduct()) {
			storeActionsToAffect.add(storeAction);
		    }
		}
	    }
	    storeActions.removeAll(storeActionsToAffect);

	    // ajout des actions de mouvements et de stockage au robot
	    ArrayList<Action> actionMoveAndStore = sortActionsAndMoves(storeActionsToAffect, map, map.getInput().getAccess());
	    for (Action action : actionMoveAndStore) {
		robot.addAction(action);
	    }
	}

    }

    public ArrayList<Action> sortActionsAndMoves(ArrayList<Action> storeOrDestockActions, Map map, Rail startRail) {
	ArrayList<Action> actions = new ArrayList<Action>();
	Rail firstRail = startRail;
	Action nextBestAction = null;
	ArrayList<Rail> rails = null;

	// tant qu'il reste des actions a traiter
	while (storeOrDestockActions.size() > 0) {

	    if (storeOrDestockActions.get(0) instanceof StoreAction) {
		nextBestAction = (StoreAction) storeOrDestockActions.get(0);
		rails = map.getPath(firstRail, ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess());
	    } else if (storeOrDestockActions.get(0) instanceof DestockingAction) {
		nextBestAction = (DestockingAction) storeOrDestockActions.get(0);
		rails = map.getPath(firstRail, ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess());
	    }

	    for (Action action : storeOrDestockActions) {
		ArrayList<Rail> tmpRails = null;
		if (storeOrDestockActions.get(0) instanceof StoreAction)
		    tmpRails = map.getPath(firstRail, ((StoreAction) action).getDrawer().getColumn().getAccess());
		else if (storeOrDestockActions.get(0) instanceof DestockingAction)
		    tmpRails = map.getPath(firstRail, ((DestockingAction) action).getDrawer().getColumn().getAccess());

		if (tmpRails.size() < rails.size()) {
		    nextBestAction = action;
		    rails = tmpRails;
		}
	    }

	    // ajout des move actions au total
	    actions.addAll(railsToMoveActions(rails));

	    // ajout de l'action au total
	    actions.add(nextBestAction);

	    if (storeOrDestockActions.get(0) instanceof StoreAction)
		firstRail = ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess();
	    else if (storeOrDestockActions.get(0) instanceof DestockingAction)
		firstRail = ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess();

	    storeOrDestockActions.remove(nextBestAction);
	}
	return actions;
    }

    public ArrayList<MoveAction> railsToMoveActions(ArrayList<Rail> rails) {
	if(rails.size()==1)
	    return new ArrayList<MoveAction>();
	ArrayList<MoveAction> moves = new ArrayList<MoveAction>();
	Rail nextRail = null;
	//System.out.println(rails);

	for (Rail rail : rails) {
	    int i = rails.indexOf(rail) + 1;
	    nextRail = null;
	    if (rails.size() > i)
		nextRail = rails.get(i);
	    //si il y a un nextRail 
	    if (nextRail != null) {
		MoveAction move = new MoveAction(1000, null, rail, nextRail);
		moves.add(move);
	    }
	}
	return moves;
    }

    public Robot getBestRobot(ArrayList<Robot> robots) {
	Robot best = robots.get(0);
	for (Robot robot : robots) {
	    if (robot.getActions().size() < best.getActions().size())
		best = robot;
	}
	return best;
    }
}
