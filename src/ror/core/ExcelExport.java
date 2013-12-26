package ror.core;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.*;

public class ExcelExport {

    /**
     * @param simulationManager
     */
    public ExcelExport(SimulationManager simulationManager) {

	// Blank workbook
	XSSFWorkbook workbook = new XSSFWorkbook();

	// Create a blank sheet
	XSSFSheet sheet = workbook.createSheet("ROR-Simulation");

		
	// This data needs to be written (Object[])
	TreeMap<String, Object[]> data = new TreeMap<String, Object[]>();
	data.put("1", new Object[] { "Configuration", "", "" });
	data.put("2", new Object[] { "Algorithme de stockage", simulationManager.getiAlgStore().getClass().getSimpleName() });
	data.put("3", new Object[] { "Algorithme de destockage", simulationManager.getiAlgDestocking().getClass().getSimpleName() });
	data.put("4", new Object[] { "Algorithme de mouvement", simulationManager.getiAlgMove().getClass().getSimpleName() });
	data.put("5", new Object[] { "Nombre de robots",simulationManager.getNbRobot()});
	data.put("5", new Object[] { "",""});
	data.put("6", new Object[] { "Nombre de commande traitée",simulationManager.getOrdersDoneCount()});
	data.put("7", new Object[] { "Temps de traitement moyen par commande",simulationManager.getAverageOrderProcessingTime()});
	data.put("8", new Object[] { "Durée de la simulation",simulationManager.getUptime()});
	data.put("9", new Object[] { "Consommation moyenne par commande",simulationManager.getAverageConsumption()});
	data.put("10", new Object[] { "Consommation totale ",simulationManager.getTotalConsumption()});

	// Iterate over data and write to sheet
	Set<String> keyset = data.keySet();
	int rownum = 0;
	for (String key : keyset) {
	    Row row = sheet.createRow(rownum++);
	    Object[] objArr = data.get(key);
	    int cellnum = 0;
	    for (Object obj : objArr) {
		Cell cell = row.createCell(cellnum++);
		if (obj instanceof String)
		    cell.setCellValue((String) obj);
		else if (obj instanceof Integer)
		    cell.setCellValue((Integer) obj);
	    }
	}
	try {
	    // Write the workbook in file system
	    FileOutputStream out = new FileOutputStream(new File("howtodoinjava_demo.xlsx"));
	    workbook.write(out);
	    out.close();
	    System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
