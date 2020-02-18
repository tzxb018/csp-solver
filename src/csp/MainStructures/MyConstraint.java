package csp.MainStructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PVariable;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public abstract class MyConstraint implements Comparator<MyConstraint> {

    protected String name;
    protected ArrayList<MyVariable> scope;
    protected PConstraint refConstraint;

    // Creating a lexiographic comparator for the comparator by looking at the
    // constraint's name
    public static final Comparator<MyConstraint> ConstraintComparer = new Comparator<MyConstraint>() {
        @Override
        public int compare(MyConstraint c1, MyConstraint c2) {
            return extractInt(c1.getName()) - extractInt(c2.getName());
        }

        public int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            // return 0 if no digits found
            if (num.isEmpty()) {
                return 0;
            } else {
                return Integer.parseInt(num);
            }
        }
    };

    public MyConstraint(PConstraint refConstraint) {
        this.refConstraint = refConstraint;
        this.name = refConstraint.getName();

        scope = new ArrayList<MyVariable>();

        // Getting the scope of the constraint
        PVariable[] tempVariables = refConstraint.getScope();
        for (int i = 0; i < tempVariables.length; i++) {
            MyVariable varTemp = new MyVariable(tempVariables[i]);
            scope.add(varTemp);
        }

    }

    public MyConstraint() {

    }

    public void setScope(ArrayList<MyVariable> scope) {
        ArrayList<MyVariable> newScope = new ArrayList<MyVariable>();

        for (MyVariable var : scope) {
            newScope.add(var);
        }

        this.scope = newScope;
    }

    public ArrayList<MyVariable> getScope() {
        return this.scope;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<MyVariable> getVariables() {
        return this.scope;
    }

    public PConstraint getRefConstraint() {
        return this.refConstraint;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MyConstraint)) {
            return false;
        }
        MyConstraint myConstraint = (MyConstraint) o;
        return Objects.equals(name, myConstraint.name) && Objects.equals(scope, myConstraint.scope)
                && Objects.equals(refConstraint, myConstraint.refConstraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scope, refConstraint);
    }

    @Override
    public String toString() {
        String s = "Name: " + this.name + ", variables: {";
        for (int i = 0; i < scope.size() - 1; i++) {
            s += scope.get(i).getName();
            s += ",";
        }
        s += scope.get(scope.size() - 1).getName() + "}";

        return s;
    }

}