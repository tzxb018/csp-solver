package csp.Structures;

import java.util.Objects;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PRelation;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class MyExtensionConstraint extends MyConstraint {

    protected PExtensionConstraint conRef;
    protected PRelation relationRef;
    protected int[][] relation;
    protected String semantics; // conflict/support

    // Using the MyConstraint as a parent class, this is a data structure for just
    // extension constraints
    public MyExtensionConstraint(PConstraint refConstraint, PExtensionConstraint refExtensionConstraint) {
        super(refConstraint);
        this.conRef = refExtensionConstraint;
        this.relationRef = refExtensionConstraint.getRelation();
        this.semantics = relationRef.getSemantics();

        this.relation = relationRef.getTuples();

    }

    public PExtensionConstraint getConRef() {
        return this.conRef;
    }

    public void setConRef(PExtensionConstraint conRef) {
        this.conRef = conRef;
    }

    public PRelation getRelationRef() {
        return this.relationRef;
    }

    public void setRelationRef(PRelation relationRef) {
        this.relationRef = relationRef;
    }

    public int[][] getRelation() {
        return this.relation;
    }

    public void setRelation(int[][] definition) {
        this.relation = definition;
    }

    public String getSemantics() {
        return this.semantics;
    }

    public void setSemantics(String semantics) {
        this.semantics = semantics;
    }

    public MyExtensionConstraint conRef(PExtensionConstraint conRef) {
        this.conRef = conRef;
        return this;
    }

    public MyExtensionConstraint relationRef(PRelation relationRef) {
        this.relationRef = relationRef;
        return this;
    }

    public MyExtensionConstraint relation(int[][] definition) {
        this.relation = definition;
        return this;
    }

    public MyExtensionConstraint semantics(String semantics) {
        this.semantics = semantics;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MyExtensionConstraint)) {
            return false;
        }
        MyExtensionConstraint myExtensionConstraint = (MyExtensionConstraint) o;
        return Objects.equals(conRef, myExtensionConstraint.conRef)
                && Objects.equals(relationRef, myExtensionConstraint.relationRef)
                && Objects.equals(relation, myExtensionConstraint.relation)
                && Objects.equals(semantics, myExtensionConstraint.semantics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conRef, relationRef, relation, semantics);
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += ", definition: " + semantics + " {";

        for (int i = 0; i < relation.length; i++) {
            s += "(";
            for (int j = 0; j < relation[i].length; j++) {
                s += (relation[i][j]);
                if (j != relation[i].length - 1)
                    s += (',');
            }
            if (i != relation.length - 1)
                s += ("),");
        }
        s += ")}";

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