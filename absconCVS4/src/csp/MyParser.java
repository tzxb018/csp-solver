package csp;

import csp.Variable;
import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PConstraint;
import java.util.List;
import java.util.ArrayList;

public class MyParser {

	private List<Variable> variables;

	public MyParser(String filename) {
		InstanceParser parser = new InstanceParser();
		parser.loadInstance(filename);
		parser.parse(false);
		variables = new ArrayList<Variable>();
		System.out.println("Instance name: <Not currently parsed! Need to modify the InstanceParser()>");
		System.out.println("Variables");
		for (int i = 0; i < parser.getVariables().length; i++) {

			// System.out.println(parser.getVariables()[i].getName());
			Variable newVar = new Variable(parser.getVariables()[i]);
			System.out.println(newVar);
			variables.add(newVar);
		}
		System.out.println("Constraints");
		for (String key : parser.getMapOfConstraints().keySet()) {
			PConstraint con = parser.getMapOfConstraints().get(key);
			System.out.println(con.getName());
		}
	}

	public static void main(String[] args) {
		// Hardcoded now... but should read in the file through the arguments, -f
		// <XML-NAME>
		MyParser parser = new MyParser("./4queens-supports.xml");
	}
}
