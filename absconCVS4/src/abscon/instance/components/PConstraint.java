package abscon.instance.components;

public abstract class PConstraint {
	protected String name;

	protected PVariable[] scope;

	public String getName() {
		return name;
	}

	public PVariable[] getScope() {
		return scope;
	}

	public String scopeToString(PVariable[] scope) {
		String s = "{";
		for (int i = 0; i < scope.length - 1; i++) {
			s += scope[i].getName() + ",";
		}
		s += scope[scope.length - 1].getName() + "}";
		return s;
	}

	public int getArity() {
		return scope.length;
	}

	public PConstraint(String name, PVariable[] scope) {
		this.name = name;
		this.scope = scope;
	}

	public int getMaximalCost() {
		return 1;
	}

	/**
	 * For CSP, returns 0 is the constraint is satified and 1 if the constraint is
	 * violated. <br>
	 * For WCSP, returns the cost for the given tuple.
	 */
	public abstract long computeCostOf(int[] tuple);

	public String toString() {
		// String s = " constraint " + name + " with arity = " + scope.length + ", scope
		// = ";
		// s += scope[0].getName();
		// for (int i = 1; i < scope.length; i++)
		// s += " " + scope[i].getName();
		// return s;

		String s = "Name: " + name + ", variables: " + scopeToString(scope);

		return s;
	}

	public String toString1() {
		String s = "Name: " + name + ", variables: " + scopeToString(scope);

		return s;

	}

	public boolean isGuaranteedToBeDivisionByZeroFree() {
		return true;
	}

	public boolean isGuaranteedToBeOverflowFree() {
		return true;
	}
}
