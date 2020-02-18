package csp.BacktrackSearch;

import java.util.*;

import csp.CheckSupportRevise;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3
 * @since 2/17/2020
 */
public class BCSSP {

    protected MyProblem myProblem;
    protected ArrayList<MyVariable> current_path;
    protected int[] assignments;
    protected ArrayList<ArrayList<Integer>> domains;
    protected ArrayList<ArrayList<Integer>> current_domains;
    protected boolean consistent;
    protected ArrayList<MyVariable> variables;

    public BCSSP(MyProblem myProblem, ArrayList<MyVariable> current_path, int[] assignments,
            ArrayList<ArrayList<Integer>> domains, ArrayList<ArrayList<Integer>> current_domains) {
        this.myProblem = myProblem;
        this.current_domains = current_domains;
        this.current_path = current_path;
        this.assignments = assignments;
        this.domains = domains;
        this.variables = myProblem.getVariables();
    }

    /**
     * This is the method for running the BCSSP algorithm for search
     * 
     * @param n      the number of variables in the CSP problem
     * @param status determines if there is a solution or not (the three possible
     *               states are 'unknown', 'solution', or 'impossible')
     * @return whether there is a solution in the CSP after running BCSSP
     */
    public boolean execute(int n, String status) {

        consistent = true;
        status = "unknown";
        int i = 1;

        while (status.equals("unknown")) {
            if (consistent) {
                System.out.println("BT LABEL: " + i);
                i = BT_label(i);
            } else {
                System.out.println("BT UNLABEL: " + i);
                i = BT_unlabel(i);
            }

            System.out.println("===========");
            for (ArrayList<Integer> doms : current_domains) {
                System.out.println(doms);
            }
            System.out.println("===========");

            // determining if there is a solution or not
            if (i > n) {
                status = "solution";
                return true;
            }
            // reach the top of the tree
            else if (i == 0) {
                status = "false";
                return false;
            }
            System.out.println();
        }

        return true; // idk
    }

    public int BT_label(int i) {
        consistent = false;
        CheckSupportRevise csr = new CheckSupportRevise(myProblem.getConstraints(), this.current_path,
                myProblem.getExtension());
        // going through each possible assignment in the current domain of the variable
        // at v[i]
        // Iterator<Integer> iterator = current_domains.get(i).iterator();
        Iterator<Integer> iterator = current_path.get(i).getCurrentDomain().iterator();
        while (iterator.hasNext() && !consistent) {

            // assigning the next possible value for v[i]
            int next = iterator.next();
            assignments[i] = next;
            System.out.println("Assignment: " + current_path.get(i).getName() + " <-- " + assignments[i]);
            consistent = true;

            // back checking against all past variables with their respective assignments
            for (int h = 1; h <= i - 1; h++) {
                consistent = csr.check(current_path.get(h), assignments[h], current_path.get(i), assignments[i]);
                System.out.println(current_path.get(h).getName() + " " + assignments[h] + " <> "
                        + current_path.get(i).getName() + " " + assignments[i] + " ==> " + consistent);

                if (!consistent) {
                    iterator.remove();
                }
            }

        }

        if (consistent) {
            return i + 1; // an assignment to v[i] works
        } else {
            return i;
        }

    }

    public int BT_unlabel(int i) {
        int h = i - 1;
        System.out.println("domain: " + i + " " + domains.get(i));
        current_path.get(i).resetDomain();

        // current_domains.set(i, domains.get(i)); // resetting the domain to be the
        // starting domain at level i
        // Iterator<Integer> iterator = current_domains.get(h).iterator();
        Iterator<Integer> iterator = current_path.get(h).currentDomain.iterator();

        // finding the index of assignments[h] in current-domain[h] to remove it
        while (iterator.hasNext()) {
            int nextVal = iterator.next();
            System.out.println(nextVal + " == " + assignments[h]);
            if (nextVal == assignments[h]) {
                iterator.remove();
            }
        }

        System.out.println("updated: " + current_path.get(h).getCurrentDomain());

        if (current_path.get(h).getCurrentDomain().size() == 0) {
            consistent = false;
        } else
            consistent = true;

        return h;

    }
}