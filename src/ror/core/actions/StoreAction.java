package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;

public class StoreAction extends Action {

	private Drawer drawer;
	private Product product;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
