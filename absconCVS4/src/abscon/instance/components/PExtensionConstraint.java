package abscon.instance.components;

/**
 * Author: Tomo Bessho Course: CSCE 421 Date: 2/1/2020
 */

public class PExtensionConstraint extends PConstraint {

	private PRelation relation;

	public PRelation getRelation() {
		return relation;
	}

	public PExtensionConstraint(String name, PVariable[] scope, PRelation relation) {
		super(name, scope);
		this.relation = relation;
	}

	public int getMaximalCost() {
		return relation.getMaximalCost();
	}

	public long computeCostOf(int[] tuple) {
		return relation.computeCostOf(tuple);
	}

	public String toString() {
		return super.toString() + ", definition: " + relation.getSemantics() + " " + relation.getStringListOfTuples();
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
}
