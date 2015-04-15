import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class GraphingCalculator implements ActionListener, Grapher
{
	// GUI declarations
JFrame calcWindow = new JFrame();	
JLabel errorLabelField = new JLabel(); // displays errors here
JTextField inputArea = new JTextField("Choose calculator mode FIRST, then enter input");	// numerical input here
JLabel empty = new JLabel();
JLabel ForX = new JLabel("for X = ", SwingConstants.RIGHT);
JLabel ToX = new JLabel("to X = ", SwingConstants.RIGHT);
JTextField forX = new JTextField("");
JTextField toX = new JTextField("");
JTextArea totalDisplay = new JTextArea(); // displays total
JTextArea logDisplay = new JTextArea();
ButtonGroup group = new ButtonGroup();
JRadioButton accumulatorMode = new JRadioButton("Accumulator");
JRadioButton expressionMode = new JRadioButton("Expression");
JRadioButton testMode = new JRadioButton("Test");
JRadioButton graphMode = new JRadioButton("Graph");    
JButton clearButton = new JButton("Clear"); // clear button
JScrollPane logScrollPane = new JScrollPane(logDisplay);
// Construct the Accumulating Calculator and that's it.
JPanel topPanel = new JPanel();
JPanel centerPanel = new JPanel();
JPanel bottomPanel = new JPanel();
String newLine = System.lineSeparator();
boolean debug;
int totalRight = 0;
int totalWrong = 0;

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

	@Override
	public String calculate(String Expression, String x)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String accumulate(String total, String amount)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawGraph(String expression, String xStart, String increment)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
