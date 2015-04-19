import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class GraphPanel extends JPanel implements MouseListener {
	private double xValuetoPixelsConversionFactor;
	private double yValuetoPixelsConversionFactor;
	private double xPixelsToValueConversionFactor;
	private double yPixelsToValueConversionFactor;
	private double[] xValues;
	private double[] yValues;

	public GraphPanel (String     expression, // CONSTRUCTOR
            double[]   xValues,
            double[]   yValues,
            Calculator calculatorProgram)
            throws IllegalArgumentException
	{
		this.xValues = xValues;
		this.yValues = yValues;
		// To-do for this constructor method:
		// 1 Verify arrays are same size
		// 2 Verify x increment is positive
		// 3 Save Calculator address for call back
		// 4 Save expression for call back
		// 5 Register with the panel as MouseListener
		// 6 Calculate Y scale values (and save them) 
		// 7 Build miniXYdisplayWindow (reuse for each mouse click!)
	}
	
	@Override
	public void paint(Graphics g) // overrides paint() in JPanel!
	{
		// 1 Calculate x and y pixels-to-value conversion factors	 

		int windowWidth  = getWidth(); // call methods
		int windowHeight = getHeight();// in JPanel!
		
		// Let's assume we have 25 pixel margins on all four sides
		// The x and y axis will be located along the left and bottom margins
		// NOTE: Assuming x and y array values are in ascending order
		xValuetoPixelsConversionFactor = (windowWidth - 25*2)/(Math.abs(xValues[0]) + Math.abs(xValues[xValues.length-1]));
		yValuetoPixelsConversionFactor = (windowHeight - 25*2)/(Math.abs(yValues[0]) + Math.abs(yValues[xValues.length-1]));
		xPixelsToValueConversionFactor = 1/xValuetoPixelsConversionFactor;
		yPixelsToValueConversionFactor = 1/yValuetoPixelsConversionFactor;
		

		// 2 Do ALL drawing here in paint() 		
	}
	
	public void mousePressed(MouseEvent me) // show tiny x,y values window
	{
		// xTextField and yTextField are in the miniXYdisplayWindow
		int xInPixels = me.getX();
		double xValue = xInPixels * xPixelsToValueConversionFactor;
		String xValueString = String.valueOf(xValue);
		xTextField.setText("X = " + xValueString);
		
		String yValueString = calculator.calculate(expression,xValueString); 
		yTextField.setText("Y = " + yValueString);
		
		// show mini x,y display window
		miniXYdisplayWindow.setLocation(me.getX(), me.getY());
		miniXYdisplayWindow.setVisible(true); 
	}
	
	public void mouseReleased(MouseEvent me) // hide tiny window
	{
		// "erase" mini x,y display window	
		miniXYdisplayWindow.setVisible(false);
	}
	
	public void mouseClicked(MouseEvent me){} // take no action
	public void mouseEntered(MouseEvent me){} // on these
	public void mouseExited(MouseEvent  me){} // window events
}
