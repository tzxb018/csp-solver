package csp;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PVariable;
import java.lang.String;

public class Variable {

	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the homework
	protected String name;
	public Variable(PVariable var) {
	varRef = var;
	name = var.getName();
	}
	public String getName() {
	return name;
	}
	public String toString() {
	return "Name: " + name + ", initial-domain: x, constraints: x, neighbors: x";
	}
	
}
