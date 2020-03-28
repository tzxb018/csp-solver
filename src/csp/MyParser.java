package csp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import abscon.instance.InstanceTokens;
import abscon.instance.XMLManager;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.components.PVariable;
import abscon.instance.tools.InstanceParser;
import csp.Search.SearchTypes;
import csp.Search.SetFunctions;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyExtensionConstraint;
import csp.MainStructures.MyIntensionConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2,3,4
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

		if (args.length == 6 && args[0].equals("-f") && args[2].equals("-s") && args[4].equals("-u")) {
			// parsing the inputs from the command line
			filename = args[1];
			searchAlgorithm = args[3];
			orderingHeuristic = args[5];
		} else if (args.length == 4 && args[0].equals("-f") && args[2].equals("-a")) {
			filename = args[1];
			ACAlgorithmString = args[3];
		} else {
			System.out.println(
					"Invalid arguments given. \nThey should be in the following form: \n -f <filename> -s <search algorithm> -u <ordering herusitic> \nor\n -a <AC algorithm type>");
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
			boolean staticOrdering = orderingHeuristic.substring(0, 1).equals("d") ? false : true;
			SearchTypes st = new SearchTypes(myProblem, orderingHeuristic, staticOrdering, searchAlgorithm);
			st.runSearch(searchAlgorithm);

		}

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// // for (int i = 10; i <= 90; i += 10) {
		// // String fileName =
		// "/home/tbessho/Documents/Tools2008/absconCVS4/v32_d8_p20/v32_d8_p20_t" + i;

		// File dir = new
		// File("/home/tbessho/Documents/Tools2008/absconCVS4/problems/");
		// File[] directoryListing = dir.listFiles();

		if (args.length > 0 && args[0].equals("csv")) {
			String[] typesOfOrder = { "LX", "LD", "DEG", "DD" };
			String[] fileLocations = { "/home/tbessho/Documents/Tools2008/absconCVS4/problems/3q-conflicts.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/3q-intension.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/4q-conflicts.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/4q-supports.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/5q-intesion.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/6q-conflicts.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/6q-intension.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/random_20_8_100_20.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/chain4-conflicts.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/map-coloring-australia.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/map-coloring-australia-intension.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/k4-coloring.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/zebra-extension-supports-and-conflicts.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/zebra-intension.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/zebra-supports.xml",
					"/home/tbessho/Documents/Tools2008/absconCVS4/problems/12q.xml" };
			for (String fileLoc : fileLocations) {
				for (String typeoforder : typesOfOrder) {
					String[] argsString = { "-f", fileLoc, "-s", "FC", "-u", typeoforder };
					MyParser parser = new MyParser(argsString);
				}
			}
		} else {

			MyParser parser = new MyParser(args);
		}

	}
}