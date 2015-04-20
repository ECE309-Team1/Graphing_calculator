import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GraphPanel extends JPanel implements MouseListener {
	private double xValuetoPixelsConversionFactor;
	private double yValuetoPixelsConversionFactor;
	private double xPixelsToValueConversionFactor;
	private double[] xValues;
	private double[] yValues;

	static JPanel pane = new JPanel();
    static JTextField xTextField = new JTextField("X");
    static JTextField yTextField = new JTextField("Y");
    static JFrame miniXYdisplayWindow = new JFrame("mini");
    
    String expression;
        
    
    Calculator calculatorProgram;
    
    //testing purposes only.
    public void main()
    {
        Calculator cp = new GraphingCalculator();
        
        double[] xv = {1,2,3,4};
        double[] yv = {2,4,6,8};
        
        GraphPanel gp = new GraphPanel("x*2", xv,yv,cp);
        
    }
    
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
		
		// Let's assume we have 25 pixel margins on all four sides
		// The x and y axis will be located along the left and bottom margins
		// NOTE: Assuming x and y array values are in ascending order
		xValuetoPixelsConversionFactor = (windowWidth - 25*2)/(Math.abs(xValues[xValues.length-1]-xValues[0]));
		yValuetoPixelsConversionFactor = (windowHeight - 25*2)/(Math.abs(yValues[xValues.length-1]-yValues[0]));
		xPixelsToValueConversionFactor = 1/xValuetoPixelsConversionFactor;
		

		// 2 Do ALL drawing here in paint() 
		g.drawLine(0, windowHeight - 25, windowWidth, windowHeight - 25); // Draw X-Axis
		g.drawLine(25, 0, 25, windowHeight);						      //Draw Y-Axis
		
		//Draw Y-Axis tics
		// TODO: 1) Number the axes.
		for(int i = 0; i < yValues.length; i++)
		{
			//					x		y
			g.drawString("__", 25, yScale);
			g.drawString(Double.toString(yValues[i]), 2, yScale+7);
			yScale -= yIncrements;
			
		}
		yScale = windowHeight-25;
		//Draw X-Axis tics
		for(int i = 0; i < xValues.length; i++)
		{
			int yValToPixel = (int) (yValues[i] * (yValuetoPixelsConversionFactor));
			int yToPlot = yScale-yValToPixel;
			//						x		y
			g.drawString("|", xScale, windowHeight-25 );
			g.drawString(Double.toString(xValues[i]),xScale-7,windowHeight-8);
			g.drawOval(xScale, yToPlot, 4, 4);
			if(i>0)
			{
				g.drawLine(previousXPixel, previousYPixel, xScale, yToPlot);
			}
			previousXPixel = xScale;
			previousYPixel = yToPlot;
			xScale += xIncrements;
			
		}
		
		
		// 2) plot the points.
		// 3) Connect the points.
		
	}
	
	public void mousePressed(MouseEvent me) // show tiny x,y values window
	{
		// xTextField and yTextField are in the miniXYdisplayWindow
		int xInPixels = me.getX()-25;
		
		// Test to make sure x point is within the graph
		if(xInPixels < 0 || xInPixels > (getWidth()-25*2))
			return;
		
		double xValue = (xInPixels*xPixelsToValueConversionFactor)+xValues[0] ;
		
		// Round x value to 2 decimal places.
		BigDecimal  xBD = new BigDecimal(xValue,MathContext.DECIMAL64);//set precision to 16 digits
		xBD = xBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.
		String xString = xBD.toPlainString();// no exponents
		
		xTextField.setText("X = " + xString);
		
		// Round y value to 2 decimal places.
		Double yValue = Double.parseDouble(calculatorProgram.calculate(expression, xString));
		BigDecimal  yBD = new BigDecimal(yValue,MathContext.DECIMAL64);//set precision to 16 digits
		yBD = yBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.
		String yString = xBD.toPlainString();// no exponents
		
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
}
