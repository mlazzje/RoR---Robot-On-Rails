package ror.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Cabinet class : Core class that represents a cabinet
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
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
     * @param Column
     *            c
     */
    public void addColumn(Column c) {
	columnList.add(c);
    }
}
