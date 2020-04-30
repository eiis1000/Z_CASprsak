package tools.integral;

import config.Settings;
import functions.Function;
import functions.commutative.Sum;
import functions.special.Constant;
import functions.unitary.UnitaryFunction;

import java.util.Map;

public class Integral extends UnitaryFunction {
	public final char respectTo;

	public Integral(Function integrand, char respectTo) {
		super(integrand);
		this.respectTo = respectTo;
	}

	@Override
	public String toString() {
		return "∫[" + function.toString() + "]d" + respectTo;
	}

	@Override
	public UnitaryFunction clone() {
		return new Integral(function.clone(), respectTo);
	}

	@Override
	public UnitaryFunction substitute(char varID, Function toReplace) {
		return new Integral(function.substitute(varID, toReplace), respectTo);
	}

	@Override
	public boolean equals(Function that) {
		if (that instanceof Integral integral)
			return respectTo == integral.respectTo && function.equals(integral.function);
		else
			return false;
	}

	@Override
	public int compareSelf(Function that) {
		if (that instanceof Integral integral) {
			if (respectTo == integral.respectTo)
				return function.compareTo(integral.function);
			else
				return respectTo - integral.respectTo;
		} else {
			return 1;
		}
	}

	@Override
	public Function getDerivative(char varID) {
		return null; //TODO implement
	}

	@Override
	public double evaluate(Map<Character, Double> variableValues) {
		return 0; //TODO implement
	}

	@Override
	public Function simplify() {
		return integrate(); //TODO implement
	}

	@Override
	public UnitaryFunction simplifyInternal() {
		if(Settings.trustImmutability)
			return this;
		else
			return clone();
	}

	@Override
	public UnitaryFunction me(Function function) {
		return new Integral(function, respectTo);
	}

	public Function integrate() {
		if (function instanceof Sum terms) {
			Function[] integratedTerms = new Function[terms.getFunctionsLength()];
			for(int i = 0; i < terms.getFunctionsLength(); i++) {
				integratedTerms[i] = new Integral(terms.getFunctions()[i], respectTo);
			}
			return new Sum(integratedTerms);
		}
		return StageOne.derivativeDivides(function, respectTo);
	}
}
