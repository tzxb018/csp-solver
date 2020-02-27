package csp.BacktrackSearch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import csp.MyACAlgorithms;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyExtensionConstraint;
import csp.MainStructures.MyIntensionConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3
 * @since 2/17/2020
 */

public class BacktrackSearch {

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
    public BacktrackSearch(MyProblem myProblem, String ordering_heursitic, boolean staticOrdering) throws IOException {

        this.myProblem = myProblem;
        this.variables = myProblem.getVariables();
        this.current_path = new ArrayList<MyVariable>();
        this.assignments = new int[variables.size() + 1];
        this.orderingHeuristic = ordering_heursitic;

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
            }
            // for (MyVariable var : variables) {
            // current_path.add(var);
            // }

            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = -1;
            }

            // using the ordering heuristic to determine how the variables are put into
            // current-path
            switch (ordering_heursitic) {
                case ("LX"):
                    Collections.sort(current_path, MyVariable.LX_COMPARATOR);
                    break;
                case ("LD"):
                    Collections.sort(current_path, MyVariable.LD_COMPARATOR);
                    break;
                case ("DEG"):
                    this.current_path = degreeOrdering(this.current_path);
                    break;
                case ("DD"):
                    this.current_path = ddrOrdering(this.current_path);
                    break;

            }

            // for writing the order of the varialbes to a csv file
            // csvOrder("BT");

            current_path.add(0, null); // pointer starts at 1
            // orderedCurrentPathString = "[";
            // for (int i = 1; i < current_path.size() - 1; i++) {
            // orderedCurrentPathString += (current_path.get(i).getName() + ",");
            // }
            // orderedCurrentPathString += (current_path.get(current_path.size() -
            // 1)).getName();
        }
    }

    // function to write the order of the variables after ordering heuristic to a
    // csv file
    public void csvOrder(String searchType) throws IOException {

        if (searchType.equals("BT")) {

            String fileContent = myProblem.getProblemName() + "," + this.orderingHeuristic + ",";

            for (MyVariable v : current_path) {
                fileContent += v.getName() + ",";
            }
            fileContent += "\n";

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("/home/tbessho/Documents/Tools2008/absconCVS4/out.csv", true));
            writer.write(fileContent);
            writer.close();
        }
    }

    public void runSearch(String searchType) throws IOException {

        if (searchType.equals("BT")) {
            // Run BCSSP
            BCSSP bcssp = new BCSSP(this.myProblem, this.current_path, this.assignments);
            bcssp.execute(this.variables.size(), "unknown");

            // Writing the results to a csv file
            // String fileContent = myProblem.getProblemName() + "," + "BT" + "," +
            // this.orderingHeuristic + ","
            // + bcssp.getCSVRow() + "\n";

            // BufferedWriter writer = new BufferedWriter(
            // new FileWriter("/home/tbessho/Documents/Tools2008/absconCVS4/out.csv",
            // true));
            // writer.write(fileContent);
            // writer.close();
        }
    }

    // function for ordering by degree size
    public ArrayList<MyVariable> degreeOrdering(ArrayList<MyVariable> input) {

        // setting the degree sizes of all the variables
        ArrayList<MyVariable> degreeOrdered = new ArrayList<MyVariable>();
        for (MyVariable v : input) {
            v.getDegree();
        }

        // going through each element in the arraylist
        while (input.size() > 0) {
            int maxDegree = input.get(0).getDegree();
            MyVariable maxVar = input.get(0);
            // finding the largest element
            for (MyVariable var : input) {
                // if the degree is larger
                if (var.getDegree() > maxDegree) {
                    maxVar = var;
                    maxDegree = var.getDegree();
                }
                // if the degree is the same, then break lexiographically
                else if (var.getDegree() == maxDegree && var.getName().compareTo(maxVar.getName()) < 0) {
                    maxVar = var;
                    maxDegree = var.getDegree();
                }
            }

            // add the variable with the max degree to the array list and remove from the
            // inital list to avoid redundancy
            degreeOrdered.add(maxVar);
            input.remove(maxVar);

            // once the max degree has been found, remove all the instances in the neighbors
            // of all the other variables
            for (MyVariable var : input) {
                ArrayList<MyConstraint> constraints = var.getConstraints();

                // remove the max var from all the neighbors and update the neighbors
                Iterator<MyConstraint> iterator = constraints.iterator();
                while (iterator.hasNext()) {
                    MyConstraint next = iterator.next();

                    // making sure it is not a unary constraint
                    if (next.getScope().size() > 1) {
                        // finding the constraints that make the variable incident with the max var
                        if (next.getScope().get(0).getName().equals(maxVar.getName())
                                || next.getScope().get(1).getName().equals(maxVar.getName())) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
                // update the neighbors
                var.setConstraints(constraints);
            }

        }
        return degreeOrdered;
    }

    // function used to do ddr ordering heuristic for the variables
    public ArrayList<MyVariable> ddrOrdering(ArrayList<MyVariable> input) {

        // setting the degree sizes of all the variables
        ArrayList<MyVariable> ddrOrdered = new ArrayList<MyVariable>();
        for (MyVariable v : input) {
            v.getDegree();
        }

        // going through each element in the arraylist
        while (input.size() > 0) {
            MyVariable maxVar = input.get(0);
            // finding the largest element
            for (MyVariable var : input) {
                double dom1 = maxVar.getDomain().length;
                double dom2 = var.getDomain().length;
                double deg1 = maxVar.getDegree();
                double deg2 = var.getDegree();
                // if the ddr is larger than the max var's ddr
                if (dom1 * deg2 > dom2 * deg1) {
                    maxVar = var;
                }
                // if the degree is the same, then break lexiographically
                else if (dom1 * deg2 == dom2 * deg1 && var.getName().compareTo(maxVar.getName()) < 0) {
                    maxVar = var;
                }
            }

            ddrOrdered.add(maxVar);
            input.remove(maxVar);

            // once the max degree has been found, remove all the instances in the neighbors
            // of all the other variables
            for (MyVariable var : input) {
                ArrayList<MyConstraint> constraints = var.getConstraints();
                // remove the max var from all the neighbors and update the neighbors
                Iterator<MyConstraint> iterator = constraints.iterator();
                while (iterator.hasNext()) {
                    MyConstraint next = iterator.next();
                    if (next.getScope().size() > 1) {
                        if (next.getScope().get(0).getName().equals(maxVar.getName())
                                || next.getScope().get(1).getName().equals(maxVar.getName())) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
                // update the neighbors
                var.setConstraints(constraints);
            }

        }
        return ddrOrdered;
    }

    public ArrayList<MyVariable> getCopyCurrentPath() {
        return this.copy_current_path;
    }

    public ArrayList<MyVariable> getUnassignedVariables() {
        this.unassignedVariables = new ArrayList<MyVariable>();

        for (int i = 1; i < this.assignments.length; i++) {

            // if the assignment value is the default value, get the varaible at that index
            // in the current path (after running the varaible value heruisitic)
            if (this.assignments[i] == -1) {
                this.unassignedVariables.add(this.current_path.get(i));
            }
        }

        return this.unassignedVariables;
    }

    public ArrayList<MyVariable> getAssignedVariables() {
        this.assignedVariables = new ArrayList<MyVariable>();

        for (int i = 1; i < this.assignments.length; i++) {

            // if the assignment value is the not the default value, get the varaible at
            // that index
            // in the current path (after running the varaible value heruisitic)
            if (this.assignments[i] > -1) {
                this.assignedVariables.add(this.current_path.get(i));
            }
        }

        return this.assignedVariables;
    }

}