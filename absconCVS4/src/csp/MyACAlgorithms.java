package csp;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;

public class MyACAlgorithms {

    protected int cc;
    protected int fval;

    protected long captureTime;
    protected long runningTime;

    protected double isize;
    protected double fsize;
    protected double feffect;

    public double computeISize(ArrayList<MyVariable> variables) {
        double a = 1;
        for (MyVariable v : variables) {
            a *= v.getDomain().length;
        }
        this.isize = Math.log(a);
        return this.isize;
    }

    public double computeFSize(ArrayList<MyVariable> variables) {
        double a = 1;
        for (MyVariable v : variables) {
            a *= v.currentDomain.size();
            System.out.println(v.currentDomain.size());

        }
        this.fsize = Math.log(a);
        return this.fsize;
    }

    public double computeFEffect(double isize, double fsize) {
        this.feffect = Math.log((Math.pow(isize, Math.E) / Math.pow(fsize, Math.E)));
        return this.feffect;
    }

    public boolean AC1(MyProblem myProblem) {

        captureTime = getCpuTime();

        ArrayList<MyConstraint> constraints = myProblem.getConstraints();
        ArrayList<MyVariable> variables = myProblem.getVariables();

        boolean extension = (constraints.get(0).getClass().toString().contains("Extension"));

        SearchFunctions sf = new SearchFunctions(constraints, variables, extension);

        // Initialzing the queue used by the ac 1 algorithm
        ArrayList<ArrayList<MyVariable>> queue = new ArrayList<ArrayList<MyVariable>>();
        // computing isize
        this.computeISize(variables);

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

            for (MyVariable v : variables) {
                if (v.getName().equals(copy.get(1).getName())) {
                    putIn.add(v);
                }
            }

            queue.add(putIn);

            // making sure to add the other direction
            // ArrayList<MyVariable> a = c.getScope();
            // MyVariable temp = a.get(0);
            // a.set(0, a.get(1));
            // a.set(1, temp);
            // queue.add(a);

        }

        // Node consistency algorithm here

        boolean changed = true; // tracks if there was a change made in the algorithm
        while (changed == true) {
            changed = false;

            for (ArrayList<MyVariable> tuple : queue) {

                System.out.println("AC1 start");
                System.out.println(tuple.get(0).getName() + " " + tuple.get(0).getCurrentDomain().toString());
                System.out.println(tuple.get(1).getName() + " " + tuple.get(1).getCurrentDomain().toString());

                // run the revised function for the two variables being tested
                boolean updated = sf.revised(tuple.get(0), tuple.get(1));
                boolean updated1 = sf.revised(tuple.get(1), tuple.get(0));

                // if there is a domain wipeout in the first variable
                // System.out.println(tuple.get(0).getCurrentDomain().toString());
                if (tuple.get(0).getCurrentDomain().size() == 0) {
                    System.out.println("cc: " + sf.getCC());
                    System.out.println("CPU time: " + (getCpuTime() - captureTime));

                    System.out.println("fval: " + sf.getfval());
                    System.out.println("isize: " + this.isize);
                    System.out.println("fsize: false\nfeffect: false");
                    return false;
                } else {
                    // keep track of whether there has been a change made within the while loop
                    changed = (changed || updated || updated1);
                }

            }

            System.out.println("AC all doms: " + myProblem.printDomains());
        }

        System.out.println("cc: " + sf.getCC());
        System.out.println("CPU time: " + (getCpuTime() - captureTime));

        System.out.println("fval: " + sf.getfval());
        System.out.println("isize: " + this.isize);

        System.out.println("fsize: " + this.computeFSize(variables));
        System.out.println("feffect: " + this.feffect);
        return true;

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