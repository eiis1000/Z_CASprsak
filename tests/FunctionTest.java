import parsing.Parser;
import functions.Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionTest {

	@Test void fxReturnsX() {
		Function test = Parser.parse("x");
		assertEquals(2, test.evaluate(2));
	}

	@Test void basicFunctionsWithX() {
		Function test = Parser.parse("x^x+2+5*x");
		assertEquals(44, test.evaluate(3));
	}

	@Test void multipleVariablesWorks() {
        Function test = Parser.parse("x+y+y");
		assertEquals(11, test.evaluate(3, 4));
	}

	@Test void logbWorks() {
		Function test = Parser.parse("logb_3(x)");
		assertEquals(4, test.evaluate(81));
	}

	@Test void trigWorks() {
		Function test = Parser.parse("(sin(x))^2+(cos x)^2");
		assertEquals(1, test.evaluate(81));
	}

	@Test void eToTheXWorks() {
		Function test = Parser.parse("e^x");
		assertEquals(1, test.evaluate(0));
	}

	@Test void lnWorks() {
		Function test = Parser.parse("ln(e)");
		assertEquals(1, test.evaluate(0));
	}

	@Test void tanWorks() {
		Function test = Parser.parse("tan(x)");
		assertEquals(1, test.evaluate(Math.PI/4), 0.0000001);
	}

	@Test void multiplyWorks() {
		Function test = Parser.parse("(1/(x+1))*(cos x)*(x^2-1)");
		assertEquals(-1, test.evaluate(0));
	}

	@Test void addWorks() {
		Function test = Parser.parse("(1/(x+1))+(sec(x))+(x^3-1)");
		assertEquals(1, test.evaluate(0));
	}

	@Test void noSpaces() {
		Function test = Parser.parse("1+x*-3");
		assertEquals(-5, test.evaluate(2));
	}

	@Test void basicPolynomial() {
		Function test = Parser.parse("x^2+5*x+4");
//		System.out.println(test);
		assertEquals(28, test.evaluate(3));
	}

	@Test void mediumPolynomial() {
		Function test = Parser.parse("x^4+5*x^2+4");
//		System.out.println(test);
		assertEquals(130, test.evaluate(3));
	}

	@Test void orderOfOperations() {
		Function test = Parser.parse("10*4-2*(4^2/4)/2/0.5+9");
//		System.out.println(test);
		assertEquals(41, test.evaluate(1));
	}

	@Test void multiplyingByAdjacency() {
		Function test = Parser.parse("3x+5x^2");
		assertEquals(26, test.evaluate(2));
	}

	@Test void multiplyingByAdjacencyAndParentheses() {
		Function test = Parser.parse("3(x+5x^2)");
		assertEquals(66, test.evaluate(2));
	}

	@Test void multiplyingAdjacentVariables() {
		Function test = Parser.parse("xy");
		assertEquals(12, test.evaluate(3, 4));
	}

	@Test void multiplyingAdjacentVariablesAndPowers() {
		Function test = Parser.parse("x^2y^-1");
		assertEquals(8, test.evaluate(4, 2));
	}

	@Test void multiplyingByAdjacencyAndParenthesesBackwards() {
		Function test = Parser.parse("(x+5x^2)3");
		assertEquals(66, test.evaluate(2));
	}

	@Test void multiplyingByAdjacencyFunctions() {
		Function test = Parser.parse("(5+x^2)(cos(x))");
		assertEquals(5, test.evaluate(0));
	}

	@Test void multiplyingByAdjacencyThreeTerms0() {
		Function test = Parser.parse("(1/4)(2/3)sin(x)");
		assertEquals(0, test.evaluate(0));
	}

	@Test void multiplyingByAdjacencyThreeTerms1() {
		Function test = Parser.parse("sin(pi/2)2(x)");
		assertEquals(2, test.evaluate(1), 0.01);
	}


	@Test void multiplyingAdjacencyLogb_2() {
		Function test = Parser.parse("logb_{2}(x)");
		assertEquals(2, test.evaluate(4));
	}

	@Test void multiplyingByAdjacencyLogb_33() {
		Function test = Parser.parse("logb_{33}(x)");
		assertEquals(2, test.evaluate(1089));
	}

	@Test void multiplyingAdjacencyLogb_y() {
		Function test = Parser.parse("-logb_{y}(x)");
		assertEquals(-2, test.evaluate(4, 2));
	}

	@Test void multiplyVariablesNoSpace() {
		Function test = Parser.parse("2xy+3x");
		assertEquals(4, test.evaluate(4,-1));
	}
}
