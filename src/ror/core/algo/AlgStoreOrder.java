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

public class AlgStoreOrder implements IAlgStore {
	/* (non-Javadoc)
	 * @see ror.core.algo.IAlgStore#getActions(java.util.ArrayList, java.util.ArrayList, ror.core.Map)
	 */
	public ArrayList<StoreAction> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, Map map) {
		
		ArrayList<StoreAction> actions = new ArrayList<StoreAction>(); // init
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
						
						drawer=this.getDrawerFree(currentOrder, map);
						if(drawer!=null) {
        						drawer.setStatus(Drawer.BOOKED);
        						StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
        						actions.add(currentAction);
        						currentProduct.setStatus(Product.BEING_STORED); // on met à jour le inputProducts !
						} else {
						    System.out.println("No Drawer free");
						}
					}
				}	
			}
		}
		return actions;
	}
	
	
	/**
	 * @param order
	 * @param map
	 * @return drawer free / null
	 */
	public Drawer getDrawerFree(Order order,Map map) {
		Drawer drawer = null;
		if(order.getDrawers().size()==0) { // Si aucun drawers réservé on va le faire
			if(!this.bookDrawers(order,map)) { // Si on peut réserver des armoires
				System.out.println("Plus de drawers dispos");
				return null;
			}
		}
		if(order.getDrawers().size() != order.getProductsName().size()) { System.out.println("Eror Algo Destockage : Order ID "+ order.getIdOrder() + " | nb Drawers réservé "+ order.getDrawers().size() + " & nb products Name "+order.getProductsName().size()+" " ) ; }
		// On parcours les drawers de la commande
		for(Drawer drawerTest : order.getDrawers()) {
			if(drawerTest.getStatus()==Drawer.BOOKED_FOR_ORDER) { // Si drawer booked n'as pas de produit
				drawer=drawerTest;
				break;
			}
		}
		return drawer;
	}
	
	/**
	 * @param order
	 * @param map
	 * @return
	 */
	public boolean bookDrawers (Order order,Map map) {
		int nbDrawersToBook = order.getProductsName().size();
		//int nbDrawersDec = order.getProductsName().size();
		int nbDrawersBooked = 0; // Simulation
		Column firstColumn=null,secondColumn=null,thirdColumn=null; // 20 objets par commande ne peut pas être dans plus de 3 colonnes !
		// Simulation here !
		for( Column column : map.getColumns()) {
			if(column.getAvailableDrawer()!=null) {
				if(nbDrawersBooked==nbDrawersToBook) { break; }
				if(nbDrawersBooked>0) { // Tkt pas ça marche ici & Première colonne rempli
					if (nbDrawersToBook-nbDrawersBooked >= 10 && column.getNbAvailableDrawers()!=10 && firstColumn!=null) { // pas assez de place dans la seconde colonne car <10
						nbDrawersBooked=0;
						firstColumn=null; secondColumn=null; thirdColumn=null;
					} else if (nbDrawersToBook-nbDrawersBooked < 10 && firstColumn != null && nbDrawersToBook-nbDrawersBooked>column.getNbAvailableDrawers()) { // pas assez de place dans la seconde colonne
						nbDrawersBooked=0;
						firstColumn=null; secondColumn=null; thirdColumn=null;
					} else if (nbDrawersToBook-nbDrawersBooked < 10 && firstColumn != null && secondColumn != null && nbDrawersToBook-nbDrawersBooked>column.getNbAvailableDrawers()) { // Pas assez de place dans la troisième colonne
						nbDrawersBooked=0;
						firstColumn=null; secondColumn=null; thirdColumn=null;
					} else {
						// Book drawers
						if(column.getNbAvailableDrawers()==10) { // Si colonne peut se remplir entièrement
							if(firstColumn==null) {
								firstColumn=column;
							} else if(secondColumn==null) {
								secondColumn=column;
							} else if(thirdColumn==null) {
								thirdColumn=column;
							}
							// Maj nbDrawersBooked
							if(nbDrawersToBook-nbDrawersBooked<10) { // Voilà
								nbDrawersBooked+=nbDrawersToBook-nbDrawersBooked; // = nbDrawersBooked=nbDrawersToBook;
							} else if (nbDrawersToBook-nbDrawersBooked>=10) {
								nbDrawersBooked+=10;
							}
						} else if (nbDrawersToBook-nbDrawersBooked > column.getNbAvailableDrawers()) { // On remplit la première colonne ou la dernière
							if(firstColumn==null) {
								firstColumn=column;
							} else if(secondColumn==null) {
								secondColumn=column;
							} else if(thirdColumn==null) {
								thirdColumn=column;
							}
							// Maj nbDrawersBooked
							nbDrawersBooked+=nbDrawersToBook-nbDrawersBooked; // = nbDrawersBooked=nbDrawersToBook; // Voilà
						}	
					}
				}
				else if (nbDrawersBooked==0) {
					if(column.getNbAvailableDrawers()>nbDrawersToBook) {
						nbDrawersBooked=nbDrawersToBook;
					}
					else {
						nbDrawersBooked=column.getNbAvailableDrawers();
					}
					firstColumn=column;
				}
			}
		}
		
		if(nbDrawersBooked!=nbDrawersToBook) {
			return false;
		} else {
			// Book Drawers really here !
			int nbDrawersBookedReally = 0; // Reality
			int nbDrawersToBookForColumn=0;
			int nbDrawersAvailable;
			if(firstColumn!=null) {
				nbDrawersAvailable=firstColumn.getNbAvailableDrawers();
				if(nbDrawersToBook-nbDrawersBookedReally<nbDrawersAvailable) { // Voilà
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersToBook-nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
				} else if (nbDrawersToBook-nbDrawersBookedReally>nbDrawersAvailable) {
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersAvailable;
				}
				firstColumn.bookNDrawersOrder(nbDrawersToBookForColumn);
			} 
			if(secondColumn!=null) {
				nbDrawersAvailable=firstColumn.getNbAvailableDrawers();
				if(nbDrawersToBook-nbDrawersBookedReally<nbDrawersAvailable) { // Voilà
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersToBook-nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
				} else if (nbDrawersToBook-nbDrawersBookedReally>nbDrawersAvailable) {
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersAvailable;
				}
				secondColumn.bookNDrawersOrder(nbDrawersToBookForColumn);
			}
			if(thirdColumn!=null) {
				nbDrawersAvailable=firstColumn.getNbAvailableDrawers();
				if(nbDrawersToBook-nbDrawersBookedReally<nbDrawersAvailable) { // Voilà
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersToBook-nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
				} else if (nbDrawersToBook-nbDrawersBookedReally>nbDrawersAvailable) {
					nbDrawersBookedReally+=nbDrawersToBookForColumn=nbDrawersAvailable;
				}
				thirdColumn.bookNDrawersOrder(nbDrawersToBookForColumn);
			}
			return true;
		}
	}
}
