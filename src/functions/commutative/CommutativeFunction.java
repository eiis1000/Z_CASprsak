package functions.commutative;

import functions.GeneralFunction;
import functions.special.Constant;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommutativeFunction extends GeneralFunction {

	/**
	 * Array of {@link GeneralFunction}s operated on by the {@link CommutativeFunction}
	 */
	protected final GeneralFunction[] functions;

	/**
	 * The identity of the {@link CommutativeFunction}. (e.g. 1 for * and 0 for +)
	 */
	protected double identityValue;

	/**
	 * The operation performed by the {@link CommutativeFunction}
	 */
	public DoubleBinaryOperator operation;

	/**
	 * Constructs a new {@link CommutativeFunction}
	 * @param functions The {@link GeneralFunction}s that will be acted on
	 */
	public CommutativeFunction(GeneralFunction... functions) {
		this.functions = functions;
		Arrays.sort(this.functions);
	}

	/**
	 * Returns {@link #functions}
	 * @return {@link #functions}
	 */
	public GeneralFunction[] getFunctions() {
		return functions;
	}

	/**
	 * Returns an instance of this {@link GeneralFunction}
	 * @param functions Constructor parameter
	 * @return an instance of this {@link GeneralFunction}
	 */
	public abstract CommutativeFunction me(GeneralFunction... functions);

	public boolean equalsFunction(GeneralFunction that) {
		if (that instanceof CommutativeFunction function && this.getClass().equals(that.getClass()))
			return Arrays.equals(functions, function.getFunctions());
		else
			return false;
	}

	public int compareSelf(GeneralFunction that) {
		if (that instanceof CommutativeFunction function) {
			if (functions.length != function.getFunctions().length)
				return functions.length - function.getFunctions().length;
			GeneralFunction[] thisFunctions = functions;
			GeneralFunction[] thatFunctions = function.getFunctions();
			for (int i = 0; i < thisFunctions.length; i++)
				if (!thisFunctions[i].equalsFunction(thatFunctions[i]))
					return thisFunctions[i].compareTo(thatFunctions[i]);
		} else {
			throw new IllegalCallerException("Illegally called CommutativeFunction.compareSelf on a non-CommutativeFunction");
		}
		throw new IllegalStateException("This code in CommutativeFunction.compareSelf should never run.");
	}

	public GeneralFunction substituteAll(Predicate<? super GeneralFunction> test, Function<? super GeneralFunction, ? extends GeneralFunction> replacer) {
		if (test.test(this))
			return replacer.apply(this);

		GeneralFunction[] newFunctions = new GeneralFunction[functions.length];
		for (int i = 0; i < functions.length; i++)
			newFunctions[i] = functions[i].substituteAll(test, replacer);
		return me(newFunctions);
	}

	public GeneralFunction simplify() {
		return this.simplifyInternal().simplifyTrivialElement();
	}

	/**
	 * Returns current {@link CommutativeFunction} after {@link #simplifyElements()}, {@link #simplifyPull()}, {@link #simplifyIdentity()}, and {@link #simplifyConstants()}
	 * @return the simplified {@link CommutativeFunction}
	 */
	public CommutativeFunction simplifyInternal() {
		CommutativeFunction current = this;
		current = current.simplifyElements();
		current = current.simplifyPull();
		current = current.simplifyIdentity();
		current = current.simplifyConstants();
		return current;
	}

	/**
	 * Returns current {@link CommutativeFunction} after simplifying each {@link GeneralFunction} in {@link #functions}
	 * @return current {@link CommutativeFunction} after simplifying each {@link GeneralFunction} in {@link #functions}
	 */
	public abstract CommutativeFunction simplifyElements();

	/**
	 * Returns current {@link CommutativeFunction} after simplifying the {@link #identityValue}
	 * @return current {@link CommutativeFunction} after simplifying the {@link #identityValue}
	 */
	public CommutativeFunction simplifyIdentity() {
		List<GeneralFunction> toPut = new ArrayList<>(Arrays.asList(functions));
		toPut.removeIf(generalFunction -> generalFunction instanceof Constant constant && constant.constant == identityValue);
		return me(toPut.toArray(new GeneralFunction[0]));
	}

	/**
	 * Returns current {@link CommutativeFunction} after combined the {@link Constant}s
	 * @return current {@link CommutativeFunction} after combined the {@link Constant}s
	 */
	public CommutativeFunction simplifyConstants() {
		if (hasMultipleConstants()) {
			double accumulator = identityValue;
			List<GeneralFunction> functionList = new LinkedList<>(List.of(functions));
			ListIterator<GeneralFunction> iter = functionList.listIterator();
			while (iter.hasNext()) {
				if (iter.next() instanceof Constant constant) {
					accumulator = operation.applyAsDouble(accumulator, constant.constant);
					iter.remove();
				}
			}
			functionList.add(new Constant(accumulator));
			return me(functionList.toArray(new GeneralFunction[0]));
		} else {
			return this;
		}
	}

	private boolean hasMultipleConstants() {
		boolean flag = false;
		for (GeneralFunction function : functions)
			if (function instanceof Constant)
				if (flag)
					return true;
				else
					flag = true;
		return false;
	}


	/**
	 * Returns current {@link CommutativeFunction} after instance of the same {@link CommutativeFunction} have been pulled out and each term added to {@link #functions}
	 * @return current {@link CommutativeFunction} after instance of the same {@link CommutativeFunction} have been pulled out and each term added to {@link #functions}
	 */
	public CommutativeFunction simplifyPull() {
		for (int i = 0; i < functions.length; i++)
			if (this.getClass().equals(functions[i].getClass()))
				return me(pullUp(functions, ((CommutativeFunction) functions[i]).getFunctions(), i)).simplifyPull();

		return this;
	}

	private static GeneralFunction[] pullUp(GeneralFunction[] outer, GeneralFunction[] inner, int indexInOuter) {
		GeneralFunction[] newArray = new GeneralFunction[outer.length + inner.length - 1];
		if (indexInOuter > 0)
			System.arraycopy(outer, 0, newArray, 0, indexInOuter);
		if (indexInOuter < outer.length - 1)
			System.arraycopy(outer, indexInOuter + 1, newArray, indexInOuter, outer.length - indexInOuter - 1);
		System.arraycopy(inner, 0, newArray, outer.length - 1, inner.length);
		return newArray;
	}

	/**
	 * Returns identity {@link Constant} if {@link #functions} length is 0 or the {@link GeneralFunction} if {@link #functions} length is 1
	 * @return identity {@link Constant} if {@link #functions} length is 0 or the {@link GeneralFunction} if {@link #functions} length is 1
	 */
	public GeneralFunction simplifyTrivialElement() {
		if (functions.length == 0)
			return new Constant(identityValue);
		else if (functions.length == 1)
			return functions[0].simplify();
		else
			return this;
	}

	public @NotNull Iterator<GeneralFunction> iterator() {
		return new CommutativeIterator();
	}

	private class CommutativeIterator implements Iterator<GeneralFunction> {
		private int loc;

		private CommutativeIterator() {
			loc = 0;
		}

		@Override
		public boolean hasNext() {
			return loc < functions.length;
		}

		@SuppressWarnings("ValueOfIncrementOrDecrementUsed")
		@Override
		public GeneralFunction next() {
			if (!hasNext())
				throw new NoSuchElementException("Out of elements in CommutativeFunction " + Arrays.toString(functions));
			return functions[loc++];
		}
	}
}
