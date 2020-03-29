package csp.Search;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.Map.Entry;

import csp.CheckSupportRevise;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3, 4
 * @since 2/17/2020
 */
public class SearchAlgorithms {

    protected MyProblem myProblem; // instance of the CSP
    protected ArrayList<MyVariable> current_path; // the current path of all the variables ordered
    protected int assignments[]; // all the assignments

    protected boolean consistent; // global variable to check for consistency
    protected ArrayList<MyVariable> variables; // all the variables in the CSP

    protected ArrayList<LinkedList<Integer>> conf_set; // for CBJ

    // for FC
    protected ArrayList<Stack<Stack<Integer>>> reductions; // store sets of values remove from current-domain[j] by some
    // variable before v[j]
    protected ArrayList<Stack<Integer>> future_fc; // subset of the future variables that v[i] checks against
                                                   // (redundant)
    protected ArrayList<Stack<Integer>> past_fc; // past variables that checked against v[i]
    protected Map<MyVariable, Integer> assignments_for_FC; // assignment map for FC

    protected ArrayList<MyVariable> past_variables;
    protected ArrayList<MyVariable> future_variables;

    protected ArrayList<String> solutions;

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

    protected String algorithm; // type of search algorithm being used (from flag -s in command line)

    public SearchAlgorithms(MyProblem myProblem, ArrayList<MyVariable> current_path, int[] assignments,
            String algorithm) {
        this.myProblem = myProblem;
        this.current_path = current_path;
        this.assignments = assignments;
        this.variables = myProblem.getVariables();
        this.algorithm = algorithm;
        this.solutions = new ArrayList<>();

        this.cc = 0;
        this.nv = 0;
        this.bt = 0;

        this.algorithm = algorithm;

        if (algorithm.equals("CBJ")) {

            // initalizing the map conf-set
            this.conf_set = new ArrayList<LinkedList<Integer>>();

            // iterate through every variable in the current_path and initalize it with {0}
            for (MyVariable v : current_path) {
                if (v != null) {
                    LinkedList<Integer> init = new LinkedList<Integer>();
                    init.add(0);
                    conf_set.add(init);
                }
            }

            this.conf_set.add(0, null);
        } else if (algorithm.equals("FC")) {

            this.future_fc = new ArrayList<>();
            this.past_fc = new ArrayList<>();
            this.reductions = new ArrayList<>();

            this.past_variables = new ArrayList<MyVariable>();
            this.future_variables = new ArrayList<MyVariable>();

            this.assignments_for_FC = new HashMap<MyVariable, Integer>();

            // initalizing the data structures
            // iterate through every variable in the current_path and initalize it with
            // empty stack
            for (MyVariable v : current_path) {

                Stack<Integer> init = new Stack<Integer>();
                Stack<Integer> init1 = new Stack<Integer>();
                this.future_fc.add(init);
                this.past_fc.add(init1);

                Stack<Stack<Integer>> init2 = new Stack<Stack<Integer>>();
                this.reductions.add(init2);

                this.assignments_for_FC.put(v, -1);
                this.future_variables.add(v);

            }

            this.future_variables.remove(null);
            this.past_variables.add(null);

        }
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
    public boolean BCSSP(int n, String status, String dynamicOrdering) {

        this.cc = 0;
        this.nv = 0;
        this.bt = 0;

        captureTime = getCpuTime();

        consistent = true;
        status = "unknown";
        int i = 1;

        while (status.equals("unknown")) {

            if (consistent) {
                // System.out.println("LABEL: " + i);
                if (this.algorithm.equals("BT"))
                    i = BT_label(i);
                else if (this.algorithm.equals("CBJ"))
                    i = CBJ_label(i);
                else if (this.algorithm.equals("FC"))
                    i = FC_label(i, dynamicOrdering);
            } else {
                // System.out.println("UNLABEL: " + i);
                if (this.algorithm.equals("BT"))
                    i = BT_unlabel(i);
                else if (this.algorithm.equals("CBJ"))
                    i = CBJ_unlabel(i);
                else if (this.algorithm.equals("FC"))
                    i = FC_unlabel(i);
            }

            // determining if there is a solution or not
            if (i > n) {
                // for fc, need to remove the current instantation
                if (this.algorithm.equals("FC")) {
                    MyVariable recent = this.past_variables.remove(this.past_variables.size() - 1);
                    this.future_variables.add(0, recent);
                }
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

                    // //printCurrentDomains();

                    // adding the assignments of the all variables to the solution
                    if (algorithm.equals("FC")) {

                        ArrayList<MyVariable> sortedKeys = new ArrayList<MyVariable>(this.assignments_for_FC.keySet());

                        sortedKeys.remove(null);

                        Collections.sort(sortedKeys, MyVariable.SOL_COMPARATOR);

                        // Display the TreeMap which is naturally sorted
                        for (MyVariable x : sortedKeys) {
                            solution += (x + ": " + assignments_for_FC.get(x) + " ");
                        }

                    } else {
                        // adding the assignments of the all variables to the solution
                        for (MyVariable var : current_path) {
                            if (var != null) {
                                solution += (var.getName() + ": " + var.getCurrentDomain().get(0) + " ");
                            }
                        }
                        current_path.add(0, null); // pointer starts at 1
                    }
                    this.firstSolution = solution;
                    System.out.println("First solution: " + solution);

                    current_path.add(0, null); // pointer starts at 1
                    this.numberOfSolutions++;
                    solutions.add(solution);
                } else {
                    String solution = "";
                    current_path.remove(0);

                    // //printCurrentDomains();

                    if (algorithm.equals("FC")) {
                        for (Map.Entry<MyVariable, Integer> entry : this.assignments_for_FC.entrySet()) {
                            if (entry.getKey() != null) {
                                solution += entry.getKey() + ": " + entry.getValue() + " ";
                            }
                        }

                    } else {
                        // adding the assignments of the all variables to the solution
                        for (MyVariable var : current_path) {
                            if (var != null) {
                                solution += (var.getName() + ": " + var.getCurrentDomain().get(0) + " ");
                            }
                        }
                    }
                    current_path.add(0, null); // pointer starts at 1

                    this.numberOfSolutions++;
                    solutions.add(solution);

                }

                if (algorithm.equals("BT")) {
                    // backtrack one level to find more solutions
                    i = i - 1;
                    consistent = true;
                    current_path.get(i).currentDomain.remove(0);
                } else if (algorithm.equals("CBJ")) {
                    i = i - 1;
                    consistent = false;
                    LinkedList<Integer> conflict = new LinkedList<Integer>();
                    for (int ii = 0; ii < n; ii++) {
                        conflict.add(ii);
                    }
                    conf_set.set(n, conflict);

                    System.out.println(conf_set);
                } else if (algorithm.equals("FC")) {

                    i = i - 1;
                    consistent = true;
                    current_path.get(i).currentDomain.remove(0);

                    // updated_current_domain(i + 1);
                    undo_reduction(i);

                }
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

                    printSolutions();
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
                if (nextVal == assignments[h]) {
                    iterator.remove();
                }
            }

            if (current_path.get(h).getCurrentDomain().size() == 0) {
                consistent = false;
            } else
                consistent = true;
        }

        return h;

    }

