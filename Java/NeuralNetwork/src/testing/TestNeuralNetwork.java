package testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import basics.NeuralEntry;
import functions.Fermi;
import functions.Integrate;
import functions.Linear;
import functions.Sum;
import functions.Transfer;
import neuralnetwork.NeuralNetwork;

public class TestNeuralNetwork {

	/*
	 * with help of blablabla
	 */
	private static NeuralNetwork neuralnet;
	private static int sizeData = 1001;
	private static int[] dim = { 1, 10, 1 };
	private static int numHidden = 1;
	private static int maxDepth = 10;
	private static double learningrate = 0.00005;
	private static int learnIterations = 10000;
		
	public static void main(String[] args) {
		XYSeries showError1 = new XYSeries("Error1");
		XYSeries showError2 = new XYSeries("Error2");
		XYSeries showNN = new XYSeries("Ouptut");
		XYSeries showFunc = new XYSeries("Funktion");
		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeriesCollection errset = new XYSeriesCollection();

		errset.addSeries(showError1);
		errset.addSeries(showError2);
		dataset.addSeries(showFunc);
		dataset.addSeries(showNN);

		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("y");
		ApplicationFrame frame1 = new ApplicationFrame("Vergleich von Netzwerk mit Ergebnis");
		XYSplineRenderer spline = new XYSplineRenderer();
		// System.out.println("spline precision = "+(spline.getPrecision()));
		spline.setPrecision(10);
		((XYLineAndShapeRenderer)spline).setBaseShapesVisible(false);
		XYPlot plot1 = new XYPlot(dataset, x1, y1, spline);
		JFreeChart chart1 = new JFreeChart(plot1);
		ChartPanel chartPanel1 = new ChartPanel(chart1);
		frame1.setContentPane(chartPanel1);
		frame1.pack();
		frame1.setVisible(true);

		NumberAxis x2 = new NumberAxis("x");
		NumberAxis y2 = new NumberAxis("y");
		ApplicationFrame frame2 = new ApplicationFrame("Fehlerentwicklung");
		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
		line.setBaseShapesVisible(false);
		XYPlot plot2 = new XYPlot(errset, x2, y2, line);
		JFreeChart chart2 = new JFreeChart(plot2);
		ChartPanel chartPanel2 = new ChartPanel(chart2);
		frame2.setContentPane(chartPanel2);
		frame2.pack();
		frame2.setVisible(true);

		/*
		 * NumberAxis x3 = new NumberAxis("x"); NumberAxis y3 = new
		 * NumberAxis("y"); ApplicationFrame frame3 = new ApplicationFrame(
		 * "Vergleich nach Lernen"); XYSplineRenderer spline2 = new
		 * XYSplineRenderer(); //System.out.println("spline precision = "
		 * +(spline.getPrecision())); spline.setPrecision(10); XYPlot plot3 =
		 * new XYPlot(dataset,x3,y3, spline2); JFreeChart chart3 = new
		 * JFreeChart(plot3); ChartPanel chartPanel3 = new ChartPanel(chart3);
		 * frame3.setContentPane(chartPanel3); frame3.pack();
		 * frame3.setVisible(true);
		 */

		// generate testdata
		Collection<NeuralEntry> testdata = new ArrayList<NeuralEntry>();
		double x, fx;

		for (int i = 0; i < sizeData; i++) {
			x = ThreadLocalRandom.current().nextDouble(-10, 10);
			fx = Math.cos(x / 2) + Math.sin(5 / (Math.abs(x) + 0.2)) - 0.1 * x;
			double[] tmp_in = new double[1];
			tmp_in[0] = x;
			double[] tmp_out = new double[1];
			tmp_out[0] = fx;
			showFunc.add(x, fx);
			testdata.add(new NeuralEntry(tmp_in, tmp_out));
		}

		// generate Network
		
		neuralnet = new NeuralNetwork(dim, numHidden, maxDepth, learningrate);
		Transfer t1 = new Fermi();
		Transfer t2 = new Linear();
		Integrate inte = new Sum();
		neuralnet.setLayer(0, inte, t2);
		neuralnet.setLayer(1, inte, t1);
		neuralnet.setLayer(2, inte, t2);


		// show the untrained network
		// compare(neuralnet, testdata);

		// Show error
		System.out.println(neuralnet.measureMeanError(testdata));

		// Currently the singleLayerTraining method is not working
		/*
		  //train the output layer 
		for(int i=0; i<2000;i++){ 
			trainLayer(testdata, 1); 
			error1.add((double) i,neuron.measureMeanError(testdata)); }
		  
		compare(neuralnet, testdata);
		
		
//		train the hidden layer 
		for(int i=0; i<2000;i++){ 
		  trainLayer(testdata, 0);
		  error2.add((double) i,neuron.measureMeanError(testdata)); }

		compare(neuralnet, testdata);
*/
		// for testing, train the whole net
		for (int i = 0; i < learnIterations; i++) {
			neuralnet.learn(testdata);
			showError1.add((double) i, neuralnet.measureMeanError(testdata));
		}
		// show the trained network
		compare(showNN, testdata);

		System.out.println("End");
	}

	public static void compare(XYSeries chart, Collection<? extends Entry<double[],double[]>> testdata) {
		Iterator<? extends Entry<double[], double[]>> it = testdata.iterator();
		Entry<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			neuralnet.propagate(elem.getKey());
			double[] out = new double[1];
			out = neuralnet.getResult();
			chart.add(elem.getKey()[0], out[0]);
		}
	}

	/**
	 * currently not wokring
	 * @param testdata
	 * @param layer
	 */
	public static void trainLayer(Collection<? extends Entry<double[],double[]>> testdata, int layer) {
		Iterator<? extends Entry<double[], double[]>> it = testdata.iterator();
		Entry<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
//			neuron.stepBackPropagate(elem, layer);

		}
	}

}
