package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;

public class DestockingAction extends Action {

	private Drawer drawer;
	private Product product;

	public DestockingAction(Integer duration, Drawer drawer, Product product) {
		super(duration);
		this.setDrawer(drawer);
		this.setProduct(product);
	}

	public Drawer getDrawer() {
		return drawer;
	}

	public void setDrawer(Drawer drawer) {
		this.drawer = drawer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
