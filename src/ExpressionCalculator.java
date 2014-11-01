import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.List;


public class ExpressionCalculator implements ActionListener, Calculator {
	
	//begin dynamic var block
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	double total;
	String newLine = System.lineSeparator();
	
	Frame  modewindow  = new JFrame("Mode Select");
	JRadioButton ExpressionModeButton = new JRadioButton("Expression Mode");
    JRadioButton AccumulatingModeButton = new JRadioButton("Accumulating Mode");
    ButtonGroup modeButtonGroup = new ButtonGroup();
	
	Frame  window      = new JFrame("Accumulating Calculator");
	
	JPanel accpanel = new JPanel();
	JPanel exppanel = new JPanel();
	JPanel sPanel = new JPanel();
	JPanel cPanel = new JPanel();
	
    JButton clearExpButton = new JButton("CLEAR");
    JButton clearAccButton = new JButton("CLEAR");
    
    JLabel     amountLabel     = new JLabel("Enter amount",SwingConstants.RIGHT);
    JLabel     totalLabel      = new JLabel("Total",SwingConstants.RIGHT);
    JLabel	   checkLabel	   = new JLabel("Drop .00",SwingConstants.RIGHT);
    
    JLabel     expLabel     	= new JLabel("Enter expression",SwingConstants.RIGHT);
    JLabel     resultLabel      = new JLabel("Result",SwingConstants.RIGHT);
    JLabel     forxLabel		= new JLabel("Value for x", SwingConstants.RIGHT);
    
    JTextField amountTextField = new JTextField(8);
    JTextField totalTextField  = new JTextField(8);
    JTextField errorTextField  = new JTextField(64);
    JTextField expTextField	   = new JTextField(16);
    JTextField resultTextField = new JTextField(8);
    JTextField forxTextField   = new JTextField(8);
    
    JCheckBox  decimalCheck = new JCheckBox();
    JTextArea   logTextArea   = new JTextArea(20,40);//depth(in rows),width(in chars)
    JScrollPane logScrollPane = new JScrollPane(logTextArea);
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //end dynamic var block
    
    //CMD line loader
    //-------------------------------------------------------------------------------//
  	public static void main(String[] args) {
  		System.out.println("main thread enters main() from command line loader.");
  		new ExpressionCalculator(); //load rest of program
  		System.out.println("main thread returning to the command line loader");
  		
  	}
    //-------------------------------------------------------------------------------//

    //-------------------------------------------------------------------------------//
	public ExpressionCalculator() {
		System.out.println("main thread enters constructor method, called from the NEW program loader.");
		
		//Build GUI	
		//Accumulator mode panel
		accpanel.setLayout(new GridLayout(1,7));// 1 row, 7 cols
		accpanel.add(clearAccButton);
	    accpanel.add(amountLabel);
	    accpanel.add(amountTextField);
	    accpanel.add(totalLabel);
	    accpanel.add(totalTextField);
	    accpanel.add(checkLabel);
	    accpanel.add(decimalCheck);
	    
	    //Expression mode panel
	    exppanel.setLayout(new GridLayout(1,7));// 1 row, 5 cols
	    exppanel.add(clearExpButton);
	    exppanel.add(expLabel);
	    exppanel.add(expTextField);
	    exppanel.add(forxLabel);
	    exppanel.add(forxTextField);
	    exppanel.add(resultLabel);
	    exppanel.add(resultTextField);
	    
	    //errors and log
	    sPanel.add(errorTextField);
	    cPanel.add(logScrollPane);
	    
	    //Main window and field adjustments
		window.setSize(900,550);
		window.setLocation(300, 300);
	    
	    //Set fonts
	    logTextArea.setFont(new Font("Times Roman", Font.BOLD, 16));
	    errorTextField.setFont(new Font("Times Roman", Font.BOLD, 14));
	    
	    amountLabel.setFont(new Font("Times Roman", Font.BOLD, 14));
	    totalLabel.setFont(new Font("Times Roman", Font.BOLD, 14));
	    checkLabel.setFont(new Font("Times Roman", Font.BOLD, 14));
	    
	    expLabel.setFont(new Font("Times Roman", Font.BOLD, 14));
	    resultLabel.setFont(new Font("Times Roman", Font.BOLD, 14));
	    
	    clearExpButton.setFont(new Font("Times Roman", Font.BOLD, 14));
	    clearAccButton.setFont(new Font("Times Roman", Font.BOLD, 14));
	    
	    //Set access
	    totalTextField.setEditable(false);
	    errorTextField.setEditable(false);
	    resultTextField.setEditable(false);
	    logTextArea.setEditable(false);
	    
	    clearExpButton.addActionListener(this);//give clear button our address
	    clearAccButton.addActionListener(this);//give clear button our address
	    amountTextField.addActionListener(this);// give amount text field our address
	    expTextField.addActionListener(this);// give expression text field our address
	    forxTextField.addActionListener(this);//get listener for x field

	    //set button group and window for mode selection
	    modeButtonGroup.add(ExpressionModeButton);
	  	modeButtonGroup.add(AccumulatingModeButton);
	  	
	  	ExpressionModeButton.addActionListener(this);//give expression mode our address
	    AccumulatingModeButton.addActionListener(this);//give accumulate mode our address
	  	    
	  	ExpressionModeButton.setFont(new Font("Times Roman", Font.BOLD, 14));
	  	AccumulatingModeButton.setFont(new Font("Times Roman", Font.BOLD, 14));
	  	    
	  	modewindow.setSize(250, 100);
	  	modewindow.setLocation(50, 300);
	  	((JFrame) modewindow).getContentPane().add(AccumulatingModeButton, "North");
	  	((JFrame) modewindow).getContentPane().add(ExpressionModeButton, "South");
	  	((JFrame) modewindow).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	modewindow.setVisible(true);
		
		System.out.println("after building GUI, main thread returning from constructor to NEW program loader.");
		
	}
	//-------------------------------------------------------------------------------//

