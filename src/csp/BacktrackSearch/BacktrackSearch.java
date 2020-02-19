package csp.BacktrackSearch;

import java.util.ArrayList;
import java.util.Arrays;

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
    protected ArrayList<MyVariable> current_path;
    protected int[] assignments;

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
    public BacktrackSearch(MyProblem myProblem, String ordering_heursitic, String searchType) {

        this.myProblem = myProblem;
        ArrayList<MyVariable> variables = myProblem.getVariables();
        this.current_path = new ArrayList<MyVariable>();
        this.assignments = new int[variables.size() + 1];

        // using the ordering heuristic to determine how the variables are put into
        // current-path
        if (ordering_heursitic.equals("LX")) {

            current_path.add(null); // pointer starts at 1

            // adding into the current-path in order lexiographically
            for (MyVariable var : variables) {
                current_path.add(var);
            }

            for (int i = 0; i < assignments.length; i++){
                assignments[i] = -1;
            }

        } else if (ordering_heursitic.equals("LD")) {

        } else if (ordering_heursitic.equals("DEG")) {

        } else {

        }

        if (searchType.equals("BT")) {
            BCSSP bcssp = new BCSSP(myProblem, current_path, assignments);
            bcssp.execute(variables.size(), "unknown");
        }

    }
}