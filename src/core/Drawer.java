package core;

public class Drawer {

	// Properties
	private Integer positionInColumn;
	private Integer status; // 1 = Libre | 0 = Occupé

	// Setters and getters
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

	// Constructor
	public Drawer(Integer positionInColumn) {
		this.positionInColumn = positionInColumn;
		this.status = 1;
	}

}
