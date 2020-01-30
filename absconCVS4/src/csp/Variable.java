package csp;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PDomain;
import abscon.instance.components.PVariable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Variable {

	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the
	/// homework
	protected String name;
	protected PDomain domain;
	protected ArrayList<PConstraint> constraints;
	protected ArrayList<PVariable> neighbors;
	

	public Variable(PVariable var) {
		varRef = var;
		name = var.getName();
		domain = var.getDomain();		
	}

	public String getName() {
		return name;
	}

	public PDomain getDomain(){
		return domain;
	}

	public void setConstraints(InstanceParser parser) {
		this.constraints = parser.getConstraintsOfVar(varRef);
	}

	public ArrayList<PConstraint> getConstraints() {
		return constraints;
	}

	public void setNeighbors(InstanceParser parser) {
		this.neighbors = parser.getNeighborsOfVar(varRef);
	}

	public ArrayList<PVariable> getNeighbors() {
		return neighbors;
	}

	public String toString() {
		String s = "Name: " + name + ", initial-domain: " + domain.getValuesString() + ", constraints: {";
		// formatting the constraints

		Collections.sort(constraints, PConstraint.ConstraintComparer);
		for (int i = 0; i < constraints.size(); i++){
			s += (constraints.get(i).getName() + ",");

		}
		s = s.substring(0, s.length() - 1);
		s += "}, neighbors: {";

		// formatting the neighbors
		for (int i = 0; i < neighbors.size(); i++) {
			s += (neighbors.get(i).getName() + ",");
		}
		s = s.substring(0, s.length() - 1);
		s += "}";
		return s;
	}

}
