package selfOrganizingMap;

import java.awt.Color;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
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
		XYSeries netShow = new XYSeries("SOM",false);
		XYSeries winInpShow = new XYSeries("Inputpunkt");
		XYSeries winNeuShow	= new XYSeries("Winner-Neuron");
		XYSeries learnShow = new XYSeries("trainiertes SOM",false);
		
			//collect the series in collections
		XYSeriesCollection colldata = new XYSeriesCollection();
		colldata.addSeries(dataShow);
		
		XYSeriesCollection collnet = new XYSeriesCollection();
		collnet.addSeries(netShow);
		collnet.addSeries(learnShow);
		
		XYSeriesCollection collwin = new XYSeriesCollection();
		collwin.addSeries(winInpShow);
		collwin.addSeries(winNeuShow);

			//make charts
				// initialise the axes
		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("y");
		
				//select the render methods
		XYDotRenderer dotrender = new XYDotRenderer();
		dotrender.setDotHeight(2);
		dotrender.setDotWidth(2);
		XYDotRenderer dotrender2 = new XYDotRenderer();
		dotrender2.setDotHeight(5);
		dotrender2.setDotWidth(5);
		XYLineAndShapeRenderer linerender = new XYLineAndShapeRenderer();
		linerender.setBaseShapesVisible(false);

				//make the plots
		XYPlot plot = new XYPlot();
				//plot for the data
		plot.setDataset(0,colldata);
		plot.setDomainAxis(0, x1);
		plot.setRangeAxis(0, y1);
		plot.setRenderer(0, dotrender);
				//plot for the SOM
		plot.setDataset(1, collnet);
		plot.setRenderer(1,linerender);
				//the plot for the winnerneurons
		plot.setDataset(2, collwin);
		plot.setRenderer(2,dotrender2);
		
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		plot.setBackgroundPaint(new Color(220,220,225));
		
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
			//size of the data
		int sizeD = 1001;
			// initialise collection
		Collection <double []> data = new ArrayList<double []>(sizeD);
			// find a way to compute the normal distribution
			//add forall u the the datapoints to the collection
		double u;
		double [] init = {0,1};
		data.add(init);
		for(int i=1; i<sizeD;i++){
			u=i*0.02;
			double fst = 2* (1+Math.sqrt(u))*Math.sin(u)+gauss(0,0.01*u);
			double snd = -(1+Math.sqrt(u))*Math.cos(u)+gauss(0,0.05*u);
			double [] tmp = {fst,snd};
			data.add(tmp);
			dataShow.add(fst,snd);
		}
		
		//show data
		frame1.setVisible(true);
		
		//initialise SOM adn show it
			// set the size and learningrate of the SOM
		int size = 100;
		double learn = 0.05;
			//take 100 randomly chosen centers from our collection
			// a treeSet is chosen to sustain the order
		Collection<double []> centers = new HashSet<double[]>();
		Random rand = new Random(LocalTime.now().getNano());
		int tmp;
		for(int i =0; i<size;i++){
			tmp = rand.nextInt(sizeD);
			double [] cent = ((ArrayList <double []>) data).get(tmp);
			if(! centers.add(cent) ){
				i--;
			}
			netShow.add(cent[0], cent[1]);
			
		}
		SOM net = new SOM(2, size, learn, new EuclidsDistance(), new OneDimNonCyclic(), centers);
		
		//show Winners
		int win;
		for(int i =100; i<1000; i+=100){
			double [] point = ((ArrayList<double[]>) data).get(i);
			win = net.computeWinner(point);
			double [] neuron = net.getCenter(win);
			winInpShow.add(point[0],point[1]);
			winNeuShow.add(neuron [0], neuron [1]);
			try{
				Thread.sleep(1250);
			}
			catch (InterruptedException e){
				System.err.println("Thread Suspended:" + e.getMessage());
			}
			winInpShow.clear();
			winNeuShow.clear();
		}
		
		//train SOM
		
		for(int i=0; i<2000; i++) net.learn(data, i);
		
		try{
			Thread.sleep(1500);
		}
		catch (InterruptedException e){
			System.err.println("Thread Suspended:" + e.getMessage());
		}
		
		//show training
		linerender.setSeriesVisible(0, false);
		for(int i=0; i<size;i++){
			double cent [] = net.getCenter(i);
			learnShow.add(cent[0], cent[1]);
		}
		
	}

	public static double gauss(double e,double v){
		double x = ThreadLocalRandom.current().nextDouble(1);
		return (1/Math.sqrt(2*v*Math.PI))*Math.exp(-Math.pow(x-e, 2)/(2*v));
	}
}
