package csp.Search;

import java.util.*;

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

    private Map<String, Integer> fcount;
    private ArrayList<MyVariable> graph;
    private ArrayList<MyVariable> refList;

    public ArrayList<MyVariable> minfill(ArrayList<MyVariable> input) {

        ArrayList<MyVariable> ordered = new ArrayList<>();
        refList = new ArrayList<>();
        refList = (ArrayList<MyVariable>) input.clone();
        graph = input;
        int n = input.size();
        fcount = fillcount(graph);

        for (int i = 0; i < n; i++) {

            // get the vertex with the smallest value in fcount
            int min = Integer.MAX_VALUE;
            MyVariable minVar = graph.get(0);
            for (MyVariable var : graph) {
                if (fcount.get(var.getName()) < min) {
                    min = fcount.get(var.getName());
                    minVar = var;
                } else if (fcount.get(var.getName()) == min && var.getName().compareTo(minVar.getName()) < 0) {
                    minVar = var;
                }
            }
            ordered.add(minVar); // adding to perfect elimination order
            System.out.println(minVar);
            System.out.println(fcount);
            System.out.println(graph);

            graph = addFillEdgesAndRemoveNode(graph, minVar);
            System.out.println();

        }

        // System.out.println(ordered);

        return ordered;

    }

    public Map<String, Integer> fillcount(ArrayList<MyVariable> V) {
        fcount = new HashMap<>();

        for (MyVariable v : V) {
            LinkedList<MyVariable> neigh = new LinkedList<>();
            neigh = v.getNeighbors(); // getting the neighbors of each variable
            int count = 0;

            for (int i = 1; i < neigh.size(); i++) {
                for (int j = i + 1; j < neigh.size(); j++) {
                    // checking if the two neighbors do not share an edge together
                    // meaning checking to see if the two negihbors are NOT neighbors of each other
                    if (notInEdge(neigh.get(i), neigh.get(j))) {
                        count++;
                    }
                }
            }

            fcount.put(v.getName(), count);
        }
        System.out.println(fcount);
        System.out.println();
        return fcount;
    }

    public ArrayList<MyVariable> addFillEdgesAndRemoveNode(ArrayList<MyVariable> variables, MyVariable v) {
        // System.out.println("addfill");
        LinkedList<MyVariable> neigh = v.getNeighbors();
        // System.out.println(v + "neighbors: " + neigh);
        for (int i = 0; i < neigh.size(); i++) { // i = 0?
            // System.out.println("fcount of " + v.getName() + " : " +
            // fcount.get(v.getName()));
            if (fcount.get(v.getName()) == 0)
                break;

            MyVariable v_prime = neigh.get(i);
            for (int j = i + 1; j < neigh.size(); j++) {
                if (fcount.get(v.getName()) == 0) {
                    break;
                }

                MyVariable v_double_prime = neigh.get(j);
                // if v' and v'' are not an edge
                if (notInEdge(v_prime, v_double_prime)) {
                    for (MyVariable x : v_prime.getNeighbors()) {
                        if (!notInEdge(x, v_double_prime)) {
                            fcount.replace(x.getName(), fcount.get(x.getName()) - 1);
                        } else {
                            fcount.replace(v_prime.getName(), fcount.get(v_prime.getName()) + 1);
                        }
                    }

                    for (MyVariable x : v_double_prime.getNeighbors()) {
                        if (!x.getName().equals(v.getName())) {
                            if (notInEdge(x, v_prime)) {
                                fcount.replace(v_double_prime.getName(), fcount.get(v_double_prime.getName()) + 1);
                            }
                        }
                    }

                    // adding the edge <v', v''>
                    v_prime.addNeighbors(v_double_prime);
                    v_double_prime.addNeighbors(v_prime);

                    for (int k = 0; k < refList.size(); k++) {
                        if (v_prime.getName().equals(refList.get(k).getName())) {
                            refList.get(k).addNeighbors(v_double_prime);
                        } else if (v_double_prime.getName().equals(refList.get(k).getName())) {
                            refList.get(k).addNeighbors(v_prime);

                        }
                    }

                }
            }
        }

        // System.out.println("neighbors(v): " + v.getNeighbors());
        for (int i = 0; i < v.getNeighbors().size(); i++) {
            // for (MyVariable v_prime : v.getNeighbors()) {
            MyVariable v_prime = null;

            for (int j = 0; j < refList.size(); j++) {
                if (v.getNeighbors().get(i).getName().equals(refList.get(j).getName())) {
                    v_prime = refList.get(j);
                }
            }
            // System.out.println("v'= " + v_prime + " fcount: " +
            // fcount.get(v_prime.getName()));
            // System.out.println(v_prime + " " + v.getNeighbors().get(i));

            // MyVariable v_prime = v.getNeighbors().get(i);

            if (fcount.get(v_prime.getName()) == 0) {
                continue;
            }

            // System.out.println(v_prime.getName() + " neightbors: " +
            // v_prime.getNeighbors());
            for (MyVariable y : v_prime.getNeighbors()) {
                if (!y.getName().equals(v.getName())) {
                    // System.out.println(notInEdge(y, v));
                    if (notInEdge(y, v)) {
                        fcount.replace(v_prime.getName(), fcount.get(v_prime.getName()) - 1);
                        // System.out.println(v_prime + ": new fcount: " +
                        // fcount.get(v_prime.getName()));
                        if (fcount.get(v_prime.getName()) == 0) {
                            // System.out.println("new v'");
                            break;
                        }
                    }
                }
            }
        }
        graph.remove(v);
        return graph;
    }

    public boolean notInEdge(MyVariable v1, MyVariable v2) {
        // System.out.println("not in edge " + v1 + "<>" + v2);
        MyVariable var1 = null;
        MyVariable var2 = null;
        for (int j = 0; j < refList.size(); j++) {
            if (v1.getName().equals(refList.get(j).getName())) {
                var1 = refList.get(j);
            } else if (v2.getName().equals(refList.get(j).getName())) {
                var2 = refList.get(j);
            }
        }
        // System.out.println(var1 + ": " + var1.getNeighbors() +
        // var1.getNeighbors().contains(var2));
        // System.out.println(var2.getNeighbors());

        if (!var1.getNeighbors().contains(var2) && !var2.getNeighbors().contains(var1)) {
            return true;
        } else
            return false;
    }
}