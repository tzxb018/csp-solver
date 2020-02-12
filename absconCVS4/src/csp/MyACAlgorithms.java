package csp;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class holds all the code for running the ac-1 and ac-3 algorithms. In
 * addition, it keeps track of the cc, fval, *
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyACAlgorithms {

    protected int cc;
    protected int fval;

    protected long captureTime;
    protected long runningTime;

    protected double iSize;
    protected double fSize;
    protected double fEffect;

    public double computeISize(ArrayList<MyVariable> variables) {
        double a = 1;
        for (MyVariable v : variables) {
            a *= v.getDomain().length;
        }
        this.iSize = Math.log(a);
        return this.iSize;
    }

    public double computeFSize(ArrayList<MyVariable> variables) {
        double a = 1;
        for (MyVariable v : variables) {
            a *= v.currentDomain.size();
            // System.out.println(v.currentDomain.size());

        }
        this.fSize = Math.log(a);
        return this.fSize;
    }

    public double computeFEffect(double iSize, double fSize) {
        this.fEffect = Math.log((Math.pow(Math.E, this.iSize) / Math.pow(Math.E, this.fSize)));
        return this.fEffect;
    }

    public void nodeConsistencyIntension(ArrayList<MyVariable> vars, MyIntensionConstraint unaryConstraint) {

        // iterating through each variable
        for (MyVariable var : vars) {

            // if the unary constrain'ts scope matches the current variable being tested
            if (var.getName().equals(unaryConstraint.getScope().get(0).getName())) {

                Iterator<Integer> iterator = var.getCurrentDomain().iterator();

                while (iterator.hasNext()) {
                    int[] tuple = { iterator.next() };
                    if (unaryConstraint.getRefCon().computeCostOf(tuple) > 0) {
                        iterator.remove();
                    }
                }

            }
        }
    }

    public void nodeConsistencyExtension(ArrayList<MyVariable> vars, MyExtensionConstraint unaryConstraint) {

        // iterating through each variable
        for (MyVariable var : vars) {

            // if the unary constraint's scope matches the current varaible being tested
            if (var.getName().equals(unaryConstraint.getScope().get(0).getName())) {
                // getting the current domain
                ArrayList<Integer> domain = var.getCurrentDomain();
                ArrayList<Integer> updatedDomain = new ArrayList<Integer>();

                // determining if this is a support or conflict constraint
                boolean supports = unaryConstraint.getSemantics().contains("supports");

                // iterate through every value in the domain of the current domain
                Iterator<Integer> iterator = domain.iterator();

                // if it is a conflict variable, remove every instance of the value that is in
                // the unary constraint
                if (!supports) {
                    while (iterator.hasNext()) {
                        if (iterator.next() == unaryConstraint.getRelation()[0][0]) {
                            iterator.remove();
                        }
                    }
                    // if it is a support variable, the unary constraint will only add the values in
                    // the constraint
                } else {
                    for (int i = 0; i < unaryConstraint.getRelation().length; i++) {
                        updatedDomain.add(unaryConstraint.getRelation()[i][0]);

                    }
                    // updating the current domain
                    var.setCurrentDomain(updatedDomain);
                }
            }

        }
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

    /**
     * This is the method for running the AC-1 algorithm. Will reduce the domains of
     * all the variables
     * 
     * @param myProblem takes in the problem data structure to get the constraints,
     *                  variables, and whether it is an extension/intension problem.
     *                  If it is an extension problem, then this will also tell the
     *                  algorithm if the extension variables are supports or
     *                  conflict
     * @return whether there exists some partial solution to the csp
     */
    public boolean AC1(MyProblem myProblem) {

        captureTime = getCpuTime();

        ArrayList<MyConstraint> constraints = myProblem.getConstraints();
        ArrayList<MyVariable> variables = myProblem.getVariables();

        boolean extension = (constraints.get(0).getClass().toString().contains("Extension"));

        SearchFunctions sf = new SearchFunctions(constraints, variables, extension);

        // Initialzing the queue used by the ac 1 algorithm
        ArrayList<ArrayList<MyVariable>> queue = new ArrayList<ArrayList<MyVariable>>();
        // computing iSize
        this.computeISize(variables);

        // List to hold unary constraints
        ArrayList<MyConstraint> unaryConstraints = new ArrayList<MyConstraint>();

        // Taking in all the possible combinations of each variable paired with another
        // variable besdies itself that are associated with a constraint
        for (MyConstraint c : constraints) {
            ArrayList<MyVariable> copy = c.getScope();
            ArrayList<MyVariable> putIn = new ArrayList<>();

            for (MyVariable v : variables) {
                if (v.getName().equals(copy.get(0).getName())) {
                    putIn.add(v);
                }
            }

            // making sure that this is a binary constraint
            if (copy.size() > 1) {
                for (MyVariable v : variables) {
                    if (v.getName().equals(copy.get(1).getName())) {
                        putIn.add(v);
                    }
                }
            } else if (copy.size() == 1) {
                unaryConstraints.add(c);
            }

            queue.add(putIn);

            // only need to put in the reverse if it is a binary constraint
            if (copy.size() > 1) {
                putIn = new ArrayList<>();

                for (MyVariable v : variables) {
                    if (v.getName().equals(copy.get(1).getName())) {
                        putIn.add(v);
                    }
                }

                for (MyVariable v : variables) {
                    if (v.getName().equals(copy.get(0).getName())) {
                        putIn.add(v);
                    }
                }

                queue.add(putIn);
            }
        }

        // Node consistency algorithm here
        if (extension) {
            for (MyConstraint c : unaryConstraints) {
                nodeConsistencyExtension(variables, (MyExtensionConstraint) c);
            }
        } else {
            for (MyConstraint c : unaryConstraints) {
                nodeConsistencyIntension(variables, (MyIntensionConstraint) c);
            }
        }

        boolean changed = true; // tracks if there was a change made in the algorithm
        while (changed == true) {
            changed = false;

            for (ArrayList<MyVariable> tuple : queue) {

                boolean updated = false;
                if (tuple.size() > 1) {
                    updated = sf.revised(tuple.get(0), tuple.get(1));
                }
                // boolean updated1 = sf.revised(tuple.get(1), tuple.get(0));

                // if there is a domain wipeout in any of the variables
                // System.out.println(tuple.get(0).getName() + " " +
                // tuple.get(0).getCurrentDomain().toString());
                for (MyVariable v : variables) {
                    if (v.getCurrentDomain().size() == 0) {
                        DecimalFormat df = new DecimalFormat("#.#####");
                        System.out.println("cc: " + sf.getCC());
                        System.out.println("CPU time: " + (getCpuTime() - captureTime) / 1000000);

                        System.out.println("fval: " + sf.getfval());
                        System.out.println("iSize: " + df.format(this.iSize));
                        System.out.println("fSize: false\nfEffect: false");
                        return false;
                    }
                }
                // keep track of whether there has been a change made within the while loop
                changed = (changed || updated);
                // changed = (changed || updated || updated1);

            }

            // System.out.println(myProblem.printDomains());

        }
        DecimalFormat df = new DecimalFormat("#.#####");

        System.out.println("cc: " + sf.getCC());
        System.out.println("CPU time: " + ((getCpuTime() - captureTime) / 1000000));

        System.out.println("fval: " + sf.getfval());
        System.out.println("iSize: " + df.format(this.iSize));

        System.out.println("fSize: " + df.format(this.computeFSize(variables)));
        System.out.println("fEffect: " + df.format(this.computeFEffect(this.iSize, this.fSize)));
        return true;

    }

    /**
     * This is the method for running the AC-3 algorithm. Will reduce the domains of
     * all the variables. Instead of using a static 'queue' of all the variable
     * tuples, this will use a dynamic queue of only the tuples that have been
     * modified before to avoid redundant calls
     * 
     * @param myProblem takes in the problem data structure to get the constraints,
     *                  variables, and whether it is an extension/intension problem.
     *                  If it is an extension problem, then this will also tell the
     *                  algorithm if the extension variables are supports or
     *                  conflict
     * @return whether there exists some partial solution to the csp
     */
    public boolean AC3(MyProblem myProblem) {

        captureTime = getCpuTime();

        ArrayList<MyConstraint> constraints = myProblem.getConstraints();
        ArrayList<MyVariable> variables = myProblem.getVariables();

        boolean extension = (constraints.get(0).getClass().toString().contains("Extension"));

        SearchFunctions sf = new SearchFunctions(constraints, variables, extension);

        // Initialzing the queue used by the ac 3 algorithm
        Deque<ArrayList<MyVariable>> queue = new LinkedList<ArrayList<MyVariable>>();

        // An arraylist (only used to look through) all the tuples that correspond to a
        // scope of a constraint in the csp
        ArrayList<ArrayList<MyVariable>> listOftuples = new ArrayList<ArrayList<MyVariable>>();

        // Array list for all the unary constraints
        ArrayList<MyConstraint> unaryConstraints = new ArrayList<MyConstraint>();

        // computing iSize
        this.computeISize(variables);

        System.out.println(constraints);

        ArrayList<MyVariable> tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(0).getScope().get(0));
        tuple.add(constraints.get(0).getScope().get(1));
        queue.add(tuple);
        listOftuples.add(tuple);

        tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(0).getScope().get(1));
        tuple.add(constraints.get(0).getScope().get(0));
        queue.add(tuple);
        listOftuples.add(tuple);

        tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(1).getScope().get(0));
        tuple.add(constraints.get(1).getScope().get(1));
        queue.add(tuple);
        listOftuples.add(tuple);

        tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(2).getScope().get(0));
        tuple.add(constraints.get(2).getScope().get(1));
        queue.add(tuple);
        listOftuples.add(tuple);

        tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(1).getScope().get(1));
        tuple.add(constraints.get(1).getScope().get(0));
        queue.add(tuple);
        listOftuples.add(tuple);

        tuple = new ArrayList<MyVariable>();
        tuple.add(constraints.get(2).getScope().get(1));
        tuple.add(constraints.get(2).getScope().get(0));
        queue.add(tuple);
        listOftuples.add(tuple);

        /*
         * // putting in every pair (both directions) that exist in a constraint for
         * (MyConstraint constraint : constraints) {
         * 
         * // putting in unary constraints if (constraint.getScope().size() == 1) {
         * unaryConstraints.add(constraint); } // finding binary constraints and putting
         * their scopes in as tuples into the // queue else if
         * (constraint.getScope().size() == 2) {
         * 
         * // the tuple being added into the queue ArrayList<MyVariable> tuple = new
         * ArrayList<MyVariable>(); tuple.add(constraint.getScope().get(0));
         * tuple.add(constraint.getScope().get(1)); // since the first part of the tuple
         * is already in the tuple
         * 
         * queue.add(tuple); listOftuples.add(tuple);
         * 
         * // adding in the reversed version fo the tupel ArrayList<MyVariable>
         * tupleReverse = new ArrayList<MyVariable>();
         * tupleReverse.add(constraint.getScope().get(1));
         * tupleReverse.add(constraint.getScope().get(0));
         * 
         * queue.add(tupleReverse); listOftuples.add(tupleReverse);
         * 
         * } }
         */

        for (ArrayList<MyVariable> a : queue) {
            System.out.print("(" + a.get(0).getName() + " " + a.get(1).getName() + "),");
        }
        System.out.println();
        System.out.println();

        // looping through the queue while there is a tuple inside of it
        while (queue.size() != 0) {

            // first tuple in the queue
            ArrayList<MyVariable> tupleToTest = queue.remove();

            // run the revise function for this tuple
            boolean revised = sf.revised(tupleToTest.get(0), tupleToTest.get(1));

            // if there was a change in the domain with the revise function
            if (revised) {
                String x_i = tupleToTest.get(0).getName();
                String x_j = tupleToTest.get(1).getName();

                // going through all the scopes in the problem
                // queue <- queue U {(x_k, x_i), k != i, k != j)
                for (ArrayList<MyVariable> tupleInList : listOftuples) {
                    // if x_i is in the second part of a scope
                    if (tupleInList.get(1).getName().equals(x_i)) {
                        String x_k = tupleInList.get(0).getName();

                        // making sure x_k != i and x_k != j
                        if (!x_k.equals(x_i) && !x_k.equals(x_j)) {

                            // making sure that we don't add it in the queue if it already exists
                            boolean exists = false;
                            for (ArrayList<MyVariable> tupleInQueue : queue) {
                                if (tupleInList.get(0).getName().equals(tupleInQueue.get(0).getName())
                                        && tupleInList.get(1).getName().equals(tupleInQueue.get(1).getName())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                queue.addFirst(tupleInList);
                                System.out.println("Add to queue " + tupleInList.get(0).getName() + " "
                                        + tupleInList.get(1).getName());
                            }
                        }
                    }
                }
            }

            // if there is a domain wipeout in any of the variables
            for (MyVariable v : variables) {
                if (v.getCurrentDomain().size() == 0) {
                    DecimalFormat df = new DecimalFormat("#.#####");
                    System.out.println("cc: " + sf.getCC());
                    System.out.println("CPU time: " + (getCpuTime() - captureTime) / 1000000);

                    System.out.println("fval: " + sf.getfval());
                    System.out.println("iSize: " + df.format(this.iSize));
                    System.out.println("fSize: false\nfEffect: false");
                    return false;
                }
            }

            for (ArrayList<MyVariable> a : queue) {
                System.out.print("(" + a.get(0).getName() + " " + a.get(1).getName() + "),");
            }
            System.out.println();
        }

        for (MyVariable var : variables) {
            System.out.println(var.getCurrentDomain());
        }

        DecimalFormat df = new DecimalFormat("#.#####");

        System.out.println("cc: " + sf.getCC());
        System.out.println("CPU time: " + df.format(((getCpuTime() - captureTime) / 1000000)));

        System.out.println("fval: " + sf.getfval());
        System.out.println("iSize: " + df.format(this.iSize));

        System.out.println("fSize: " + df.format(this.computeFSize(variables)));
        System.out.println("fEffect: " + df.format(this.computeFEffect(this.iSize, this.fSize)));
        return true;
    }
}