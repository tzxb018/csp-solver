package csp.MainStructures;

import java.util.ArrayList;
// import csp.MainStructures.MyVariable;
import java.util.LinkedList;

public class MyClique {

    ArrayList<MyVariable> variables_in_clique;
    LinkedList<MyClique> neighbors;

    public MyClique() {
        variables_in_clique = new ArrayList<MyVariable>();
        neighbors = new LinkedList<>();
    }

    public void addToClique(MyVariable newVar) {
        if (this.variables_in_clique.indexOf(newVar) < 0) {
            this.variables_in_clique.add(newVar);
        }
    }

    public void addToNeighbors(MyClique newClique) {
        if (this.neighbors.indexOf(newClique) < 0) {
            this.neighbors.add(newClique);
        }
    }

    public ArrayList<MyVariable> getVariables_in_clique() {
        return this.variables_in_clique;
    }

    public void setVariables_in_clique(ArrayList<MyVariable> variables_in_clique) {
        this.variables_in_clique = variables_in_clique;
    }

    public LinkedList<MyClique> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(LinkedList<MyClique> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public String toString() {
        return this.variables_in_clique.toString();
    }

}