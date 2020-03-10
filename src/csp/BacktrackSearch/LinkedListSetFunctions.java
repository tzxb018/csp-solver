package csp.BacktrackSearch;

import java.util.*;

/**
 * 
 * @author Tomo Bessho
 * @version HW 4
 * @since 3/1/2020
 */

public class LinkedListSetFunctions {

    // returns the unions of the two sets (expressed with a linked list)
    public LinkedList<Integer> union(LinkedList<Integer> s1, LinkedList<Integer> s2) {

        for (int j : s2) {
            // if j is not already in s1, add to it, else ignore (no duplicates)
            if (s1.indexOf(j) < 0) {
                s1.add(j);
            }
        }

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

}