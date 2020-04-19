package csp.Search.OrderingHeuristics;

import java.util.*;

import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyVariable;

public class Minwidth {

    public ArrayList<MyVariable> widthOrdering(ArrayList<MyVariable> input) {

        // setting the degree sizes of all the variables
        ArrayList<MyVariable> widthOrdered = new ArrayList<MyVariable>();
        Iterator<MyVariable> iterator1 = input.iterator();
        while (iterator1.hasNext()) {
            MyVariable v = iterator1.next();
            v.getDegree();
            // removing all the nodes that have a degree of 0
            if (v.getDegree() == 0) {
                widthOrdered.add(v);
                iterator1.remove();
            }
        }

        // sorting the list lexiographically, since all the variables here have degree 0
        // atm
        Collections.sort(widthOrdered, MyVariable.LX_COMPARATOR);
        int k = 0;

        // while there are nodes in the graph
        while (input.size() > 0) {
            k++; // incrementing the value of the current width

            int minDegree = 0;
            while (minDegree <= k && input.size() > 0) {
                while (input.size() > 0) {
                    MyVariable minVar = computeMinDegree(input);
                    minDegree = minVar.getDegree();

                    // add the variable with the min degree to the array list and remove from the
                    // inital list to avoid redundancy
                    widthOrdered.add(minVar);
                    input.remove(minVar);

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
                                if (next.getScope().get(0).getName().equals(minVar.getName())
                                        || next.getScope().get(1).getName().equals(minVar.getName())) {
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
            }

        }
        return widthOrdered;
    }

    public MyVariable computeMinDegree(ArrayList<MyVariable> input) {

        // finding the variable with the smallest degree
        int minDegree = input.get(0).getDegree();
        MyVariable minVar = input.get(0);

        // finding the smallest element
        for (MyVariable var : input) {
            // if the degree is smaller
            if (var.getDegree() < minDegree) {
                minVar = var;
                minDegree = var.getDegree();
            }
            // if the degree is the same, then break lexiographically
            else if (var.getDegree() == minDegree && var.getName().compareTo(minVar.getName()) < 0) {
                minVar = var;
                minDegree = var.getDegree();
            }
        }

        return minVar;
    }
}