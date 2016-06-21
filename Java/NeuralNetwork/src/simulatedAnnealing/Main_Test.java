package simulatedAnnealing;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Main_Test {

	private static final int DEGREE =5;
	
	public static void main(String[] args) {
		
		//graphics
		XYSeries showFx = new XYSeries("f(x)",true);
		XYSeries showGx = new XYSeries("g(x)",true);
		XYSeries showBest = new XYSeries("Ergebnis von RandomSearch",true);
		
		XYSeriesCollection init = new XYSeriesCollection();
		init.addSeries(showFx);
		init.addSeries(showGx);
		init.addSeries(showBest);

		NumberAxis x1 = new NumberAxis("x");
		NumberAxis y1 = new NumberAxis("Function Output");

		XYDotRenderer dotrender = new XYDotRenderer();
		dotrender.setDotHeight(2);
		dotrender.setDotWidth(2);
		
		XYSplineRenderer spline = new XYSplineRenderer();
		spline.setPrecision(10);
		spline.setBaseShapesVisible(false);
		
		XYPlot plot = new XYPlot();
		plot.setDataset(0, init);
		plot.setDomainAxis(0, x1);
		plot.setRangeAxis(0, y1);
		plot.setRenderer(0, spline);
		
		JFreeChart chart1 = new JFreeChart(plot);
		ChartPanel chartPanel1 = new ChartPanel(chart1);
		ApplicationFrame frame1 = new ApplicationFrame("Keine Ahnung");
		frame1.setContentPane(chartPanel1);
		frame1.pack();
		
		//generate Data
		double [] dataX = new double[1001];
		double [] dataFx = new double[1001];
		double [] dataGx = new double[1001];
		double x, fx, gx;
		double [] coeff = {0.5,-0.25,0.125,-0.0625,0.03125,-0.015625};
		double err =0;
		for(int i=0;i<1001;i++){
			x= ThreadLocalRandom.current().nextDouble(-10, 10);
			fx = 3*Math.cos(x/5)+Math.sin(1/(2*Math.abs(x)+0.25))-1;
			gx = polynom(DEGREE,coeff,x);
			err +=Math.abs(fx-gx);
			dataX[i]=x;
			dataFx[i]=fx;
			dataGx[i]=gx;
			showFx.add(x, fx);
			showGx.add(x, gx);
		}
		err/=1001;
		System.out.println("Durchschnittlicher Fehler:"+err);
		//show data
		frame1.setVisible(true);
		
		double [] dataBest = searchRandom(dataX,dataFx,1000);
		for(int i=0;i<dataBest.length;i++){
			showBest.add(dataX[i],dataBest[i]);
		}

	}
	
	public static double polynom(int degree,double [] coefficient, double inp){
		if(coefficient.length != degree+1) throw new IllegalArgumentException("Die Länge der Koeffzientenliste muss dem Polynomgrad+1 entsprechen");
		double result = coefficient[0];
		double inp_mul=1;
		for(int i=1;i<degree;i++){
			inp_mul*=inp;
			result+=coefficient[i]*inp_mul;
		}
		return result;
	}
	
	public static double [] searchRandom(double [] data,double [] compare,  int tries){
		double [] best = null;
		double meanerror=0;
		double best_meanerror=Double.MAX_VALUE;
		double result;
		double [] coeff = new double[DEGREE+1];
		for(int i=0;i< tries; i++){
			double [] curr = new double[data.length];
			//generate random values
			for(int j=0;j<DEGREE+1;j++){
				coeff[j] = ThreadLocalRandom.current().nextDouble(-5, 5);
			}
			//compute the function output
			for(int j=0; j<data.length;j++){
				result = polynom(DEGREE,coeff,data[j]);
				meanerror+=Math.abs(compare[j]-result);
				curr[j]=result;
			}
			meanerror/=data.length;
			if(meanerror<best_meanerror){
				best=curr;
				best_meanerror = meanerror;
			}
		}
		System.out.println("Durchschnittlicher Fehler bei zufälliger Suche:"+best_meanerror);
		System.out.println(Arrays.toString(coeff));
		return best;
	}

}
