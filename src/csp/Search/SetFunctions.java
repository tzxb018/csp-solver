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
        LinkedList<Integer> clone_s1 = new LinkedList<>();
        clone_s1 = (LinkedList<Integer>) s1.clone();
        LinkedList<Integer> clone_s2 = new LinkedList<>();
        clone_s2 = (LinkedList<Integer>) s2.clone();

        for (int j : clone_s2) {
            // if j is not already in s1, add to it, else ignore (no duplicates)
            if (clone_s1.indexOf(j) < 0) {
                clone_s1.add(j);
            }
        }

        return clone_s1;
    }

    public LinkedList<Integer> unionLS(LinkedList<Integer> s1, Stack<Integer> s2) {
        LinkedList<Integer> clone_s1 = new LinkedList<>();
        clone_s1 = (LinkedList<Integer>) s1.clone();
        Stack<Integer> copy_of_s2 = new Stack<>();
        copy_of_s2 = (Stack<Integer>) s2.clone();

        LinkedList<Integer> s2_LL = new LinkedList<>();
        while (!copy_of_s2.empty()) {
            s2_LL.add(copy_of_s2.pop());
        }

        Collections.reverse(s2_LL);

        for (int j : s2_LL) {
            // if j is not already in s1, add to it, else ignore (no duplicates)
            if (clone_s1.indexOf(j) < 0) {
                clone_s1.add(j);
            }
        }

        return clone_s1;

    }

    public ArrayList<Integer> unionAS(ArrayList<Integer> s1, Stack<Integer> s2) {
        ArrayList<Integer> s1_clone = (ArrayList<Integer>) s1.clone();
        Stack<Integer> s2_clone = (Stack<Integer>) s2.clone();
        while (!s2_clone.empty()) {
            int j = s2_clone.pop();

            if (s1_clone.indexOf(j) < 0) {
                s1_clone.add(j);
            }
        }

        Collections.sort(s1_clone);

        return s1_clone;
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

    // returns the max element of a satck
    public int maxInStack(Stack<Integer> s1) {
        Stack<Integer> copy_of_s1 = new Stack<>();
        copy_of_s1 = (Stack<Integer>) s1.clone();
        int max = 0;
        while (!copy_of_s1.empty()) {
            int next = copy_of_s1.pop();
            if (next > max) {
                max = next;
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