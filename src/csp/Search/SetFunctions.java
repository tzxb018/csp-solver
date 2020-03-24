package csp.Search;

import java.util.*;

/**
 * 
 * @author Tomo Bessho
 * @version HW 4
 * @since 3/1/2020
 */

public class SetFunctions {

    // returns the unions of the two sets (expressed with a linked list)
    public LinkedList<Integer> unionLL(LinkedList<Integer> s1, LinkedList<Integer> s2) {

        for (int j : s2) {
            // if j is not already in s1, add to it, else ignore (no duplicates)
            if (s1.indexOf(j) < 0) {
                s1.add(j);
            }
        }

        return s1;
    }

    public ArrayList<Integer> unionAS(ArrayList<Integer> s1, Stack<Integer> s2) {

        while (!s2.empty()) {
            int j = s2.pop();

            if (s1.indexOf(j) < 0) {
                s1.add(j);
            }
        }

        Collections.sort(s1);

        return s1;
    }

    // returns the max element of the linked list
    public int maxInLinkedList(LinkedList<Integer> l1) {
        int max = l1.getFirst();

        for (int i = 0; i < l1.size(); i++) {
            if (l1.get(i) > max) {
                max = l1.get(i);
            }
        }

        return max;
    }

    // returns the set difference of two sets
    public ArrayList<Integer> setDiff(ArrayList<Integer> s1, Stack<Integer> s2) {

        ArrayList<Integer> setDiff = new ArrayList<Integer>();
        ArrayList<Integer> temp = new ArrayList<>(s2);

        // if element from s1 is not in s2, add to return list
        for (Integer i : s1) {
            boolean found = false;

            for (Integer j : temp) {
                if (i == j) {
                    found = true;
                }
            }

            if (!found) {
                setDiff.add(i);
            }
        }

        return setDiff;
    }

}