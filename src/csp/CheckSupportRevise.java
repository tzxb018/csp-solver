package csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

public class CheckSupportRevise {

    protected ArrayList<MyConstraint> constraintList;
    protected ArrayList<MyVariable> variableList;
    protected boolean extension;

    protected int cc;
    protected int fval;

    public CheckSupportRevise(ArrayList<MyConstraint> constraintList, ArrayList<MyVariable> variables, boolean extension) {
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
        Map<MyConstraint, Boolean> constraint = new HashMap<MyConstraint, Boolean>();
        ArrayList<MyConstraint> constraints = new ArrayList<MyConstraint>();
        int iterator = 0;

        MyVariable[] scopeWithTwoVars = { variable1, variable2 };
        boolean reversed = false;

        // iterating through the constraint list to find our constraint to test
        // make it a lsit
        while (iterator < constraintList.size()) {
            MyConstraint testConstraint = constraintList.get(iterator);

            // finding the binary constraint that holds the two given variables
            if (testConstraint.getScope().size() > 1) {
                // checking if the scope of the constriant matches the two variables being put
                // in
                if ((testConstraint.getScope().get(0).getName().equals(scopeWithTwoVars[0].getName())
                        && testConstraint.getScope().get(1).getName().equals(scopeWithTwoVars[1].getName()))) {
                    constraint.put(testConstraint, false);
                    constraints.add(testConstraint);
                    reversed = false;

                    // making sure we check the reverse of the scope, since we are checking both
                    // directions
                }
                if (testConstraint.getScope().get(1).getName().equals(scopeWithTwoVars[0].getName())
                        && testConstraint.getScope().get(0).getName().equals(scopeWithTwoVars[1].getName())) {
                    constraint.put(testConstraint, true);
                    constraints.add(testConstraint);
                    reversed = true;

                }
            }
            iterator++;
        }

        // return true for unary and universal constraints
        for (Map.Entry<MyConstraint, Boolean> a : constraint.entrySet()) {
            if (a.getKey().getScope().size() < 2)
                return true;
        }

        cc++;

        // If we are working with extension
        if (extension) {

            boolean satisfied = true;
            int counter = 0;

            for (Map.Entry<MyConstraint, Boolean> entry : constraint.entrySet()) {

                // if (constraint.size() > 1)
                // System.out.println(entry);

                MyExtensionConstraint extensionConstraint = (MyExtensionConstraint) entry.getKey();

                boolean foundMatch = false;
                // for binary constraints
                if (extensionConstraint.getRelation()[0].length == 2) {
                    int[][] relation = extensionConstraint.getRelation();

                    // finding the two values in one tuple within the relation
                    for (int i = 0; i < relation.length; i++) {
                        // System.out.println(relation[i][0] + " " + relation[i][1] + " : " +
                        // scopeWithTwoVars[0].getName()
                        // + " " + scopeWithTwoVars[1].getName());
                        // if the relation holds the two values in the argument
                        if (relation[i][0] == val1 && relation[i][1] == val2 && entry.getValue() == false) {
                            foundMatch = true;
                            // System.out.println("Check found relation " + val1 + "," + val2 + " for " +
                            // variable1.getName()
                            // + "," + variable2.getName());
                            break;

                            // gotta check the reversed order of the values (wow this took me 6 hours to
                            // find!)
                        } else if (relation[i][1] == val1 && relation[i][0] == val2 && entry.getValue() == true) {
                            foundMatch = true;
                            // System.out.println("Check found relation " + val1 + "," + val2 + " for " +
                            // variable1.getName()
                            // + "," + variable2.getName());
                            break;
                        }
                    }
                }

                boolean support = extensionConstraint.getSemantics().contains("support");
                if (!support) foundMatch = !foundMatch;

                    satisfied = satisfied && foundMatch;

                // if (constraint.size() > 1)
                //     System.out.println(extensionConstraint.getName() + " " + variable1.getName() + " " + val1 + " "
                //             + variable2.getName() + " " + val2 + " " + ", reversed: " + entry.getValue()
                //             + ", found match: " + foundMatch + ", SAT: " + satisfied);

                counter++;

            }
            // if (constraint.size() > 1)
            //     System.out.println("Return of check: " + satisfied + "\n");
            return satisfied;

        } else

        {

            int[] tuple = new int[2];
            if (reversed) {
                tuple[0] = val2;
                tuple[1] = val1;
            } else {
                tuple[0] = val1;
                tuple[1] = val2;
            }

            boolean satisfied = true;
            for (Map.Entry<MyConstraint, Boolean> entry : constraint.entrySet()) {
                MyIntensionConstraint intensionConstraint = (MyIntensionConstraint) entry.getKey();

                satisfied = satisfied && (intensionConstraint.refCon.computeCostOf(tuple) == 0);

            }
            return satisfied;

        }
    }

    // using the check function, the function determines if there is a value for var
    // 1 that is in the relation
    public boolean supported(MyVariable var1, int a, MyVariable var2) {

        // go through each value of the second MyVariable and find if that value is in
        // the relation with MyVariable 1 using the check function
        for (int i = 0; i < var2.getCurrentDomain().size(); i++) {
            // System.out.println("SUPPORT FUNCTION:" + var1.getName() + " " + a + " " +
            // var2.getName() + " "
            // + var2.getCurrentDomain().get(i));
            if (check(var1, a, var2, var2.getCurrentDomain().get(i))) { //
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

        // System.out.println("REV: " + var1.getName() + " " +
        // var1.getCurrentDomain().toString());
        // System.out.println("REV: " + var2.getName() + " " +
        // var2.getCurrentDomain().toString());

        ArrayList<Integer> domainOfVar1 = var1.getCurrentDomain();
        Iterator<Integer> iterator = domainOfVar1.iterator();

        // go through each possible value within the current domain of var 1
        while (iterator.hasNext()) {
            int val = (int) iterator.next();
            found = supported(var1, val, var2);
            // System.out.println(var1.getName() + " " + var2.getName() + ", FOUND: " +
            // found);

            // if we find that it is not supported, we need to remove this value from the
            // domain
            if (found == false) {
                // System.out.println(var2.getCurrentDomain());
                // System.out.println("*REMOVE: " + var1.getName() + " " + val + " ");
                fval++;
                revised = true;
                iterator.remove();
            }
        }
        // making sure to update the current domain of the variable
        // System.out.println("Dom of (revised): " + var1.getName() + " " +
        // domainOfVar1.toString());
        ArrayList<Integer> passThrough = (ArrayList<Integer>) domainOfVar1.clone();
        var1.setCurrentDomain(passThrough);

        return revised;

    }

}