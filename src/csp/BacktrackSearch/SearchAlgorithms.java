package csp.BacktrackSearch;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

import csp.CheckSupportRevise;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3
 * @since 2/17/2020
 */
public class SearchAlgorithms {

    protected MyProblem myProblem;
    protected ArrayList<MyVariable> current_path;
    protected int[] assignments;

    protected boolean consistent;
    protected ArrayList<MyVariable> variables;

    protected int cc;
    protected int nv;
    protected int bt;

    protected int first_cc;
    protected int first_nv;
    protected int first_bt;

    protected long captureTime;
    protected long runningTime;
    protected long cpuTime;
    protected long first_cpu;

    protected String firstSolution;
    protected int numberOfSolutions;

    protected String algorithm;

    public SearchAlgorithms(MyProblem myProblem, ArrayList<MyVariable> current_path, int[] assignments,
            String algorithm) {
        this.myProblem = myProblem;
        this.current_path = current_path;
        this.assignments = assignments;
        this.variables = myProblem.getVariables();
        this.algorithm = algorithm;

        this.cc = 0;
        this.nv = 0;
        this.bt = 0;
    }

    // function for returning a csv row of the information of each problem
    public String getCSVRow() {
        return this.first_cc + "," + this.first_nv + "," + this.first_bt + "," + this.first_cpu + "," + this.cc + ","
                + this.nv + "," + this.bt + "," + this.cpuTime + "," + this.numberOfSolutions;

    }

    /**
     * This is the method for running the BCSSP algorithm for search
     * 
     * @param n      the number of variables in the CSP problem
     * @param status determines if there is a solution or not (the three possible
     *               states are 'unknown', 'solution', or 'impossible')
     * @return whether there is a solution in the CSP after running BCSSP
     */
    public boolean BCSSP(int n, String status) {

        this.cc = 0;
        this.nv = 0;
        this.bt = 0;

        captureTime = getCpuTime();

        consistent = true;
        status = "unknown";
        int i = 1;

        while (status.equals("unknown")) {

            if (consistent) {
                // System.out.println("BT LABEL: " + i);
                i = BT_label(i);
            } else {
                // System.out.println("BT UNLABEL: " + i);
                i = BT_unlabel(i);
            }

            // System.out.println("===========");
            // for (int j = 1; j < current_path.size(); j++){
            // System.out.println(current_path.get(j).getName() + " " +
            // current_path.get(j).getCurrentDomain());
            // }
            // System.out.println("===========");

            // determining if there is a solution or not
            if (i > n) {
                // if this is the first solution
                if (this.numberOfSolutions == 0) {
                    System.out.println("cc: " + this.cc);
                    System.out.println("nv: " + this.nv);
                    System.out.println("bt: " + this.bt);
                    this.cpuTime = (long) ((getCpuTime() - captureTime) / 1000000.0);

                    // copying the data for the first solution found into other variables
                    this.first_cc = this.cc;
                    this.first_nv = this.nv;
                    this.first_bt = this.bt;
                    this.first_cpu = this.cpuTime;

                    System.out.println("cpu: " + this.cpuTime);

                    String solution = "";
                    current_path.remove(0);

                    // adding the assignments of the all variables to the solution
                    for (MyVariable var : current_path) {
                        if (var != null) {
                            solution += (var.getCurrentDomain().get(0) + " ");
                        }
                    }
                    this.firstSolution = solution;
                    System.out.println("First solution: " + solution);

                    current_path.add(0, null); // pointer starts at 1
                    this.numberOfSolutions++;
                } else {
                    this.numberOfSolutions++;

                }

                // backtrack one level to find more solutions
                i = i - 1;
                consistent = true;
                current_path.get(i).currentDomain.remove(0);
            }
            // reach the top of the tree
            else if (i == 0) {

                status = "false";

                // if there were no solutions found in totoal
                if (this.numberOfSolutions == 0) {

                    this.first_cc = this.cc;
                    this.first_nv = this.nv;
                    this.first_bt = this.bt;
                    this.first_cpu = this.cpuTime;

                    System.out.println("cc: " + this.cc);
                    System.out.println("nv: " + this.nv);
                    System.out.println("bt: " + this.bt);
                    this.cpuTime = (long) ((getCpuTime() - captureTime) / 1000000.0);
                    System.out.println("cpu: " + this.cpuTime);
                    this.firstSolution = "No Solution";
                    System.out.println("First solution: No Solution");
                    System.out.println("all-sol cc: " + this.cc);
                    System.out.println("all-sol nv: " + this.nv);
                    System.out.println("all-sol bt: " + this.bt);
                    this.cpuTime = (long) ((getCpuTime() - captureTime) / 1000000.0);
                    System.out.println("all-sol cpu: " + this.cpuTime);
                    System.out.println("Number of solutions: " + this.numberOfSolutions);

                } else {

                    // output all the info after finding all the solutions
                    System.out.println("all-sol cc: " + this.cc);
                    System.out.println("all-sol nv: " + this.nv);
                    System.out.println("all-sol bt: " + this.bt);
                    this.cpuTime = (long) ((getCpuTime() - captureTime) / 1000000.0);
                    System.out.println("all-sol cpu: " + this.cpuTime);
                    System.out.println("Number of solutions: " + this.numberOfSolutions);
                }

                return false;
            }
        }

        return true;
    }

