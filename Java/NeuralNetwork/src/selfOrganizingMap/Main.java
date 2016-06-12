package selfOrganizingMap;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.plot.XYPlot;

import neuralnetwork.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	public static void main(String [] args){
		//initialise charts
		// A frame contains a panel, which contains a chart. a chart contains a plot. a plot has a Series-Collection, two axes and a rendermethod. a Series collection has diffrent series.
			//make data Series
		XYSeries data = new XYSeries("Datenpunkte");
		
			//collect the series in collections
		XYSeriesCollection coldata = new XYSeriesCollection();
		coldata.addSeries(data);
		
			//make charts
				// initialise the axes
		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("y");
		
				//select the render methods
		XYDotRenderer dotrender = new XYDotRenderer();
		dotrender.setDotHeight(2);
		dotrender.setDotWidth(2);
				//make the plots
		XYPlot plot1 = new XYPlot(coldata,x1,y1,dotrender);		
				//make the charts
		JFreeChart chart1 = new JFreeChart(plot1);
				//make the windows
		ChartPanel chartPanel1 = new ChartPanel(chart1);
				//set Frame
					//make it
		ApplicationFrame frame1 = new ApplicationFrame("Keine Ahnung");
					//assign the chart and make the thing visible
		frame1.setContentPane(chartPanel1);
		frame1.pack();
		
		
		//Generate Data
			// initialise collection
		Collection <Tuple<Double,Double>> testdata = new ArrayList<Tuple<Double,Double>>();
			// find a way to compute the normal distribution
			//add forall u the the datapoints to the collection
		for(int u=0; u<2000;u++){
			double fst = 2* (1+Math.sqrt(u))*Math.sin(u)+gauss(0,0.01*u);
			double snd = -(1+Math.sqrt(u))*Math.cos(u)+gauss(0,0.05*u);
			testdata.add(new Tuple <Double,Double> (fst,snd));
			data.add(fst,snd);
		}
		
		//show data
		frame1.setVisible(true);
		
		//initialise SOM
		
		//show SOM
		
		//show Winner
		
		//train SOM
		
		//show training
		
	}
	
	public static double gauss(double e,double v){
		double x = ThreadLocalRandom.current().nextDouble(1);
		return (1/Math.sqrt(2*v*Math.PI))*Math.exp(-Math.pow(x-e, 2)/(2*v));
	}
}
