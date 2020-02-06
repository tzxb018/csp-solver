package csp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PVariable;

public abstract class MyConstraint implements Comparator<MyConstraint> {

    protected String name;
    protected ArrayList<MyVariable> variables;
    protected PConstraint refConstraint;

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

        variables = new ArrayList<MyVariable>();

        PVariable[] tempVariables = refConstraint.getScope();
        for (int i = 0; i < tempVariables.length; i++) {
            MyVariable varTemp = new MyVariable(tempVariables[i]);
            variables.add(varTemp);
        }

    }

    public String getName() {
        return this.name;
    }

    public ArrayList<MyVariable> getVariables() {
        return this.variables;
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
        return Objects.equals(name, myConstraint.name) && Objects.equals(variables, myConstraint.variables)
                && Objects.equals(refConstraint, myConstraint.refConstraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, variables, refConstraint);
    }

    @Override
    public String toString() {
        String s = "Name: " + this.name + ", variables: {";
        for (int i = 0; i < variables.size() - 1; i++) {
            s += variables.get(i).getName();
            s += ",";
        }
        s += variables.get(variables.size() - 1).getName() + "}";

        return s;
    }

}