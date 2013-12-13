package ror.core;

import java.util.Observable;

public class Drawer extends Observable {

	public static final Integer FREE = 0;
	public static final Integer BOOKED = 1;

	private Integer positionInColumn;
	private Integer status;
	private Column column;
	private Product product;

	public Drawer(Column column, Integer positionInColumn) {
		this.column = column;
		this.positionInColumn = positionInColumn;
		this.status = FREE;
		this.product = null;
		this.addObserver(column);
	}

	protected Integer getPositionInColumn() {
		return positionInColumn;
	}

	protected void setPositionInColumn(Integer positionInColumn) {
		this.positionInColumn = positionInColumn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product == null)
			this.status = FREE;
		this.setChanged();
		this.notifyObservers();
	}

}