	@Override
	//-------------------------------------------------------------------------------//
	public void actionPerformed(ActionEvent ae) {

		//when clearing
		if ((ae.getSource() == clearAccButton) ||
			(ae.getSource() == clearExpButton) ){ // do clear function
			clear();
			
			logTextArea.append(newLine + "CLEARED");
			// Scroll log all the way to the bottom.  
		    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
			
			return;
		}

		//when inputting amount to accumulate
		if (ae.getSource() == amountTextField){ // add amount to total
			try {
				//attempt to accumulate
				String enteredAmount = amountTextField.getText();
				String newTotal = accumulate(enteredAmount);
				String prevTotal = totalTextField.getText();
				
				//remove .00 if needed
				if(newTotal.endsWith(".00") && decimalCheck.isSelected())
					newTotal = newTotal.substring(0, newTotal.length() - 3);
				
				//add log entry
				if(prevTotal.trim().length() == 0)  
					prevTotal = "0";
				
				logTextArea.append(newLine + prevTotal + " + " + enteredAmount.trim() + 
								   " = " + newTotal);
				// Scroll log all the way to the bottom.  
			    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
				
				//set field
				totalTextField.setText(newTotal);
				amountTextField.setText(""); // clear
				errorTextField.setText(""); // clear
				errorTextField.setBackground(Color.white);
			}
			catch(IllegalArgumentException iae){
				//set error field
				errorTextField.setText(iae.getMessage());
				errorTextField.setBackground(Color.pink);
			}
			return;
		}
		
		//When inputting expression
		if(ae.getSource() == expTextField || ae.getSource() == forxTextField){
			try {
				//attempt to accumulate
				String enteredExpression = expTextField.getText();
				String result			 = calculate(enteredExpression, forxTextField.getText());
				
				logTextArea.append(newLine + enteredExpression + " = " + result);
				// Scroll log all the way to the bottom.  
			    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
				
				//set fields
				resultTextField.setText(result);
				errorTextField.setText(""); // clear
				errorTextField.setBackground(Color.white);
			}
			catch(IllegalArgumentException iae){
				//set error field
				errorTextField.setText(iae.getMessage());
				errorTextField.setBackground(Color.pink);
			}
			
		}
		
		//Selection for Expression Mode
		if(ae.getSource() == ExpressionModeButton){
			
			window.setVisible(false);
			
			//clear anything with fields involving accumulating
			totalTextField.setText(""); // clear
			amountTextField.setText(""); // clear
			errorTextField.setText(""); // clear
			errorTextField.setBackground(Color.white);
			
			((JFrame) window).getContentPane().removeAll();
			((JFrame) window).getContentPane().add(exppanel, "North");
			((JFrame) window).getContentPane().add(sPanel,"South");
		    ((JFrame) window).getContentPane().add(cPanel,"Center");
		    window.setTitle("Expression Calculator");
			
			window.setVisible(true);
				
		}
		
		//Selection for Accumulating Mode
		if(ae.getSource() == AccumulatingModeButton){
			
			window.setVisible(false);
			
			//clear anything with fields involving expressions
			resultTextField.setText(""); // clear
			expTextField.setText(""); // clear
			forxTextField.setText(""); // clear
			errorTextField.setText(""); // clear
			errorTextField.setBackground(Color.white);
			
			((JFrame) window).getContentPane().removeAll();
			((JFrame) window).getContentPane().add(accpanel, "North");
			((JFrame) window).getContentPane().add(sPanel,"South");
		    ((JFrame) window).getContentPane().add(cPanel,"Center");
		    window.setTitle("Accumulating Calculator");
			
			window.setVisible(true);
					
		}
			
	}
	//-------------------------------------------------------------------------------//

