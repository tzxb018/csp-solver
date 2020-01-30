package csp;

import csp.Variable;
import abscon.instance.tools.InstanceParser;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PVariable;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Arrays;

public class MyParser {

	private List<Variable> variables;

	public MyParser(String filename) {
		InstanceParser parser = new InstanceParser();
		parser.loadInstance(filename);
		parser.parse(false);
		variables = new ArrayList<Variable>();
		
		String nameOfProblem = parser.getName();
		System.out.println("Instance name: " +  nameOfProblem);

		System.out.println("Variables");
		for (int i = 0; i < parser.getVariables().length; i++) {

			// System.out.println(parser.getVariables()[i].getName());
			Variable newVar = new Variable(parser.getVariables()[i]);
			newVar.setConstraints(parser);
			System.out.println(newVar);

			variables.add(newVar);
		}

		System.out.println("Constraints");
		for (String key : parser.getMapOfConstraints().keySet()) {
			PConstraint con = parser.getMapOfConstraints().get(key);
			
			System.out.println(con.toString());
		}
	}

	public static void main(String[] args) {
		// Hardcoded now... but should read in the file through the arguments, -f
		// <XML-NAME>
		MyParser parser = new MyParser(args[1]);
	}
}
