package csp;

import csp.MyVariable;
import abscon.instance.tools.InstanceParser;
import abscon.instance.InstanceTokens;
import abscon.instance.XMLManager;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.components.PVariable;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyParser {

	private ArrayList<MyVariable> variables;
	private ArrayList<MyConstraint> constraints;

	public MyParser(String filename, String algoirthm) {

		InstanceParser parser = new InstanceParser();
		parser.loadInstance(filename);
		parser.parse(false);
		variables = new ArrayList<MyVariable>();

		// Finding the name of the problem by parsing the XML file
		Document document = XMLManager.load(filename);
		Element presentationElement = (Element) document.getDocumentElement()
				.getElementsByTagName(InstanceTokens.PRESENTATION).item(0);
		String problemName = presentationElement.getAttribute(InstanceTokens.NAME);

		// Use the parser to obtain the list of variables and assign them to an
		// arrayList
		for (int i = 0; i < parser.getVariables().length; i++) {
			PVariable tempVar = parser.getVariables()[i];
			MyVariable newVar = new MyVariable(tempVar);
			variables.add(newVar);
		}

		// Determining if the problem is extension or intension
		boolean extension = parser.getConstraintsCategory().contains("E");

		// An arraylist to hold all the constraints parsed by the parser
		constraints = new ArrayList<MyConstraint>();

		// Going through the map of constraints made by the parser to obtain the correct
		// constraint
		for (String key : parser.getMapOfConstraints().keySet()) {
			PConstraint con = parser.getMapOfConstraints().get(key);

			// Depending on the type of constraint, make a different version of the
			// constraint
			if (extension) {
				MyExtensionConstraint extCon = new MyExtensionConstraint(con, (PExtensionConstraint) con);
				constraints.add(extCon);
			} else {
				MyIntensionConstraint intcon = new MyIntensionConstraint(con, (PIntensionConstraint) con);
				constraints.add(intcon);
			}
		}

		// An instance of the problem to store the variables and constraints in one
		// place
		MyProblem myProblem = new MyProblem(problemName, variables, constraints);
		SearchFunctions sf = new SearchFunctions(constraints, variables, true);
		// sf.revised(variables.get(7), variables.get(9));
		System.out.println("Instance name: " + problemName);
		// System.out.println(myProblem);
		MyACAlgorithms ac = new MyACAlgorithms();

		if (algoirthm.equals("ac1"))
			ac.AC1(myProblem);
		else if (algoirthm.equals("ac3"))
			ac.AC3(myProblem);

	}

	public static void main(String[] args) {

		MyParser parser = new MyParser(args[1], args[3]);
	}
}