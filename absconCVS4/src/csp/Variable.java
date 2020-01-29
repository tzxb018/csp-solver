package csp;

import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PDomain;
import abscon.instance.components.PVariable;
import java.lang.String;

public class Variable {

	/// Keep a reference to the original variable, just in case it is needed later
	protected PVariable varRef;
	/// Best to create a *deep copy* of the data-structures that are needed for the
	/// homework
	protected String name;
	protected PDomain domain;
	

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


	public String toString() {
		return "Name: " + name + ", initial-domain: " + domain.getValuesString() + ", constraints: x, neighbors: x";
	}

}
