package ror.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Order class : Core class that represents an order
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Order implements Comparable<Order> {

	/**
	 * Order placed
	 */
	public static final Integer INIT = 0;
	/**
	 * All products aren't ready
	 */
	public static final Integer WAITING = 1;
	/**
	 * All products are stocked and ready for destocking
	 */
	public static final Integer READY_FOR_DESTOCKING = 2;
	/**
	 * All actions are created for all products in order, being destocked
	 */
	public static final Integer BEING_DESTOCKED = 3;
	/**
	 * Order shipped
	 */
	public static final Integer DONE = 4;

	/**
	 * Id Order
	 */
	private Integer idOrder;
	/**
	 * Last If Order
	 */
	private static Integer lastIdOrder = 0;
	/**
	 * Order status
	 */
	private Integer status;
	/**
	 * Time of processing order
	 */
	private Long processingTime;

	/**
	 * List of products name in Order
	 */
	private List<String> productsName;
	/**
	 * List of real products booked for order
	 */
	private List<Product> products;
	/**
	 * for algoStoreOrder ! DON'T TOUCH THIS ! OR MLAZZJE WILL KILL YOU !
	 */
	private List<Product> productsStored;
	/**
	 * List of drawers booked for order
	 */
	private List<Drawer> drawers;

	/**
	 * Constructor
	 */
	public Order() {
		Order.lastIdOrder++;
		this.setIdOrder(Order.lastIdOrder);
		this.status = 0;
		this.processingTime = (long) 0;
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
	 * @return the processing time
	 */
	public Long getProcessingTime() {
		return processingTime;
	}

	/**
	 * Set processing Time
	 * 
	 * @param processingTime
	 */
	public void setProcessingTime(Long processingTime) {
		this.processingTime = processingTime;
	}

	/**
	 * 
	 * Set drawers booked by Order
	 * 
	 * @param drawers
	 */
	public void setDrawers(List<Drawer> drawers) {
		this.drawers = drawers;
	}

	/**
	 * @param drawer
	 */
	public void addDrawer(Drawer drawer) {
		this.drawers.add(drawer);
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
	 *            Add product to the list of product of the order, set the order of the product and set status of product to BOOKED
	 */
	public void addProduct(Product product) {
		product.setStatus(Product.BOOKED);
		product.setOrder(this);

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
	 * @return number of this productName already booked (being stored)
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

		if (status == Order.INIT) {
			statusString = "Initialisée";
		} else if (status == Order.WAITING) {
			statusString = "En attente";
		} else if (status == Order.READY_FOR_DESTOCKING) {
			statusString = "Prêt à être destocké";
		} else if (status == Order.BEING_DESTOCKED) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Order o) {
		Integer s1 = 0, s2 = 0;
		switch (this.status) {
		case 3:
			s1 = 0;
			break;
		case 2:
			s1 = 1;
			break;
		case 1:
			s1 = 2;
			break;
		case 0:
			s1 = 3;
			break;
		case 4:
			s1 = 4;
			break;
		}
		switch (o.status) {
		case 3:
			s2 = 0;
			break;
		case 2:
			s2 = 1;
			break;
		case 1:
			s2 = 2;
			break;
		case 0:
			s2 = 3;
			break;
		case 4:
			s2 = 4;
			break;
		}
		return s1.compareTo(s2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "\n";
		s += "Commande " + this.idOrder + "\n";
		s += "Status : " + this.status + "\n";
		s += "ProductsName : " + this.productsName.toString() + "\n";
		s += "Produits en stocks : " + this.products.toString() + "\n";
		s += "Drawers associés : " + this.drawers.toString() + "\n";
		return s;
	}

	public static void resetLastId() {
		Order.lastIdOrder = 0;
	}
	
	/*
	 * Method use for AlgoDestocking order.
	 * 
	 * Return true if all Drawers of order (booked in algoStoreOrder) are full (with a product), else return false
	 */
	public boolean isReadyForDestocking() {
	    int nbDrawerFull=0;
	    for (Drawer d : this.getDrawers()) {
		if(d.getProduct()!=null) {
		    nbDrawerFull++;
		}
	    }
	    if(nbDrawerFull==this.getProductsName().size()) {
		return true;
	    }
	    else {
		return false;
	    }
	}
}
