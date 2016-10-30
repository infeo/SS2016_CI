package testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

import basics.Tuple;
import deprecated.NeuralNetwork;
import functions.Fermi;
import functions.Integrate;
import functions.Linear;
import functions.Sum;
import functions.Transfer;

public class TestDeprecated {

	/*
	 * with help of blablabla
	 */
	private static NeuralNetwork neuron;

	public static void main(String[] args) {
		XYSeries error1 = new XYSeries("Error1");
		XYSeries error2 = new XYSeries("Error2");
		XYSeries neuralnet = new XYSeries("Ouptut");
		XYSeries actfunc = new XYSeries("Funktion");
		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeriesCollection errset = new XYSeriesCollection();

		errset.addSeries(error1);
		errset.addSeries(error2);
		dataset.addSeries(actfunc);
		dataset.addSeries(neuralnet);

		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("y");
		ApplicationFrame frame1 = new ApplicationFrame("Vergleich von Netzwerk mit Ergebnis");
		XYSplineRenderer spline = new XYSplineRenderer();
		// System.out.println("spline precision = "+(spline.getPrecision()));
		spline.setPrecision(10);
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
		Collection<Tuple<double[], double[]>> testdata = new ArrayList<Tuple<double[], double[]>>();
		double x, fx;

		for (int i = 0; i < 1001; i++) {
			x = ThreadLocalRandom.current().nextDouble(-10, 10);
			fx = Math.cos(x / 2) + Math.sin(5 / (Math.abs(x) + 0.2)) - 0.1 * x;
			double[] tmp_in = new double[1];
			tmp_in[0] = x;
			double[] tmp_out = new double[1];
			tmp_out[0] = fx;
			actfunc.add(x, fx);
			testdata.add(new Tuple<double[], double[]>(tmp_in, tmp_out));
		}

		// generate Network
		int[] dim = { 10, 1 };
		neuron = new NeuralNetwork(dim, 10, 0.0001);
		Transfer t = new Fermi();
		Transfer t2 = new Linear();
		Integrate inte = new Sum();
		neuron.setLayer(1, 0, inte, t);
		neuron.setLayer(10, 1, inte, t2);

		// show the untrained network
		// compare(neuralnet, testdata);

		// Show error
		System.out.println(neuron.measureMeanError(testdata));

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
		for (int i = 0; i < 1000; i++) {
			neuron.learn(testdata);
			error1.add((double) i, neuron.measureMeanError(testdata));
		}
		// show the trained network
		compare(neuralnet, testdata);

		System.out.println("End");
	}

	public static void compare(XYSeries chart, Collection<Tuple<double[], double[]>> testdata) {
		Iterator<Tuple<double[], double[]>> it = testdata.iterator();
		Tuple<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			neuron.propagate(elem.getFst());
			double[] out = new double[1];
			out = neuron.getResult();
			chart.add(elem.getFst()[0], out[0]);
		}
	}

	public static void trainLayer(Collection<Tuple<double[], double[]>> testdata, int layer) {
		Iterator<Tuple<double[], double[]>> it = testdata.iterator();
		Tuple<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			neuron.stepBackPropagate(elem, layer);

		}
	}

}
