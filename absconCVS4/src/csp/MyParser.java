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

public class MyParser {
	private ArrayList<MyVariable> variables;
	private ArrayList<MyConstraint> constraints;

	public MyParser(String filename) {
		InstanceParser parser = new InstanceParser();
		parser.loadInstance(filename);
		parser.parse(false);
		variables = new ArrayList<MyVariable>();

		Document document = XMLManager.load(filename);
		Element presentationElement = (Element) document.getDocumentElement()
				.getElementsByTagName(InstanceTokens.PRESENTATION).item(0);
		String problemName = presentationElement.getAttribute(InstanceTokens.NAME);

		for (int i = 0; i < parser.getVariables().length; i++) {
			PVariable tempVar = parser.getVariables()[i];
			MyVariable newVar = new MyVariable(tempVar);
			variables.add(newVar);
		}

		boolean extension = parser.getConstraintsCategory().contains("E");

		constraints = new ArrayList<MyConstraint>();

		for (String key : parser.getMapOfConstraints().keySet()) {
			PConstraint con = parser.getMapOfConstraints().get(key);
			if (extension) {
				MyExtensionConstraint extCon = new MyExtensionConstraint(con, (PExtensionConstraint) con);
				constraints.add(extCon);
			} else {
				MyIntensionConstraint intcon = new MyIntensionConstraint(con, (PIntensionConstraint) con);
				constraints.add(intcon);
			}
		}

		MyProblem myProblem = new MyProblem(problemName, variables, constraints);
		System.out.println(myProblem.toString());

	}

	public static void main(String[] args) {
		// Hardcoded now... but should read in the file through the arguments, -f
		// <XML-NAME>
		MyParser parser = new MyParser(args[1]);
	}
}