	//accumulate function
	//-------------------------------------------------------------------------------//
	@Override
	public String accumulate(String enteredAmount) throws IllegalArgumentException, StringIndexOutOfBoundsException {
			
		//look for leading zeroes
		if (enteredAmount.startsWith("0"))
		    throw new IllegalArgumentException(CalculatorConstants.LEADING_ZERO);
			
		//look for bad operators
		if (enteredAmount.startsWith("/")  // all of these
			|| enteredAmount.startsWith("x")  // are input
			|| enteredAmount.startsWith("*")) // errors.
			throw new IllegalArgumentException(CalculatorConstants.ONLY_ADD);
			
		//Look for null/empty strings
		enteredAmount = enteredAmount.trim();
		
		if ((enteredAmount == null)        // bad parm!
			|| (enteredAmount.length() == 0)) // bad parm!
			throw new IllegalArgumentException(CalculatorConstants.MISSING_AMOUNT);
			
			//Make +/- strings more lenient
		if (enteredAmount.startsWith("+ ")
			|| enteredAmount.startsWith("- "))				
			enteredAmount = enteredAmount.substring(0,1) + enteredAmount.substring(1).trim();
			
		//check decimals
		if (enteredAmount.contains(".")){
			int periodOffset = enteredAmount.indexOf(".");
			String decimalPortion = enteredAmount.substring(periodOffset+1);
			if (decimalPortion.length() != 2)  
				throw new IllegalArgumentException(CalculatorConstants.TWO_DECIMALS);
		}
			
		//attempt to parse string into double
		double amount;
		try{
			amount = Double.parseDouble(enteredAmount);
		}
		//catch NaN
		catch(NumberFormatException nfe){
			throw new IllegalArgumentException(CalculatorConstants.NOT_NUMERIC);
		}
		//accumulate into total
		total = total + amount;
			
		String newTotal = String.valueOf(total);
			
		//add trailing 0's or round up if necessary for new total
		if (newTotal.contains(".")){
			int periodOffset = newTotal.indexOf(".");
			String decimalPortion = newTotal.substring(periodOffset+1);
				
			if (decimalPortion.length() == 0)
				newTotal += "00";
			if (decimalPortion.length() == 1)
				newTotal += "0";
			if (decimalPortion.length() >  2){
				total += .005; // round up 
				newTotal = String.valueOf(total);
				periodOffset = newTotal.indexOf(".");
				newTotal = newTotal.substring(0,periodOffset+3);
			}
		}
			
		return newTotal;
	}
	//-------------------------------------------------------------------------------//

	//Clear - Clears all text boxes in GUI
	//-------------------------------------------------------------------------------//
	@Override
	public void clear() {
		//if accumulate, clear accumulator stuff
		if(modeButtonGroup.getSelection() == AccumulatingModeButton.getModel()){
			amountTextField.setText("");//set to blank on GUI
			totalTextField.setText(""); //blank (better than "0", which could be a total value.)
			total = 0;
			amountTextField.requestFocus(); // set cursor in.
		}
		
		//if expression clear expression stuff
		if(modeButtonGroup.getSelection() == ExpressionModeButton.getModel()){
			expTextField.setText("");//set to blank on GUI
			forxTextField.setText("");//blank
			resultTextField.setText(""); //blank (better than "0", which could be a total value.)
			expTextField.requestFocus(); // set cursor in.
		}
			
		//reset error field
		errorTextField.setText("");
		errorTextField.setBackground(Color.white);
			
		return;

	}
	//-------------------------------------------------------------------------------//

