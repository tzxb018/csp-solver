package csp.Search.TreeDecompisition;

import java.util.*;

import csp.MainStructures.MyClique;
import csp.MainStructures.MyVariable;

public class MaxClique {

    protected ArrayList<MyClique> cliques;

    /**
     * This is the method for finding the number of cliques in the CSP
     * 
     * @param peo an arraylist of variables that are in the perfect elimination
     *            ordering after running minfill or max cardinality
     * @return the cliques found in the problem
     */
    public ArrayList<MyClique> getMaxClique(ArrayList<MyVariable> peo) {

        cliques = new ArrayList<MyClique>();
        Map<String, Integer> S = new HashMap<>();

        // Initalize S(v) for all variables
        for (MyVariable v : peo) {
            S.put(v.getName(), 0);
            v.resetNeighbors(); // resets to original neighbors (neighbors removed during minfill)
        }

        // Going through each variable in the PEO and finding its cliques
        for (int i = 0; i < peo.size(); i++) {

            MyVariable v = peo.get(i);

            // finding the variables adjacent to v that come after v in the ordering
            ArrayList<MyVariable> X = new ArrayList<>();
            for (int j = i + 1; j < peo.size(); j++) {
                if (v.getNeighbors().contains(peo.get(j))) {
                    X.add(peo.get(j));
                }
            }

            // if v has no neighbors, then put it in its own clique
            MyClique clique = new MyClique();
            if (v.getNeighbors().isEmpty()) {
                clique.addToClique(v);
                cliques.add(clique);
                // System.out.println("New clique: " + clique);
                continue;
            }

            // if v has neighbors, but all of them comve before v in the ordering, terminate
            if (X.isEmpty()) {
                // System.out.println("DONE");
                break;
            }

            // let u be the member of X that's earliest in the ordering
            MyVariable u = X.get(0);

            S.put(u.getName(), Math.max(S.get(u.getName()), X.size() - 1));

            // determining if there is a clique
            if (S.get(v.getName()) < X.size()) {

                clique.addToClique(v);
                // adding the union of X and v to the new clique
                for (MyVariable var : X) {
                    clique.addToClique(var);
                }
                cliques.add(clique);
                // System.out.println("New clique: " + clique);
            }

        }
        return cliques;
    }

    public int getNumberOfCliques() {
        return this.cliques.size();
    }

    public int getLargestClique() {
        int max = 0;
        for (MyClique c : this.cliques) {
            if (c.getVariables_in_clique().size() > max) {
                max = c.getVariables_in_clique().size();
            }
        }

        return max;
    }

}