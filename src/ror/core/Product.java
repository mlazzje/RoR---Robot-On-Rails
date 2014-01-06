package ror.core;

import java.util.UUID;

/**
 * Product class : Core class that represents a product
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Product {

    /**
     * Status done of product, if destock
     */
    public static final Integer DONE = 4;
    /**
     * Status booked for and order
     */
    public static final Integer BOOKED = 3;
    /**
     * Status stored in stock
     */
    public static final Integer STORED = 2;
    /**
     * Status being stored in stock
     */
    public static final Integer BEING_STORED = 1;
    /**
     * Status free of product
     */
    public static final Integer FREE = 0;
    /**
     * Status waiting of product
     */
    public static final Integer WAITING = -1;

    /**
     * Product name
     */
    private String name;
    /**
     * Product status
     */
    private Integer status;
    /**
     * Order that owns this product
     */
    private Order order;
    /**
     * Drawer that contains thi product
     */
    private Drawer drawer;
    /**
     * product ID
     */
    private UUID id;

    /**
     * Constructor
     * 
     * @param name
     */
    public Product(String name) {
	id = java.util.UUID.randomUUID();
	this.name = name;
	this.status = FREE;
	this.order = null;
	this.drawer = null;
    }

    /**
     * @return product ID
     */
    public UUID getId() {
	return id;
    }

    /**
     * @return product name
     */
    public String getName() {
	return name;
    }

    /**
     * @return drawer that contains the product
     */
    public Drawer getDrawer() {
	return drawer;
    }

    /**
     * Set product name
     * 
     * @param name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Set drawer that contains this product
     * 
     * @param drawer
     */
    public void setDrawer(Drawer drawer) {
	if (drawer != null && drawer.getProduct() != null && drawer.getProduct() != this)
	    System.out.println("Erreur tiroir plein");
	this.drawer = drawer;
    }

    /**
     * Return the status of products
     * 
     * -1 = Waiting 0 = Free 1 = Being stored 2 = Stored 3 = Booked (for an order) 4 = Done (in Ouput !)
     * 
     * @return The status 0 = Free, 1 = Waiting, 2 = Booked, 3 = In progress, 4 = Done
     * @see Diagramme Etats-transitions "Statut product"
     */
    public Integer getStatus() {
	return status;
    }

    /**
     * Set the status of products
     * 
     * -1 = Waiting 0 = Free 1 = Being stored 2 = Stored 3 = Booked (for an order) 4 = Done (in Ouput !)
     * 
     * @see Diagramme Etats-transitions "Statut product"
     */
    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     * @return Order that owns this product
     */
    public Order getOrder() {
	return order;
    }

    /**
     * Set order that owns this product
     * 
     * @param order
     */
    public void setOrder(Order order) {
	this.order = order;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Product [name=" + name + ", status=" + status + "]";
    }

}
