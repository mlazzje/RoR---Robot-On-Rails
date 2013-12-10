package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.RoRElement;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.StoreAction;

public class AlgStoreOrder implements IAlgStore {
	public ArrayList<Action> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, Map map) {
		
		// TODO Recommencer cet algo !
		
		ArrayList<Action> actions = new ArrayList<Action>(); // init
		Drawer drawer=null;
		
		Iterator<Product> itProduct = inputProducts.iterator();
		while (itProduct.hasNext()) { // Parcours les produits a stocker
			Product currentProduct = itProduct.next();
			if (currentProduct.getStatus() == Product.FREE) { // Si produit PAS déjà réservé à une action
				Iterator<Order> itOrder = orders.iterator();
				while (itOrder.hasNext()) { // Parcours les commandes
					Order currentOrder = itOrder.next(); // Commande actuelle
					if(currentOrder.wantsProduct(currentProduct.getName())) {
						// On cherche où on doit mettre le produit
						
						StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
						actions.add(currentAction);
						currentProduct.setStatus(Product.BEING_STORED); // on met à jour le inputProducts !
						
						currentOrder.addProductStored(currentProduct);
					}
				}	
			}
		}
		return actions;
	}
	
	public Drawer drawerFree(Order order,Map map) {
		Drawer drawer = null;
		if(order.getDrawers().size()==0) { // Si aucun drawers réservé on va le faire
			if(!this.bookDrawers(order,map)) { // Si on peut réserver des armoires
				System.out.println("Plus de drawers dispos");
			}
		}
		if(order.getDrawers().size() != order.getProductsName().size()) { System.out.println("Eror Algo Destockage : Order ID "+ order.getIdOrder() + " | nb Drawers réservé "+ order.getDrawers().size() + " & nb products Name "+order.getProductsName().size() ) ; }
		// On parcours les drawers de la commande
		for(Drawer drawerTest : order.getDrawers()) {
			if(drawerTest.getStatus() == Drawer.FREE) {
				drawer=drawerTest;
				break;
			}
		}
		return drawer;
	}
	
	public boolean bookDrawers (Order order,Map map) {
		int nbDrawersToBook = order.getProductsName().size();
		for( Column column : map.getColumns()) {
			
		}
		return false;
	}
}
