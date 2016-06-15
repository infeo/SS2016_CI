package neuralnetwork;

import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.xy.*;
/**
 *
 * @author rasch
 */
public class Demo_JFreeChart {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //2 Moeglichkeiten ein Dataset zu erzeugen:

        //Moeglichkeit 1 mit DefaultXYDataset:
        double [][] A = {{1,2,5},{3,4,0}};

        DefaultXYDataset dataset = Dataset.dataset(A);

        //Moeglichkeit 2 mit XYSeries:
        double[] x={1,2,3,4,5,6,7};
        double[] y={-1,2,-3,4,-5,6,-7};
        XYSeriesCollection dataset2 = Dataset.dataset(x,y);

//2.Versuch mit XYPlot

//Achsenbezeichungen
            
        NumberAxis xax = new NumberAxis("x");
        NumberAxis yax = new NumberAxis("y");

//-------------------
        XYDotRenderer dot = new XYDotRenderer();
        // Groesse der Punkte
        dot.setDotHeight(5);
        dot.setDotWidth(5);
        XYPlot plot = new XYPlot(dataset2,xax,yax, dot);

        //"Punkte" entspricht der Ueberschrift des Fensters
        ApplicationFrame punkteframe = new ApplicationFrame("Punkte"); 

        JFreeChart chart2 = new JFreeChart(plot);


        ChartPanel chartPanel2 = new ChartPanel(chart2);
        punkteframe.setContentPane(chartPanel2);
        punkteframe.pack();
        punkteframe.setVisible(true);

//-----------------------------------------------------

        ApplicationFrame frame2 = new ApplicationFrame("Punkte mit Splines verbunden");
        XYSplineRenderer spline = new XYSplineRenderer();
        //System.out.println("spline precision = "+(spline.getPrecision()));
        spline.setPrecision(10);
        XYPlot plot2 = new XYPlot(dataset2,xax,yax, spline);


        JFreeChart chart3 = new JFreeChart(plot2);


        ChartPanel chartPanel3 = new ChartPanel(chart3);
        frame2.setContentPane(chartPanel3);
        frame2.pack();
        frame2.setVisible(true);


//-----------------------------------------------------

        ApplicationFrame frame4 = new ApplicationFrame("Punkte mit Linien verbunden (XYPlot)");
        XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
        XYPlot plot4 = new XYPlot(dataset2,xax,yax, line);


        JFreeChart chart4 = new JFreeChart(plot4);


        ChartPanel chartPanel4 = new ChartPanel(chart4);
        frame4.setContentPane(chartPanel4);
        frame4.pack();
        frame4.setVisible(true);
        
//------------------------------------------------------
        ApplicationFrame frame5 = new ApplicationFrame("Test");
		DefaultKeyedValues2DDataset test = new DefaultKeyedValues2DDataset();
		CategoryAxis testx = new CategoryAxis("x");
		ValueAxis testy = new NumberAxis("y");
		LineAndShapeRenderer testrend = new LineAndShapeRenderer();
		CategoryPlot plot5 = new CategoryPlot(test,testx,testy,testrend);
		
		test.addValue( (Number) 1.0, 0, 0);
		test.addValue( (Number) 1.0, 0, 1);
		test.addValue( (Number) 1.0, 0, 2);
		test.addValue( (Number) 2.0, 0, 1);
        JFreeChart chart5 = new JFreeChart(plot5);


        ChartPanel chartPanel5 = new ChartPanel(chart5);
        frame5.setContentPane(chartPanel5);
        frame5.pack();
        frame5.setVisible(true);
        


    }

}
