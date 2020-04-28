package csp.TreeDecompisition;

import java.util.*;
import csp.MainStructures.MyClique;
import csp.MainStructures.MyVariable;

public class JointTree {

    protected int largestSeperator;

    /**
     * This is the method for finding the joint tree of all the cliques found
     * earlier
     * 
     * @param cliques an arraylist of cliques MaxClique will return the list to be
     *                in the indexing order Dechter requires, which is by sorting by
     *                the highest variable in the clique (highest refereing to the
     *                variable highest in the ordering in the peo)
     * @return the joint tree of all the cliques
     */
    public ArrayList<MyClique> primalAcyclicity(ArrayList<MyClique> cliques) {
        // since we are returning a peo, we assume that it is chordial
        // need to start from the end of the clique ordering
        Collections.reverse(cliques);

        for (int i = 0; i < cliques.size(); i++) {
            MyClique this_clique = cliques.get(i);
            ArrayList<MyVariable> vars_of_this_clique = this_clique.getVariables_in_clique();
            int shared_var_num = 0;

            // holds the cliques that could be the neighbors of the current clique in the
            // tree
            ArrayList<MyClique> potential_neighbors = new ArrayList<>();

            for (int j = i + 1; j < cliques.size(); j++) {
                // going through every clique and conecting it with an earlier clique with which
                // it shares a maximial number of variables in the clique

                MyClique next_clique = cliques.get(j); // the next clique

                // System.out.println(cliques.get(i) + " <> " + next_clique);

                // getting the variables of the next clique
                ArrayList<MyVariable> vars_of_next_clique = next_clique.getVariables_in_clique();

                // finding the common variables in the two cliques
                List<MyVariable> common = new ArrayList<MyVariable>(vars_of_next_clique);
                common.retainAll(vars_of_this_clique);
                // System.out.println(common);

                // if the number of shared is greater than the number of shared in the previous
                // cliques,
                // update the potential neighbors to just be this one
                if (common.size() > shared_var_num) {
                    potential_neighbors.clear();
                    potential_neighbors.add(next_clique);
                    shared_var_num = common.size();

                    if (shared_var_num > this.largestSeperator) {
                        this.largestSeperator = shared_var_num;
                    }
                } else if (common.size() == shared_var_num) {
                    // if they are the same, just add to the neighbors list (can have more than 1
                    // neighbor)
                    potential_neighbors.add(next_clique);
                }

            }

            // update the neighbors of this current clique
            for (MyClique c : potential_neighbors) {
                this_clique.addToNeighbors(c);
                c.addToNeighbors(this_clique);
            }
        }

        return cliques;
    }

    public int getLargestSepartor() {
        return this.largestSeperator;
    }

}