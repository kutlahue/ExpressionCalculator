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
    JTextField errorTextField  = new JTextField(32);
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
		if(ae.getSource() == expTextField){
			try {
				//attempt to accumulate
				String enteredExpression = expTextField.getText();
				String result			 = calculate(enteredExpression, forxTextField.getText());
				
				logTextArea.append(newLine + enteredExpression + " = " + result);
				// Scroll log all the way to the bottom.  
			    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
				
				//set fields
				resultTextField.setText(result);
				expTextField.setText(""); // clear
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
	public String accumulate(String enteredAmount) throws IllegalArgumentException {
			
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
	public String calculate(String Expression, String x)
			throws IllegalArgumentException {
		return "0";
	}
	//-------------------------------------------------------------------------------//

}
