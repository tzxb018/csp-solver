package csp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import abscon.instance.InstanceTokens;
import abscon.instance.XMLManager;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.components.PVariable;
import abscon.instance.tools.InstanceParser;
import csp.BacktrackSearch.BacktrackSearch;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyExtensionConstraint;
import csp.MainStructures.MyIntensionConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyParser {

	private ArrayList<MyVariable> variables;
	private ArrayList<MyConstraint> constraints;

	public MyParser(String[] args) throws FileNotFoundException, IOException {

		String filename = "";
		String ACAlgorithmString = "";
		String orderingHeuristic = "";
		String searchAlgorithm = "";

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-f")) {
				filename = args[i + 1];
			} else if (args[i].equals("-a")) {
				ACAlgorithmString = args[i + 1];
			} else if (args[i].equals("-u")) {
				orderingHeuristic = args[i + 1];
			} else if (args[i].equals("-s")) {
				searchAlgorithm = args[i + 1];
			}
		}

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

		// Going through the map of constraints made by the parser to obtain the
		// correct constraint
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
		System.out.println("Instance name: " + problemName);
		MyACAlgorithms ac = new MyACAlgorithms();

		if (ACAlgorithmString.equals("ac1"))
			ac.AC1(myProblem);
		else if (ACAlgorithmString.equals("ac3"))
			ac.AC3(myProblem);

		if (!orderingHeuristic.equals("") && !searchAlgorithm.equals("")) {
			BacktrackSearch bt = new BacktrackSearch(myProblem, orderingHeuristic, true);
			bt.runSearch(searchAlgorithm);

		}

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// // for (int i = 10; i <= 90; i += 10) {
		// // String fileName =
		// "/home/tbessho/Documents/Tools2008/absconCVS4/v32_d8_p20/v32_d8_p20_t" + i;

		File dir = new File("/home/tbessho/Documents/Tools2008/absconCVS4/problems/");
		File[] directoryListing = dir.listFiles();
		String[] typesOfOrder = { "LX", "LD", "DEG", "DDR" };

		// for (File child : directoryListing) {
		// for (String typeoforder : typesOfOrder) {
		// // String[] argsString = { "-f", child.getAbsolutePath(), "-s", "BT", "-u",
		// // typeoforder };
		// String[] argsString = { "-f",
		// "/home/tbessho/Documents/Tools2008/absconCVS4/problems/zebra-supports.xml",
		// "-s", "BT", "-u", typeoforder };

		// MyParser parser = new MyParser(argsString);
		// System.out.println(Arrays.toString(argsString));
		// }
		String[] argsString = { "-f", "/home/tbessho/Documents/Tools2008/absconCVS4/problems/zebra-supports.xml", "-s",
				"BT", "-u", "LD" };

		MyParser parser = new MyParser(argsString);

		// }
		// // }

		// String[] argsString = { "-f", child.getAbsolutePath(), "-s", "BT", "-u",
		// typeoforder };
		// MyParser parser = new MyParser(argsString);
		// MyParser parser = new MyParser(args);

	}
}