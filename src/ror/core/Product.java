package ror.core;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Product {

    public static final Integer DONE = 4;
    public static final Integer BOOKED = 3;
    public static final Integer STORED = 2;
    public static final Integer BEING_STORED = 1;
    public static final Integer FREE = 0;
    public static final Integer WAITING = -1;

    private String name;
    private Integer status;
    private Order order;
    private Drawer drawer;
    private UUID id;

    public Product(String name) {
	id = java.util.UUID.randomUUID();
	this.name = name;
	this.status = FREE;
	this.order = null;
	this.drawer = null;
    }

    public UUID getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public Drawer getDrawer() {
	return drawer;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setDrawer(Drawer drawer) {
	if (drawer != null && drawer.getProduct() != null && drawer.getProduct() != this)
	    System.out.println("Erreur tiroir plein");
	this.drawer = drawer;
    }

    /**
     * Return the status of products
     * 
     * 0 = Free 1 = Waiting (not in stock) 2 = Booked (for an order) 3 = In progress (assigned to an action of destocking) 4 = Done (in Ouput !)
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
     * 0 = Free 1 = Waiting (not in stock) 2 = Booked (for an order) 3 = In progress (assigned to an action of destocking) 4 = Done (in Ouput !)
     * 
     * @return The status 0 = Free, 1 = Waiting, 2 = Booked, 3 = In progress, 4 = Done
     * @see Diagramme Etats-transitions "Statut product"
     */
    public void setStatus(Integer status) {
	this.status = status;
    }

    public Order getOrder() {
	return order;
    }

    public void setOrder(Order order) {
	this.order = order;
    }

    @Override
    public String toString() {
	return "Product [name=" + name + ", status=" + status + "]";
    }

}
