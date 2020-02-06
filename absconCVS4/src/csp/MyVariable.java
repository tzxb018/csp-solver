package csp;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PDomain;
import abscon.instance.components.PVariable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MyVariable {
	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the
	/// homework
	protected String name;
	protected int[] initialDomain;
	protected int[] currentDomain;
	protected ArrayList<MyConstraint> constraints;
	protected ArrayList<MyVariable> neighbors;

	// public static final Comparator<MyVariable> variableComparator = new
	// Comparator<MyVariable>() {
	// @Override
	// public int compare(MyVariable c1, MyVariable c2) {
	// return extractInt(c1.getName()) - extractInt(c2.getName());
	// }

	// public int extractInt(String s) {
	// String num = s.replaceAll("\\D", "");
	// // return 0 if no digits found
	// if (num.isEmpty()) {
	// return 0;
	// } else {
	// return Integer.parseInt(num);
	// }
	// }
	// };

	public MyVariable(PVariable var) {
		varRef = var;
		name = var.getName();
		initialDomain = var.getDomain().getValues();

		constraints = new ArrayList<MyConstraint>();
		neighbors = new ArrayList<MyVariable>();

	}

	public String getName() {
		return this.name;
	}

	public void addConstraints(MyConstraint cnst) {
		constraints.add(cnst);
	}

	public void addNeighbors(MyVariable var) {
		neighbors.add(var);
	}

	public ArrayList<MyConstraint> getConstraints() {
		return this.constraints;

	}

	public ArrayList<MyVariable> getNeighbors() {
		return this.neighbors;
	}

	public void setConstraints(ArrayList<MyConstraint> constraints) {
		this.constraints = constraints;
	}

	public void setNeighbors(ArrayList<MyVariable> neighbors) {
		this.neighbors = neighbors;
	}

	public String toString() {
		String s = "Name: " + name + ", initial-domain: {";

		for (int i = 0; i < initialDomain.length - 1; i++) {
			s += initialDomain[i];
			s += ",";
		}
		s += initialDomain[initialDomain.length - 1] + "}, ";
		s += "constraints: {";

		for (int i = 0; i < constraints.size() - 1; i++) {
			s += constraints.get(i).getName() + ",";
		}
		s += constraints.get(constraints.size() - 1).getName() + "}";
		s += ", neighbors: {";

		for (int i = 0; i < neighbors.size() - 1; i++) {
			s += neighbors.get(i).getName() + ",";
		}
		s += neighbors.get(neighbors.size() - 1).getName() + "}";

		return s;
	}

}