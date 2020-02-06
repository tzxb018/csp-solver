package csp;

import java.util.ArrayList;
import java.util.Objects;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PIntensionConstraint;

public class MyIntensionConstraint extends MyConstraint {

    protected MyFunction definition;
    protected String[] univeralPostExpression;
    protected PIntensionConstraint refCon;
    protected String[] param;

    public MyIntensionConstraint(PConstraint refConstraint, PIntensionConstraint refcon) {
        super(refConstraint);
        this.refCon = refcon;

        this.definition = new MyFunction(this.refCon.getFunction());
        this.univeralPostExpression = refcon.getUniversalPostfixExpression();

        String[] paramholder = new String[5];
        paramholder[4] = this.univeralPostExpression[0];
        paramholder[0] = super.variables.get(0).getName();
        paramholder[1] = super.variables.get(1).getName();
        paramholder[2] = super.variables.get(0).getName();
        paramholder[3] = super.variables.get(1).getName();

        this.param = paramholder;

    }

    public MyFunction getDefinition() {
        return this.definition;
    }

    public void setDefinition(MyFunction definition) {
        this.definition = definition;
    }

    public PIntensionConstraint getRefCon() {
        return this.refCon;
    }

    public void setRefCon(PIntensionConstraint refCon) {
        this.refCon = refCon;
    }

    public MyIntensionConstraint definition(MyFunction definition) {
        this.definition = definition;
        return this;
    }

    public MyIntensionConstraint refCon(PIntensionConstraint refCon) {
        this.refCon = refCon;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MyIntensionConstraint)) {
            return false;
        }
        MyIntensionConstraint myIntensionConstraint = (MyIntensionConstraint) o;
        return Objects.equals(definition, myIntensionConstraint.definition)
                && Objects.equals(refCon, myIntensionConstraint.refCon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition, refCon);
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += ", definition: intension function: ";
        s += definition;
        s += ", params: {";
        for (int i = 0; i < this.param.length - 1; i++) {
            s += this.param[i] + ",";
        }
        s += this.param[this.param.length - 1] + "}";

        return s;
    }

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

}