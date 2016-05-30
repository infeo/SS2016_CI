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
	
	public static void main(String[] args) {
		XYSeries error = new XYSeries("Error");
		XYSeries neuralnet = new XYSeries("Ouptut");
		XYSeries actfunc = new XYSeries ("Funktion");
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(error);
		dataset.addSeries(actfunc);
		dataset.addSeries(neuralnet);
		
        NumberAxis xax = new NumberAxis("x");
        NumberAxis yax = new NumberAxis("y");
		ApplicationFrame frame2 = new ApplicationFrame("Fehlerentwicklung");
        XYSplineRenderer spline = new XYSplineRenderer();
        //System.out.println("spline precision = "+(spline.getPrecision()));
        spline.setPrecision(10);
        XYPlot plot2 = new XYPlot(dataset,xax,yax, spline);


        JFreeChart chart3 = new JFreeChart(plot2);


        ChartPanel chartPanel3 = new ChartPanel(chart3);
        frame2.setContentPane(chartPanel3);
        frame2.pack();
        frame2.setVisible(true);
        
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
        NeuralNetwork test = new NeuralNetwork(dim,10,1);
        Transfer t = new Fermi();
        Transfer t2 = new Linear();
        Integrate inte = new Sum();
        test.setLayer(1, 0, inte, t);
        test.setLayer(10, 1, inte, t2);
        
        //show the untrained network
        Iterator <Tuple <double [], double []>> it = testdata.iterator();
        while(it.hasNext()){
        	elem = it.next();
        	test.propagate(elem.getFst());
        	double [] out =new double[1];
        	out= test.getResult();
        	neuralnet.add(elem.getFst()[0], out[0]);
        }
        
        //Show error
        System.out.println(test.measureMeanError(testdata));
        
        //for the half trained Network, the debugger is used
        test.learn(testdata);
        
	}

}
