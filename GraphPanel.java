import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GraphPanel extends JPanel implements MouseListener {
	private double yValuetoPixelsConversionFactor;
	private double xPixelsToValueConversionFactor;
	private double[] xValues;
	private double[] yValues;
	private double yValueIncrement;

	static JPanel pane = new JPanel();
    static JTextField xTextField = new JTextField("X");
    static JTextField yTextField = new JTextField("Y");
    static JFrame miniXYdisplayWindow = new JFrame("mini");
    
    String expression;
        
    
    Calculator calculatorProgram;
    
	public GraphPanel (String expression, // CONSTRUCTOR
            double[]   xValues,
            double[]   yValues,
            Calculator calculatorProgram)
            throws IllegalArgumentException
	{
		
		// To-do for this constructor method:
		// 1 Verify arrays are same size
		if(xValues.length != yValues.length) 
		    throw new IllegalArgumentException("X and Y arrays wrong length");
	        
		    this.xValues = xValues;
	        this.yValues = yValues;
		// 2 Verify x increment is positive
	        for(int i=1; i < xValues.length; i++){
	            if ((xValues[i] - xValues[i-1]) <= 0)
	            {
	                throw new IllegalArgumentException("Negative or overlapping x increments");
	            }
	        }
	        
		// 3 Save Calculator address for call back
        this.calculatorProgram = calculatorProgram;

		// 4 Save expression for call back
        this.expression = expression;

		// 5 Register with the panel as MouseListener
		addMouseListener(this);
        
        // 6 Calculate Y scale values (and save them)
		double minY = min(yValues);
		double maxY = max(yValues);
		yValueIncrement = (maxY-minY)/(yValues.length-1);
		
		// 7 Build miniXYdisplayWindow (reuse for each mouse click!)
		
		pane.add(xTextField);
	    pane.add(yTextField);
	    miniXYdisplayWindow.getContentPane().add(pane, "Center");
	    miniXYdisplayWindow.setSize(100,100);
	}
	
	@Override
	public void paint(Graphics g) // overrides paint() in JPanel!
	{
		// 1 Calculate x and y pixels-to-value conversion factors	 

		int windowWidth  = getWidth(); // call methods
		int windowHeight = getHeight();// in JPanel!
		int xIncrements  = (windowWidth -50)/(xValues.length-1);
		int yIncrements  = (windowHeight-50)/(yValues.length-1);
		int xScale = 25;
		int previousXPixel = 25;
		int yScale = windowHeight-25;
		int previousYPixel = windowHeight-25;
		double yStart = min(yValues);
		
		// Let's assume we have 25 pixel margins on all four sides
		// The x and y axis will be located along the left and bottom margins
		// NOTE: Assuming x and y array values are in ascending order
		yValuetoPixelsConversionFactor = (windowHeight - 25*2)/(Math.abs(max(yValues)-min(yValues)));
		xPixelsToValueConversionFactor = (Math.abs(xValues[xValues.length-1]-xValues[0]))/(windowWidth - 25*2);
		

		// 2 Do ALL drawing here in paint().
		// Draw X-Axis and Y-Axis.
		g.drawLine(0, windowHeight - 25, windowWidth, windowHeight - 25); 
		g.drawLine(25, 0, 25, windowHeight);
		
		for(int i = 0; i < yValues.length; i++)
		{
			// Draw tics on the y-axis.
			g.drawString("__", 25, yScale);
			// Number the y axis.
			// Round y value to 2 decimal places.
			BigDecimal  y_BD = new BigDecimal(yStart ,MathContext.DECIMAL64);//set precision to 16 digits
			y_BD = y_BD.setScale(2,BigDecimal.ROUND_HALF_UP);//scale (2) is # of digits to right of decimal point.
			String yString = y_BD.toPlainString();// no exponents
			g.drawString(yString, 2, yScale+7);
			
			yScale -= yIncrements;
			yStart += yValueIncrement;
		}
		
		// Resetting y to start point.
		yScale = windowHeight-25;
		
		for(int i = 0; i < xValues.length; i++)
		{
			// Convert value to pixel
			int yValToPixel = (int) ((yValues[i] - min(yValues)) * (yValuetoPixelsConversionFactor));
			int yToPlot = yScale-yValToPixel;

			// Draw tic marks for x axis.
			g.drawString("|", xScale, windowHeight-25 );
			
			// Number the x axis.
			// Round x value to 2 decimal places.
			BigDecimal  xBD = new BigDecimal(xValues[i],MathContext.DECIMAL64);//set precision to 16 digits
			xBD = xBD.setScale(2,BigDecimal.ROUND_HALF_UP);//scale (2) is # of digits to right of decimal point.
			String xString = xBD.toPlainString();// no exponents
			g.drawString(xString,xScale-7,windowHeight-8);
			
			// Plot the points
			g.drawOval(xScale, yToPlot, 4, 4);
			
			// Connect the points
			g.drawLine(previousXPixel, previousYPixel, xScale, yToPlot);
			
			previousXPixel = xScale;
			previousYPixel = yToPlot;
			xScale += xIncrements;	
		}
	}
	
	public void mousePressed(MouseEvent me) // show tiny x,y values window
	{
		// xTextField and yTextField are in the miniXYdisplayWindow
		int xInPixels = me.getX()-25;
		int yInPixels = me.getY()-25;
		
		// Test to make sure x,y point is within the graph
		if(xInPixels < 0 || xInPixels > (getWidth()-25*2))
			return;
		
		if(yInPixels < 0 || yInPixels > (getHeight()-25*2))
			return;
		
		double xValue = (xInPixels*xPixelsToValueConversionFactor)+xValues[0] ;
		
		// Round x value to 2 decimal places.
		BigDecimal  xBD = new BigDecimal(xValue,MathContext.DECIMAL64);//set precision to 16 digits
		xBD = xBD.setScale(2,BigDecimal.ROUND_HALF_UP);//scale (2) is # of digits to right of decimal point.
		String xString = xBD.toPlainString();// no exponents
		
		xTextField.setText("X = " + xString);
		
		// Round y value to 2 decimal places.
		double yValue = Double.parseDouble(calculatorProgram.calculate(expression, xString));
		BigDecimal  yBD = new BigDecimal(yValue,MathContext.DECIMAL64);//set precision to 16 digits
		yBD = yBD.setScale(2,BigDecimal.ROUND_HALF_UP);//scale (2) is # of digits to right of decimal point.
		String yString = yBD.toPlainString();// no exponents
		
		yTextField.setText("Y = " + yString);
		
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
	
	// Find the min in a double array.
	private static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
	
	// Find the max in a double array.
	private static double max(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}