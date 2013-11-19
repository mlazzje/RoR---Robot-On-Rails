package ror.core.algo;

import java.util.ArrayList;

import ror.core.Robot;
import ror.core.actions.Action;

public interface IAlgMove {
	public void updateRobotsActions(ArrayList<Action> newActions,
			ArrayList<Robot> robots);
}
