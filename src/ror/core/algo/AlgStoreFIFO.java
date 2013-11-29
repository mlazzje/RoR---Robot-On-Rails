package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Order;
import ror.core.Product;
import ror.core.RoRElement;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.StoreAction;

public class AlgStoreFIFO implements IAlgStore {
	public ArrayList<Action> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, RoRElement[][] map) {
		// TODO RoRElement[][] map to delete ?
		
		ArrayList<Action> actions = new ArrayList<Action>();

		Iterator<Product> itProduct = inputProducts.iterator();
		while (itProduct.hasNext()) {
			Product currentProduct = itProduct.next();
			if (currentProduct.getStatus() == 0) {
				StoreAction currentAction = new StoreAction(null, null, null, currentProduct);
				actions.add(currentAction);
				currentProduct.setStatus(2); // on met à jour le inputProducts, c'est de la simulation ! C'est algoMove qui mettra a jour le vrai inputproducts
				break; // on sort de la boucle on a trouvé notre produit pas besoin d'aller voir ailleurs
			}
		}
		return actions;
		
	}
}
