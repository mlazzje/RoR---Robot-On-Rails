package ror.core.algo;

import java.util.ArrayList;
import java.util.TimerTask;

import ror.core.Drawer;
import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.StoreAction;

public class AlgMoveEco implements IAlgMove {
    public void updateRobotsActions(ArrayList<Action> newActions, ArrayList<Robot> robots, Map map) {

	Robot robot = getFreeRobot(robots);

	ArrayList<MoveAction> moves = new ArrayList<MoveAction>();
	ArrayList<Rail> rails = new ArrayList<Rail>();

	ArrayList<InputAction> inputActions = new ArrayList<InputAction>();
	for (Action action : newActions) {
	    if (action instanceof InputAction) {
		inputActions.add((InputAction) action);
	    }
	}

	// calcul chemin pour emmener le robot jusqu'au point d'entrée
	if (robot.getLastActionRail() != null) {
	    Rail start = robot.getLastActionRail();
	    Rail end = map.getOutput().getAccess();

	    rails.addAll(map.getPath(start, end));
	    
	    
	    for (Rail rail : rails) {
		int i = rails.indexOf(rail)+1;
		Rail nextRail=null;
		if(rails.size()>i)
			nextRail=rails.get(i);
		MoveAction move = new MoveAction(1000, robot, rail, nextRail);
		robot.addAction(move);
	    }

	    // ajout des inputs actions
	    for (InputAction inputAction : inputActions) {
		while (robot.getLastActionSpaceAvailability() > 0) {
		    robot.addAction(inputAction);
		    inputActions.remove(inputAction);
		}
	    }

	    for (Action action : newActions) {

		if (action instanceof StoreAction) {
		    final StoreAction storeAction = ((StoreAction) action);

		} else if (action instanceof DestockingAction) {
		    final DestockingAction destockingAction = ((DestockingAction) action);

		} else if (action instanceof InputAction) {
		    final InputAction inputAction = ((InputAction) action);

		} else if (action instanceof OutputAction) {
		    final OutputAction outputAction = ((OutputAction) action);

		}
	    }
	}

    }

    public Robot getFreeRobot(ArrayList<Robot> robots) {
	for (Robot robot : robots) {
	    if (robot.getLastActionSpaceAvailability() > 0)
		return robot;
	}
	return null;

    }
}
