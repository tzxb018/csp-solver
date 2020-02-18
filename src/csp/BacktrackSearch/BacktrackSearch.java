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
    protected ArrayList<ArrayList<Integer>> domains;
    protected ArrayList<ArrayList<Integer>> current_domains;

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
        this.domains = new ArrayList<ArrayList<Integer>>();
        this.current_domains = new ArrayList<ArrayList<Integer>>();

        // using the ordering heuristic to determine how the variables are put into
        // current-path
        if (ordering_heursitic.equals("LX")) {

            current_path.add(null); // pointer starts at 1
            domains.add(null);
            current_domains.add(null);

            // adding into the current-path in order lexiographically
            for (MyVariable var : variables) {
                current_path.add(var);

                // adding the initial domains of all the variables
                int[] dom_of_var = var.getDomain();

                // converting from array to arraylist
                ArrayList<Integer> startingDomain = new ArrayList<Integer>();
                ArrayList<Integer> anotherDomain = var.getCurrentDomain();

                for (int i = 0; i < dom_of_var.length; i++) {
                    startingDomain.add(dom_of_var[i]);
                }
                
                domains.add(startingDomain);
                current_domains.add(anotherDomain);
            }

        } else if (ordering_heursitic.equals("LD")) {

        } else if (ordering_heursitic.equals("DEG")) {

        } else {

        }

        if (searchType.equals("BT")) {
            BCSSP bcssp = new BCSSP(myProblem, current_path, assignments, domains, current_domains);
            System.out.println("FINAL: " + bcssp.execute(variables.size(), "unknown"));
        }

    }
}