package ror.core.actions;

import ror.core.Drawer;

public class DestockingAction extends Action {

	private Drawer drawer;

	public DestockingAction(Integer duration) {
		super(duration);
	}

	public Drawer getDrawer() {
		return drawer;
	}

	public void setDrawer(Drawer drawer) {
		this.drawer = drawer;
	}

}
