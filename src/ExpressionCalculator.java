
public class ExpressionCalculator {
String expression = null;

	public ExpressionCalculator(String expression) {
		this.expression = expression;
	}

	public static void main(String[] args) {
		ExpressionCalculator ep = new ExpressionCalculator(args[0]);
		double result = ep.evalExpr(ep.expression);
		System.out.println(result);
	}
	
	
	//Returns substring containing paren set dictated by offSet
	//use length of returned substring to find next offSet
	public String getParen(int offSet)
	{
		int endOffset = offSet+1;
		//until endOffset overflows
		while(endOffset <= this.expression.length())
		{
			if ((this.expression.indexOf('(', endOffset) != -1) && (this.expression.indexOf('(', endOffset))<(this.expression.indexOf(')', endOffset)))
			{
				endOffset = this.expression.indexOf(')', endOffset)+1;
				System.out.println(endOffset);
			}
			else
			{
				return expression.substring(offSet, this.expression.indexOf(")", endOffset)+1);
			}
		}
		return "None found";	
	}
	
	
	public double evalExpr(String expr)
	{
		
		//-----------------------Addition
		String[] toAdd = expr.split("[+]");
		double totalSum = 0;
		if (toAdd.length > 1)
		{
			for(int i =0; i < toAdd.length; i++)
			{
				System.out.println(evalExpr(toAdd[i]));
				totalSum += evalExpr(toAdd[i]);
			}
			return totalSum;
		}
		//----------------------------------
		
		//Subtract---------------------------
		String[] toSubtr = expr.split("[-]");
		totalSum = 0;
		if (toSubtr.length > 1)
		{
			for(int i =0; i < toSubtr.length; i++)
			{
				System.out.println(evalExpr(toSubtr[i]));
				if ((i & 1) == 0)
				{
					totalSum+= evalExpr(toSubtr[i]);
				}
				else
				{
					totalSum -= evalExpr(toSubtr[i]);
				}
			}
			return totalSum;
		}
		//--------------------------------------
		
			try
			{
				double val = Double.parseDouble(expr);
				return val;
			}
			catch (NumberFormatException e)
			{
				
				//Multiply---------------------------------
				String[] toMult = expr.split("[*]");
				if (toMult.length > 1)
				{
					double retVal = 1;
					for(int i =0; i < toMult.length; i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						retVal *= evalExpr(toMult[i]);
					}
					return retVal;
				}
				//-----------------------------------------
				
				//Divide---------------------------------
				String[] toDiv = expr.split("[/]");
				if (toDiv.length > 1)
				{
					double retVal = 1;
					for(int i =0; i < toDiv.length; i++)
					{
						//retVal *= Double.parseDouble(toAdd[i]);
						//retVal /= evalExpr(toDiv[i]);
						
						if (i== 0)
						{
							retVal = evalExpr(toDiv[i]);
						}
						else
						{
							retVal /= evalExpr(toDiv[i]);
						}
					}
					return retVal;
				}
				//-----------------------------------------
			}
			
		//Shit didn't work
		return -666;
	}
}
