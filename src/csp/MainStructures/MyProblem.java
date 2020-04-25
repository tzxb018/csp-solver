package csp.MainStructures;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyProblem {

    public String problemName;
    public ArrayList<MyVariable> variables;
    public ArrayList<MyConstraint> constraints;
    public boolean extension; // determines if the problem is intension or extension
    protected int edges;

    // Stores the important informations about the problem, the variables (which
    // have their own domains) and the constraints (the three compoennets of a csp)
    public MyProblem(String problemName, ArrayList<MyVariable> variables, ArrayList<MyConstraint> constraints) {
        this.problemName = problemName;
        this.variables = variables;
        this.constraints = constraints;

        // assigning all the constraints and neighbors for each variable
        for (MyVariable myVar : variables) {
            setConstraintsForVariable(myVar);
        }

        // adding "edges" for each constraint by adding neighbors for each constraint
        // for the two corresponding variables
        for (MyConstraint c : this.constraints) {
            if (c.getScope().size() > 1) {
                MyVariable scope1 = null;
                MyVariable scope2 = null;
                for (MyVariable v : this.variables) {
                    if (v.equals(c.getScope().get(0))) {
                        scope1 = v;
                    } else if (v.equals(c.getScope().get(1))) {
                        scope2 = v;
                    }
                }
                for (MyVariable v : this.variables) {
                    if (v.equals(scope1)) {
                        if (!v.getNeighbors().contains(scope2)) {
                            v.addNeighbors(scope2);
                            edges++;
                        }
                    } else if (v.equals(scope2)) {
                        if (!v.getNeighbors().contains(scope1)) {
                            v.addNeighbors(scope1);
                        }
                    }
                }
            }
        }

        // sorting the two lists, constraints and variables lexiographically
        for (MyVariable myVar : variables) {
            // System.out.println(myVar + ": " + myVar.getNeighbors());
            // if (myVar.getNeighbors().size() > 0) {
            // for (MyVariable v : myVar.getNeighbors()) {
            // System.out.println(v + ": " + v.getNeighbors());
            // }
            // }
            // System.out.println();
            ArrayList<MyConstraint> toSortConstraints = myVar.getConstraints();
            Collections.sort(toSortConstraints, MyConstraint.ConstraintComparer);
            myVar.setConstraints(toSortConstraints);
        }

        this.extension = (constraints.get(0).getClass().toString().contains("Extension"));

    }

    public int getEdges() {
        return this.edges;
    }

    // public void normalizeConstraints(ArrayList<MyConstraint> list_constraints) {
    // ArrayList<MyConstraint> normalizedConstraints = new
    // ArrayList<MyConstraint>();

    // Iterator<MyConstraint> constraintIterator = list_constraints.iterator();

    // while (constraintIterator.hasNext()) {
    // MyConstraint unnomralizedConstraint = constraintIterator.next();

    // for (MyConstraint c : list_constraints) {
    // if (c.getScope().size() > 1) {
    // if
    // (c.getScope().get(0).getName().equals(unnomralizedConstraint.getScope().get(0).getName())
    // && c
    // .getScope().get(1).getName().equals(unnomralizedConstraint.getScope().get(1).getName()))
    // {

    // }
    // }
    // }
    // }

    // }

    // Sets all the constraints linked to the variable
    public void setConstraintsForVariable(MyVariable myVar) {
        // Looping through each constraint to see if a variable is assigned to that
        // particular constraint
        for (MyConstraint c : this.constraints) {
            for (int i = 0; i < c.getVariables().size(); i++) {
                if (c.getVariables().get(i).getName().equals(myVar.getName())) {
                    myVar.addConstraints(c);
                }
            }
        }
    }

    // Sets all the neighbors of the variable
    public void setNeighborsForVariable(MyVariable myVar) {
        // Looping through the constraints to see if there is a constraint (not unary)
        // that has a scope with it in it
        // Add the other variable in the scope as its neighbor
        for (MyConstraint c : this.constraints) {
            if (c.getScope().size() > 1) {
                if (c.getScope().get(0).getName().equals(myVar.getName())) {
                    for (MyVariable v : this.variables) {
                        if (v.getName().equals(c.getScope().get(1).getName())) {
                            myVar.addNeighbors(v);
                        }
                    }
                    // myVar.addNeighbors(c.getScope().get(1));
                } else if (c.getScope().get(1).getName().equals(myVar.getName())) {
                    for (MyVariable v : this.variables) {
                        if (v.getName().equals(c.getScope().get(0).getName())) {
                            myVar.addNeighbors(v);
                        }
                    }
                    // myVar.addNeighbors(c.getScope().get(0));
                }
            }
        }
    }

    public String getProblemName() {
        return this.problemName;
    }

    public ArrayList<MyVariable> getVariables() {
        return this.variables;
    }

    public ArrayList<MyConstraint> getConstraints() {
        return this.constraints;
    }

    public boolean getExtension() {
        return this.extension;
    }

    public String printDomains() {
        String s = "";
        for (MyVariable v : variables) {
            s += v.getName() + " " + v.getCurrentDomain().toString() + "\n";
        }

        return s;
    }

    @Override
    public String toString() {

        String s = "Instance name: " + problemName;
        s += "\nVariables:";
        for (MyVariable myVar : variables) {
            s += "\n" + myVar;
        }
        s += "\nConstraints:\n";
        Collections.sort(constraints, MyConstraint.ConstraintComparer);
        for (MyConstraint myConstraint : constraints) {
            s += myConstraint + "\n";
        }
        return s;
    }

}