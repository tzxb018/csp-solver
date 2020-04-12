package csp.Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyVariable;

public class Ordering {

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

    // function used to do ddr ordering heuristic for the variables
    public ArrayList<MyVariable> ddrOrdering(ArrayList<MyVariable> input) {

        // setting the degree sizes of all the variables
        ArrayList<MyVariable> ddrOrdered = new ArrayList<MyVariable>();
        for (MyVariable v : input) {
            v.getDegree();
        }

        // going through each element in the arraylist
        while (input.size() > 0) {
            MyVariable maxVar = input.get(0);
            // finding the largest element
            for (MyVariable var : input) {
                double dom1 = maxVar.getDomain().length;
                double dom2 = var.getDomain().length;
                double deg1 = maxVar.getDegree();
                double deg2 = var.getDegree();
                // if the ddr is larger than the max var's ddr
                if (dom1 * deg2 > dom2 * deg1) {
                    maxVar = var;
                }
                // if the degree is the same, then break lexiographically
                else if (dom1 * deg2 == dom2 * deg1 && var.getName().compareTo(maxVar.getName()) < 0) {
                    maxVar = var;
                }
            }

            ddrOrdered.add(maxVar);
            input.remove(maxVar);

            // once the max degree has been found, remove all the instances in the neighbors
            // of all the other variables
            for (MyVariable var : input) {
                ArrayList<MyConstraint> constraints = var.getConstraints();
                // remove the max var from all the neighbors and update the neighbors
                Iterator<MyConstraint> iterator = constraints.iterator();
                while (iterator.hasNext()) {
                    MyConstraint next = iterator.next();
                    if (next.getScope().size() > 1) {
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
        return ddrOrdered;
    }

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