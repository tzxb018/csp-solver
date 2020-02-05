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
import java.util.Collections;
import java.util.Comparator;

/**
 * Author: Tomo Bessho Course: CSCE 421 Date: 2/1/2020
 */

public class MyParser {

	private ArrayList<Variable> variables;

	public MyParser(String filename) {
		InstanceParser parser = new InstanceParser();
		parser.loadInstance(filename);
		parser.parse(false);
		variables = new ArrayList<Variable>();

		String nameOfProblem = parser.getName();
		System.out.println("Instance name: " + nameOfProblem);

		System.out.println("Variables:");
		for (int i = 0; i < parser.getVariables().length; i++) {

			Variable newVar = new Variable(parser.getVariables()[i]);
			newVar.setConstraints(parser);
			newVar.setNeighbors(parser);
			System.out.println(newVar);

			variables.add(newVar);
		}

		ArrayList<PConstraint> listOfConstraints = new ArrayList<PConstraint>();
		for (String key : parser.getMapOfConstraints().keySet()) {
			listOfConstraints.add(parser.getMapOfConstraints().get((key)));
		}

		Collections.sort(listOfConstraints, PConstraint.ConstraintComparer);

		System.out.println("Constraints:");
		for (PConstraint con : listOfConstraints) {

			System.out.println(con.toString());
		}

		Algorithms_AC ac = new Algorithms_AC("AC1", listOfConstraints, variables, true);
	}

	public static void main(String[] args) {
		// Hardcoded now... but should read in the file through the arguments, -f
		// <XML-NAME>
		MyParser parser = new MyParser(args[1]);
	}
}
