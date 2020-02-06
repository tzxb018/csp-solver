package csp;

import java.util.ArrayList;
import java.util.Collections;

public class MyProblem {

    protected String problemName;
    protected ArrayList<MyVariable> variables;
    protected ArrayList<MyConstraint> constraints;

    public MyProblem(String problemName, ArrayList<MyVariable> variables, ArrayList<MyConstraint> constraints) {
        this.problemName = problemName;
        this.variables = variables;
        this.constraints = constraints;

        // assigning all the constraints and neighbors for each variable
        for (MyVariable myVar : variables) {
            setConstraintsForVariable(myVar);
            setNeighborsForVariable(myVar);
        }

        // sorting the two lists, constraints and variables lexiographically
        for (MyVariable myVar : variables) {
            ArrayList<MyConstraint> toSortConstraints = myVar.getConstraints();
            Collections.sort(toSortConstraints, MyConstraint.ConstraintComparer);
            myVar.setConstraints(toSortConstraints);
        }
    }

    public void setConstraintsForVariable(MyVariable myVar) {
        for (MyConstraint c : this.constraints) {
            for (int i = 0; i < c.getVariables().size(); i++) {
                if (c.getVariables().get(i).getName().equals(myVar.getName())) {
                    myVar.addConstraints(c);
                }
            }
        }
    }

    public void setNeighborsForVariable(MyVariable myVar) {
        for (MyVariable v : variables) {
            if (!v.getName().equals(myVar.getName())) {
                myVar.addNeighbors(v);
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