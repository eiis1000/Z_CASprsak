package functions.unitary;

import functions.Function;

public class Acsc extends UnitaryFunction {
    public Acsc(Function function) {
        super(function);
    }

    @Override
    public UnitaryFunction me(Function function) {
        return null;
    }

    @Override
    public Function getDerivative(int varID) {
        return null;
    }

    @Override
    public double evaluate(double... variableValues) {
        return 0;
    }
}