    public int CBJ_label(int i) {
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
                            consistent = csr.check(current_path.get(i), assignments[i], current_path.get(h),
                                    assignments[h]);

                            this.cc++;
                        }
                    }
                }

                if (!consistent) {

                    // adding the conflict level (h) to the conf-set for this particular variable
                    SetFunctions llsf = new SetFunctions();
                    LinkedList<Integer> addTo = new LinkedList<>();
                    addTo.add(h);

                    conf_set.set(i, llsf.unionLL(conf_set.get(i), addTo));

                    // removing the inconsistent value from the current domain of the instantiated
                    // variable and breaking the loop
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

    public int CBJ_unlabel(int i) {

        SetFunctions llsf = new SetFunctions();
        int h = llsf.maxInLinkedList(conf_set.get(i));

        if (h > 0) {
            LinkedList<Integer> temp = conf_set.get(h);

            temp = llsf.unionLL(conf_set.get(h), conf_set.get(i));

            for (int k = 0; k < temp.size(); k++) {
                if (temp.get(k) == h) {
                    temp.remove(k);
                }
            }

            conf_set.set(h, temp);

            for (int j = h + 1; j <= i; j++) {

                // reinitalizing the conf_set for the levels in between h+1 and i
                LinkedList<Integer> init = new LinkedList<>();
                init.add(0);
                conf_set.set(j, init);

                // reseetting the domain
                current_path.get(j).resetDomain();
            }

            this.bt++;

            current_path.get(i).resetDomain();

            // starting domain at level i
            if (h > 0) {
                Iterator<Integer> iterator = current_path.get(h).currentDomain.iterator();

                // finding the index of assignments[h] in current-domain[h] to remove it
                while (iterator.hasNext()) {
                    int nextVal = iterator.next();
                    if (nextVal == assignments[h]) {
                        iterator.remove();
                    }
                }

                if (current_path.get(h).getCurrentDomain().size() == 0) {
                    consistent = false;
                } else
                    consistent = true;
            }
        }

        // System.out.println("After : " + conf_set);

        return h;

    }

    public boolean check_forward(int i, int j) {

        // System.out.println("check forward " + i + " " + j);

        Stack<Integer> reduction = new Stack<Integer>();

        CheckSupportRevise csr = new CheckSupportRevise(myProblem.getConstraints(), this.current_path,
                myProblem.getExtension());

        // going through each possible assignment in the current domain of the variable
        // at v[i]
        Iterator<Integer> iterator = current_path.get(j).getCurrentDomain().iterator();

        while (iterator.hasNext()) {

            // assigning the next possible value for v[j]
            int next = iterator.next();

            // assignments[j] = next;
            this.assignments_for_FC.replace(current_path.get(j), next);

            // need to make sure that there is a constraint in between the two variables
            for (MyConstraint c : myProblem.getConstraints()) {
                if (c.getScope().size() > 1) {
                    if ((c.getScope().get(0).getName().equals(current_path.get(j).getName())
                            && c.getScope().get(1).getName().equals(current_path.get(i).getName()))
                            || (c.getScope().get(1).getName().equals(current_path.get(j).getName())
                                    && c.getScope().get(0).getName().equals(current_path.get(i).getName()))) {
                        consistent = csr.check(current_path.get(i), this.assignments_for_FC.get(current_path.get(i)),
                                current_path.get(j), this.assignments_for_FC.get(current_path.get(j)));

                        // System.out.println("V" + i + ":" + assignments[i] + " <> " + "V" + j + ":" +
                        // assignments[j]
                        // + " at level " + j + " ==> " + consistent);

                        this.cc++;
                    }
                }
            }

            // if not check i and j
            if (!consistent) {
                reduction.push(this.assignments_for_FC.get(current_path.get(j)));
            }
        }

        // System.out.println("new reduction at level " + j + ": " + reduction);
        if (!reduction.empty()) {
            SetFunctions sf = new SetFunctions();

            current_path.get(j).setCurrentDomain(sf.setDiff(current_path.get(j).getCurrentDomain(), reduction));

            this.reductions.get(j).push(reduction);
            this.future_fc.get(i).push(j);
            // System.out.println("pushing " + reduction + " into reductions[" + j + "]");
            // System.out.println("pushing " + j + " into future-fc[" + i + "]");
            this.past_fc.get(j).push(i);

            // System.out.println("reductions update in check forward " + this.reductions);
            // printFCTables();
        }

        return (current_path.get(j).getCurrentDomain().size() > 0);

    }

    public void undo_reduction(int i) {
        // System.out.println("UNDO REDUCTIONS " + i);
        // System.out.println("future fc " + this.future_fc);
        // System.out.println("future fc[" + i + "]: " + this.future_fc.get(i));
        while (!future_fc.get(i).empty()) {

            int j = future_fc.get(i).pop();

            Stack<Integer> reduction = new Stack<Integer>();
            if (!this.reductions.get(j).empty())
                reduction = this.reductions.get(j).pop();

            // System.out.println("reduction to be added back " + reduction);

            SetFunctions sf = new SetFunctions();

            ArrayList<Integer> updatedCurrentDomain = sf.unionAS(current_path.get(j).getCurrentDomain(), reduction);

            // System.out.println("updated domain: " +
            // current_path.get(j).getCurrentDomain() + " U " + reduction);

            current_path.get(j).setCurrentDomain(updatedCurrentDomain);

            // System.out.println("updated domain at " + j + ": " +
            // current_path.get(j).getCurrentDomain());

            // System.out.println("past fc" + this.past_fc);
            if (!this.past_fc.get(j).empty()) {
                this.past_fc.get(j).pop();
            }
        }

        Stack<Integer> empty = new Stack<>();
        future_fc.set(i, empty);

        // System.out.println("FINISHED undo reductions: " + future_fc);
        // printCurrentDomains();
        //// printFCTables();
    }

    public void updated_current_domain(int i) {

        // System.out.println("updated current domains " + i);

        // //printCurrentDomains();

        current_path.get(i).resetDomain();
        SetFunctions sf = new SetFunctions();

        ArrayList<Stack<Integer>> reduction_at_i = new ArrayList<>(this.reductions.get(i));

        for (Stack<Integer> reduction : reduction_at_i) {
            current_path.get(i).setCurrentDomain(sf.setDiff(current_path.get(i).getCurrentDomain(), reduction));
        }

        // //printCurrentDomains();
    }

    // given a list of future variables, this function will return which variable to
    // instantite next
    public MyVariable selectNextInstantiatedVariable(ArrayList<MyVariable> futureVariables, String varOrdering) {

        MyVariable nextToInstantiate = null;

        // if ordering is dyanmic
        if (varOrdering.equals("dLD")) {
            // searches for all the variables that have a domain size of 1 in the future
            // variables to implement the domino effect
            for (MyVariable v : futureVariables) {
                // if the current domain has a size 1 and is lexiographically ahead
                if (v.getCurrentDomain().size() == 1) {
                    // if a variable has not been found yet
                    if (nextToInstantiate == null) {
                        nextToInstantiate = v;
                    } else {
                        if (v.getName().compareTo(nextToInstantiate.getName()) < 0) {
                            nextToInstantiate = v;
                        }
                    }
                }
            }

            // if there is a variable with a domain size of 1, return this one
            if (nextToInstantiate != null) {
                return nextToInstantiate;
            }

            nextToInstantiate = futureVariables.get(0);
            // using the variable ordering herusitic to find the best variable
            if (varOrdering.equals("dLD")) {
                // searches for all the variables to find the variable with the smallest domain
                for (MyVariable v : futureVariables) {
                    // if the current domain has a size 1 and is lexiographically ahead
                    if (v.getCurrentDomain().size() <= nextToInstantiate.getCurrentDomain().size()
                            && v.getName().compareTo(nextToInstantiate.getName()) < 0) {
                        nextToInstantiate = v;
                    }
                }
            }

            return nextToInstantiate;

        } else {
            // if the variable ordering heursitic is static, just return the next variable
            // in the list of future variables
            return futureVariables.get(0);
        }

    }

    public int FC_label(int i, String varOrdering) {
        consistent = false;

        MyVariable instantiatedVar;
        instantiatedVar = selectNextInstantiatedVariable(this.future_variables, varOrdering);
        // String s = "[";
        // for (MyVariable v : future_variables) {
        // if (v.equals(instantiatedVar))
        // s += "*" + v.getName() + "(" + v.getCurrentDomain().size() + "), ";
        // else
        // s += v.getName() + "(" + v.getCurrentDomain().size() + "), ";
        // }
        // System.out.println("List of variables to instantiate from: " + s.substring(0,
        // s.length() - 2) + "]");

        this.future_variables.remove(instantiatedVar);
        this.past_variables.add(instantiatedVar);

        this.current_path = new ArrayList<MyVariable>();
        this.current_path.addAll(this.past_variables);
        this.current_path.addAll(this.future_variables);

        // System.out.println("Instantiate: " + instantiatedVar.getName() + " with a
        // domain of "
        // + instantiatedVar.getCurrentDomain());

        // printOrdering();

        // going through each possible assignment in the current domain of the variable
        // at v[i]
        Iterator<Integer> iterator = instantiatedVar.getCurrentDomain().iterator();
        while (iterator.hasNext() && !consistent) {

            // assigning the next possible value for v[i]
            int next = iterator.next();
            // assignments[i] = next;
            this.assignments_for_FC.put(this.current_path.get(i), next);

            // System.out.println("Assignment: " + current_path.get(i).getName() + " <-- " +
            // assignments[i]);
            // System.out.println("assignments: " + Arrays.toString(this.assignments));
            consistent = true;
            this.nv++;

            // forward checking against all past variables with their respective assignments
            for (int j = i + 1; j < current_path.size(); j++) {

                // System.out.println("checking forward at levels " + i + " & " + j);
                consistent = check_forward(i, j);

                // System.out.println(current_path.get(h).getName() + " " + assignments[h] + "
                // <> "
                // + current_path.get(i).getName() + " " + assignments[i] + " ==> " +
                // consistent);
                // System.out.println(current_path.get(i).getCurrentDomain());
                if (!consistent) {
                    iterator.remove();
                    undo_reduction(i);
                    break;
                }

            }

        }

        // printOrdering(i);

        if (consistent) {
            return i + 1; // an assignment to v[i] works
        } else {
            MyVariable recent = this.past_variables.remove(this.past_variables.size() - 1);
            this.future_variables.add(0, recent);
            return i;
        }

    }

    public int FC_unlabel(int i) {

        // printOrdering();
        MyVariable recent = this.past_variables.remove(this.past_variables.size() - 1);
        this.future_variables.add(0, recent);

        // System.out.println("Uninstantiated " + recent);

        this.current_path = new ArrayList<MyVariable>();
        this.current_path.addAll(this.past_variables);
        this.current_path.addAll(this.future_variables);

        // printOrdering();

        this.bt++;
        int h = i - 1;
        undo_reduction(h);
        updated_current_domain(i);
        // starting domain at level i
        if (h > 0) {
            Iterator<Integer> iterator = current_path.get(h).currentDomain.iterator();

            // finding the index of assignments[h] in current-domain[h] to remove it
            while (iterator.hasNext()) {
                int nextVal = iterator.next();
                // System.out.println(nextVal + " == " + assignments[h]);
                if (nextVal == this.assignments_for_FC.get(this.current_path.get(h))) {
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

    public void printCurrentDomains() {
        System.out.println("Printing current domains");
        for (MyVariable v : current_path) {
            if (v != null) {
                System.out.println(v.getName() + ": " + v.getCurrentDomain());
            }
        }
    }

    public void printFCTables() {

        for (int i = 1; i < current_path.size(); i++) {
            System.out.println(current_path.get(i).getName() + ": reductions: " + reductions.get(i) + " future: "
                    + future_fc.get(i));
        }
    }

    public void printOrdering() {
        String s = "Past Variables: [";
        for (int i = 1; i < this.past_variables.size(); i++) {
            if (this.past_variables.get(i) != null) {
                s += this.past_variables.get(i).getName() + "(" + this.past_variables.get(i).getCurrentDomain().size()
                        + ")= " + this.assignments_for_FC.get(this.past_variables.get(i)) + ", ";
            }
        }
        s = s.substring(0, s.length() - 2) + "]\nFuture Variables: [";
        for (int i = 0; i < this.future_variables.size(); i++) {
            if (this.future_variables.get(i) != null) {
                s += this.future_variables.get(i).getName() + "("
                        + this.future_variables.get(i).getCurrentDomain().size() + ")= "
                        + this.assignments_for_FC.get(this.future_variables.get(i)) + ", ";
            }
        }
        s = s.substring(0, s.length() - 2) + "]";
        System.out.println(s);
        System.out.println("Current path: " + this.current_path);
        System.out.println();
    }

    public void printSolutions() {
        for (int i = 0; i < this.solutions.size(); i++) {
            System.out.println(this.solutions.get(i));
        }
    }
}