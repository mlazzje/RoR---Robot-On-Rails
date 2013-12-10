package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.StoreAction;

public class AlgStoreFifo implements IAlgStore {
	public ArrayList<Action> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, Map map) {
		
		ArrayList<Action> actions = new ArrayList<Action>(); // init
		Drawer drawer=null;
		
		Iterator<Product> itProduct = inputProducts.iterator();
		while (itProduct.hasNext()) {
			Product currentProduct = itProduct.next();
			if (currentProduct.getStatus() == Product.FREE) {
				for (Column currentColumn : map.getColumns()) {
					if (currentColumn.getAvailableDrawer()!=null) {
						drawer=currentColumn.getAvailableDrawer();
						StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
						actions.add(currentAction);
						currentProduct.setStatus(Product.BEING_STORED); // on met à jour le inputProducts.
						drawer.setStatus(Drawer.BOOKED); // on met à jour le drawer
						break;
					}
				}
				if(drawer==null) {
					break;
				}
			}
		}
		return actions;
		
	}
}
