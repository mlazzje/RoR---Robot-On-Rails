package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.algo.IAlgDestocking;

public class AlgDestockingFifo implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts) {

		ArrayList<Action> actions = new ArrayList<Action>();
		ArrayList<String> stockProductsName = new ArrayList<String>();

		// if destocking FIFO et première commande tjrs pas prête on arrête tout !
		Iterator<Product> itProductsStock = stockProducts.iterator();
		while(itProductsStock.hasNext()) { // Parcours les produits du stocks
			Product currentProductStock = itProductsStock.next();
			stockProductsName.add(currentProductStock.getName());
		}

		Iterator<Order> itOrder = orders.iterator();
		while (itOrder.hasNext()) { // Parcours les commandes
			Order currentOrder = itOrder.next(); // Commande actuelle
			
			if(stockProductsName.containsAll(currentOrder.getProductsName())) {
				
				Iterator<String> itProductName = currentOrder.getProductsName().iterator();
				while (itProductName.hasNext()) { // Parcours les produits de la commande
					String currentProductName = itProductName.next();
					Iterator<Product> itTestProduct = stockProducts.iterator();
					while (itTestProduct.hasNext()) {  // Parcours les produits en stock
						Product currentProduct = itTestProduct.next();
						if (currentProduct.getName().equals(currentProductName) && currentProduct.getStatus()==Product.STORED) {
							DestockingAction currentAction = new DestockingAction(0, null, currentProduct);
							currentProduct.setStatus(Product.BOOKED);
							currentOrder.addProduct(currentProduct);
							actions.add(currentAction);
							break; // on a récupéré le produit, on parcours le produit suivant de la commande
						}
					}
				}
				if(currentOrder.getRatePerform()!=1) { // On test que tous les produits soit affectés à la commande
					System.out.println("Problem with the destocking FIFO : Order ID = " + currentOrder.getIdOrder() + " , Rate Perf = " + currentOrder.getRatePerform());
					return null;
				}
				currentOrder.setStatus(Order.BEING_DESTOCKED);
			}
			else {
				break; // On sort du while, Fifo on regarde pas les autres commandes
			}
		}
		return actions;
	}
}
