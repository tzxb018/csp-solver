package csp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import csp.Setup;
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
        } else if (args.length == 4 && args[0].equals("-f") && args[2].equals("-t")) {
            filename = args[1];
            orderingHeuristic = args[3];
        } else {
            System.out.println(
                    "Invalid arguments given. \nTo run the CSP search algorithm with a variable ordering heuristic, run the following: \n -f <filename> -s <search algorithm> -u <ordering herusitic> \nor\n To run arc-consistency on a CSP, run the following: -f <filename> -a <AC algorithm type> \nor\n To run tree-decompisition on a CSP, run the following: -f <filename> -t <Tree Decompisition type>");
            return;
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
        if (problemName.equals("")) {
            problemName = args[1];
            problemName = problemName.replace(
                    "C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\problems\\Benchmark Problems for Tree Decompisition\\random\\",
                    "");
        }
        if (parser.getMaxConstraintArity() == 2) {
            System.out.println("Instance name: " + problemName);

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

                try {
                    // Depending on the type of constraint, make a different version of the
                    // constraint
                    if (extension) {
                        MyExtensionConstraint extCon = new MyExtensionConstraint(con, (PExtensionConstraint) con);
                        constraints.add(extCon);
                    } else {
                        MyIntensionConstraint intcon = new MyIntensionConstraint(con, (PIntensionConstraint) con);
                        constraints.add(intcon);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    return;
                }
            }

            // An instance of the problem to store the variables and constraints in one
            // place
            MyProblem myProblem = new MyProblem(problemName, variables, constraints);
            MyACAlgorithms ac = new MyACAlgorithms();

            if (ACAlgorithmString.equals("ac1"))
                ac.AC1(myProblem);
            else if (ACAlgorithmString.equals("ac3"))
                ac.AC3(myProblem);
            if (orderingHeuristic.contains("TD")){
                Setup st = new Setup(myProblem, orderingHeuristic, true, searchAlgorithm);
            }
            
            if (!orderingHeuristic.equals("") && !searchAlgorithm.equals("")) {
                Setup st = new Setup(myProblem, orderingHeuristic, true, searchAlgorithm);
                if (!orderingHeuristic.contains("TD"))
                    st.runSearch(searchAlgorithm);

            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // String[] debug = { "-f",
        // "C:\\Users\\14022\\Documents\\VS Code
        // Projects\\csp-solver\\problems\\zebra-extension-supports-and-conflicts.xml",
        // "-s", "CBJ", "-u", "MF" };
        // MyParser p = new MyParser(debug);

        if (args.length > 0 && args[0].equals("csv")) {
            String[] typesOfOrder = { "LX", "LD", "DEG", "DD", "W" };
            String[] typesOfAlgo = { "BT", "CBJ", "FC", "FCCBJ" };

            String[] fileLocations = { "3q-conflicts.xml", "3q-intension.xml", "4q-conflicts.xml", "4q-supports.xml",
                    "5q-intesion.xml", "6q-conflicts.xml", "6q-intension.xml", "random_20_8_100_20.xml",
                    "chain4-conflicts.xml", "map-coloring-australia.xml", "map-coloring-australia-intension.xml",
                    "k4-coloring.xml", "zebra-extension-supports-and-conflicts.xml", "zebra-intension.xml",
                    "zebra-supports.xml", "12q.xml" };
            for (String algo : typesOfAlgo) {
                for (String fileLoc : fileLocations) {
                    String loc = "C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\problems\\" + fileLoc;

                    String[] argsString = { "-f", loc, "-s", algo, "-u", "W" };
                    MyParser parser = new MyParser(argsString);
                }
            }

        } else if (args.length > 0 && args[0].equals("17d")) {

            for (int i = 10; i <= 90; i += 10) {
                String dir = "C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\v32_d8_p20\\v32_d8_p20_t" + i;
                for (int j = 0; j < 50; j++) {
                    String appender = "\\v32_d8_p20_t" + i + "_" + j + ".xcsp";
                    String fileName = dir + appender;
                    // System.out.println(fileName);
                    // System.out.println();
                    String[] aStrings = { "-f", fileName, "-s", "FCCBJ", "-u", "W" };
                    MyParser parser = new MyParser(aStrings);

                }

            }

        } else if (args.length > 0 && args[0].equals("order")) {
            String[] typesOfOrder = { "LX", "LD", "DEG", "DD", "W" };
            // String[] typesOfAlgo = { "BT", "CBJ", "FC", "FCCBJ" };

            String[] fileLocations = { "3q-conflicts.xml", "3q-intension.xml", "4q-conflicts.xml", "4q-supports.xml",
                    "5q-intesion.xml", "6q-conflicts.xml", "6q-intension.xml", "random_20_8_100_20.xml",
                    "chain4-conflicts.xml", "map-coloring-australia.xml", "map-coloring-australia-intension.xml",
                    "k4-coloring.xml", "zebra-extension-supports-and-conflicts.xml", "zebra-intension.xml",
                    "zebra-supports.xml", "12q.xml" };
            for (String fileLoc : fileLocations) {
                for (String algo : typesOfOrder) {

                    String loc = "C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\problems\\" + fileLoc;

                    String[] argsString = { "-f", loc, "-s", "FCCBJ", "-u", algo };
                    MyParser parser = new MyParser(argsString);

                }
            }
        } else if (args.length > 0 && args[0].equals("tree")) {
            new FileWriter("C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\tree_stats.csv", false).close();
            File[] files = new File(
                    "C:\\Users\\14022\\Documents\\VS Code Projects\\csp-solver\\Benchmark Problems for Tree Decompisition\\random")
                            .listFiles();

            for (File f : files) {
                String[] argsString = { "-f", f.getAbsolutePath(), "-t", "TD" };
                MyParser parser = new MyParser(argsString);

            }
        } else {
            MyParser parser = new MyParser(args);

        }

    }
}