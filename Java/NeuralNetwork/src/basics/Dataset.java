package basics;

import org.jfree.data.xy.*;

/**
 *
 * @author rasch
 */
public class Dataset {

	public static DefaultXYDataset dataset(double[][] A) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("xy", A); // A wird unter dem Namen xy abgespeichert
		return dataset;
	}

	public static XYSeriesCollection dataset(double[] x, double[] y) {
		// Bezeichung Punkte1 erscheint in der Legende
		XYSeries series1 = new XYSeries("Punkte1");
		for (int i = 0; i < x.length / 2; i++)
			series1.add(x[i], y[i]);

		// Bezeichung Punkte2 erscheint in der Legende
		XYSeries series2 = new XYSeries("Punkte2");
		for (int i = x.length / 2; i < x.length; i++)
			series2.add(x[i], y[i]);

		// Zusammenfassen der beiden Serien
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(series1);
		dataset2.addSeries(series2);
		return dataset2;
	}
}
