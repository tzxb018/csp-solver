package csp.TreeDecompisition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import csp.MainStructures.MyVariable;

public class ComponentClassifier {

    protected ArrayList<ArrayList<MyVariable>> components = new ArrayList<>();

    // Determines and returns all the connected components of the graph
    // Code adapted from
    // https://www.geeksforgeeks.org/connected-components-in-an-undirected-graph/

    public ArrayList<ArrayList<MyVariable>> connectedComponents(ArrayList<MyVariable> input) {

        // an array that keeps track of all the vertecies that have been visited
        Map<MyVariable, Boolean> visited = new HashMap<>();
        for (MyVariable v : input) {
            visited.put(v, false);
        }

        for (Map.Entry<MyVariable, Boolean> entry : visited.entrySet()) {
            // if the vertex has not been visited yet, make a new compoenent and run DFS
            // from this
            if (!entry.getValue()) {
                ArrayList<MyVariable> component = new ArrayList<MyVariable>();
                DFSFinder(entry.getKey(), visited, component);
                components.add(component);
            }
        }

        return components;

    }

    // function to find the edges that have not been visited recursivley with DFS
    public void DFSFinder(MyVariable v, Map<MyVariable, Boolean> visited, ArrayList<MyVariable> component) {

        // marking the current vertex as visisted
        visited.replace(v, true);

        // putting this variable into the component
        component.add(v);

        // recursion for all the vertecies that are adjacent to this vertex (DFS)
        for (MyVariable neighbor : v.getNeighbors()) {
            if (!visited.get(neighbor)) {
                DFSFinder(neighbor, visited, component);
            }
        }

    }

}