package ror.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * GraphicExport class : Core class that permits us to extract statistics from a simulation
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-12-19
 */
public class GraphicExport {

    /**
     * @param simulationManager
     */
    public GraphicExport(SimulationManager simulationManager, File file) {

	/*
	 * Activity Chart
	 */
	if(simulationManager==null || file==null)
	    return;
	ArrayList<HashMap<Long, Integer>> dataActivityChart = simulationManager.getDataRobotActivity();
	XYSeriesCollection activityResult = new XYSeriesCollection();
	int cptRobot = 0;
	// Parcours des robots
	for (Robot robot : simulationManager.getRobots()) {
	    XYSeries series = new XYSeries("Activité " + robot.toString());
	    // Remplissage des series
	    for (Entry<Long, Integer> e : dataActivityChart.get(cptRobot).entrySet()) {
		series.add(e.getKey() / 1000, e.getValue());
	    }
	    activityResult.addSeries(series);
	    cptRobot++;
	}

	// Configuration du graphique
	NumberAxis xAxis = new NumberAxis("Temps (en seconde)");
	xAxis.setAutoRangeIncludesZero(false);
	NumberAxis yAxis = new NumberAxis("Nombre d'actions en attente");
	yAxis.setAutoRangeIncludesZero(false);

	StandardXYItemRenderer renderer = new StandardXYItemRenderer();
	XYPlot plot = new XYPlot(activityResult, xAxis, yAxis, renderer);
	plot.setBackgroundPaint(Color.lightGray);
	plot.setDomainGridlinePaint(Color.white);
	plot.setRangeGridlinePaint(Color.white);
	plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

	JFreeChart chart = new JFreeChart("Activité des robots", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	chart.addSubtitle(new TextTitle(simulationManager.getiAlgStore().getClass().getSimpleName().replace("Alg", "").replace("Store", "Stockage ") + " - " + simulationManager.getiAlgDestocking().getClass().getSimpleName().replace("Alg", "").replace("Destocking", "Destockage ") + " - " + simulationManager.getiAlgMove().getClass().getSimpleName().replace("Alg", "").replace("Move", "Déplacement ")));
	chart.addSubtitle(new TextTitle("Nombre de commandes traitées : " + simulationManager.getOrdersDoneCount()));
	chart.addSubtitle(new TextTitle("Temps de traitement moyen par commande : " + (simulationManager.getAverageOrderProcessingTime() / 1000) / 60 + "min " + (simulationManager.getAverageOrderProcessingTime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Durée de la simulation : " + (simulationManager.getUptime() / 1000) / 60 + "min " + (simulationManager.getUptime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Consommation moyenne par commande : " + simulationManager.getAverageConsumption() + "W"));
	chart.addSubtitle(new TextTitle("Consommation totale : " + simulationManager.getTotalConsumption() + "W"));

	// Export du diagramme
	BufferedImage objBufferedImage = chart.createBufferedImage(800, 600);
	ByteArrayOutputStream bas = new ByteArrayOutputStream();
	try {
	    ImageIO.write(objBufferedImage, "png", bas);

	    byte[] byteArray = bas.toByteArray();
	    InputStream in = new ByteArrayInputStream(byteArray);
	    BufferedImage image = ImageIO.read(in);
	    File outputfile = new File(file.getAbsolutePath() + "_activityChart.png");
	    ImageIO.write(image, "png", outputfile);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	/*
	 * Consumption Chart
	 */
	HashMap<Long, Integer> dataChartComsumption = simulationManager.getDataConsumption();
	ArrayList<HashMap<Long, Integer>> dataChartComsumptionRobot = simulationManager.getDataRobotConsumption();
	XYSeriesCollection consumptionResult = new XYSeriesCollection();
	XYSeries series = new XYSeries("Consommation totale");
	Iterator<Entry<Long, Integer>> it = dataChartComsumption.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<Long, Integer> e = it.next();
	    series.add(e.getKey() / 1000, e.getValue());
	}
	consumptionResult.addSeries(series);

	cptRobot = 0;
	// Parcours des robots
	for (Robot robot : simulationManager.getRobots()) {
	    series = new XYSeries("Consommation " + robot.toString());
	    // Remplissage des series
	    for (Entry<Long, Integer> e : dataChartComsumptionRobot.get(cptRobot).entrySet()) {
		series.add(e.getKey() / 1000, e.getValue());
	    }
	    consumptionResult.addSeries(series);
	    cptRobot++;
	}

	xAxis = new NumberAxis("Temps (en seconde)");
	xAxis.setAutoRangeIncludesZero(false);
	yAxis = new NumberAxis("Consommation (en Watt)");
	yAxis.setAutoRangeIncludesZero(false);

	renderer = new StandardXYItemRenderer();
	plot = new XYPlot(consumptionResult, xAxis, yAxis, renderer);
	plot.setBackgroundPaint(Color.lightGray);
	plot.setDomainGridlinePaint(Color.white);
	plot.setRangeGridlinePaint(Color.white);
	plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

	chart = new JFreeChart("Consommation", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	chart.addSubtitle(new TextTitle(simulationManager.getiAlgStore().getClass().getSimpleName().replace("Alg", "").replace("Store", "Stockage ") + " - " + simulationManager.getiAlgDestocking().getClass().getSimpleName().replace("Alg", "").replace("Destocking", "Destockage ") + " - " + simulationManager.getiAlgMove().getClass().getSimpleName().replace("Alg", "").replace("Move", "Déplacement ")));
	chart.addSubtitle(new TextTitle("Nombre de commandes traitées : " + simulationManager.getOrdersDoneCount()));
	chart.addSubtitle(new TextTitle("Temps de traitement moyen par commande : " + (simulationManager.getAverageOrderProcessingTime() / 1000) / 60 + "min " + (simulationManager.getAverageOrderProcessingTime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Durée de la simulation : " + (simulationManager.getUptime() / 1000) / 60 + "min " + (simulationManager.getUptime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Consommation moyenne par commande : " + simulationManager.getAverageConsumption() + "W"));
	chart.addSubtitle(new TextTitle("Consommation totale : " + simulationManager.getTotalConsumption() + "W"));

	// Export du diagramme
	objBufferedImage = chart.createBufferedImage(800, 600);
	bas = new ByteArrayOutputStream();
	try {
	    ImageIO.write(objBufferedImage, "png", bas);

	    byte[] byteArray = bas.toByteArray();
	    InputStream in = new ByteArrayInputStream(byteArray);
	    BufferedImage image = ImageIO.read(in);
	    File outputfile = new File(file.getAbsolutePath() + "_consumptionChart.png");
	    ImageIO.write(image, "png", outputfile);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	/* Order Chart */
	HashMap<Long, Integer> dataChartOrder = simulationManager.getDataOrder();
	HashMap<Long, Integer> dataChartOrderTotal = simulationManager.getDataOrderTotal();
	XYSeriesCollection orderResult = new XYSeriesCollection();
	series = new XYSeries("Commandes Traitées");
	it = dataChartOrder.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<Long, Integer> e = it.next();
	    series.add(e.getKey() / 1000, e.getValue());
	}
	orderResult.addSeries(series);

	series = new XYSeries("Commandes Totales");
	it = dataChartOrderTotal.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<Long, Integer> e = it.next();
	    series.add(e.getKey() / 1000, e.getValue());
	}
	orderResult.addSeries(series);

	xAxis = new NumberAxis("Temps (en seconde)");
	xAxis.setAutoRangeIncludesZero(false);
	yAxis = new NumberAxis("Nombre de Commandes");
	yAxis.setAutoRangeIncludesZero(false);

	renderer = new StandardXYItemRenderer();
	plot = new XYPlot(orderResult, xAxis, yAxis, renderer);
	plot.setBackgroundPaint(Color.lightGray);
	plot.setDomainGridlinePaint(Color.white);
	plot.setRangeGridlinePaint(Color.white);
	plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

	chart = new JFreeChart("Commandes", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	chart.addSubtitle(new TextTitle(simulationManager.getiAlgStore().getClass().getSimpleName().replace("Alg", "").replace("Store", "Stockage ") + " - " + simulationManager.getiAlgDestocking().getClass().getSimpleName().replace("Alg", "").replace("Destocking", "Destockage ") + " - " + simulationManager.getiAlgMove().getClass().getSimpleName().replace("Alg", "").replace("Move", "Déplacement ")));
	chart.addSubtitle(new TextTitle("Nombre de commandes traitées : " + simulationManager.getOrdersDoneCount()));
	chart.addSubtitle(new TextTitle("Temps de traitement moyen par commande : " + (simulationManager.getAverageOrderProcessingTime() / 1000) / 60 + "min " + (simulationManager.getAverageOrderProcessingTime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Durée de la simulation : " + (simulationManager.getUptime() / 1000) / 60 + "min " + (simulationManager.getUptime() / 1000) % 60 + "s"));
	chart.addSubtitle(new TextTitle("Consommation moyenne par commande : " + simulationManager.getAverageConsumption() + "W"));
	chart.addSubtitle(new TextTitle("Consommation totale : " + simulationManager.getTotalConsumption() + "W"));

	// Export du diagramme
	objBufferedImage = chart.createBufferedImage(800, 600);
	bas = new ByteArrayOutputStream();
	try {
	    ImageIO.write(objBufferedImage, "png", bas);

	    byte[] byteArray = bas.toByteArray();
	    InputStream in = new ByteArrayInputStream(byteArray);
	    BufferedImage image = ImageIO.read(in);
	    File outputfile = new File(file.getAbsolutePath() + "_orderChart.png");
	    ImageIO.write(image, "png", outputfile);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
