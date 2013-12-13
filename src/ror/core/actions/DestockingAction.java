package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

public class DestockingAction extends Action {

	private Drawer drawer;
	private Product product;

	public DestockingAction(Integer duration, Robot robot, Product product) {
		super(duration, robot);
		this.setDrawer(product.getDrawer());
		this.setProduct(product);
		this.getProduct().setDrawer(drawer);
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
