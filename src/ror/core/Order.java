package ror.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mlazzje
 *
 */
public class Order implements Comparable<Order> {

	/**
	 * Commande passée
	 */
	public static final Integer INIT = 0;
	/**
	 * Tous les produits ne sont pas dispo
	 */
	public static final Integer WAITING = 1;
	/**
	 * Tous les produits sont disponibles et stockés
	 */
	public static final Integer READY_FOR_DESTOCKING = 2;
	/**
	 * Toutes les actions sont créés
	 */
	public static final Integer BEING_DESTOCKED = 3;
	/**
	 * La commande a été livrée
	 */
	public static final Integer DONE = 4;

	/**
	 * 
	 */
	private Integer idOrder;
	/**
	 * TODO Bug : L'ID commence à 3 si cette variable est à 0
	 */
	private static Integer lastIdOrder = 0;
	/**
	 * 
	 */
	private Integer status;
	/**
	 * 
	 */
	private Integer time;
	/**
	 * 
	 */
	private List<String> productsName;
	/**
	 * 
	 */
	private List<Product> products;
	/**
	 * for algoStoreOrder ! DON'T TOUCH THIS ! OR MLAZZJE WILL KILL YOU !
	 */
	private List<Product> productsStored;
	/**
	 * 
	 */
	private List<Drawer> drawers;

	/**
	 * Constructor
	 */
	public Order() {
		Order.lastIdOrder++;
		this.setIdOrder(Order.lastIdOrder);
		this.status = 0;
		this.time = 0;
		this.productsName = new ArrayList<String>();
		this.products = new ArrayList<Product>();
		this.productsStored = new ArrayList<Product>();
		this.drawers = new ArrayList<Drawer>();
	}
	
	
	/**
	 * @return List Drawer of Order
	 */
	public List<Drawer> getDrawers() {
		return drawers;
	}

	/**
	 * 
	 * Set a drawers booked by Order
	 * 
	 * @param drawers
	 */
	public void setDrawers(List<Drawer> drawers) {
		this.drawers = drawers;
	}

	/**
	 * @return Id Order
	 */
	public Integer getIdOrder() {
		return idOrder;
	}

	/**
	 * 
	 * Set idOrder
	 * 
	 * @param idOrder
	 */
	private void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	/**
	 * @return status of Order
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public Integer getTime() {
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return
	 */
	public List<String> getProductsName() {
		return productsName;
	}

	/**
	 * @param productsName
	 */
	public void setProductsName(List<String> productsName) {
		this.productsName = productsName;
	}

	/**
	 * @param productName
	 */
	public void addProductName(String productName) {
		this.productsName.add(productName);
	}

	/**
	 * @return
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @param products
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}

	/**
	 * @param product
	 */
	public void addProduct(Product product) {
		this.products.add(product);
	}

	/**
	 * @param productName
	 * @return boolean true : if Order didn't book yet this productName | false : if Order don't want this productName
	 */
	public boolean wantsProduct(String productName) {
		if (this.nbProductNameWants(productName) > 0 && this.nbProductNameWants(productName) > this.nbProductStored(productName)) {
			return true;
		}
		return false;
	}

	/**
	 * @param productName
	 * @return number of ProductName
	 */
	private int nbProductNameWants(String productName) {
		int nb = 0;
		for (String prodName : this.productsName) {
			if (prodName == productName) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * @param productName
	 * @return number of this productName already stored (booked) // Simulation
	 */
	private int nbProductStored(String productName) {
		int nb = 0;
		for (Product prod : this.productsStored) {
			if (prod.getName() == productName) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * @return List Product stored (booked) // Simulation
	 */
	public List<Product> getProductsStored() {
		return productsStored;
	}

	/**
	 * @param productsStored
	 */
	public void setProductsStored(List<Product> productsStored) {
		this.productsStored = productsStored;
	}

	/**
	 * @param productStored
	 */
	public void addProductStored(Product productStored) {
		this.productsStored.add(productStored);
	}

	/**
	 * @return display for Caligone
	 */
	public String[] toWeirdString() {
		Iterator<String> it = this.productsName.iterator();
		String productsString = "";
		String statusString;
		while (it.hasNext()) {
			String product = it.next();
			productsString = productsString.concat(product);
			if (it.hasNext()) {
				productsString = productsString.concat(", ");
			}

		}

		if (status == 0) {
			statusString = "Initialisée";
		} else if (status == 1) {
			statusString = "En attente";
		} else if (status == 2) {
			statusString = "En cours";
		} else {
			statusString = "Terminé";
		}
		return new String[] { "#" + this.getIdOrder(), productsString, statusString };
	}

	/**
	 * @return return percentage between 0 and 1 of end of the command
	 */
	public float getRatePerform() {
		return this.products.size() / this.productsName.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Order o) {
		int resultat = 0;

		if (this.getRatePerform() > o.getRatePerform())
			resultat = 1;
		if (this.getRatePerform() < o.getRatePerform())
			resultat = -1;
		if (this.getRatePerform() == o.getRatePerform())
			resultat = 0;

		return resultat;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    String s = "\n";
	    s += "Commande "+this.idOrder+"\n";
	    s += "Status : "+this.status+"\n";
	    s += "Time : "+this.time+"\n";
	    s += "ProductsName : "+this.productsName.toString()+"\n";
	    s += "Produits en stocks : " +this.products.toString()+"\n";
	    s += "Drawers associés : "+this.drawers.toString()+"\n";
	    return s;
	}
	
}
