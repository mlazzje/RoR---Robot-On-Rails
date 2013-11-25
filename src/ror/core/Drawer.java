package ror.core;

public class Drawer {

    private Integer positionInColumn;
    private Integer status;
    private Column column;
    private Product product;

    public Drawer(Column column, Integer positionInColumn) {
	this.column = column;
	this.positionInColumn = positionInColumn;
	this.status = 1;
	this.product = null;
    }

    protected Integer getPositionInColumn() {
	return positionInColumn;
    }

    protected void setPositionInColumn(Integer positionInColumn) {
	this.positionInColumn = positionInColumn;
    }

    protected Integer getStatus() {
	return status;
    }

    protected void setStatus(Integer status) {
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
    }

}
