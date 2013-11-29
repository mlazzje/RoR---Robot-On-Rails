package ror.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order implements Comparable<Order> {

    public static final Integer INIT = 0; // Commande passée
    public static final Integer WAITING = 1; // Tous les produits ne sont pas dispo
    public static final Integer READY_FOR_DESTOCKING = 2; // Tous les produits sont disponibles et stockés
    public static final Integer ACTIONNED = 3; // Toutes les actions sont créés
    public static final Integer DONE = 4; // La commande a été livrée

    private Integer idOrder;
    private static Integer lastIdOrder = 0;
    private Integer status;
    private Integer time;
    private List<String> productsName;
    private List<Product> products;

    public Order() {
	Order.lastIdOrder++;
	this.setIdOrder(Order.lastIdOrder);
	this.status = 0;
	this.time = 0;
	this.productsName = new ArrayList<String>();
	this.products = new ArrayList<Product>();

    }

    public Integer getIdOrder() {
	return idOrder;
    }

    private void setIdOrder(int idOrder) {
	this.idOrder = idOrder;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Integer getTime() {
	return time;
    }

    public void setTime(int time) {
	this.time = time;
    }

    public List<String> getProductsName() {
	return productsName;
    }

    public void setProductsName(List<String> productsName) {
	this.productsName = productsName;
    }

    public void addProductName(String productName) {
	this.productsName.add(productName);
    }

    public List<Product> getProducts() {
	return products;
    }

    public void setProducts(List<Product> products) {
	this.products = products;
    }

    public void addProduct(Product product) {
	this.products.add(product);
    }

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

    public float getRatePerform() {
	return this.products.size() / this.productsName.size();
    }

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
}
