// package csp;

// import java.util.ArrayList;
// import java.util.Collections;

// import abscon.instance.components.PConstraint;
// import abscon.instance.components.PExtensionConstraint;
// import abscon.instance.components.PRelation;
// import abscon.instance.components.PVariable;
// import abscon.instance.tools.InstanceParser;

// public class MainFunctions {

// protected ArrayList<PConstraint> constraintList;
// protected ArrayList<Variable> variableList;
// protected boolean extension;

// public MainFunctions(ArrayList<PConstraint> constraintList,
// ArrayList<Variable> variables, boolean extension) {
// this.constraintList = constraintList;
// this.variableList = variables;
// this.extension = extension;

// }

// public boolean check(Variable variable, int val1, Variable variable2, int
// val2) {
// PConstraint constraint = null;
// int iterator = 0;

// Variable[] scopeWithTwoVars = { variable, variable2 };

// // iterating through the constraint list to find our constraint to test
// while (constraint == null && iterator < constraintList.size()) {
// PConstraint testConstraint = constraintList.get(iterator);
// if
// (testConstraint.getScope()[0].getName().equals(scopeWithTwoVars[0].getName())
// &&
// testConstraint.getScope()[1].getName().equals(scopeWithTwoVars[1].getName()))
// {
// constraint = testConstraint;
// }
// iterator++;
// }

// if (constraint == null)
// return true; // universal constraints return true

// // CC ++

// // If we are working with extension
// if (extension) {
// PExtensionConstraint extensionConstraint = (PExtensionConstraint) constraint;
// PRelation relation = extensionConstraint.getRelation();
// boolean foundMatch = false;

// // finding the two values in one tuple within the relation
// for (int i = 0; i < relation.getTuples().length; i++) {
// if (relation.getTuples()[i][0] == val1 && relation.getTuples()[i][1] == val2)
// {
// foundMatch = true;
// break;
// }
// }

// return foundMatch;

// // returning boolean values based on semantics and whether or not a match has
// // been found
// // if (relation.getSemantics().equals("conflicts")) {
// // if (foundMatch) {
// // return false;
// // } else {
// // return true;
// // }
// // } else {
// // if (foundMatch) {
// // return true;
// // } else {
// // return false;
// // }
// // }
// } else {
// // Need to implement intension

// return true;
// }
// }

// public boolean supported(Variable var1, int a, Variable var2) {

// // go through each value of the second variable and find if that value is in
// the
// // relation with variable 1 using the check function
// for (int i = 0; i < var2.getDomain().getValues().length; i++) {
// if (check(var1, a, var2, var2.getDomain().getValues()[i])) {
// return true;
// }
// }

// return false;
// }
// }