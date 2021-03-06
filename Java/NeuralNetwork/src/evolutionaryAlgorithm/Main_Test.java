package evolutionaryAlgorithm;

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

import basics.PointwiseDistance;

public class Main_Test {

	public static void main(String[] args) {
		// graphics
		XYSeries showFx = new XYSeries("f(x)", true);
		XYSeries showGx = new XYSeries("g(x)", true);
		XYSeries showBest = new XYSeries("Beste Approximation", true);

		XYSeriesCollection collFx = new XYSeriesCollection();
		collFx.addSeries(showFx);

		XYSeriesCollection collGx = new XYSeriesCollection();
		collGx.addSeries(showGx);
		collGx.addSeries(showBest);

		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("Function Output");

		XYLineAndShapeRenderer dotrender = new XYLineAndShapeRenderer();

		XYSplineRenderer spline = new XYSplineRenderer();
		spline.setPrecision(10);
		spline.setBaseShapesVisible(false);

		XYPlot plot = new XYPlot();
		plot.setDataset(0, collFx);
		plot.setDomainAxis(0, x1);
		plot.setRangeAxis(0, y1);
		plot.setRenderer(0, spline);
		plot.setDataset(1, collGx);
		plot.setRenderer(1, dotrender);

		JFreeChart chart1 = new JFreeChart(plot);
		ChartPanel chartPanel1 = new ChartPanel(chart1);
		ApplicationFrame frame1 = new ApplicationFrame("Keine Ahnung");
		frame1.setContentPane(chartPanel1);
		frame1.pack();

		// generate Data
		int datasize = 1001;
		int approx = 21;
		int bottom = -10;
		int ceil = 10;
		int intervalsize = Math.abs(bottom - ceil);

		double[] dataX = new double[1001];
		double[] dataFx = new double[1001];
		double[] dataGx = new double[approx];
		double[] aim = new double[approx];
		double x, fx, gx;
		double err = 0;
		for (int i = 0; i < datasize; i++) {
			x = i * (((double) intervalsize) / ((double) datasize - 1)) - intervalsize / 2;
			fx = 4 * Math.cos(x / 5) + Math.sin(1 / (2 * Math.abs(x) + 0.25)) + 1;
			if (x - Math.floor(x) == 0) {
				gx = ThreadLocalRandom.current().nextDouble();
				dataGx[(int) x + intervalsize / 2] = gx;
				aim[(int) x + intervalsize / 2] = fx;
				showGx.add(x, gx);
				err += Math.abs(fx - gx);
			}
			dataX[i] = x;
			dataFx[i] = fx;
			showFx.add(x, fx);
		}
		err /= 1001;
		System.out.println("Durchschnittlicher Fehler:" + err);
		// show data
		frame1.setVisible(true);

		int populationsize=200;
		double elitefactor= 0.4;
		double mutation =0.1;
		Evo strategy = new Evo(approx, populationsize, (int) elitefactor*populationsize, mutation, 10, new PointwiseDistance(aim));
		strategy.adoptAdaptDevelop(100000);
		double[] result = strategy.getBest();

		for (int i = 0; i < approx; i++) {
			showBest.add(i - intervalsize / 2, result[i]);
		}

	}

}
