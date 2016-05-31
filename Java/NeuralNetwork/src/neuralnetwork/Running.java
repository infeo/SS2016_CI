package neuralnetwork;

import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.xy.*;

import java.util.Collection;
import java.util.Iterator;

import org.jfree.chart.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.data.xy.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;

public class Running {

/*
 * with help of blablabla
 */
	private static NeuralNetwork neuron;
	
	public static void main(String[] args) {
		XYSeries error1 = new XYSeries("Error1");
		XYSeries error2 = new XYSeries("Error2");
		XYSeries neuralnet = new XYSeries("Ouptut");
		XYSeries actfunc = new XYSeries ("Funktion");
		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeriesCollection errset = new XYSeriesCollection();
		
		errset.addSeries(error1);
		errset.addSeries(error2);
		dataset.addSeries(actfunc);
		dataset.addSeries(neuralnet);
		
        NumberAxis x1 = new NumberAxis("x");
        NumberAxis y1 = new NumberAxis("y");
		ApplicationFrame frame1 = new ApplicationFrame("Fehlerentwicklung");
        XYSplineRenderer spline = new XYSplineRenderer();
        //System.out.println("spline precision = "+(spline.getPrecision()));
        spline.setPrecision(10);
        XYPlot plot1 = new XYPlot(dataset,x1,y1, spline);
        JFreeChart chart1 = new JFreeChart(plot1);
        ChartPanel chartPanel1 = new ChartPanel(chart1);
        frame1.setContentPane(chartPanel1);
        frame1.pack();
        frame1.setVisible(true);
        
        
        NumberAxis x2 = new NumberAxis("x");
        NumberAxis y2 = new NumberAxis("y");
        ApplicationFrame frame2 = new ApplicationFrame("Punkte mit Linien verbunden (XYPlot)");
        XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
        XYPlot plot2 = new XYPlot(errset,x2,y2, line);
        JFreeChart chart2 = new JFreeChart(plot2);
        ChartPanel chartPanel2 = new ChartPanel(chart2);
        frame2.setContentPane(chartPanel2);
        frame2.pack();
        frame2.setVisible(true);
        
        NumberAxis x3 = new NumberAxis("x");
        NumberAxis y3 = new NumberAxis("y");
		ApplicationFrame frame3 = new ApplicationFrame("Fehlerentwicklung");
        XYSplineRenderer spline2 = new XYSplineRenderer();
        //System.out.println("spline precision = "+(spline.getPrecision()));
        spline.setPrecision(10);
        XYPlot plot3 = new XYPlot(dataset,x3,y3, spline2);
        JFreeChart chart3 = new JFreeChart(plot3);
        ChartPanel chartPanel3 = new ChartPanel(chart3);
        frame3.setContentPane(chartPanel3);
        frame3.pack();
        frame3.setVisible(true);
        
        
        //generate testdata
        Collection <Tuple <double [],double []>> testdata = new ArrayList<Tuple <double [],double []>>();
        double x,fx;
        double [] in = new double[1];
        
        Tuple <double[],double []> elem;
        for(int i=0; i<1001; i++){
        	x = ThreadLocalRandom.current().nextDouble(-10,10);
        	fx = Math.cos(x/2)+Math.sin(5/(Math.abs(x)+0.2))-0.1*x;
        	double [] tmp_in =new double[1];
        	tmp_in [0]=x;
        	double [] tmp_out =new double[1];
        	tmp_out [0]=fx;
        	actfunc.add(x, fx);
        	//elem = new Tuple<double[],double[]>(in,out);
        	testdata.add(new Tuple<double[],double[]>(tmp_in,tmp_out));
        }
        
        //generate Network
        int [] dim = {10,1};
        neuron = new NeuralNetwork(dim,10,0.000001);
        Transfer t = new Fermi();
        Transfer t2 = new Linear();
        Integrate inte = new Sum();
        neuron.setLayer(1, 0, inte, t);
        neuron.setLayer(10, 1, inte, t2);
        
        //show the untrained network
       // compare(neuralnet, testdata);
        
        //Show error
        System.out.println(neuron.measureMeanError(testdata));
        
        //train the output layer
        trainLayer(error1, testdata, 1);
        
        //train the hidden layer
        trainLayer(error2,testdata,0);
        
        
        //for testing, train the whole net
        neuron.learn(testdata);
        
        //show the trained network 
        compare(neuralnet,testdata);
	}
	
	public static void compare(XYSeries chart, Collection <Tuple <double [],double []>> testdata){
        Iterator <Tuple <double [], double []>> it = testdata.iterator();
        Tuple <double[],double []> elem;
        while(it.hasNext()){
        	elem = it.next();
        	neuron.propagate(elem.getFst());
        	double [] out =new double[1];
        	out= neuron.getResult();
        	chart.add(elem.getFst()[0], out[0]);
        }
	}
	
	public static void trainLayer(XYSeries chart, Collection <Tuple <double [],double []>> testdata, int layer){
		Iterator <Tuple <double [], double []>> it = testdata.iterator();
        Tuple <double[],double []> elem;
        double time =0;
        while(it.hasNext()){
        	elem = it.next();
        	double err =neuron.measureError(elem);
        	neuron.stepBackPropagate(elem,layer);
        	chart.add(time,err);
        	time++;
        }
	}

}
