// package csp;

// import java.util.ArrayList;

// import abscon.instance.components.PConstraint;
// import abscon.instance.components.PVariable;

// public class Algorithms_AC {

// protected String typeOfAlgorithm;
// protected ArrayList<PConstraint> constraints;
// protected ArrayList<Variable> variables;
// protected boolean extension;

// protected MainFunctions mainFunctions;

// // Constructor for the AC Algorithms class
// public Algorithms_AC(String typeOfAlgorithm, ArrayList<PConstraint>
// constraintList,
// ArrayList<Variable> variableList, boolean extension) {

// this.typeOfAlgorithm = typeOfAlgorithm;
// this.constraints = constraintList;
// this.variables = variableList;
// this.extension = extension;

// this.mainFunctions = new MainFunctions(constraintList, variables, extension);
// System.out.println(mainFunctions.supported(variables.get(0), 1,
// variables.get(1)));
// }

// }