	@Override
	//-------------------------------------------------------------------------------//
	public String calculate(String Expression, String x) throws IllegalArgumentException
	{
		if(Expression == null || Expression.equals("")){
			throw new IllegalArgumentException("No expression was given.");
		}
		
		if((x == null || x.equals("")) && Expression.contains("x"))
		{
			throw new IllegalArgumentException("No expression for x was given.");
		}

		if(Expression.contains("x")){
			try{
				evalExpr(x);
			}
			catch(Exception e){
				throw new IllegalArgumentException("Invalid expression for x.");
			}
			
		}
		
		Expression = Expression.replace("x", x);
		try{
			return String.valueOf(evalExpr(Expression));
		}
		catch(IllegalArgumentException iae){
			throw iae;
		}
		catch(StringIndexOutOfBoundsException sioobe){
			throw new IllegalArgumentException("Expression is invalid, please make sure all parentheses are closed.");
		}
		
	}
	//-------------------------------------------------------------------------------//
	
	//-------------------------------------------------------------------------------//
	public List<String> mySplit(String expr, char splitOn)
	{
		//Implement own split function-------
		List<String> toAdd = new ArrayList<String>();
		int offSet = 0;
		int oldOffSet = -1;
		int leftCount = 0;
		int rightCount = 0;
		boolean added = false;
		int osAdd = 0;
		for (int i = 0; i < expr.length(); i++)
		{
			if (expr.charAt(offSet) == '(')
			{
				rightCount = 0;
				leftCount = 1;
				while (leftCount != rightCount)
				{
					offSet++;
					i++;
					if(expr.charAt(offSet) == '(')
					{
						leftCount++;
					}
					if(expr.charAt(offSet) == ')')
					{
						//System.out.println("Found right, offS is " + offSet);
						rightCount++;
					}
				}
			}
			if ((expr.charAt(offSet)==splitOn))
			{
				added = true;
				
				//handle unary neg
				if ((expr.substring(oldOffSet+1, offSet).length()==0) && (splitOn == '-'))
				{
					//System.out.println("Handling unary neg");
					toAdd.add("0");
					osAdd = 0;
					
				}
				else if (((expr.charAt(offSet-1) == 'r') || (expr.charAt(offSet-1) == '^') || (expr.charAt(offSet-1) == '/') || 
						 (expr.charAt(offSet-1) == '*'))&&(splitOn=='-'))
				{
					//System.out.println("Ignoring neg with preceeding mult/div");
					//toAdd.add("0");
					osAdd = -1;
					added=false;
					//Do nothing, number will be negated when parsed to double
				}
				else
				{
					toAdd.add(expr.substring(oldOffSet+1, offSet+osAdd));
					osAdd = 0;
				}
				
				//System.out.println("old is: "+ oldOffSet+" offset is:" + offSet);
				//System.out.println("putting " + expr.substring(oldOffSet+1, offSet+osAdd));
				//System.out.println(expr.substring(oldOffSet+1, offSet).length());
				
				oldOffSet = offSet;
			}
			offSet++;
		}
				
		if (added)
		{
			
			//handle unary neg
			if ((expr.substring(oldOffSet+1, offSet).length()==0) && (splitOn == '-'))
			{
				//System.out.println("Handling unary neg");
				toAdd.add("0");
				osAdd = 0;
				
			}
			else if (((expr.charAt(offSet-1) == 'r') || (expr.charAt(offSet-1) == '^') || (expr.charAt(offSet-1) == '/') || 
					 (expr.charAt(offSet-1) == '*')) && (splitOn =='-'))
			{
				//System.out.println("Ignoring neg with preceeding mult/div");
				osAdd = -1;
				//Do nothing, number will be negated when parsed to double
			}
			else
			{
				toAdd.add(expr.substring(oldOffSet+1, offSet+osAdd));
				osAdd = 0;
			}
			//System.out.println("In last iter");
			//System.out.println("old is: "+ oldOffSet);
			//System.out.println("putting " + expr.substring(oldOffSet+1, expr.length()+osAdd));
		}
		return toAdd;
		//-----------------------------
	}
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	public double evalExpr(String expr)
	{
		
		//-----------------------Addition
		//String[] toAdd = expr.split("\\+(?![^(]*\\))");
		//String[] toAdd = expr.split("\\+");
		
		List<String> toAdd = mySplit(expr, '+');
		
		double totalSum = 0;
		if (toAdd.size() > 1)
		{
			for(int i =0; i < toAdd.size(); i++)
			{
				//System.out.println("Adding string "+ (String) toAdd.get(i));
				//System.out.println("Adding"+evalExpr((String) toAdd.get(i)));
				totalSum += evalExpr((String) toAdd.get(i));
			}
			return totalSum;
		}
		//----------------------------------
		
		//Subtract---------------------------
		//String[] toSubtr = expr.split("[-]");
		//String[] toSubtr = expr.split("\\-(?![^(]*\\))");
		
		List<String> toSubtr = mySplit(expr, '-');
		totalSum = 0;
		if (toSubtr.size() > 1)
		{
			for(int i =0; i < toSubtr.size(); i++)
			{
				//System.out.println(evalExpr(toSubtr.get(i)));
				if ((i & 1) == 0)
				{
					totalSum+= evalExpr(toSubtr.get(i));
				}
				else
				{
					totalSum -= evalExpr(toSubtr.get(i));
				}
			}
			return totalSum;
		}
		//--------------------------------------
		
			try
			{
				//System.out.println(expr);
				if (expr.indexOf("^")!= -1)
				{
					throw new NumberFormatException("Found exp");
				}
				else
				{
					double val = Double.parseDouble(expr);
					//System.out.println("Parsed "+val);
					return val;
				}
			}
			catch (NumberFormatException e)
			{
				
				//Multiply---------------------------------
				//String[] toMult = expr.split("\\*(?![^(]*\\))");
				
				List<String> toMult = mySplit(expr, '*');
				if (toMult.size() > 1)
				{
					double retVal = 1;
					for(int i =0; i < toMult.size(); i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						retVal *= evalExpr(toMult.get(i));
					}
					return retVal;
				}
				//-----------------------------------------
				
				//Divide---------------------------------
				//String[] toDiv = expr.split("\\/(?![^(]*\\))");
				//String[] toDiv = expr.split("[^(]*/[^)]*");
				
				List<String> toDiv = mySplit(expr, '/');
				if (toDiv.size() > 1)
				{
					double retVal = 1;
					for(int i =0; i < toDiv.size(); i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						//retVal /= evalExpr(toDiv[i]);
						
						if (i== 0)
						{
							retVal = evalExpr(toDiv.get(i));
						}
						else
						{
							retVal /= evalExpr(toDiv.get(i));
						}
					}
					return retVal;
				}
				//-----------------------------------------
				
				//Exponent---------------------------------
				//String[] toMult = expr.split("\\*(?![^(]*\\))");
				
				List<String> toExp = mySplit(expr, '^');
				if (toExp.size() > 1)
				{
					double retVal = evalExpr(toExp.get(0));
					//double retVal = 1;
					for(int i =1; i < toExp.size(); i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						//System.out.println("power is " + toExp.get(i));
						//System.out.println(retVal + " raised to the " + evalExpr(toExp.get(i)));
						retVal = Math.pow(retVal, evalExpr(toExp.get(i)));
					}
					return retVal;
				}
				
				//-----------------------------------------
				
				//Root---------------------------------
				//String[] toMult = expr.split("\\*(?![^(]*\\))");
				
				List<String> toRoot = mySplit(expr, 'r');
				if (toRoot.size() > 1)
				{
					double retVal = evalExpr(toRoot.get(0));
					//double retVal = 1;
					for(int i =1; i < toRoot.size(); i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						//System.out.println(toRoot.get(i));
						retVal = Math.pow(retVal, (1/evalExpr(toRoot.get(i))));
					}
					return retVal;
				}
				
				//-----------------------------------------
				
				//Parentheses----------------------------
				if (expr.indexOf("(") == 0)
				{
					//System.out.println("Detected Parens");
					return evalExpr(expr.substring(1, expr.length()-1));
				}
				//---------------------------------------
			}
			
		throw new IllegalArgumentException("The expression passed was not valid, please make sure there are no missing operands or operators.");
		//return -666;
	}
	//-------------------------------------------------------------------------------//
	
}
