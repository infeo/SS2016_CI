package neuralnetwork;

import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.data.xy.*;
public class Running {

/*
 * with help of blablabla
 */
	
	public static void main(String[] args) {
		
		DefaultXYDataset dataset = new DefaultXYDataset();
		
		//fill the Dataset
		
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

	}

}
