import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class GraphingCalculator implements  ActionListener, Grapher
{
	// GUI declarations
JFrame       calcWindow      = new JFrame();	
JLabel       errorLabelField = new JLabel(); // displays errors here
JTextField   inputArea       = new JTextField("Choose calculator mode FIRST, then enter input");	// numerical input here
JLabel       empty           = new JLabel();
JLabel       ForX            = new JLabel("for X = ", SwingConstants.RIGHT);
JLabel       ToX             = new JLabel("X increment = ", SwingConstants.RIGHT);
JTextField   forX            = new JTextField("");
JTextField   toX             = new JTextField("");
JTextArea    totalDisplay    = new JTextArea(); // displays total
JTextArea    logDisplay      = new JTextArea();
ButtonGroup  group           = new ButtonGroup();
JRadioButton accumulatorMode = new JRadioButton("Accumulator");
JRadioButton expressionMode  = new JRadioButton("Expression");
JRadioButton testMode        = new JRadioButton("Test");
JRadioButton graphMode       = new JRadioButton("Graph");    
JButton      clearButton     = new JButton("Clear"); // clear button
JScrollPane  logScrollPane   = new JScrollPane(logDisplay);
// Construct the Accumulating Calculator and that's it.
JPanel       topPanel        = new JPanel();
JPanel       centerPanel     = new JPanel();
JPanel       bottomPanel     = new JPanel();

String       newLine         = System.lineSeparator();
boolean debug;
int totalRight = 0;
int totalWrong = 0;
private OperandPair  op = new OperandPair();

	public static void main(String[] args) 
	{
		GraphingCalculator gc = new GraphingCalculator();
		// Set input argument as "true" if you want to run in debug mode.
		if(args.length != 0)
		{
			if(args[0].equals("true"))
				gc.debug = true;
			else
				gc.debug = false;
		}
	}

	public GraphingCalculator() 
	{
		// Print out the team members names.
		System.out.println("Team 1 members: \nJarvik Joshi, Vijay Thiagarajan, Bryan Chase, Ahmed Samara");
		// GUI build
		//topPanel.setSize(100,100);
		topPanel.setLayout(new GridLayout(2,1));
		centerPanel.setLayout(new GridLayout(2,1));
		bottomPanel.setLayout(new GridLayout(2,5));
		
		topPanel.add(errorLabelField);
		topPanel.add(totalDisplay);
		
		centerPanel.add(logScrollPane);
		centerPanel.add(inputArea);
		
		group.add(accumulatorMode);
		group.add(expressionMode);
		group.add(testMode);
		group.add(graphMode);
		accumulatorMode.setSelected(true);
	
		bottomPanel.add(accumulatorMode);
		bottomPanel.add(expressionMode);
		bottomPanel.add(testMode);
		bottomPanel.add(graphMode);
		bottomPanel.add(empty);
		bottomPanel.add(ForX);
		bottomPanel.add(forX);
		bottomPanel.add(ToX);
		bottomPanel.add(toX);
		bottomPanel.add(clearButton);
		
		clearButton.setBackground(Color.ORANGE);
		errorLabelField.setForeground(Color.BLUE);
		
		calcWindow.getContentPane().add(topPanel,"North");
		calcWindow.getContentPane().add(centerPanel, "Center");
		calcWindow.getContentPane().add(bottomPanel, "South");
		
		calcWindow.setTitle("Expression Calculator");
		
		totalDisplay.setEditable(false);
		logDisplay.setEditable(false);
		totalDisplay.setFont (new Font("default",Font.BOLD,14));
		logDisplay.setFont (new Font("default",Font.BOLD,14));
		inputArea.setFont (new Font("default",Font.BOLD,14));
		toX.setFont (new Font("default",Font.BOLD,14));
		forX.setFont (new Font("default",Font.BOLD,14));
		calcWindow.setFont(new Font("default", Font.BOLD, 16));
		totalDisplay.setLineWrap(true);
		logDisplay.setLineWrap(true);
		
		
		totalDisplay.setWrapStyleWord(true);
		logDisplay.setWrapStyleWord(true);
		
		
		calcWindow.setSize(850,850);
		calcWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calcWindow.setVisible(true);
		
		
		clearButton.addActionListener(this);
		graphMode.addActionListener(this);
		testMode.addActionListener(this);
		expressionMode.addActionListener(this);
		accumulatorMode.addActionListener(this);
		// Making the input field as an action listener
		inputArea.addActionListener(this);
		forX.addActionListener(this);
		toX.addActionListener(this);
	}

	/********************************************************
	 * EXPRESSION CALCULATOR INTERFACE FUNCTION
	 ********************************************************/
	@Override
	public String calculate(String Expression, String x)
			throws IllegalArgumentException {
	    
	    Double ans = null;	
		//test cases
		if(Expression.contains("X"))
			Expression = Expression.replaceAll("X", "x");
		
		try
		{
			ans = parse_expression(Expression, x);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException(e.getMessage());
		}
		return ans.toString();
	}

	/*******************************************************
	 * ACCUMULATOR INTERFACE FUNCTION
	 *******************************************************/
	@Override
	public String accumulate(String total, String amount)
			throws IllegalArgumentException {
		if(debug)
		{
			System.out.println("Entered input was: " + amount);
			System.out.println("Previous total was: " + total);
		}
		
		boolean operatorSpecified = false;
		// Check if operator is specified
		if(amount.indexOf('+') >= 0 || amount.indexOf('-') >= 0)
			operatorSpecified = true;
		
		// check for space character after the operator, if operator is specified
		if(operatorSpecified && amount.charAt(1) == ' ')
			throw new IllegalArgumentException("Please don't enter a space after the operator");
		
		// remove all blanks and $ symbols
		amount = amount.replace(" ", "");
		amount = amount.replace("$", "");
		
		if(debug)
			System.out.println("Input after formatting: " + amount);
		
		// Test for decimal digits, if it is a decimal number
		int decimalOffset = amount.indexOf('.');
		if(decimalOffset >= 0)
		{
			int decimalLength = amount.substring(decimalOffset + 1).length();
			if(decimalLength != 2)
				throw new IllegalArgumentException("If entering a decimal number, please enter"
						+ " exactly two digits after decimal point.");
		}
		
		// After passing all the tests convert String to double
		try
		{
			double newTotal = Double.parseDouble(amount) + Double.parseDouble(total);
			// Convert new total back to String
			if(newTotal == 0)
				return "0";
			BigDecimal  totalBD = new BigDecimal(newTotal,MathContext.DECIMAL64);//set precision to 16 digits
			totalBD = totalBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.
			String totalString = totalBD.toPlainString();// no exponents
			
			if(debug)
				System.out.println("Final result: " + totalString);
			
			return totalString;
		}
		
		catch(NumberFormatException nfe)
		{
			throw new IllegalArgumentException("Please enter proper arguments. Entered argument"
					+ " was :" + amount);
		}		
	}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		// clear the label field
		errorLabelField.setText("");
		
		// clear button implementation
		if (ae.getSource() == clearButton)
		{
			inputArea.setText("");
			totalDisplay.setText("");
		}
		
		//code for when the radio button is selected
		else if(ae.getSource() == accumulatorMode)
		{
			inputArea.setText("");
			totalDisplay.setText("");
			toX.setText("");
			toX.setEditable(false);
			forX.setText("");
			forX.setEditable(false);
		}
		
		else if(ae.getSource() == expressionMode)
		{
			inputArea.setText("");
			totalDisplay.setText("");
			toX.setText("");
			toX.setEditable(false);
			forX.setText("");
			forX.setEditable(true);
		}
		
		else if(ae.getSource() == testMode)
		{
			inputArea.setText("");
			totalDisplay.setText("");
			toX.setText("");
			toX.setEditable(false);
			forX.setText("");
			forX.setEditable(false);
		}
		
		else if(ae.getSource() == graphMode)
		{
			inputArea.setText("");
			totalDisplay.setText("");
			toX.setText("");
			toX.setEditable(true);
			forX.setText("");
			forX.setEditable(true);
		}
		
		else			// Code for when the "Enter" key pressed.
		{
			if(accumulatorMode.isSelected() == true)
			{
				// 1) Get the current input, current total and send it for parsing
				String total;
				try
				{
					String prevTotal;
					String input;
					
					// Simple tests for prev total - (To avoid unnecessary Double.parse exceptions) 
					if(totalDisplay.getText().equals(""))
						prevTotal = "0";
					else
						prevTotal = totalDisplay.getText();
					
					// Simple tests for input - (To avoid unnecessary Double.parse exceptions) 
					if(inputArea.getText().equals(""))
						input = "0";
					else
						input = inputArea.getText();
							
					total = accumulate(prevTotal, input);
					
					// Update the total after successful parsing
					totalDisplay.setText(total);
					errorLabelField.setText("");
					
					// Update the log by reading in the input and appending it to the log.
					// NOTE-JJ - Append to log Display (not Total Display)
					logDisplay.append(newLine + prevTotal + " + " + input + " = " + total + newLine);
				    // scroll the outChatArea to the bottom
				    logDisplay.setCaretPosition(logDisplay.getDocument().getLength());
				}
				
				catch(IllegalArgumentException e)
				{
					// If parse not successful, Update error message.
					errorLabelField.setText(e.getMessage());
				}
			}
				
			else if(expressionMode.isSelected() == true)
			{
				// 1) Get the current input, current total and send it for parsing
				String total;
				try
				{
					String input;
					
					// Simple tests for input - (To avoid unnecessary Double.parse exceptions) 
					if(inputArea.getText().equals(""))
						throw new IllegalArgumentException("An expression has to be entered.");
					else
						input = inputArea.getText();
					
					String x = null;
					if(input.contains("X") || input.contains("x"))
					{
						if(forX.getText().isEmpty())
							throw new IllegalArgumentException("Enter a value for x.");
						else
							x = forX.getText();
					}
							
					total = calculate(input, x);
					
					// Update the total after successful parsing
					totalDisplay.setText(total);
					errorLabelField.setText("");
					
					// Update the log by reading in the input and appending it to the log.
					logDisplay.append(newLine + input + " = " + total + newLine);
				    // scroll the outChatArea to the bottom
				    logDisplay.setCaretPosition(logDisplay.getDocument().getLength());
				}
				
				catch(IllegalArgumentException e)
				{
					// If parse not successful, Update error message.
					errorLabelField.setText(e.getMessage());
				}
			}
			
			else if(testMode.isSelected() == true)
			{
				learningMode(inputArea.getText(), forX.getText());
			}
			/***************************************************
			 * TODO GRAPHING MODE PART
			 ***************************************************/
			else if(graphMode.isSelected() == true)
			{
				try
				{
					String input = null;
					
					// Simple tests for input - (To avoid unnecessary Double.parse exceptions) 
					if(inputArea.getText().equals(""))
					{
						throw new IllegalArgumentException("An expression has to be entered.");
					}
					
					else
						input = inputArea.getText();
					
					String fx = forX.getText();
					String tx = toX.getText();
					
					drawGraph(input, fx, tx);
					
					inputArea.setText("");
					totalDisplay.setText("");
					toX.setText("");
					toX.setEditable(true);
					forX.setText("");
					forX.setEditable(true);
					
				}
				
				catch(IllegalArgumentException e)
				{
					// If parse not successful, Update error message.
					errorLabelField.setText(e.getMessage());
				}
			}
			
			else
			{
			}
				
			// clear the input field.
			inputArea.setText("");
		}	
	}
	
	
	// HELPER METHODS!!!
	public void learningMode(String exp, String x)
	{
		String leftExp = null;
		String rightExp = null;
		double leftVal = -2;
		double rightVal = -1;
		
		for (int i = 0; i < exp.length(); i++) 
		{
			if (exp.charAt(i) == '=')
			{
				leftExp = exp.substring(0, i).trim();
				rightExp = exp.substring(i+1).trim();
			}
		}
		if (leftExp == null || rightExp == null)
			throw new IllegalArgumentException("Please include an = sign");

		// to be tested once expression parser is working.
		leftVal = parse_expression(leftExp, x); // run leftExp through ExpressionParser(?) and get return value leftVal
		rightVal = parse_expression(rightExp, x); // run rightExp through ExpressionParser(?) and get return value rightVal
		
		if (leftVal == rightVal)
		{	
			totalDisplay.setText("Correct!");
			totalRight++;
			double percentRight = (totalRight*100) / (totalRight + totalWrong);
			logDisplay.append(newLine + "You have " + totalRight + " correct tests. You have been correct " +
								percentRight + "% of the time."); 
			
		}
		else
		{
			totalDisplay.setText("Oops!");
			totalWrong++;
			double percentWrong = (totalWrong*100)	 / (totalRight + totalWrong);
			logDisplay.append(newLine + "You have " + totalWrong + " incorrect tests. You have been incorrect " +
					percentWrong + "% of the time.");
		}				
	}
	
	/*************************************************************
	 * HELPER METHODS FOR EXPRESSION CALCULATOR
	 ************************************************************/
	
	// parses a complicated expression.
	public double parse_expression(String exp, String text) throws IllegalArgumentException
	{
		//API to outside world. Returns evaluated outside value.
    	
		Double ans;
    	// Remove spaces in the expression.
    	exp = remove_spaces(exp);
    	
    	try
    	{
        	// replace constants with their values in the expression.
    		exp = replace_consts(exp, text);
    		exp = eval_parentheses(exp);
    		if(exp.contains("--"))
    			exp = exp.replace("--", "+");
    		if(!isDouble(exp))
    			ans = evaluateComplexExpression(exp);
    		else
    			ans = Double.parseDouble(exp);
    	}
    	catch(Exception e)
    	{
    		throw new IllegalArgumentException(e.getMessage());
    	}
    	if(Double.isInfinite(ans) || Double.isNaN(ans))
    		throw new IllegalArgumentException("Cannot divide by zero or number is too big.");
        return ans;
	}
	
	// Removes all spaces in a string and returns it.
	public String remove_spaces(String exp)
    {
        //Returns expression without spaces/
       
        StringBuilder sb = new StringBuilder(exp);
        
        //remove spaces at corners without concern for surroundings.
        if(sb.charAt(0) == ' ')
        {
            sb.deleteCharAt(0);
        }
        if(sb.charAt(exp.length()-1) == ' ')
        {
            sb.deleteCharAt(exp.length()-1);
        }

        String acc = "-+*/r^x)( piePIE.";
        //iterate through rest
        for(int i=1; i<sb.length()-1 ; i++)
        {
			 //check for invalid vals
			 char c = sb.charAt(i);
			 //must be digit or one of these vals.
			 if(acc.indexOf(c) == -1 && !Character.isDigit(c))
			 {
			     throw new IllegalArgumentException("Invalid character detected: " + c);
			     
			 }
            if(sb.charAt(i) == ' ')
            {
                //Don't remove char if surroundings are both digits
                if(!(Character.isDigit(sb.charAt(i)) 
                   && Character.isDigit(sb.charAt(i))))
                   {
                       sb.deleteCharAt(i);
                   }
            }
        }
        return sb.toString();          
    }
	
	// Replace the constants in a String with its numerical value.
	public String replace_consts(String exp, String x)
    {
        String expression = exp;

        // If expression contains x, replace it with input.
        // If no value is given for x, throw exception. 
        if(expression.contains("x")){
            try
            {
            	Double.parseDouble(x);
            }
            catch(Exception NumberFormatException)
            {
                throw new IllegalArgumentException("Entered value is not a number");
            }
            expression = expression.replaceAll("x", x);
        }
        
        // Replace the mathematical constants.
        expression = expression.replaceAll("pi",Double.toString(Math.PI));
        expression = expression.replaceAll("PI",Double.toString(Math.PI));
        expression = expression.replaceAll("e",Double.toString(Math.E));
        expression = expression.replaceAll("E",Double.toString(Math.E));
        expression = expression.replaceAll("R","r");
        
        return expression;
    }
	
	// Evaluates expression with (multiple) parentheses until we end up with 
	// a simple/compound expression.
	public String eval_parentheses(String exp)
    {	

    	// Find the number of open and closed parentheses and see if they are equal.
		int openPar = exp.length() - exp.replace(")", "").length();
    	int closePar = exp.length() - exp.replace("(", "").length();
    	
    	// TEST CASES    	
    	if(openPar != closePar)
    		throw new IllegalArgumentException("Error. Please enter missing parantheses.");
    	if(exp.contains(")("))
			throw new IllegalArgumentException("Error. Parantheses not in right order.");
    	
		while(exp.contains("("))
		{	
			// 1. Find index of first ")" and then form a substring till that index.
			// 2. Then in that substring find the last "(".
			// 3. Finally send the expression inside to eval_exp function and replace it.
			// 4. Repeat until all parentheses disappear.
			
			int m = exp.indexOf(')');
    		int n = exp.substring(0,m).lastIndexOf('(');
    		
    		String toSend = exp.substring(n+1, m);
    		// To be tested.
    		String result = Double.toString(evaluateComplexExpression(toSend));
    		exp = exp.replace(exp.substring(n, m+1), result);
    		if(debug)
    			System.out.println("Expression: " + exp);
		}
        return exp;
    }
    
	// Professor's helper methods.
    private double evaluateComplexExpression(String expression)
            throws IllegalArgumentException
   {
	   // THERE SHOULD BE NO PARENTHESES IN THE EXPRESSION 
	   // System.out.println("Expression sent to evaluateExpression() is " + expression);
	   // Reduce the expression by replacing inner simple expressions
	   // in operator-precedence sequence.
	   int    operatorOffset,operator1Offset,operator2Offset;
	   double result = 0;
	   char[] operators = {'^','r','*','/','+','-'};//in priority sequence
	   
	   for (int i = 0; i < operators.length; i = i+2)
	       {
	       while (true) // operators of equal priority are
	             {      // executed in left-to-right sequence.
	             operator1Offset = expression.indexOf(operators[i]);
	             operator2Offset = expression.indexOf(operators[i+1]);
	             if (operator1Offset == operator2Offset) break;//both are -1(neither found)
	             if (operator1Offset == -1) operatorOffset = operator2Offset;
	        else if (operator2Offset == -1) operatorOffset = operator1Offset;
	             // both operators are found:
	        else if (operator1Offset < operator2Offset)
	     	        operatorOffset  = operator1Offset;
	     	     else               
	     	        operatorOffset  = operator2Offset;
	             
	             OperandPair op = findAdjacentOperands(expression,operatorOffset);
	             result = calculateSimpleExpression(op.leftOperandValue,
	             		                           expression.charAt(operatorOffset),
	             		                           op.rightOperandValue);
	             if ((op.previousOperatorOffset == -1) // both point off the ends of the expression
	              && (op.nextOperatorOffset == expression.length()))
	                return result; // THAT WAS THE LAST SIMPLE EXPRESSION!
	             expression = expression.substring(0, op.previousOperatorOffset+1)
	                        + String.valueOf(result).replaceFirst("-","n")
	                        + expression.substring(op.nextOperatorOffset);
	             // System.out.println("new expression is " + expression);
	             } // exit from while(true) when neither operator is found above.
	       }
	   // See if the "expression" is just a number
	   try 
	   {
	 	  if (expression.startsWith("n")) // watch for unary...
	 		  expression = "-" + expression.substring(1);
	 	  if (expression.startsWith("-n")) // watch for unary...
	 		  expression = expression.substring(2);
	 	  result = Double.parseDouble(expression);
	 	  return result;
	   }
	   catch(NumberFormatException nfe)
	   {
		   // shouldn't have parts left when all operators have been processed.
		   throw new IllegalArgumentException("Invalid expression. Remaining partial is " + expression );
       }
   } 
    
    private OperandPair findAdjacentOperands(String expression, int operatorOffset)
			throws IllegalArgumentException
	{
		// A single OperandPair object is (explicitly, not
		// implicitly)made when Calculator is loaded. This single
		// object is reused as a data-bean to return the
		// multiple values from this method whenever it is called.
		// This method is not called recursively, so only a single
		// OperandPair object is in play at a time, and a new
		// one need not be made (and garbage collected) for each call.	
		String rightOperand = "";
		String leftOperand  = "";
		//System.out.println("Expression to findAdjaventOperands() is " + expression
		//                 + " with operatorOffset of " + operatorOffset);
		// scan forward
		int i = operatorOffset+1;
		// System.out.println("forward scan is starting at " + i);
		for (; i < expression.length(); i++)
		{                                   
		if ((expression.charAt(i) == '+')	  
		|| (expression.charAt(i) == '-')	  
		|| (expression.charAt(i) == '*')	   
		|| (expression.charAt(i) == '/')	   
		|| (expression.charAt(i) == '^')	   
		|| (expression.charAt(i) == 'r'))	   
		break;
		}
		op.nextOperatorOffset = i; // may be end-of-expression.
		if (i == expression.length()) // didn't find a next operator in the above scan!
		rightOperand = expression.substring(operatorOffset+1).trim();
		if (i < expression.length()-1) // found an operator at i  
		rightOperand = expression.substring(operatorOffset+1,i).trim();
		if (i == expression.length()-1) // Woopsie! operator is last char  
		throw new IllegalArgumentException("Expression (or sub-expression) ends with operator " 
		             + expression.charAt(i));
		if (rightOperand.startsWith("n")) // replace 'n' with '-'
		rightOperand = "-" + rightOperand.substring(1);
		if (rightOperand.startsWith("-n")) // watch for unary...
		rightOperand = rightOperand.substring(2);
		try {
		op.rightOperandValue = Double.parseDouble(rightOperand);
		}
		catch(NumberFormatException nfe)
		{
		throw new IllegalArgumentException("Operand " + rightOperand
		          + " is not numeric.");
		}
		// now scan backwards (to the left)
		i = operatorOffset-1;
		// System.out.println("leftwards scan is starting at " + i);
		for (; i >= 0; i--)
		{                                   
		if ((expression.charAt(i) == '+')	  
		|| (expression.charAt(i) == '-')	  
		|| (expression.charAt(i) == '*')	   
		|| (expression.charAt(i) == '/')	   
		|| (expression.charAt(i) == '^')	   
		|| (expression.charAt(i) == 'r'))	   
		break;
		}
		op.previousOperatorOffset = i; // may be beginning-of-expression (-1)
		if (i > 0) // found an operator at i
		leftOperand = expression.substring(i+1, operatorOffset).trim();
		if (i < 0) // didn't find a previous operator in the above scan!
		leftOperand = expression.substring(0, operatorOffset).trim();
		if (i == 0) // oopsie! operator is 1st char
		throw new IllegalArgumentException("Expression (or sub-expression) starts with operator " 
		            + expression.charAt(0));
		if (leftOperand.startsWith("n")) // replace 'n' with '-'
		leftOperand = "-" + leftOperand.substring(1);
		if (leftOperand.startsWith("-n")) // watch for unary...
		leftOperand = leftOperand.substring(2);
		try {
		op.leftOperandValue = Double.parseDouble(leftOperand);
		}
		catch(NumberFormatException nfe)
		{
		throw new IllegalArgumentException("Operand " + leftOperand
		           + " is not numeric.");
		}
		//System.out.println("leftOperandValue is "  + op.leftOperandValue
		//		         + ", rightOperandValue is " + op.rightOperandValue
		//		         + ", operator is " + expression.charAt(operatorOffset));
		return op;
	}
    
    class OperandPair // INNER CLASS ! (An object to hold 
    {           //                multiple return values.
	  int    nextOperatorOffset;     //(or expression end)
	  int    previousOperatorOffset; //(or expression start)
	  double rightOperandValue;
	  double leftOperandValue;
    }           // end of inner class
    
    private double calculateSimpleExpression(double leftOperand, char operator, double rightOperand)
    		throws IllegalArgumentException
	{
	//System.out.println("In calculateResult() leftOperand is " + firstOperand
	//		         + ", operator is " + operator
	//                 + ", rightOperand is " + secondOperand);	
		switch(operator)
		{
			case '+': return leftOperand + rightOperand; 
			case '-': return leftOperand - rightOperand; 
			case '*': return leftOperand * rightOperand; 
			case '/': return leftOperand / rightOperand; 
			case '^': return Math.pow(leftOperand, rightOperand);
			case 'r': return Math.pow(leftOperand, (1.0/rightOperand));
			default : throw new IllegalArgumentException("INTERNAL ERROR: Operator " + operator + " not recognized in switch.");
		}          
	}
    
    // Used to test if a String is a double value.
    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/********************************************************
	 * GRAPHING CALCULATOR INTERFACE FUNCTION
	 ********************************************************/
	@Override
	public void drawGraph(String expression, String xStart, String increment)
			throws IllegalArgumentException {
		// TODO Implement function for graphing calculator.
		double[] xVals;
		double[] yVals;
		double inc, tempX;
		
		try
		{
			inc = Double.parseDouble(increment);
			tempX = Double.parseDouble(xStart);
		}
		catch(NumberFormatException nfe)
		{
			throw new IllegalArgumentException("Enter a proper number.");
		}
		
		int j; double xEnd = tempX + 11*inc;
		if((xEnd < 0 && tempX < 0) || (xEnd > 0 && tempX > 0))
			j = 10;
		else
			j = 11;
		xVals = new double[j];
		yVals = new double[j];
		for (int i=0; i<j; i++) 
		{
			xVals[i] = tempX;
			yVals[i] = Double.parseDouble(calculate(expression, String.valueOf(tempX))); // work?
			tempX += inc;
		}
		GraphPanel gp = new GraphPanel(expression, xVals, yVals, this);
		
		JFrame graphWindow = new JFrame();
		graphWindow.getContentPane().add(gp, "Center");
		graphWindow.setSize(500,500);
		graphWindow.setVisible(true);
		graphWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}