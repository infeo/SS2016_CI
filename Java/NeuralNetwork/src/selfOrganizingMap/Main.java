package selfOrganizingMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import basics.EuclidsDistance;

public class Main {

	public static void main(String [] args){
		//initialise charts
		// A frame contains a panel, which contains a chart. a chart contains a plot. a plot has a Series-Collection, two axes and a rendermethod. a Series collection has diffrent series.
			//make data Series
		XYSeries dataShow = new XYSeries("Datenpunkte");
		XYSeries netShow = new XYSeries("SOM");
		
			//collect the series in collections
		XYSeriesCollection colldata = new XYSeriesCollection();
		colldata.addSeries(dataShow);
		
		XYSeriesCollection collnet = new XYSeriesCollection();
		collnet.addSeries(netShow);
		
			//make charts
				// initialise the axes
		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("y");
		NumberAxis x2 = new NumberAxis("x");
		NumberAxis y2 = new NumberAxis("y");
		
				//select the render methods
		XYDotRenderer dotrender = new XYDotRenderer();
		XYLineAndShapeRenderer linerender = new XYLineAndShapeRenderer();
		linerender.setBaseShapesVisible(false);
		dotrender.setDotHeight(2);
		dotrender.setDotWidth(2);
				//make the plots
		XYPlot plot = new XYPlot();
		plot.setDataset(0,colldata);
		plot.setDataset(1, collnet);
		plot.setDomainAxis(0, x1);
		plot.setDomainAxis(1, x2);
		plot.setRangeAxis(0, y1);
		plot.setRangeAxis(1, y2);
		plot.setRenderer(0, dotrender);
		plot.setRenderer(1,linerender);
				//make the charts
		JFreeChart chart1 = new JFreeChart(plot);
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
		Collection <double []> data = new ArrayList<double []>(2000);
			// find a way to compute the normal distribution
			//add forall u the the datapoints to the collection
		for(int u=0; u<2000;u++){
			double fst = 2* (1+Math.sqrt(u))*Math.sin(u)+gauss(0,0.01*u);
			double snd = -(1+Math.sqrt(u))*Math.cos(u)+gauss(0,0.05*u);
			double [] tmp = {fst,snd};
			data.add(tmp);
			dataShow.add(fst,snd);
		}
		
		//show data
		frame1.setVisible(true);
		
		//initialise SOM adn show it
			// set the size of the SOM
		int size = 100;
			//take 100 randomly chosen centers from our collection
			// a treeSet is chosen to sustain the order
		TreeSet<double []> centers = new TreeSet<double[]>(new Helper());
		Random rand = new Random();
		int tmp;
		for(int i =0; i<size;i++){
			tmp = rand.nextInt(2000);
			double [] cent = ((ArrayList <double []>) data).get(tmp);
			if(! centers.add(cent) ){
				i--;
			};
			netShow.add(cent[0], cent[1]);
		}
		SOM net = new SOM(2,size,new EuclidsDistance(),new OneDimNonCyclic(), centers);
		
		//show Winner
		
		//train SOM
		
		//show training
		
	}

	public static double gauss(double e,double v){
		double x = ThreadLocalRandom.current().nextDouble(1);
		return (1/Math.sqrt(2*v*Math.PI))*Math.exp(-Math.pow(x-e, 2)/(2*v));
	}
}
