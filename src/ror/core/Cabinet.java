package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Cabinet {

	/**
	 * 
	 * Number of column by cabinet (//TODO encore utile ? On ne le génère pas ?)
	 * 
	 */
	private static final int columnNumber = 40;

	/**
	 * 
	 * List of columns
	 * 
	 */
	private List<Column> columnList;

	/**
	 * 
	 * Cabinet constructor
	 * 
	 */
	public Cabinet() {
		this.columnList = new ArrayList<Column>(columnNumber);
	}

	/**
	 * Add a column to the Cabinet
	 * 
	 * @param Column c
	 */
	public void addColumn(Column c) {
		columnList.add(c);
	}
}
