package csp.MainStructures;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PDomain;
import abscon.instance.components.PVariable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

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

	public void setDomain(ArrayList<Integer> newDomain) {
		this.initialDomain = new int[newDomain.size()];
		for (int i = 0; i < newDomain.size(); i++) {
			this.initialDomain[i] = newDomain.get(i);
		}
	}

	public void setCurrentDomain(ArrayList<Integer> currdomain) {
		this.currentDomain.clear();
		for (Integer i : currdomain) {
			this.currentDomain.add(i);
		}
	}

	public void resetDomain() {
		this.currentDomain.clear();
		for (int i : this.initialDomain) {
			this.currentDomain.add(i);
		}
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