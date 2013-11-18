package core;

public class Rail extends RoRElement {

	// Properties
	private Rail leftRail;
	private Rail rightRail;
	private Rail previous;
	private Rail next;

	// Setters and getters
	protected Rail getLeftRail() {
		return leftRail;
	}

	protected void setLeftRail(Rail leftRail) {
		this.leftRail = leftRail;
	}

	protected Rail getRightRail() {
		return rightRail;
	}

	protected void setRightRail(Rail rightRail) {
		this.rightRail = rightRail;
	}

	protected Rail getPrevioustRail() {
		return previous;
	}

	protected void setPrevioustRail(Rail previoustRail) {
		this.previous = previoustRail;
	}

	protected Rail getNextRail() {
		return next;
	}

	protected void setNextRail(Rail nextRail) {
		this.next = nextRail;
	}

	// Constructor
	public Rail(Integer x, Integer y, Rail leftRail, Rail rightRail,
			Rail previousRail, Rail nextRail) {
		super(RoRElementTypes.Rail, x, y);
		this.leftRail = leftRail;
		this.rightRail = rightRail;
		this.previous = previousRail;
		this.next = nextRail;
	}

	// Methods

	// TODO MoveAction
}
