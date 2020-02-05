package csp;

import java.util.ArrayList;
import java.util.Collections;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PRelation;
import abscon.instance.components.PVariable;
import abscon.instance.tools.InstanceParser;

public class MainFunctions {

    protected ArrayList<PConstraint> constraintList;
    protected ArrayList<Variable> variableList;
    protected boolean extension;

    public MainFunctions(ArrayList<PConstraint> constraintList, ArrayList<Variable> variables, boolean extension) {
        this.constraintList = constraintList;
        this.variableList = variables;
        this.extension = extension;

    }

    public boolean check(Variable variable, int val1, Variable variable2, int val2) {
        PConstraint constraint = null;
        int iterator = 0;

        Variable[] scopeWithTwoVars = { variable, variable2 };

        // iterating through the constraint list to find our constraint to test
        while (constraint == null && iterator < constraintList.size()) {
            PConstraint testConstraint = constraintList.get(iterator);
            System.out.println("======");
            if (testConstraint.getScope()[0].getName().equals(scopeWithTwoVars[0].getName())
                    && testConstraint.getScope()[1].getName().equals(scopeWithTwoVars[1].getName())) {
                constraint = testConstraint;
            }
            iterator++;
        }

        if (constraint == null)
            return true; // universal constraints return true

        // CC ++

        // If we are working with extension
        if (extension) {
            PExtensionConstraint extensionConstraint = (PExtensionConstraint) constraint;
            PRelation relation = extensionConstraint.getRelation();
            boolean foundMatch = false;

            // finding the two values in one tuple within the relation
            for (int i = 0; i < relation.getTuples().length; i++) {
                if (relation.getTuples()[i][0] == val1 && relation.getTuples()[i][1] == val2) {
                    foundMatch = true;
                    break;
                }
            }

            // returning boolean values based on semantics and whether or not a match has
            // been found
            if (relation.getSemantics().equals("conflicts")) {
                if (foundMatch) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (foundMatch) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}