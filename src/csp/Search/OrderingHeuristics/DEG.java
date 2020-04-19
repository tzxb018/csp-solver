package csp.Search.OrderingHeuristics;

import java.util.*;

import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyVariable;

public class DEG {
    // function for ordering by degree size
    public ArrayList<MyVariable> degreeOrdering(ArrayList<MyVariable> input) {

        // setting the degree sizes of all the variables
        ArrayList<MyVariable> degreeOrdered = new ArrayList<MyVariable>();
        for (MyVariable v : input) {
            v.getDegree();
        }

        // going through each element in the arraylist
        while (input.size() > 0) {
            int maxDegree = input.get(0).getDegree();
            MyVariable maxVar = input.get(0);
            // finding the largest element
            for (MyVariable var : input) {
                // if the degree is larger
                if (var.getDegree() > maxDegree) {
                    maxVar = var;
                    maxDegree = var.getDegree();
                }
                // if the degree is the same, then break lexiographically
                else if (var.getDegree() == maxDegree && var.getName().compareTo(maxVar.getName()) < 0) {
                    maxVar = var;
                    maxDegree = var.getDegree();
                }
            }

            // add the variable with the max degree to the array list and remove from the
            // inital list to avoid redundancy
            degreeOrdered.add(maxVar);
            input.remove(maxVar);

            // once the max degree has been found, remove all the instances in the neighbors
            // of all the other variables
            for (MyVariable var : input) {
                ArrayList<MyConstraint> constraints = var.getConstraints();

                // remove the max var from all the neighbors and update the neighbors
                Iterator<MyConstraint> iterator = constraints.iterator();
                while (iterator.hasNext()) {
                    MyConstraint next = iterator.next();

                    // making sure it is not a unary constraint
                    if (next.getScope().size() > 1) {
                        // finding the constraints that make the variable incident with the max var
                        if (next.getScope().get(0).getName().equals(maxVar.getName())
                                || next.getScope().get(1).getName().equals(maxVar.getName())) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
                // update the neighbors
                var.setConstraints(constraints);
            }

        }
        return degreeOrdered;
    }
}