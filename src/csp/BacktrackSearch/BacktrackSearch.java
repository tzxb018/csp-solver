package csp.BacktrackSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    protected int[] assignments;

    protected ArrayList<MyVariable> unassignedVariables;
    protected ArrayList<MyVariable> assignedVariables;

    /**
     * This is the constructor for all the backtrack searches
     * 
     * @param myProblem          takes in the problem data structure to get the
     *                           constraints, variables, and whether it is an
     *                           extension/intension problem. If it is an extension
     *                           problem, then this will also tell the algorithm if
     *                           the extension variables are supports or conflict
     * @param ordering_heursitic defines how the variables will be ordered
     */
    public BacktrackSearch(MyProblem myProblem, String ordering_heursitic, boolean staticOrdering) {

        this.myProblem = myProblem;
        this.variables = myProblem.getVariables();
        this.current_path = new ArrayList<MyVariable>();
        this.assignments = new int[variables.size() + 1];

        System.out.println("variable-order-heuristic: " + ordering_heursitic);

        // if we are using static ordering
        if (staticOrdering) {
            System.out.println("var-static-dynamic: static");

            // adding into the current-path in order lexiographically
            for (MyVariable var : variables) {
                current_path.add(var);
            }

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
                    Collections.sort(current_path, MyVariable.DEG_COMPARATOR);
                    break;
                case ("DDR"):
                    Collections.sort(current_path, MyVariable.DDR_COMPARATOR);
                    break;

            }

            current_path.add(0, null); // pointer starts at 1
            for (MyVariable var : current_path) {
                if (var != null && var.getConstraints().size() > 0)
                    System.out.println(var.getName() + " " + (var.getDomain().length / var.getConstraints().size()));
            }
        }
    }

    public void runSearch(String searchType) {

        if (searchType.equals("BT")) {
            BCSSP bcssp = new BCSSP(this.myProblem, this.current_path, this.assignments);
            bcssp.execute(this.variables.size(), "unknown");
        }
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