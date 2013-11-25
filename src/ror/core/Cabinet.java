package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Cabinet {

	private static final int columnNumber = 40;

	private List<Column> columnList;

	public Cabinet() {
		this.columnList = new ArrayList<Column>(columnNumber);
	}

	public void addColumn(Column c) {
		columnList.add(c);
	}
}
