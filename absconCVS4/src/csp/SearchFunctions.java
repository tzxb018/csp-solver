package csp;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SearchFunctions {

    protected ArrayList<MyConstraint> constraintList;
    protected ArrayList<MyVariable> variableList;
    protected boolean extension;

    protected int cc;
    protected int fval;

    public SearchFunctions(ArrayList<MyConstraint> constraintList, ArrayList<MyVariable> variables, boolean extension) {
        this.constraintList = constraintList;
        this.variableList = variables;
        this.extension = extension;

        cc = 0;
        fval = 0;
    }

    public int getCC() {
        return cc;
    }

    public int getfval() {
        return fval;
    }

    // checks to see if the vvp's are in the relation given in the problem
    public boolean check(MyVariable variable1, int val1, MyVariable variable2, int val2) {
        MyConstraint constraint = null;
        int iterator = 0;

        MyVariable[] scopeWithTwoVars = { variable1, variable2 };

        // iterating through the constraint list to find our constraint to test
        while (constraint == null && iterator < constraintList.size()) {
            MyConstraint testConstraint = constraintList.get(iterator);
            if ((testConstraint.getScope().get(0).getName().equals(scopeWithTwoVars[0].getName())
                    && testConstraint.getScope().get(1).getName().equals(scopeWithTwoVars[1].getName()))) {
                // ||
                // (testConstraint.getScope().get(1).getName().equals(scopeWithTwoVars[0].getName())
                // &&
                // testConstraint.getScope().get(0).getName().equals(scopeWithTwoVars[1].getName())))
                // {
                constraint = testConstraint;
            }
            iterator++;
        }

        if (constraint == null)

        {
            return true; // universal constraints return true
        }

        cc++;

        // If we are working with extension
        if (extension) {
            MyExtensionConstraint extensionConstraint = (MyExtensionConstraint) constraint;
            int[][] relation = extensionConstraint.getRelation();
            boolean foundMatch = false;

            // finding the two values in one tuple within the relation
            // System.out.println(extensionConstraint.getName() + " "
            // + extensionConstraint.relationRef.getStringListOfTuples() + " ");
            for (int i = 0; i < relation.length; i++) {
                if (relation[i][0] == val1 && relation[i][1] == val2) {
                    foundMatch = true;
                    // System.out.println("Check found relation " + val1 + "," + val2 + " for " +
                    // variable1.getName() + ","
                    // + variable2.getName());
                    break;
                }
            }

            // return foundMatch;
            boolean support = extensionConstraint.getSemantics().contains("support");
            if (support)
                return foundMatch;
            else
                return !foundMatch;

        } else {
            // Need to implement intension

            return true;
        }
    }

    // using the check function, the function determines if there is a value for var
    // 1 that is in the relation
    public boolean supported(MyVariable var1, int a, MyVariable var2) {

        // go through each value of the second MyVariable and find if that value is in
        // the relation with MyVariable 1 using the check function
        for (int i = 0; i < var2.getCurrentDomain().size(); i++) {
            if (check(var1, a, var2, var2.getCurrentDomain().get(i))) {
                // System.out.println("Supported " + var1.getName() + " " + a + " " +
                // var2.getName() + " "
                // + var2.getCurrentDomain().get(i));
                return true;
            }
        }
        return false;
    }

    // updates the domain of var 1 with a constraint
    // if the domain is modified, then return true
    public boolean revised(MyVariable var1, MyVariable var2) {
        boolean revised = false;
        boolean found = false;

        ArrayList<Integer> domainOfVar1 = var1.getCurrentDomain();
        Iterator iterator = domainOfVar1.iterator();

        // go through each possible value within the current domain of var 1
        while (iterator.hasNext()) {
            int val = (int) iterator.next();
            found = supported(var1, val, var2);

            // if we find that it is not supported, we need to remove this value from the
            // domain
            if (found == false) {
                System.out.println("REMOVE: " + var1.getName() + " " + val + " " + var2.getName());
                revised = true;
                iterator.remove();

                // System.out.println(var1.getName() + ": " +
                // var1.getCurrentDomain().toString());
                fval++;
            }
        }
        // making sure to update the current domain of the variable
        System.out.println("Dom of (revised): " + var1.getName() + " " + domainOfVar1.toString());
        ArrayList<Integer> passThrough = (ArrayList<Integer>) domainOfVar1.clone();
        var1.setCurrentDomain(passThrough);
        System.out.println();

        return revised;

    }

}