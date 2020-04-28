package csp.Search.OrderingHeuristics;

import java.util.*;

import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyVariable;

public class DDR {

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

}