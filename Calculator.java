import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Calculator {
	// using numStack as the result stack to show the result;
	// and using stateList to store the state of each operation;
	Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
	private Stack<String> numStack = new Stack<String>();
	LinkedList<String> stateList = new LinkedList<String>();

	private void calculate(String str) {
		String[] strs = str.split(" ");
		for (String element : strs) {
			if (pattern.matcher(element).matches()) {
				numStack.push(element);// store the number and then save this
										// state into linkedlist;
				stateList.add(numStack.toString());
			} else if (element.equals("clear")) {// clear stake and save the
													// state;
				numStack.clear();
				stateList.add(numStack.toString());
			} else if (!element.equals("sqrt") && numStack.size() >= 2 && !element.equals("undo")) {
				BigDecimal a = new BigDecimal(numStack.pop());// since the
																// number could
																// be finance
																// related using
																// bigdecimal
				BigDecimal b = new BigDecimal(numStack.pop());
				switch (element) {// do the operation according to the operator
									// it reads
				case "+":
					numStack.push(String.valueOf(a.add(b).intValue()));
					stateList.add(numStack.toString());
					break;
				case "-":
					numStack.push(String.valueOf(b.subtract(a).intValue()));
					stateList.add(numStack.toString());
					break;
				case "*":
					numStack.push(String.valueOf(a.multiply(b).doubleValue()));
					stateList.add(numStack.toString());
					break;
				case "/":
					numStack.push(String.valueOf(b.divide(a, 10, BigDecimal.ROUND_HALF_UP).doubleValue()));
					stateList.add(numStack.toString());
					break;
				}
			} else if (element.equals("sqrt") && numStack.size() >= 1) {
				double a = Double.valueOf(numStack.pop());
				numStack.push(String.valueOf(new BigDecimal(Math.sqrt(a)).setScale(10, 0).doubleValue()));
				stateList.add(numStack.toString());
			} else if (element.equals("undo")) {
				// using a temp stack to save the previous state of stack
				Stack<String> temp = new Stack<String>();
				stateList.removeLast();
				String[] state = stateList.getLast().replace("[", "").replace("]", "").split(", ");
				for (String s : state) {
					if (pattern.matcher(s).matches()) {
						temp.push(s);
					}
				}
				// make the stack point to the new stack;
				numStack = temp;
				
				//handle the exception with the invalid operator
			} else if (!numStack.isEmpty()) {
				System.out.println("operator " + element + " (position: " + (str.lastIndexOf(element) + 1)
						+ "): insucient parameters");
				return;
			}
		}

	}

	public String getResult(String str) {
		calculate(str);

		String result = "";
		if (numStack.size() == 1) {
			result = numStack.peek();
		} else {
			for (String n : numStack) {
				result = result + n + " ";
			}
		}
		return result;
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		Calculator cal = new Calculator();
		while (true) {
			String str = sc.nextLine();
			System.out.println("Stack: " + cal.getResult(str));
		}

	}
}