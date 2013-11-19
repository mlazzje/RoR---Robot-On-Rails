package ror.core.actions;

import ror.core.Drawer;

public class StoreAction extends Action {

	private Drawer drawer;

	public StoreAction(Integer duration, Drawer drawer) {
		super(duration);
		this.drawer = drawer;
	}

	public Drawer getDrawer() {
		return drawer;
	}

	public void setDrawer(Drawer drawer) {
		this.drawer = drawer;
	}

}
