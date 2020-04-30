package parsing;

import functions.special.Constant;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PreProcessor {

	/**
	 * List of all supported operators
	 */
	public static final String[] operations = {"^", "*", "/", "+", "-", "!", "C", "P", "logb", "log", "ln", "sqrt", "exp", "abs", "sign", "dirac", "sin", "cos", "tan", "csc", "sec", "cot", "asin", "acos", "atan", "acsc", "asec", "acot", "sinh", "cosh", "tanh", "csch", "sech", "coth", "asinh", "acosh", "atanh", "acsch", "asech", "acoth"};


	private PreProcessor(){}


	/**
	 * Checks if a given string is an operator
	 * @param input possible operator
	 * @return true if in {@link #operations}
	 */
	public static boolean isAnOperator(String input) {
		for (String x : operations) {
			if (x.equals(input)) return true;
		}
		return false;
	}

	/**
	 * Turns an infix string into a postfix array
	 * @param infix input string in infix
	 * @return array of postfix tokens
	 */
	public static String[] toPostfix(String infix) {
		List<String> tokens = InfixTokenizer.tokenizeInfix(infix);
		Deque<String> postfix = new LinkedList<>();
		Deque<String> operators = new LinkedList<>();

		for (String token : tokens) {
//			System.out.println(operators);
//			System.out.println(postfix);
//			System.out.println();
			if (Constant.isSpecialConstant(token)) {
				postfix.add(token);
			} else if (isAnOperator(token)) {
				operators.push(token);
			} else if ("(".equals(token)) {
				operators.push(token);
			} else if (")".equals(token)) {
				while (!"(".equals(operators.peek())) {
					postfix.add(operators.pop());
				}
				operators.pop();
			} else {
				postfix.add(token);
			}
		}

		while (operators.size() != 0) {
			postfix.add(operators.pop());
		}
		return postfix.toArray(new String[0]);
	}

}
