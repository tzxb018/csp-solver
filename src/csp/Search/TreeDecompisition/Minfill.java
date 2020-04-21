package csp.Search.TreeDecompisition;

import java.util.*;

import csp.MainStructures.MyVariable;

public class Minfill {
    private Map<String, Integer> fcount;
    private ArrayList<MyVariable> graph;

    public ArrayList<MyVariable> minfill(ArrayList<MyVariable> input) {

        ArrayList<MyVariable> ordered = new ArrayList<>();
        graph = input;
        int n = input.size();
        fcount = fillcount(graph);

        for (int i = 0; i < n; i++) {

            // get the vertex with the smallest value in fcount
            int min = Integer.MAX_VALUE;
            MyVariable minVar = graph.get(0);
            String s = "Selecting from : ";
            for (MyVariable var : graph) {
                s += var + " " + fcount.get(var.getName()) + ", ";
                if (fcount.get(var.getName()) < min) {
                    min = fcount.get(var.getName());
                    minVar = var;
                } else if (fcount.get(var.getName()) == min && var.getName().compareTo(minVar.getName()) < 0) {
                    minVar = var;
                }
            }
            ordered.add(minVar); // adding to perfect elimination order
            ;

            graph = addFillEdgesAndRemoveNode(graph, minVar);

        }
        return ordered;

    }

    public Map<String, Integer> fillcount(ArrayList<MyVariable> V) {
        fcount = new HashMap<>();

        for (MyVariable v : V) {
            LinkedList<MyVariable> neigh = new LinkedList<>();
            neigh = v.getNeighbors(); // getting the neighbors of each variable
            if (v.getName().equals("Kool"))
                System.out.println(neigh);
            int count = 0;

            for (int i = 0; i < neigh.size(); i++) {
                for (int j = i + 1; j < neigh.size(); j++) {

                    // checking if the two neighbors do not share an edge together
                    // meaning checking to see if the two negihbors are NOT neighbors of each other
                    if (!isEdge(neigh.get(i), neigh.get(j))) {
                        count++;
                    }
                }
            }

            fcount.put(v.getName(), count);
        }
        return fcount;
    }

    public ArrayList<MyVariable> addFillEdgesAndRemoveNode(ArrayList<MyVariable> variables, MyVariable v) {
        LinkedList<MyVariable> neigh = v.getNeighbors();
        for (int i = 0; i < neigh.size(); i++) { // i = 0?

            if (fcount.get(v.getName()) == 0)
                break;

            MyVariable v_prime = neigh.get(i);

            for (int j = i + 1; j < neigh.size(); j++) {
                if (fcount.get(v.getName()) == 0) {
                    break;
                }

                MyVariable v_double_prime = neigh.get(j);
                // if v' and v'' are not an edge
                if (!isEdge(v_prime, v_double_prime)) {

                    for (MyVariable x : v_prime.getNeighbors()) {

                        // if <x, v''> is an edge]
                        if (isEdge(x, v_double_prime)) {
                            fcount.replace(x.getName(), fcount.get(x.getName()) - 1);
                            if (fcount.get(x.getName()) < 0)
                                assert fcount.get(x.getName()) >= 0;
                        } else {
                            fcount.replace(v_prime.getName(), fcount.get(v_prime.getName()) + 1);
                        }
                    }

                    for (MyVariable x : v_double_prime.getNeighbors()) {
                        if (!x.getName().equals(v.getName())) {
                            if (!isEdge(x, v_prime)) {
                                fcount.replace(v_double_prime.getName(), fcount.get(v_double_prime.getName()) + 1);
                            }
                        }
                    }

                    // adding the edge <v', v''>
                    v_prime.addNeighbors(v_double_prime);
                    v_double_prime.addNeighbors(v_prime);

                }

            }
        }

        for (int i = 0; i < v.getNeighbors().size(); i++) {

            MyVariable v_prime = v.getNeighbors().get(i);

            if (fcount.get(v_prime.getName()) == 0) {
                continue;
            }

            for (MyVariable y : v_prime.getNeighbors()) {

                if (!y.getName().equals(v.getName())) {

                    if (!isEdge(y, v)) {

                        fcount.replace(v_prime.getName(), fcount.get(v_prime.getName()) - 1);
                        assert fcount.get(v_prime.getName()) >= 0;
                        if (fcount.get(v_prime.getName()) == 0) {
                            break;
                        }
                    }
                }
            }
        }
        graph.remove(v);
        for (MyVariable var : graph) {
            var.removeNeighbor(v);
        }
        return graph;
    }

    public boolean isEdge(MyVariable v1, MyVariable v2) {
        if (v1.getNeighbors().contains(v2) || v2.getNeighbors().contains(v1)) {
            return true;
        } else {
            return false;
        }
    }
}