    public int BT_label(int i) {
        consistent = false;
        CheckSupportRevise csr = new CheckSupportRevise(myProblem.getConstraints(), this.current_path,
                myProblem.getExtension());

        // going through each possible assignment in the current domain of the variable
        // at v[i]
        Iterator<Integer> iterator = current_path.get(i).getCurrentDomain().iterator();
        while (iterator.hasNext() && !consistent) {

            // assigning the next possible value for v[i]
            int next = iterator.next();
            assignments[i] = next;
            // System.out.println("Assignment: " + current_path.get(i).getName() + " <-- " +
            // assignments[i]);
            consistent = true;
            this.nv++;

            // back checking against all past variables with their respective assignments
            for (int h = 1; h <= i - 1; h++) {
                // need to make sure that there is a constraint in between the two variables
                for (MyConstraint c : myProblem.getConstraints()) {
                    if (c.getScope().size() > 1) {
                        if ((c.getScope().get(0).getName().equals(current_path.get(h).getName())
                                && c.getScope().get(1).getName().equals(current_path.get(i).getName()))
                                || (c.getScope().get(1).getName().equals(current_path.get(h).getName())
                                        && c.getScope().get(0).getName().equals(current_path.get(i).getName()))) {
                            consistent = csr.check(current_path.get(h), assignments[h], current_path.get(i),
                                    assignments[i]);

                            this.cc++;
                        }
                    }
                }

                // System.out.println(current_path.get(h).getName() + " " + assignments[h] + "
                // <> "
                // + current_path.get(i).getName() + " " + assignments[i] + " ==> " +
                // consistent);
                // System.out.println(current_path.get(i).getCurrentDomain());
                if (!consistent) {
                    iterator.remove();
                    break;
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
        this.bt++;
        int h = i - 1;

        current_path.get(i).resetDomain();

        // starting domain at level i
        if (h > 0) {
            Iterator<Integer> iterator = current_path.get(h).currentDomain.iterator();

            // finding the index of assignments[h] in current-domain[h] to remove it
            while (iterator.hasNext()) {
                int nextVal = iterator.next();
                // System.out.println(nextVal + " == " + assignments[h]);
                if (nextVal == assignments[h]) {
                    iterator.remove();
                }
            }
            // System.out.println("updated: " + current_path.get(h).getCurrentDomain());

            if (current_path.get(h).getCurrentDomain().size() == 0) {
                consistent = false;
            } else
                consistent = true;
        }

        return h;

    }

    /** Get cpu time in nanoseconds. */
    public long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
    }

    /** Get user time in nanoseconds. */
    public long getUserTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadUserTime(java.lang.Thread.currentThread().getId());
    }

    /** Get system time in nanoseconds. */
    public long getSystemTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadCpuTime(java.lang.Thread.currentThread().getId())
                + bean.getThreadUserTime(java.lang.Thread.currentThread().getId());
    }
}