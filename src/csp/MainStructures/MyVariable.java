package csp.MainStructures;

import abscon.instance.components.PVariable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyVariable {
	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the
	/// homework
	protected String name;
	protected int[] initialDomain;
	public ArrayList<Integer> currentDomain;
	protected ArrayList<MyConstraint> constraints;
	protected ArrayList<MyConstraint> constraints_static;
	protected LinkedList<MyVariable> neighbors;
	protected LinkedList<MyVariable> neighbors_static;

	// Creating a lexiographic comparator for the comparator by looking at the
	// variable's name

	// taken from
	// https://stackoverflow.com/questions/13973503/sorting-strings-that-contains-number-in-java
	public static final Comparator<MyVariable> SOL_COMPARATOR = new Comparator<MyVariable>() {
		@Override
		public int compare(MyVariable v1, MyVariable v2) {
			return extractInt(v1.getName()) - extractInt(v2.getName());

		}

		public int extractInt(String s) {
			String num = s.replaceAll("\\D", "");
			// return 0 if no digits found
			if (num.isEmpty()) {
				return 0;
			} else {
				return Integer.parseInt(num);
			}
		}
	};

	public static final Comparator<MyVariable> LX_COMPARATOR = new Comparator<MyVariable>() {
		@Override
		public int compare(MyVariable v1, MyVariable v2) {
			return v1.getName().compareTo(v2.getName());
		}
	};

	// Creating a comparator with domain sizes with ties broken up with
	// lexiographical ordering
	public static final Comparator<MyVariable> LD_COMPARATOR = new Comparator<MyVariable>() {
		@Override
		public int compare(MyVariable v1, MyVariable v2) {

			Integer s1 = v1.getDomain().length;
			Integer s2 = v2.getDomain().length;

			int sComp = s1.compareTo(s2);

			if (sComp != 0) {
				return sComp;
			}
			return v1.getName().compareTo(v2.getName());

		}
	};

	// Creating a comparator with ddr sizes with ties broken up with
	// lexiographical ordering
	public static final Comparator<MyVariable> DDR_COMPARATOR = new Comparator<MyVariable>() {
		@Override
		public int compare(MyVariable v1, MyVariable v2) {

			double ddr1, ddr2 = 0;

			ddr1 = v1.getDomain().length * v1.getConstraints().size();

			ddr2 = v2.getDomain().length * v2.getConstraints().size();

			int sComp = Double.compare(ddr1, ddr2);

			if (sComp != 0) {
				return sComp;
			}

			return v1.getName().compareTo(v2.getName());
		}

	};

	public MyVariable(PVariable var) {
		varRef = var;
		name = var.getName();
		initialDomain = var.getDomain().getValues();

		currentDomain = new ArrayList<Integer>();
		// Deep copying all the elements of the inital array into the current domain
		for (int i = 0; i < initialDomain.length; i++) {
			currentDomain.add((Integer) initialDomain[i]);
		}

		constraints = new ArrayList<MyConstraint>();
		neighbors = new LinkedList<MyVariable>();
		neighbors_static = new LinkedList<MyVariable>();

	}

	public String getName() {
		return this.name;
	}

	public int[] getDomain() {
		return this.initialDomain;
	}

	public void addConstraints(MyConstraint cnst) {
		constraints.add(cnst);
	}

	public void addNeighbors(MyVariable var) {
		if (!this.neighbors.contains(var)) {
			this.neighbors.add(var);
			this.neighbors_static.add(var);
		}
	}

	public void removeNeighbor(MyVariable var) {
		if (this.neighbors.contains((var))) {
			this.neighbors.remove(var);
		}
	}

	public LinkedList<MyVariable> getNeighborsStatic() {
		return this.neighbors_static;
	}

	public void resetNeighbors() {
		this.neighbors = this.neighbors_static;
	}

	public ArrayList<MyConstraint> getConstraints() {
		return this.constraints;

	}

	public LinkedList<MyVariable> getNeighbors() {
		return this.neighbors;
	}

	public void setConstraints(ArrayList<MyConstraint> constraints) {
		this.constraints = constraints;
		this.constraints_static = constraints;
	}

	public ArrayList<Integer> getCurrentDomain() {
		return this.currentDomain;
	}

	public void setDomain(ArrayList<Integer> newDomain) {
		this.initialDomain = new int[newDomain.size()];
		this.currentDomain.clear();
		for (int i = 0; i < newDomain.size(); i++) {
			this.initialDomain[i] = newDomain.get(i);
			this.currentDomain.add(newDomain.get(i));
		}
	}

	public void setCurrentDomain(ArrayList<Integer> currdomain) {
		this.currentDomain = currdomain;
	}

	public void resetDomain() {
		this.currentDomain.clear();
		for (int i : this.initialDomain) {
			this.currentDomain.add(i);
		}
	}

	public int getDegree() {

		ArrayList<MyConstraint> list = (ArrayList<MyConstraint>) this.constraints.clone();
		ArrayList<MyConstraint> dupes = new ArrayList<MyConstraint>();
		Iterator<MyConstraint> iterator = list.iterator();

		// removing all the unary constraints from being counted as a degreee
		while (iterator.hasNext()) {
			if (iterator.next().getScope().size() == 1) {
				iterator.remove();
			}
		}

		// going through all the constraints of the variable
		for (int i = 0; i < list.size(); i++) {
			MyConstraint test = list.get(i);

			// finding the constraints that share the same scope as the instantiated
			// constraint
			for (int j = i + 1; j < list.size(); j++) {

				MyConstraint c = list.get(j);

				// if not a unary constraint (to avoid errors)
				if (c.getScope().size() > 1 && test.getScope().size() > 1) {

					// if the scope matches, add to the dupliated scope array list
					if (test.getScope().get(0).getName().equals(c.getScope().get(0).getName())
							&& test.getScope().get(1).getName().equals(c.getScope().get(1).getName())) {
						dupes.add(c);
					}
				}
			}
		}

		// go through the constraint list and remove all the cosntraints that have the
		// same scope
		list.removeAll(dupes);

		return list.size();
	}

	public String toString() {
		return this.name;
		// String s = "Name: " + name + ", domain: {";

		// for (int i = 0; i < currentDomain.size() - 1; i++) {
		// s += currentDomain.get(i);
		// s += ",";
		// }
		// s += currentDomain.get(currentDomain.size() - 1) + "}, ";
		// s += "constraints: {";

		// for (int i = 0; i < constraints.size() - 1; i++) {
		// s += constraints.get(i).getName() + ",";
		// }
		// s += constraints.get(constraints.size() - 1).getName() + "}";
		// s += ", neighbors: {";

		// for (int i = 0; i < neighbors.size() - 1; i++) {
		// s += neighbors.get(i).getName() + ",";
		// }
		// s += neighbors.get(neighbors.size() - 1).getName() + "}";

		// return s;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		MyVariable v = (MyVariable) o;
		if (v.getName().equals(this.getName())) {
			return true;
		} else {
			return false;
		}
	}

}