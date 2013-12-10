package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;

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
		
		ArrayList<Action> actions = new ArrayList<Action>();
		Iterator<Product> itProduct = inputProducts.iterator();
		Drawer drawer = null;
		
		// Pour chaque produit libre du input on va chercher une place de libre
		while (itProduct.hasNext()) {
			Product currentProduct = itProduct.next();
			if (currentProduct.getStatus() == Product.FREE) {
				for (Column currentColumn : map.getColumns()) {
				    drawer = currentColumn.getAvailableDrawer();
					if (drawer!=null) 
					{
					    	currentProduct.setStatus(Product.BEING_STORED); // on passe le produit en attente de stockage
						drawer.setStatus(Drawer.BOOKED); // on réserve le tiroir dans la colonne
						StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
						actions.add(currentAction);
						break;
					}
				}
				if(drawer==null){
				    // Si on arrive ici c'est qu'il n'y a plus de columns disponibles
				    return actions;
				}
			}
		}
		return actions;
		
	}
}
