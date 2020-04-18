package csp.Search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import csp.MyACAlgorithms;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyExtensionConstraint;
import csp.MainStructures.MyIntensionConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3, HW 4
 * @since 2/17/2020
 */

public class Setup {

    protected MyProblem myProblem;
    protected ArrayList<MyVariable> variables;
    protected ArrayList<MyVariable> current_path;
    protected ArrayList<MyVariable> copy_current_path;
    protected int[] assignments;

    protected ArrayList<MyVariable> unassignedVariables;
    protected ArrayList<MyVariable> assignedVariables;

    protected String orderingHeuristic;
    protected String orderedCurrentPathString;

    /**
     * This is the constructor for all the backtrack searches
     * 
     * @param myProblem          takes in the problem data structure to get the
     *                           constraints, variables, and whether it is an
     *                           extension/intension problem. If it is an extension
     *                           problem, then this will also tell the algorithm if
     *                           the extension variables are supports or conflict
     * @param ordering_heursitic defines how the variables will be ordered
     * @throws IOException
     */
    public Setup(MyProblem myProblem, String ordering_heursitic, boolean staticOrdering, String searchType)
            throws IOException {

        this.myProblem = myProblem;
        this.variables = myProblem.getVariables();
        this.current_path = new ArrayList<MyVariable>();
        this.assignments = new int[variables.size() + 1];
        this.orderingHeuristic = ordering_heursitic;

        System.out.println("Search: " + searchType);
        System.out.println("variable-order-heuristic: " + ordering_heursitic);

        // if we are using static ordering
        if (staticOrdering) {
            System.out.println("var-static-dynamic: static");
            System.out.println("value-ordering-heuristic: " + ordering_heursitic);
            System.out.println("val-static-dynamic: static");

            // running NC before running search
            ArrayList<MyConstraint> unaryConstraints = new ArrayList<MyConstraint>();

            // finding the unary constraints
            for (MyConstraint constraint : myProblem.getConstraints()) {
                if (constraint.getScope().size() == 1) {
                    unaryConstraints.add(constraint);
                }
            }

            // executing node consistency
            if (unaryConstraints.size() > 0) {
                MyACAlgorithms ac = new MyACAlgorithms();

                // depending on which type of unary constraint it is, run NC
                if (myProblem.getExtension()) {
                    for (MyConstraint c : unaryConstraints) {
                        ac.nodeConsistencyExtension(variables, (MyExtensionConstraint) c);
                    }
                } else {
                    for (MyConstraint c : unaryConstraints) {
                        ac.nodeConsistencyIntension(variables, (MyIntensionConstraint) c);
                    }
                }
            }

            // adding into the current-path in order lexiographically
            for (int i = 0; i < variables.size(); i++) {
                current_path.add(variables.get(i));
                // System.out.println(variables.get(i).getName() + ": " +
                // variables.get(i).getNeighbors());
            }

            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = -1;
            }

            // using the ordering heuristic to determine how the variables are put into
            // current-path
            Ordering ordering = new Ordering();

            switch (ordering_heursitic) {
                case ("LX"):
                    Collections.sort(current_path, MyVariable.LX_COMPARATOR);
                    break;
                case ("SOL"):
                    Collections.sort(current_path, MyVariable.SOL_COMPARATOR);
                    break;
                case ("LD"):
                    Collections.sort(current_path, MyVariable.LD_COMPARATOR);
                    break;
                case ("DEG"):
                    this.current_path = ordering.degreeOrdering(this.current_path);
                    break;
                case ("DD"):
                    this.current_path = ordering.ddrOrdering(this.current_path);
                    break;
                case ("W"):
                    this.current_path = ordering.widthOrdering(this.current_path);
                    Collections.reverse(this.current_path);
                    break;
                case ("MFO"):
                    // this.current_path = ordering.minfill(this.current_path);
                    Collections.reverse(this.current_path);

            }

            // for writing the order of the varialbes to a csv file
            // csvOrder();

            current_path.add(0, null); // pointer starts at 1
            orderedCurrentPathString = "[";
            for (int i = 1; i < current_path.size() - 1; i++) {
                orderedCurrentPathString += (current_path.get(i).getName() + ",");
            }
            orderedCurrentPathString += (current_path.get(current_path.size() - 1)).getName() + "]";
            System.out.println(orderedCurrentPathString);
        }
    }

    // function to write the order of the variables after ordering heuristic to a
    // csv file
    public void csvOrder() throws IOException {

        String fileContent = myProblem.getProblemName() + "," + this.orderingHeuristic + ",";

        for (MyVariable v : current_path) {
            fileContent += v.getName() + ",";
        }
        fileContent += "\n";

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:/Users/14022/Documents/VS Code Projects/csp-solver/out.csv", true));
        writer.write(fileContent);
        writer.close();

    }

    public void runSearch(String searchType) throws IOException {

        // Run BCSSP
        SearchAlgorithms searchAlgorithms = new SearchAlgorithms(this.myProblem, this.current_path, this.assignments,
                searchType);
        // searchAlgorithms.BCSSP(this.variables.size(), "unknown");

        // Writing the results to a csv fileSD
        // String fileContent = myProblem.getProblemName() + "," + searchType + "," +
        // this.orderingHeuristic + ","
        // + searchAlgorithms.getCSVRow() + "\n";

        // BufferedWriter writer = new BufferedWriter(
        // new FileWriter("C:\\Users\\14022\\Documents\\VS Code
        // Projects\\csp-solver\\out.csv", true));
        // writer.write(fileContent);
        // writer.close();

    }

}