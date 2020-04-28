package csp.TreeDecompisition;

import java.util.*;

import csp.MainStructures.MyVariable;

public class Maxcard {

    private Map<String, Integer> neighbor_counter = new HashMap<>(); // neighbor counter keeps track of the number of
                                                                     // neighbors of all the ordered variables

    public ArrayList<MyVariable> maxCardinality(ArrayList<MyVariable> input) {

        Collections.sort(input, MyVariable.LX_COMPARATOR);
        // Collections.reverse(input);

        // initalizing the neighbor counter
        for (MyVariable v : input) {
            v.resetNeighbors();
            neighbor_counter.put(v.getName(), 0);
        }

        ArrayList<MyVariable> ordered = new ArrayList<MyVariable>();
        MyVariable arbitrary = input.get(0);
        updateNeighborCounter(arbitrary);
        ordered.add(arbitrary);
        input.remove(arbitrary);

        while (!input.isEmpty()) {

            // finding which node in input is adjacent to the most nodes in ordered
            int maxAdj = Integer.MIN_VALUE;
            MyVariable nextVariable = null;
            for (MyVariable next : input) {
                if (neighbor_counter.containsKey(next.getName()) && neighbor_counter.get(next.getName()) > maxAdj) {
                    nextVariable = next;
                    maxAdj = neighbor_counter.get(next.getName());
                }
            }
            updateNeighborCounter(nextVariable);
            input.remove(nextVariable);
            ordered.add(nextVariable);

        }

        Collections.reverse(ordered);
        return ordered;
    }

    public void updateNeighborCounter(MyVariable v) {
        // removes the next variable from the map to prevent it from being added again
        neighbor_counter.remove(v.getName());

        // getting the neighbors of the next variable in the ordering
        LinkedList<MyVariable> neighbors_of_v = v.getNeighbors();

        // going through all the neighbors and adding it to the running total of that
        // specific variable
        for (MyVariable v_prime : neighbors_of_v) {
            if (neighbor_counter.containsKey(v_prime.getName())) {
                neighbor_counter.replace(v_prime.getName(), neighbor_counter.get(v_prime.getName()) + 1);
            }
        }

    }
}