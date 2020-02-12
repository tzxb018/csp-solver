package csp;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PDomain;
import abscon.instance.components.PVariable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Name: Tomo Bessho Date: 2/6/2020 Class: CSCE 421
 */
public class MyVariable {
	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the
	/// homework
	protected String name;
	protected int[] initialDomain;
	protected ArrayList<Integer> currentDomain;
	protected ArrayList<MyConstraint> constraints;
	protected ArrayList<MyVariable> neighbors;

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
		neighbors = new ArrayList<MyVariable>();

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

	public ArrayList<Integer> getCurrentDomain() {
		return this.currentDomain;
	}

	public void setCurrentDomain(ArrayList<Integer> currdomain) {
		this.currentDomain.clear();
		// System.out.println("Passed thru: " + currdomain.toString());
		for (Integer i : currdomain) {
			this.currentDomain.add(i);
		}
		// System.out.println("In var " + this.currentDomain.toString());
	}

	public String toString() {
		String s = "Name: " + name + ", domain: {";

		for (int i = 0; i < currentDomain.size() - 1; i++) {
			s += currentDomain.get(i);
			s += ",";
		}
		s += currentDomain.get(currentDomain.size() - 1) + "}, ";
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