import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GraphPanel extends JPanel implements MouseListener {
	private double xValuetoPixelsConversionFactor;
	private double yValuetoPixelsConversionFactor;
	private double xPixelsToValueConversionFactor;
	private double yPixelsToValueConversionFactor;
	private double[] xValues;
	private double[] yValues;

    JTextField xTextField = new JTextField("X");
    JTextField yTextField = new JTextField("Y");

    JFrame miniXYdisplayWindow = new JFrame("mini");
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
	    // Let's assume we have 25 pixel margins on all four sides
        // The x and y axis will be located along the left and bottom margins
        // NOTE: Assuming x and y array values are in ascending order
     
		
		// 7 Build miniXYdisplayWindow (reuse for each mouse click!)
		miniXYdisplayWindow.getContentPane().add(xTextField);
	    miniXYdisplayWindow.getContentPane().add(yTextField);
	    miniXYdisplayWindow.getContentPane().add(this);
	    miniXYdisplayWindow.setSize(300,300);
		miniXYdisplayWindow.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) // overrides paint() in JPanel!
	{
		// 1 Calculate x and y pixels-to-value conversion factors	 

		int windowWidth  = getWidth(); // call methods
		int windowHeight = getHeight();// in JPanel!
		int xIncrements  = (windowWidth -50)/xValues.length;
		int yIncrements  = (windowHeight-25)/yValues.length;
		int xScale = xIncrements;
		int yScale = yIncrements;
		
		
		// Let's assume we have 25 pixel margins on all four sides
		// The x and y axis will be located along the left and bottom margins
		// NOTE: Assuming x and y array values are in ascending order
		xValuetoPixelsConversionFactor = (windowWidth - 25*2)/(Math.abs(xValues[0]) + Math.abs(xValues[xValues.length-1]));
		yValuetoPixelsConversionFactor = (windowHeight - 25*2)/(Math.abs(yValues[0]) + Math.abs(yValues[xValues.length-1]));
		xPixelsToValueConversionFactor = 1/xValuetoPixelsConversionFactor;
		yPixelsToValueConversionFactor = 1/yValuetoPixelsConversionFactor;
		

		// 2 Do ALL drawing here in paint() 
		g.drawLine(0, windowHeight - 25, windowWidth, windowHeight - 25); // Draw X-Axis
		g.drawLine(25, 0, 25, windowHeight);						      //Draw Y-Axis
		
		//Draw Y-Axis
		for(int i = 0; i < yValues.length; i++)
		{
			g.drawString("--", 25, yScale-25 );
			yScale += yIncrements;
			
		}
		
		//Draw X-Axis
		for(int i = 0; i < xValues.length; i++)
		{
			g.drawString("|", xScale+25, windowHeight-25 );
			xScale += xIncrements;
		}
		
	}
	
	public void mousePressed(MouseEvent me) // show tiny x,y values window
	{
		// xTextField and yTextField are in the miniXYdisplayWindow
		int xInPixels = me.getX();
		double xValue = xInPixels * xPixelsToValueConversionFactor;
		String xValueString = String.valueOf(xValue);
		xTextField.setText("X = " + xValueString);
		
		String yValueString = calculatorProgram.calculate(expression,xValueString); 
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
