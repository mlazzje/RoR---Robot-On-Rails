package core;

import java.util.ArrayList;
import java.util.List;

public class Cabinet {

	// Constants
	private static final int columnNumber = 40;

	// Properties
	private List<Column> columnList;

	// Constructor
	public Cabinet() {
		this.columnList = new ArrayList<Column>(columnNumber);
	}

	// Methods
	public void addColumn(Column c) {
		columnList.add(c);
	}
}
